package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;

public final class FloatPointer extends Pointing {

    private static final int __float_size = 4;

    public FloatPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public FloatPointer() {
        this(1);
    }

    public FloatPointer(int size) {
        this(size, true, true);
    }

    public FloatPointer(int size, boolean freeOnGC, boolean guard) {
        super(__float_size * size, freeOnGC, guard);
    }

    public FloatPointer guardCount(long count) {
        super.guardBytes(count * __float_size);
        return this;
    }

    public float getFloat() {
        return getFloat(0);
    }

    public float getFloat(int index) {
        int offset = index * __float_size;
        assertBounds(offset);
        return Float.intBitsToFloat((int)CHandler.getPointerPart(getPointer(), __float_size, offset));
    }

    public void setFloat(float value) {
        setFloat(value, 0);
    }

    public void setFloat(float value, int index) {
        int offset = index * __float_size;
        assertBounds(offset);
        CHandler.setPointerPart(getPointer(), __float_size, offset, Float.floatToIntBits(value));
    }
}
