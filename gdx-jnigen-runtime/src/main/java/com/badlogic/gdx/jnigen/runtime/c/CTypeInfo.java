package com.badlogic.gdx.jnigen.runtime.c;

import com.badlogic.gdx.jnigen.runtime.util.Utils;

public final class CTypeInfo {

    private final long ffiType;
    private final int size;
    private final boolean signed;
    private final boolean stackElement;
    private final boolean isVoid;

    public CTypeInfo(long ffiType, int size, boolean signed, boolean stackElement, boolean isVoid) {
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
