package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

public class UInt64Pointer extends VoidPointer {

    private static final int BYTE_SIZE = 8;

    public UInt64Pointer(int count, boolean freeOnGC, boolean guard) {
        super(count * BYTE_SIZE, freeOnGC, guard);
    }

    public UInt64Pointer() {
        this(1);
    }

    public UInt64Pointer(int count) {
        super(count * BYTE_SIZE);
    }

    public UInt64Pointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public UInt64Pointer guardCount(long count) {
        super.guardBytes(count * BYTE_SIZE);
        return this;
    }

    private int calculateOffset(int index) {
        int offset = index * BYTE_SIZE;
        assertBounds(offset);
        return offset;
    }

    public long getLong() {
        return getLong(0);
    }

    public long getLong(int index) {
        return CHandler.getPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index));
    }

    public void setLong(long value) {
        setLong(value, 0);
    }

    public void setLong(long value, int index) {
        CHandler.setPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index), value);
    }
}
