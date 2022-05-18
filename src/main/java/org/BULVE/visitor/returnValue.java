package org.BULVE.visitor;

//sita klase saugos value, kurios reiksme rodys ar jau pabaiga programos ar ne
//ar sutiko return sakini ar ne
public class returnValue {
    private final Object value; ///niekada nesikeis

    public returnValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
