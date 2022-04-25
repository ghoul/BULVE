package org.BULVE.visitor;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Stack;

public class BULVEVisitorImpl extends BULVEBaseVisitor<Object> {

    private final StringBuilder SYSTEM_OUT = new StringBuilder();

    private final Stack<BULVEScope> scopeStack = new Stack<>();

    private BULVEScope currentScope = new BULVEScope();

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
        //TODO implement other types
        return null;
    }

    @Override
    public Object visitVariableDeclaration(BULVEParser.VariableDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        Object value = visit(ctx.expression()); //cia expression, o jei integer butent?
        this.currentScope.declareVariable(varName, value);
        return null;
    }

    @Override
    public Object visitIntegerDeclaration(BULVEParser.IntegerDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        Integer value = Integer.parseInt(ctx.INTEGER().getText()) ;
        this.currentScope.declareVariable(varName, value);
        return null;
    }

    @Override
    public Object visitStringDeclaration(BULVEParser.StringDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        String value = ctx.STRING().toString() ;
        this.currentScope.declareVariable(varName, value);
        return null;
    }

    @Override
    public Object visitAssignment(BULVEParser.AssignmentContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        Object value = visit(ctx.expression()); //jeigu pvz a=a-1, tai vistiek visada i a eis, kaip cia rekursija padaryt?
        this.currentScope.changeVariable(varName, value);
        return null;
    }

    @Override
    public Object visitDecimalDeclaration(BULVEParser.DecimalDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        Double value = Double.parseDouble(ctx.DECIMAL().toString());
        this.currentScope.declareVariable(varName, value);
        return null;
    }

    @Override
    public Object visitBoolDeclaration(BULVEParser.BoolDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        Boolean value = Boolean.parseBoolean( ctx.BOOLEAN().toString());
        this.currentScope.declareVariable(varName, value);
        return null;
    }

    /* int a = 1;
    @Override
    public Object visitAssignmentExpression(BULVEParser.AssignmentExpressionContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        Integer value = (Integer)visit(ctx.expression(0));
        if(visit(ctx.expression(a))!=null)
        {
            a++;
            visitAssignmentExpression(ctx);
        }
        return null;
    }*/

    @Override
    public Object visitIdentifierExpression(BULVEParser.IdentifierExpressionContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        return this.currentScope.resolveVariable(varName);
    }

    @Override
    public Object visitNumericAddOpExpression(BULVEParser.NumericAddOpExpressionContext ctx) {
        Object val1 = visit(ctx.expression(0));
        Object val2 = visit(ctx.expression(1));
        //TODO - validation etc
        return switch (ctx.numericAddOp().getText()) {
            case "+" -> (Integer) val1 + (Integer) val2;
            case "-" -> (Integer) val1 - (Integer) val2;
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
            visit(ctx.block(0));
        } else {
            visit(ctx.block(1));
        }
        return null;
    }

    @Override
    public Object visitBlock(BULVEParser.BlockContext ctx) {
        scopeStack.push(currentScope);
        currentScope = new BULVEScope(currentScope);
        super.visitBlock(ctx);
        currentScope = scopeStack.pop();
        return null;
    }

    @Override
    public Object visitParenthesesExpression(BULVEParser.ParenthesesExpressionContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Object visitKolStatement(BULVEParser.KolStatementContext ctx) {
        boolean value = (Boolean) visit(ctx.expression());
        if (value) {
            visit(ctx.block());
            visitKolStatement(ctx);
        } else {
            return null;
        }
        return null;
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
}
