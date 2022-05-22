package org.BULVE.visitor.exception;

public class BULVEArrayDeclarationExeption extends IllegalArgumentException{
    public BULVEArrayDeclarationExeption(String arrName)
    {
        super(String.format(" Array '%s' size was in incorrect form", arrName));
    }
}