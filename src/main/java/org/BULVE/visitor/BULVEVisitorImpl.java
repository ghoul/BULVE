package org.BULVE.visitor;

import org.BULVE.visitor.exception.BULVEArrayDeclarationExeption;
import org.BULVE.visitor.exception.BULVEBadTypeException;
import org.BULVE.visitor.exception.BULVEFunctionParametersExeption;
import org.BULVE.visitor.exception.BULVEIndexOutsideTheBoundsOfTheArray;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.*;


public class BULVEVisitorImpl extends BULVEBaseVisitor<Object> {

    private final StringBuilder SYSTEM_OUT = new StringBuilder();

    private final Stack<BULVEScope> scopeStack = new Stack<>();

    private BULVEScope currentScope = new BULVEScope();

    private final List<String> specialArgs = new ArrayList<>();

    private final Map<String, BULVEParser.FunctionDeclarationContext> functions = new HashMap<>();

    @Override
    public Object visitProgram(BULVEParser.ProgramContext ctx) {
        super.visitProgram(ctx);
        return SYSTEM_OUT.toString();
    }

    @Override
    public Object visitPrintFunctionCall(BULVEParser.PrintFunctionCallContext ctx) {
        String text0 = visit(ctx.expression()).toString();
        String text = text0.replace("\"", "");
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
        Object value = this.visit(ctx.expression());
        if(value instanceof Integer)
        {
            Integer value1 = Integer.parseInt(value.toString());
            this.currentScope.declareVariable(varName, value1);
            return value1;
        }
        else throw new BULVEBadTypeException(varName);

    }

    @Override
    public Object visitStringDeclaration(BULVEParser.StringDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        String value = this.visit(ctx.expression()).toString();
        this.currentScope.declareVariable(varName, value);
        return value;
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
        Object value = this.visit(ctx.expression());
        if(value instanceof Double)
        {
                Double value1 = Double.parseDouble(value.toString());
                this.currentScope.declareVariable(varName, value1);
                return value1;
        }
        else throw new BULVEBadTypeException(varName);
    }

    @Override
    public Object visitBoolDeclaration(BULVEParser.BoolDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        Object value = this.visit(ctx.expression());
        if(value instanceof Boolean) {
            Boolean value1 = Boolean.parseBoolean(value.toString());
            this.currentScope.declareVariable(varName, value1);
            return value1;
        }
        else throw new BULVEBadTypeException(varName);
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
        if(!(val1 instanceof String) && !(val1 instanceof Boolean) && !(val2 instanceof String) && !(val2 instanceof Boolean)) {
            if (val1 instanceof Integer && val2 instanceof Integer) {
                return switch (ctx.numericAddOp().getText()) {
                    case "+" -> (Integer) val1 + (Integer) val2;
                    case "--" -> (Integer) val1 - (Integer) val2;
                    default -> null;
                };
            } else {
                double val1d, val2d;
                if (val1 instanceof Integer) {
                    int val1i = Integer.parseInt(val1.toString());
                    val1d = val1i;
                } else {
                    val1d = Double.parseDouble(val1.toString());
                }

                if (val2 instanceof Integer) {
                    int val2i = Integer.parseInt(val2.toString());
                    val2d = val2i;
                } else {
                    val2d = Double.parseDouble(val2.toString());
                }
                return switch (ctx.numericAddOp().getText()) {
                    case "+" -> val2d + val1d;
                    case "--" -> val1d - val2d;
                    default -> null;
                };
            }
        }
        else if(val1 instanceof String || val1 instanceof Boolean)
        {
            throw new BULVEBadTypeException(val1.toString());
        }
        else
        {
            throw new BULVEBadTypeException(val2.toString());
        }
    }

    @Override
    public Object visitNumericMultiOpExpression(BULVEParser.NumericMultiOpExpressionContext ctx) {
        Object val1 = visit(ctx.expression(0));
        Object val2 = visit(ctx.expression(1));
        if(!(val1 instanceof String) && !(val1 instanceof Boolean) && !(val2 instanceof String) && !(val2 instanceof Boolean)) {
            if (val1 instanceof Integer && val2 instanceof Integer) {
                return switch (ctx.numericMultiOp().getText()) {
                    case "*" -> (Integer) val1 * (Integer) val2;
                    case "/" -> (Integer) val1 / (Integer) val2;
                    case "%" -> (Integer) val1 % (Integer) val2;
                    default -> null;
                };
            } else {
                double val1d, val2d;
                if (val1 instanceof Integer) {
                    int val1i = Integer.parseInt(val1.toString());
                    val1d = val1i;
                } else {
                    val1d = Double.parseDouble(val1.toString());
                }

                if (val2 instanceof Integer) {
                    int val2i = Integer.parseInt(val2.toString());
                    val2d = val2i;
                } else {
                    val2d = Double.parseDouble(val2.toString());
                }
                return switch (ctx.numericMultiOp().getText()) {
                    case "*" -> val1d * val2d;
                    case "/" -> val1d / val2d;
                    case "%" -> val1d % val2d;
                    default -> null;
                };
            }
        }
        else if(val1 instanceof String || val1 instanceof Boolean)
        {
            throw new BULVEBadTypeException(val1.toString());
        }
        else
        {
            throw new BULVEBadTypeException(val2.toString());
        }
    }

    @Override
    public Object visitIntArrayDeclaration(BULVEParser.IntArrayDeclarationContext ctx) {
        String arrName = ctx.IDENTIFIER().getText();
        Object value = this.visit(ctx.expression());
        if(value instanceof Integer)
        {
            Integer size = Integer.parseInt(value.toString());
            int[] arr = new int[size];
            this.currentScope.declareVariable(arrName, arr);
            return arr;
        }
        else throw new BULVEArrayDeclarationExeption(arrName);

    }

    @Override
    public Object visitIntArrayAdd(BULVEParser.IntArrayAddContext ctx) {
        String arrName = ctx.IDENTIFIER().getText();
        Object value = this.visit(ctx.expression());
        if(value instanceof Integer)
        {
            Integer value1 = Integer.parseInt(value.toString());
            //pasiimt is scope
            int[] arr = (int[])this.currentScope.resolveVariable(arrName);
            int index = Integer.parseInt(ctx.INTEGER().getText());
            if(index>arr.length || index<0)
            {
                throw new BULVEIndexOutsideTheBoundsOfTheArray(arrName);
            }
            else{
                arr[index]=value1;
                this.currentScope.changeVariable(arrName, arr);
            }
            return arr;
        }
        else throw new BULVEBadTypeException(arrName);

    }

    @Override
    public Object visitIntArrayGet(BULVEParser.IntArrayGetContext ctx) {
        String arrName = ctx.IDENTIFIER().getText();
        int[] arr = (int[])this.currentScope.resolveVariable(arrName);
            int index = Integer.parseInt(ctx.INTEGER().getText());
            if(index>arr.length || index<0)
            {
                throw new BULVEIndexOutsideTheBoundsOfTheArray(arrName);
            }
            else{
                return arr[index];
            }
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
    public Object visitStringBinaryOpExpression(BULVEParser.StringBinaryOpExpressionContext ctx) {
        String first = visit(ctx.expression(0)).toString();
        String second = visit(ctx.expression(1)).toString();
        String both = first+second;
        return both;
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


    @Override
    public Object visitType(BULVEParser.TypeContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitFunctionDeclaration(BULVEParser.FunctionDeclarationContext ctx) {
        String funcName = ctx.IDENTIFIER().getText();
        this.functions.put(funcName, ctx);
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

        if(func.paramList()!=null) {
            if (func.paramList().IDENTIFIER().size() != ctx.expressionList().expression().size())
            {
                throw new BULVEFunctionParametersExeption(funcName);
            }
        }
        else {
            if(ctx.expressionList()!=null)
            {
                throw new BULVEFunctionParametersExeption(funcName);
            }
        }
        List<Object> args = new ArrayList<>();
        if(ctx.expressionList()!=null)
        {
            for(var exp : ctx.expressionList().expression())
            {
                args.add(this.visit(exp));
            }
        }
        BULVEScope functionScope = new BULVEScope();
        if(func.paramList()!=null) {
            for (int i = 0; i < func.paramList().IDENTIFIER().size(); i++) {
                String type = visitType(func.paramList().type(i)).toString();
                String paramName = func.paramList().IDENTIFIER(i).getText();
                Object arg = args.get(i);
                if(paramName.charAt(0)=='&')
                {
                    String identReal = paramName.substring(1);
                    specialArgs.add(identReal);
                    if(type.equals("siulas")) {
                        if(arg instanceof String) {
                            functionScope.declareVariable(identReal, args.get(i));
                        }
                        else throw new BULVEBadTypeException(identReal);}
                    else if(type.equals("sveikuolis")) {
                        if(arg instanceof Integer) {
                            functionScope.declareVariable(identReal, args.get(i));
                        }
                        else throw new BULVEBadTypeException(identReal);}
                    else if(type.equals("dvigubas")) {
                        if(arg instanceof Double){
                            functionScope.declareVariable(identReal, args.get(i));
                        }
                        else throw new BULVEBadTypeException(identReal);}
                    else if(type.equals("artikrai")) {
                        if(arg instanceof Boolean) {
                            functionScope.declareVariable(identReal, args.get(i));
                        }
                        else throw new BULVEBadTypeException(identReal);}
                    else throw new BULVEFunctionParametersExeption(funcName);
                }
                else {
                    if (type.equals("siulas")) {
                        if (arg instanceof String) {
                            functionScope.declareVariable(paramName, arg);
                        } else throw new BULVEBadTypeException(paramName);
                    } else if (type.equals("sveikuolis")) {
                        if (arg instanceof Integer) {
                            functionScope.declareVariable(paramName, arg);
                        } else throw new BULVEBadTypeException(paramName);
                    } else if (type.equals("dvigubas")) {
                        if (arg instanceof Double) {
                            functionScope.declareVariable(paramName, arg);
                        } else throw new BULVEBadTypeException(paramName);
                    } else if (type.equals("artikrai")) {
                        if (arg instanceof Boolean) {
                            functionScope.declareVariable(paramName, arg);
                        } else throw new BULVEBadTypeException(paramName);
                    }
                    else throw new BULVEFunctionParametersExeption(funcName);
                }
            }
        }
        scopeStack.push(currentScope);
        currentScope = functionScope;
        returnValue value = (returnValue)this.visitFunctionBody(func.functionBody());
        specialArgs.clear();
        currentScope = scopeStack.pop();

        return value.getValue();
    }

    @Override
    public Object visitReturnStatment(BULVEParser.ReturnStatmentContext ctx) {
        if(ctx.expression()==null)
        {
            return new returnValue(null);
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
