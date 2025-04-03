package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.gdx.jnigen.runtime.util.Utils;

public class SLongPointer extends VoidPointer {

    private static final int BYTE_SIZE = CHandler.LONG_SIZE;

    public SLongPointer(int count, boolean freeOnGC) {
        super(count * BYTE_SIZE, freeOnGC);
    }

    public SLongPointer() {
        this(1);
    }

    public SLongPointer(int count) {
        super(count * BYTE_SIZE);
    }

    public SLongPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public SLongPointer guardCount(long count) {
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
        return getBufPtr().getNativeLong(calculateOffset(index));
    }

    public void setLong(long value) {
        setLong(value, 0);
    }

    public void setLong(long value, int index) {
        if (Utils.checkBoundsForNumber(value, BYTE_SIZE, true))
            throw new IllegalArgumentException("SLong out of range: " + value);
        getBufPtr().setNativeLong(calculateOffset(index), value);
    }
}
