package org.BULVE.visitor;

import org.BULVE.visitor.exception.BULVEBadTypeException;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.*;

public class BULVEVisitorImpl extends BULVEBaseVisitor<Object> {

    private final StringBuilder SYSTEM_OUT = new StringBuilder();

    private final Stack<BULVEScope> scopeStack = new Stack<>();

    private BULVEScope currentScope = new BULVEScope();

/*    private BULVEScope functionsScope = new BULVEScope(); //gal reikes funkcijom
    private final Stack<BULVEScope> funcStack = new Stack<>();*/

    private final List<String> specialArgs = new ArrayList<>();

    private final Map<String, BULVEParser.FunctionDeclarationContext> functions = new HashMap<>();

    @Override
    public Object visitProgram(BULVEParser.ProgramContext ctx) {
        super.visitProgram(ctx);
        return SYSTEM_OUT.toString();
    }

    @Override
    public Object visitPrintFunctionCall(BULVEParser.PrintFunctionCallContext ctx) {
        String text = visit(ctx.expression()).toString();
        System.out.println(text);
        SYSTEM_OUT.append(text).append("\n");
        return null;
    }

    @Override
    public Object visitConstantExpression(BULVEParser.ConstantExpressionContext ctx) {
        return visit(ctx.constant());
    }

    @Override
    public Object visitConstant(BULVEParser.ConstantContext ctx) {
        if (ctx.INTEGER() != null) {
            return Integer.parseInt(ctx.INTEGER().getText());
        }
        if (ctx.BOOLEAN() != null) {
            return Boolean.parseBoolean(ctx.BOOLEAN().getText());
        }
        if (ctx.DECIMAL() != null) {
            return Double.parseDouble(ctx.DECIMAL().getText());
        }
        if (ctx.STRING() != null) {
            return ctx.STRING().getText();
        }
        return null;
    }

    @Override
    public Object visitVariableDeclaration(BULVEParser.VariableDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        Object value = visit(ctx.expression());
        this.currentScope.declareVariable(varName, value);
        return null;
    }

    @Override
    public Object visitIntegerDeclaration(BULVEParser.IntegerDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        try{
                Integer value = Integer.parseInt(this.visit(ctx.expression()).toString());
                this.currentScope.declareVariable(varName, value);
                return value;
        }
        catch (BULVEBadTypeException ex)
        {
            throw new BULVEBadTypeException(varName);
        }

    }
    //TODO:VISIEM DECLARATION TIKRINT AR TEISINGAI PRISKIRTAS TIPAS, exeption sitas netinka
    @Override
    public Object visitStringDeclaration(BULVEParser.StringDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        try{
                String value = this.visit(ctx.expression()).toString();
                this.currentScope.declareVariable(varName, value);
                return value;
    }
        catch (BULVEBadTypeException ex)
        {
            throw new BULVEBadTypeException(varName);
        }
    }

    @Override
    public Object visitAssignment(BULVEParser.AssignmentContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        Object value = visit(ctx.expression());
        this.currentScope.changeVariable(varName, value);
        return value;
    }

    @Override
    public Object visitDecimalDeclaration(BULVEParser.DecimalDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        try{
                Double value = Double.parseDouble(this.visit(ctx.expression()).toString());
                this.currentScope.declareVariable(varName, value);
                return value;
        }
        catch (BULVEBadTypeException ex)
        {
            throw new BULVEBadTypeException(varName);
        }
    }

    @Override
    public Object visitBoolDeclaration(BULVEParser.BoolDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        try{
        Boolean value = Boolean.parseBoolean(this.visit(ctx.expression()).toString());
        this.currentScope.declareVariable(varName, value);
        return value;
        }
        catch (BULVEBadTypeException ex)
            {
                throw new BULVEBadTypeException(varName);
            }

    }

    @Override
    public Object visitIdentifierExpression(BULVEParser.IdentifierExpressionContext ctx) {
        String varName = ctx.IDENTIFIER().getText();

        Object value = this.currentScope.resolveVariable(varName);
        if(specialArgs.contains(varName))
        {
            System.out.println(varName+" = "+ value);
        }
        return value;
    }

    @Override
    public Object visitNumericAddOpExpression(BULVEParser.NumericAddOpExpressionContext ctx) {
        Object val1 = visit(ctx.expression(0));
        Object val2 = visit(ctx.expression(1));
        //TODO - validation etc
        return switch (ctx.numericAddOp().getText()) {
            case "+" -> (Integer) val1 + (Integer) val2;
            case "--" -> (Integer) val1 - (Integer) val2;
            default -> null;
        };
    }

    @Override
    public Object visitNumericMultiOpExpression(BULVEParser.NumericMultiOpExpressionContext ctx) {
        Object val1 = visit(ctx.expression(0));
        Object val2 = visit(ctx.expression(1));
        //TODO - validation etc
        return switch (ctx.numericMultiOp().getText()) {
            case "*" -> (Integer) val1 * (Integer) val2;
            case "/" -> (Integer) val1 / (Integer) val2;
            case "%" -> (Integer) val1 % (Integer) val2;
            default -> null;
        };
    }

    @Override
    public Object visitIfElseStatement(BULVEParser.IfElseStatementContext ctx) {
        boolean value = (Boolean) visit(ctx.expression());
        if (value) {
            return visit(ctx.block(0));
        } else {
            return visit(ctx.block(1));
        }
    }

    @Override
    public Object visitBlock(BULVEParser.BlockContext ctx) {
        scopeStack.push(currentScope);
        currentScope = new BULVEScope(currentScope);
        Object value = super.visitBlock(ctx);
        currentScope = scopeStack.pop();
        return value;
    }

    @Override
    public Object visitParenthesesExpression(BULVEParser.ParenthesesExpressionContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Object visitKolStatement(BULVEParser.KolStatementContext ctx) {
        try{
            boolean value = (Boolean) visit(ctx.expression());
            if (value) {
                visit(ctx.block());
                visitKolStatement(ctx);
            } else {
                return null;
            }
            return null;
        }
        catch (BULVEBadTypeException exception)
        {
            throw new BULVEBadTypeException(ctx.expression().getText());
        }

    }

    @Override
    public Object visitNumericCompareOpExpresion(BULVEParser.NumericCompareOpExpresionContext ctx) {
        Object val1 = visit(ctx.expression(0));
        Object val2 = visit(ctx.expression(1));
        return switch (ctx.numericCompareOp().getText()) {
            case ">" -> (Integer) val1 > (Integer)val2;
            case "<" -> (Integer) val1 < (Integer) val2;
            case ">=" -> (Integer) val1 >= (Integer) val2;
            case "<=" -> (Integer) val1 <= (Integer) val2;
            case "==" -> (Integer) val1 == (Integer) val2;
            case "!=" -> (Integer) val1 != (Integer) val2;
            default -> null;
        };
    }

    @Override
    public Object visitVisiemsStatement(BULVEParser.VisiemsStatementContext ctx) {

        Integer counterStart = (Integer) visit(ctx.integerDeclaration());
        String ident = ctx.IDENTIFIER().getText();
        String op = ctx.numericCompareOp().getText();
        Integer bound = (Integer) visit(ctx.expression());
        Integer counterIncrease = (Integer) visit(ctx.assignment()) - counterStart;
        switch (op)
        {
            case "<":
            {
                for(int i=counterStart; i<bound; i+=counterIncrease)
                {
                    //perduoda f i reiksme, nes cia i keiciasi, o ten f ne, tik pirma karta
                    this.currentScope.changeVariable(ident, i);
                    visit(ctx.block());
                }
                return null;
            }
            case ">":
            {
                for(int i=counterStart; i>bound; i+=counterIncrease)
                {
                    this.currentScope.changeVariable(ident, i);
                    visit(ctx.block());
                }
                return null;
            }
            case ">=":
            {
                for(int i=counterStart; i>=bound; i+=counterIncrease)
                {
                    this.currentScope.changeVariable(ident, i);
                    visit(ctx.block());
                }
                return null;
            }
            case "<=":
            {
                for(int i=counterStart; i<=bound; i+=counterIncrease)
                {
                    this.currentScope.changeVariable(ident, i);
                    visit(ctx.block());
                }
                return null;
            }
            default:  return null;
        }

    }

    //TODO - ValueType enum
    //class Value (wraps value+type), return type accepts Value
    //VOID return type
    //Function class, with validation and invoke methods
    // small visitor classes
    @Override
    public Object visitType(BULVEParser.TypeContext ctx) {
        return ctx.getText();
    }

    //TODO implement nested functions
    @Override
    public Object visitFunctionDeclaration(BULVEParser.FunctionDeclarationContext ctx) {
        String funcName = ctx.IDENTIFIER().getText();


        this.functions.put(funcName, ctx);
        // create function class that has constructor(FunctionDeclarationContext), Invoke method
        // validations
        return ctx;

    }

    @Override
    public Object visitFunctionBody(BULVEParser.FunctionBodyContext ctx) {
        Object value =  super.visitFunctionBody(ctx);
        if(value instanceof returnValue)
        {
            return value;
        }
        return new returnValue(null);
    }

    @Override
    public Object visitFunctionCall(BULVEParser.FunctionCallContext ctx) {
        String funcName = ctx.IDENTIFIER().getText();
        BULVEParser.FunctionDeclarationContext func = this.functions.get(funcName);

        //TODO: check if args count is the same - ar istrauktoj funkcijoj toks pat kiekis kaip ir call'e
        List<Object> args = new ArrayList<>();
        if(ctx.expressionList()!=null)
        {
            for(var exp : ctx.expressionList().expression())
            {
                args.add(this.visit(exp));
            }
        }
        //TODO: validate args types
        BULVEScope functionScope = new BULVEScope();
        if(func.paramList()!=null) {
            for (int i = 0; i < func.paramList().IDENTIFIER().size(); i++) {
                String paramName = func.paramList().IDENTIFIER(i).getText();
                if(paramName.charAt(0)=='&')
                {
                    String identReal = paramName.substring(1); //real varname
                    specialArgs.add(identReal);
                    functionScope.declareVariable(identReal, args.get(i)); //special
                }
                else functionScope.declareVariable(paramName, args.get(i)); //susieja declaration su call'o argumentais
            }
        }
        //sitoj vietoj scope kitoks jei funkcija funkcijoj? o gal ne
        scopeStack.push(currentScope);
        currentScope = functionScope;
        returnValue value = (returnValue)this.visitFunctionBody(func.functionBody());
        currentScope = scopeStack.pop();
        specialArgs.clear();
        return value.getValue();
    }

    @Override
    public Object visitReturnStatment(BULVEParser.ReturnStatmentContext ctx) {
        if(ctx.expression()==null)
        {
            return new returnValue(null); //returnValue, nes pagal sita tipa zino, ar reikai iseit is bloko
        }
        else {
            return new returnValue(this.visit(ctx.expression()));
        }
    }


    @Override
    protected boolean shouldVisitNextChild(RuleNode node, Object currentResult) {
        return !(currentResult instanceof returnValue);
    }
}
