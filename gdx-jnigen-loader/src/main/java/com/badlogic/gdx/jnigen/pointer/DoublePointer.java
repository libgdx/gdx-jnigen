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
        return Double.longBitsToDouble(CHandler.getPointerPart(getPointer(), __double_size, index * __double_size));
    }

    public void setDouble(double value, int index) {
        CHandler.setPointerPart(getPointer(), __double_size, index * __double_size, Double.doubleToLongBits(value));
    }
}
