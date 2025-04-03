package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

public class SBytePointer extends VoidPointer {
    private static final int BYTE_SIZE = 1;

    public SBytePointer(int count, boolean freeOnGC) {
        super(count * BYTE_SIZE, freeOnGC);
    }

    public SBytePointer() {
        this(1);
    }

    public SBytePointer(int count) {
        super(count * BYTE_SIZE);
    }

    public SBytePointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public SBytePointer guardCount(long count) {
        super.guardBytes(count * BYTE_SIZE);
        return this;
    }

    private int calculateOffset(int index) {
        int offset = index * BYTE_SIZE;
        assertBounds(offset);
        return offset;
    }

    public boolean getBoolean() {
        return getBoolean(0);
    }

    public boolean getBoolean(int index) {
        return getBufPtr().getBoolean(calculateOffset(index));
    }

    public void setBoolean(boolean value) {
        setBoolean(value, 0);
    }

    public void setBoolean(boolean value, int index) {
        getBufPtr().setBoolean(calculateOffset(index), value);
    }

    public byte getByte() {
        return getByte(0);
    }

    public byte getByte(int index) {
        return getBufPtr().getByte(calculateOffset(index));
    }

    public void setByte(byte value) {
        setByte(value, 0);
    }

    public void setByte(byte value, int index) {
        getBufPtr().setByte(calculateOffset(index), value);
    }
}
