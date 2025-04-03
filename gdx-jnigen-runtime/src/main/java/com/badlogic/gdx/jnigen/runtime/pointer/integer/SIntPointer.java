package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

public class SIntPointer extends VoidPointer {

    private static final int BYTE_SIZE = 4;

    public SIntPointer(int count, boolean freeOnGC) {
        super(count * BYTE_SIZE, freeOnGC);
    }

    public SIntPointer() {
        this(1);
    }

    public SIntPointer(int count) {
        super(count * BYTE_SIZE);
    }

    public SIntPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public SIntPointer guardCount(long count) {
        super.guardBytes(count * BYTE_SIZE);
        return this;
    }

    private int calculateOffset(int index) {
        int offset = index * BYTE_SIZE;
        assertBounds(offset);
        return offset;
    }

    public int getInt() {
        return getInt(0);
    }

    public int getInt(int index) {
        return (int)CHandler.getPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index));
    }

    public void setInt(int value) {
        setInt(value, 0);
    }

    public void setInt(int value, int index) {
        CHandler.setPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index), value);
    }
}
