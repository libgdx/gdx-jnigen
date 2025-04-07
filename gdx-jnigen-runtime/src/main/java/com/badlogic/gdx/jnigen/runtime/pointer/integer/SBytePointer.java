package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

public class SBytePointer extends VoidPointer {
    private static final int BYTE_SIZE = 1;

    public SBytePointer(int count, boolean freeOnGC) {
        super(count * BYTE_SIZE, freeOnGC);
    }

    public SBytePointer() {
        this(1);
    }

    public SBytePointer(int count) {
        super(count * BYTE_SIZE);
    }

    public SBytePointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public SBytePointer(long pointer, boolean freeOnGC, int capacity) {
        super(pointer, freeOnGC, capacity * BYTE_SIZE);
    }

    public boolean getBoolean() {
        return getBoolean(0);
    }

    public boolean getBoolean(int index) {
        return getBufPtr().getBoolean(index * BYTE_SIZE);
    }

    public void setBoolean(boolean value) {
        setBoolean(value, 0);
    }

    public void setBoolean(boolean value, int index) {
        getBufPtr().setBoolean(index * BYTE_SIZE, value);
    }

    public byte getByte() {
        return getByte(0);
    }

    public byte getByte(int index) {
        return getBufPtr().getByte(index * BYTE_SIZE);
    }

    public void setByte(byte value) {
        setByte(value, 0);
    }

    public void setByte(byte value, int index) {
        getBufPtr().setByte(index * BYTE_SIZE, value);
    }

    public static SBytePointer fromString(String string, boolean freeOnGC) {
        SBytePointer pointer = new SBytePointer(string.getBytes().length + 1, freeOnGC);
        pointer.setString(string);
        return pointer;
    }

    public void setString(String string) {
        getBufPtr().setString(string);
    }

    public String getString() {
        return getBufPtr().getString();
    }
}
