package org.BULVE.visitor;

import org.BULVE.visitor.exception.BULVEVariableAlreadyDeclaredException;
import org.BULVE.visitor.exception.BULVEVariableNotDeclaredException;

import java.util.HashMap;
import java.util.Map;

public class BULVEScope {

    private final BULVEScope parent;
    private final Map<String, Object> symbols = new HashMap<>();

    public BULVEScope() {
        this.parent = null;
    }

    public BULVEScope(BULVEScope parent) {
        this.parent = parent;
    }

    public void declareVariable(String variableName, Object value) {
        if (isDeclared(variableName)) {
            throw new BULVEVariableAlreadyDeclaredException(variableName);
        }
        symbols.put(variableName, value);
    }

    private boolean isDeclared(String variableName) {
        if (symbols.containsKey(variableName)) {
            return true;
        }
        return parent != null && parent.isDeclared(variableName);
    }

    public void changeVariable(String variableName, Object value) {
        if (!isDeclared(variableName)) {
            throw new BULVEVariableNotDeclaredException(variableName);
        }
        if (symbols.containsKey(variableName)) {
            symbols.put(variableName, value);
        } else {
            assert parent != null;
            parent.changeVariable(variableName, value);
        }
    }

    public Object resolveVariable(String variableName) {
        if (!isDeclared(variableName)) {
            throw new BULVEVariableNotDeclaredException(variableName);
        }
        if (symbols.containsKey(variableName)) {
            return symbols.get(variableName);
        } else {
            assert parent != null;
            return parent.resolveVariable(variableName);
        }
    }
}
