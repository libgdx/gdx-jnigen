package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

public class UIntPointer extends VoidPointer {

    private static final int BYTE_SIZE = 4;

    public UIntPointer(int count, boolean freeOnGC) {
        super(count * BYTE_SIZE, freeOnGC);
    }

    public UIntPointer() {
        this(1);
    }

    public UIntPointer(int count) {
        super(count * BYTE_SIZE);
    }

    public UIntPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public UIntPointer(long pointer, boolean freeOnGC, int capacity) {
        super(pointer, freeOnGC, capacity * BYTE_SIZE);
    }

    public long getUInt() {
        return getUInt(0);
    }

    public long getUInt(int index) {
        return getBufPtr().getInt(index * BYTE_SIZE) & 0xFFFFFFFFL;
    }

    public void setUInt(int value) {
        setUInt(value & 0xFFFFFFFFL, 0);
    }

    public void setUInt(int value, int index) {
        setUInt(value & 0xFFFFFFFFL, index);
    }

    public void setUInt(long value) {
        setUInt(value, 0);
    }

    public void setUInt(long value, int index) {
        if (value >= 1L << (BYTE_SIZE * 8))
            throw new IllegalArgumentException("UInt out of range: " + value);
        getBufPtr().setInt(index * BYTE_SIZE, (int)value);
    }
}
