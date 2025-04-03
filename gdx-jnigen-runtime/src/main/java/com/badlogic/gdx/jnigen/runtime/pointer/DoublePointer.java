package com.badlogic.gdx.jnigen.runtime.pointer;

public final class DoublePointer extends VoidPointer {

    private static final int __double_size = 8;

    public DoublePointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public DoublePointer(long pointer, boolean freeOnGC, int capacity) {
        super(pointer, freeOnGC, capacity * __double_size);
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

    public double getDouble() {
        return getDouble(0);
    }

    public double getDouble(int index) {
        int offset = index * __double_size;
        return getBufPtr().getDouble(offset);
    }

    public void setDouble(double value) {
        setDouble(value, 0);
    }

    public void setDouble(double value, int index) {
        int offset = index * __double_size;
        getBufPtr().setDouble(offset, value);
    }
}
