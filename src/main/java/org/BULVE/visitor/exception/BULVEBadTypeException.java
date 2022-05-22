package org.BULVE.visitor.exception;

public class BULVEBadTypeException extends IllegalArgumentException{ //nullponter veliau
    public BULVEBadTypeException(String variableNme) {
        super(String.format(" Variable '%s' type doesn't match it's value.", variableNme));
    }
}
