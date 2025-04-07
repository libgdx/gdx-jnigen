package com.badlogic.gdx.jnigen.runtime.c;

public final class CTypeInfo {

    private final long ffiType;
    private final int size;

    public CTypeInfo(long ffiType, int size) {
        this.ffiType = ffiType;
        this.size = size;
    }

    public long getFfiType() {
        return ffiType;
    }

    public int getSize() {
        return size;
    }
}
