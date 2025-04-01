package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

public class SShortPointer extends VoidPointer {

    private static final int BYTE_SIZE = 2;

    public SShortPointer(int count, boolean freeOnGC, boolean guard) {
        super(count * BYTE_SIZE, freeOnGC, guard);
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

    public SShortPointer guardCount(long count) {
        super.guardBytes(count * BYTE_SIZE);
        return this;
    }

    private int calculateOffset(int index) {
        int offset = index * BYTE_SIZE;
        assertBounds(offset);
        return offset;
    }

    public short getShort() {
        return getShort(0);
    }

    public short getShort(int index) {
        return (short)CHandler.getPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index));
    }

    public void setShort(short value) {
        setShort(value, 0);
    }

    public void setShort(short value, int index) {
        CHandler.setPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index), value);
    }
}
