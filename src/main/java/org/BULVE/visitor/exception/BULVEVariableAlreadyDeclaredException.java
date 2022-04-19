package org.BULVE.visitor.exception;

public class BULVEVariableAlreadyDeclaredException extends BULVEException {
    public BULVEVariableAlreadyDeclaredException(String variableNme) {
        super(String.format("Variable '%s' is already declared.", variableNme));
    }
}