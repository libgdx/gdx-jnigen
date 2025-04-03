package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

public class UShortPointer extends VoidPointer {

    private static final int BYTE_SIZE = 2;

    public UShortPointer(int count, boolean freeOnGC, boolean guard) {
        super(count * BYTE_SIZE, freeOnGC, guard);
    }

    public UShortPointer() {
        this(1);
    }

    public UShortPointer(int count) {
        super(count * BYTE_SIZE);
    }

    public UShortPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public UShortPointer guardCount(long count) {
        super.guardBytes(count * BYTE_SIZE);
        return this;
    }

    private int calculateOffset(int index) {
        int offset = index * BYTE_SIZE;
        assertBounds(offset);
        return offset;
    }

    public char getUShort() {
        return getUShort(0);
    }

    public char getUShort(int index) {
        return (char)CHandler.getPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index));
    }

    public void setUShort(char value) {
        setUShort(value, 0);
    }

    public void setUShort(char value, int index) {
        CHandler.setPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index), value);
    }
}
