package com.badlogic.gdx.jnigen.runtime.c;

import com.badlogic.gdx.jnigen.runtime.util.Utils;

public final class CTypeInfo {

    private final String name;
    private final long ffiType;
    private final int size;
    private final boolean signed;
    private final boolean stackElement;
    private final boolean isVoid;

    public CTypeInfo(String name, long ffiType, int size, boolean signed, boolean stackElement, boolean isVoid) {
        this.name = name;
        this.ffiType = ffiType;
        this.size = size;
        this.signed = signed;
        this.stackElement = stackElement;
        this.isVoid = isVoid;
    }

    public void assertBounds(long value) {
        if (!stackElement && !Utils.checkBoundsForNumber(value, size, signed))
            throw new IllegalArgumentException("Value " + value + " exceeds bounds " + size + " with signess " + signed);
    }

    public void assertCanHoldByte() {
        if (size > 1 || !signed)
            throw new IllegalArgumentException("CType " + name + " would overflow byte");
    }

    public void assertCanHoldShort() {
        if (size > 2 || !signed && size == 2)
            throw new IllegalArgumentException("CType " + name + " would overflow an short");
    }

    public void assertCanHoldChar() {
        if (size > 2 || signed)
            throw new IllegalArgumentException("CType " + name + " would overflow a char");
    }

    public void assertCanHoldInt() {
        if (size > 4 || !signed && size == 4)
            throw new IllegalArgumentException("CType " + name + " would overflow an int");
    }

    public void assertConformsTo(String typeNameToCheck) {
        if (name == null)
            throw new IllegalArgumentException("CType has no name");
        // TODO: 21.06.2024 This is stupid
        if (!typeNameToCheck.replace("const ", "").equals(name))
            throw new IllegalArgumentException("Expected type " + typeNameToCheck + " does not match actual type " + name);
    }

    public String getName() {
        if (name == null)
            throw new IllegalArgumentException("CType has no name");
        return name;
    }

    public long getFfiType() {
        return ffiType;
    }

    public int getSize() {
        return size;
    }

    public boolean isSigned() {
        return signed;
    }

    public boolean isStackElement() {
        return stackElement;
    }

    public boolean isVoid() {
        return isVoid;
    }
}
