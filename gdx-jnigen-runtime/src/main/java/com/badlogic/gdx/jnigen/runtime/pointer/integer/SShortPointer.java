package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

public class SShortPointer extends VoidPointer {

    private static final int BYTE_SIZE = 2;

    public SShortPointer(int count, boolean freeOnGC) {
        super(count * BYTE_SIZE, freeOnGC);
    }

    public SShortPointer() {
        this(1);
    }

    public SShortPointer(int count) {
        super(count * BYTE_SIZE);
    }

    public SShortPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public SShortPointer(long pointer, boolean freeOnGC, int capacity) {
        super(pointer, freeOnGC, capacity * BYTE_SIZE);
    }

    public short getShort() {
        return getShort(0);
    }

    public short getShort(int index) {
        return getBufPtr().getShort(index * BYTE_SIZE);
    }

    public void setShort(short value) {
        setShort(value, 0);
    }

    public void setShort(short value, int index) {
        getBufPtr().setShort(index * BYTE_SIZE, value);
    }
}
