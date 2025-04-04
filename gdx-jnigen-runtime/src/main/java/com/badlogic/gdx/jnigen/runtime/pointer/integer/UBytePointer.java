package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

public class UBytePointer extends VoidPointer {

    private static final int BYTE_SIZE = 1;

    public UBytePointer(int count, boolean freeOnGC, boolean guard) {
        super(count * BYTE_SIZE, freeOnGC, guard);
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

    public UBytePointer guardCount(long count) {
        super.guardBytes(count * BYTE_SIZE);
        return this;
    }

    private int calculateOffset(int index) {
        int offset = index * BYTE_SIZE;
        assertBounds(offset);
        return offset;
    }

    public boolean getBoolean() {
        return getBoolean(0);
    }

    public boolean getBoolean(int index) {
        return CHandler.getPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index)) != 0;
    }

    public void setBoolean(boolean value) {
        setBoolean(value, 0);
    }

    public void setBoolean(boolean value, int index) {
        CHandler.setPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index), value ? 1 : 0);
    }

    public char getUByte() {
        return getUByte(0);
    }

    public char getUByte(int index) {
        return (char)CHandler.getPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index));
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
        CHandler.setPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index), value);
    }
}
