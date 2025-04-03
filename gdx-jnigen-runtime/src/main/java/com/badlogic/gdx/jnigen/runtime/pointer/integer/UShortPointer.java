package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

public class UShortPointer extends VoidPointer {

    private static final int BYTE_SIZE = 2;

    public UShortPointer(int count, boolean freeOnGC) {
        super(count * BYTE_SIZE, freeOnGC);
    }

    public UShortPointer() {
        this(1);
    }

    public UShortPointer(int count) {
        super(count * BYTE_SIZE);
    }

    public UShortPointer(long pointer, boolean freeOnGC, int capacity) {
        super(pointer, freeOnGC, capacity * BYTE_SIZE);
    }

    public char getUShort() {
        return getUShort(0);
    }

    public char getUShort(int index) {
        return getBufPtr().getChar(index * BYTE_SIZE);
    }

    public void setUShort(char value) {
        setUShort(value, 0);
    }

    public void setUShort(char value, int index) {
        getBufPtr().setChar(index * BYTE_SIZE, value);
    }
}
