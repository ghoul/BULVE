package org.BULVE.visitor.exception;

public class BULVEBadTypeException extends IllegalArgumentException{
    public BULVEBadTypeException(String variableNme) {
        super(String.format("Variable '%s' has bad type.", variableNme));
    }
}
