package org.BULVE.visitor.exception;

public class BULVEVariableNotDeclaredException extends BULVEException {
    public BULVEVariableNotDeclaredException(String variableNme) {
        super(String.format("Variable '%s' is not declared.", variableNme));
    }
}
