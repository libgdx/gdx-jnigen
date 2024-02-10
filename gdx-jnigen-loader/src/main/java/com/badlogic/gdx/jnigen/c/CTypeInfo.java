package com.badlogic.gdx.jnigen.c;

import com.badlogic.gdx.jnigen.util.Utils;

public final class CTypeInfo {

    private final String name;
    private final long ffiType;
    private final int size;
    private final boolean signed;

    public CTypeInfo(String name, long ffiType, int size, boolean signed) {
        this.name = name;
        this.ffiType = ffiType;
        this.size = size;
        this.signed = signed;
    }

    public void assertBounds(long value) {
        if (!Utils.checkBoundsForNumber(value, size, signed))
            throw new IllegalArgumentException("Value " + value + " exceeds bounds " + size + " with signess " + signed);
    }

    public String getName() {
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
}
