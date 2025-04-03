package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.gdx.jnigen.runtime.util.Utils;

public class ULongPointer extends VoidPointer {

    private static final int BYTE_SIZE = CHandler.LONG_SIZE;

    public ULongPointer(int count, boolean freeOnGC) {
        super(count * BYTE_SIZE, freeOnGC);
    }

    public ULongPointer() {
        this(1);
    }

    public ULongPointer(int count) {
        super(count * BYTE_SIZE);
    }

    public ULongPointer(long pointer, boolean freeOnGC, int capacity) {
        super(pointer, freeOnGC, capacity * BYTE_SIZE);
    }

    public long getLong() {
        return getLong(0);
    }

    public long getLong(int index) {
        return getBufPtr().getNativeULong(index * BYTE_SIZE);
    }

    public void setLong(long value) {
        setLong(value, 0);
    }

    public void setLong(long value, int index) {
        if (Utils.checkBoundsForNumber(value, BYTE_SIZE, false))
            throw new IllegalArgumentException("SLong out of range: " + value);
        getBufPtr().setNativeULong(index * BYTE_SIZE, value);
    }
}
