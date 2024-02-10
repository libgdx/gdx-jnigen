package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;

public final class FloatPointer extends Pointing {

    private static final int __float_size = 4;

    public FloatPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public FloatPointer(long size) {
        super(__float_size * size);
    }

    public float getFloat(int index) {
        return Float.intBitsToFloat((int)CHandler.getPointerPart(getPointer(), __float_size, index * __float_size));
    }

    public void setFloat(float value, int index) {
        CHandler.setPointerPart(getPointer(), __float_size, index * __float_size, Float.floatToIntBits(value));
    }
}
