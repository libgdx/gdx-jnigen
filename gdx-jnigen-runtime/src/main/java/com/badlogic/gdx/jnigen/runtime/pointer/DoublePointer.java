package com.badlogic.gdx.jnigen.runtime.pointer;

import com.badlogic.gdx.jnigen.runtime.CHandler;

public final class DoublePointer extends VoidPointer {

    private static final int __double_size = 8;

    public DoublePointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public DoublePointer() {
        this(1);
    }

    public DoublePointer(int size) {
        this(size, true);
    }

    public DoublePointer(int size, boolean freeOnGC) {
        super(__double_size * size, freeOnGC);
    }

    public DoublePointer guardCount(long count) {
        super.guardBytes(count * __double_size);
        return this;
    }

    public double getDouble() {
        return getDouble(0);
    }

    public double getDouble(int index) {
        int offset = index * __double_size;
        assertBounds(offset);
        return Double.longBitsToDouble(CHandler.getPointerPart(getPointer(), __double_size, offset));
    }

    public void setDouble(double value) {
        setDouble(value, 0);
    }

    public void setDouble(double value, int index) {
        int offset = index * __double_size;
        assertBounds(offset);
        CHandler.setPointerPart(getPointer(), __double_size, offset, Double.doubleToLongBits(value));
    }
}
