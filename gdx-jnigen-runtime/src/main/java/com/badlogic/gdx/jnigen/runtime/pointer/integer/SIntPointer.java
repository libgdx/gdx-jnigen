package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

/**
 * This represents an `int` pointer.
 */
public class SIntPointer extends VoidPointer {

    private static final int BYTE_SIZE = 4;

    public SIntPointer(VoidPointer pointer) {
        super(pointer);
    }

    public SIntPointer(int count, boolean freeOnGC) {
        super(count * BYTE_SIZE, freeOnGC);
    }

    public SIntPointer() {
        this(1);
    }

    public SIntPointer(int count) {
        super(count * BYTE_SIZE);
    }

    public SIntPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public SIntPointer(long pointer, boolean freeOnGC, int capacity) {
        super(pointer, freeOnGC, capacity * BYTE_SIZE);
    }

    public int getInt() {
        return getInt(0);
    }

    public int getInt(int index) {
        return getBufPtr().getInt(index * BYTE_SIZE);
    }

    public void setInt(int value) {
        setInt(value, 0);
    }

    public void setInt(int value, int index) {
        getBufPtr().setInt(index * BYTE_SIZE, value);
    }
}
