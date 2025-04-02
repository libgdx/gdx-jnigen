package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.gdx.jnigen.runtime.util.Utils;

public class BytePointer extends VoidPointer {

    private static final int BYTE_SIZE = 1;
    private static final boolean IS_CHAR_SIGNED = CHandler.IS_CHAR_SIGNED;

    public BytePointer(int count, boolean freeOnGC, boolean guard) {
        super(count * BYTE_SIZE, freeOnGC, guard);
    }

    public BytePointer() {
        this(1);
    }

    public BytePointer(int count) {
        super(count * BYTE_SIZE);
    }

    public BytePointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public BytePointer guardCount(long count) {
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
        if (IS_CHAR_SIGNED)
            return (char) (byte)CHandler.getPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index));
        else
            return (char) CHandler.getPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index));
    }

    public void setUByte(byte value) {
        setUByte(value, 0);
    }

    public void setUByte(byte value, int index) {
        if (IS_CHAR_SIGNED)
            setUByte((char) value, index);
        else
            setUByte((char)(value & 0xFF), index);
    }

    public void setUByte(char value) {
        setUByte(value, 0);
    }

    public void setUByte(char value, int index) {
        if (Utils.checkBoundsForNumber(value, BYTE_SIZE, IS_CHAR_SIGNED))
            throw new IllegalArgumentException("Byte out of range: " + value);
        CHandler.setPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index), value);
    }
}
