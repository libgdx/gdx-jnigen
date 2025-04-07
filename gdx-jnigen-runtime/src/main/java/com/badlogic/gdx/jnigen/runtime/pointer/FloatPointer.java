package com.badlogic.gdx.jnigen.runtime.pointer;

public final class FloatPointer extends VoidPointer {

    private static final int __float_size = 4;

    public FloatPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public FloatPointer(long pointer, boolean freeOnGC, int capacity) {
        super(pointer, freeOnGC, capacity * __float_size);
    }

    public FloatPointer() {
        this(1);
    }

    public FloatPointer(int size) {
        this(size, true);
    }

    public FloatPointer(int size, boolean freeOnGC) {
        super(__float_size * size, freeOnGC);
    }


    public float getFloat() {
        return getFloat(0);
    }

    public float getFloat(int index) {
        int offset = index * __float_size;
        return getBufPtr().getFloat(offset);
    }

    public void setFloat(float value) {
        setFloat(value, 0);
    }

    public void setFloat(float value, int index) {
        int offset = index * __float_size;
        getBufPtr().setFloat(offset, value);
    }
}
