package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;

public final class DoublePointer extends Pointing {

    private static final int __double_size = 8;

    public DoublePointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public DoublePointer(long size) {
        super(__double_size * size);
    }

    public double getDouble(int index) {
        int offset = index * __double_size;
        assertBounds(offset);
        return Double.longBitsToDouble(CHandler.getPointerPart(getPointer(), __double_size, offset));
    }

    public void setDouble(double value, int index) {
        int offset = index * __double_size;
        assertBounds(offset);
        CHandler.setPointerPart(getPointer(), __double_size, offset, Double.doubleToLongBits(value));
    }
}
