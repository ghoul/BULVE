package org.BULVE.visitor.exception;

public class BULVEFunctionParametersExeption extends IllegalArgumentException {
    public BULVEFunctionParametersExeption(String funcName) {
        super(String.format(" Function '%s' parameters are in incorrect form", funcName));
    }
}
