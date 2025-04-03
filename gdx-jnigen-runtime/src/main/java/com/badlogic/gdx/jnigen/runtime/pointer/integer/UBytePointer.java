package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

public class UBytePointer extends VoidPointer {

    private static final int BYTE_SIZE = 1;

    public UBytePointer(int count, boolean freeOnGC) {
        super(count * BYTE_SIZE, freeOnGC);
    }

    public UBytePointer() {
        this(1);
    }

    public UBytePointer(int count) {
        super(count * BYTE_SIZE);
    }

    public UBytePointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public UBytePointer(long pointer, boolean freeOnGC, int capacity) {
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

    public char getUByte() {
        return getUByte(0);
    }

    public char getUByte(int index) {
        return (char)(getBufPtr().getByte(index * BYTE_SIZE) & 0xFF);
    }

    public void setUByte(byte value) {
        setUByte((char)(value & 0xFF), 0);
    }

    public void setUByte(byte value, int index) {
        setUByte((char)(value & 0xFF), index);
    }

    public void setUByte(char value) {
        setUByte(value, 0);
    }

    public void setUByte(char value, int index) {
        if (value >= 1L << (BYTE_SIZE * 8))
            throw new IllegalArgumentException("UByte out of range: " + value);
        getBufPtr().setByte(index * BYTE_SIZE, (byte)value);
    }
}
