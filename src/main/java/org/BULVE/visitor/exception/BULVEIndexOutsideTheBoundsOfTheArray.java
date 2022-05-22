package org.BULVE.visitor.exception;

public class BULVEIndexOutsideTheBoundsOfTheArray extends IndexOutOfBoundsException{
    public BULVEIndexOutsideTheBoundsOfTheArray(String arrName) {
        super(String.format(" Array's '%s' index was outside the bounds", arrName));
    }
}
