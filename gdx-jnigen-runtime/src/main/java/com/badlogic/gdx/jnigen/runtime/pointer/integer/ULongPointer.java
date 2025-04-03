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

    public ULongPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public ULongPointer guardCount(long count) {
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
        return getBufPtr().getNativeULong(calculateOffset(index));
    }

    public void setLong(long value) {
        setLong(value, 0);
    }

    public void setLong(long value, int index) {
        if (Utils.checkBoundsForNumber(value, BYTE_SIZE, false))
            throw new IllegalArgumentException("SLong out of range: " + value);
        getBufPtr().setNativeULong(calculateOffset(index), value);
    }
}
