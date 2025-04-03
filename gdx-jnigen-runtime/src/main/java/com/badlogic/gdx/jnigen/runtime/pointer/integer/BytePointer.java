package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.gdx.jnigen.runtime.util.Utils;

public class BytePointer extends VoidPointer {

    private static final int BYTE_SIZE = 1;
    private static final boolean IS_CHAR_SIGNED = CHandler.IS_CHAR_SIGNED;

    public BytePointer(int count, boolean freeOnGC) {
        super(count * BYTE_SIZE, freeOnGC);
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
        return getBufPtr().getBoolean(calculateOffset(index));
    }

    public void setBoolean(boolean value) {
        setBoolean(value, 0);
    }

    public void setBoolean(boolean value, int index) {
        getBufPtr().setBoolean(calculateOffset(index), value);
    }

    public byte getByte() {
        return getByte(0);
    }

    public byte getByte(int index) {
        return getBufPtr().getByte(calculateOffset(index));
    }

    public void setByte(byte value) {
        setByte(value, 0);
    }

    public void setByte(byte value, int index) {
        if (IS_CHAR_SIGNED)
            setByte((char) value, index);
        else
            setByte((char)(value & 0xFF), index);
    }

    public void setByte(char value) {
        setByte(value, 0);
    }

    public void setByte(char value, int index) {
        if (Utils.checkBoundsForNumber(value, BYTE_SIZE, IS_CHAR_SIGNED))
            throw new IllegalArgumentException("Byte out of range: " + value);
        getBufPtr().setByte(index, (byte) value);
    }

    public static BytePointer fromString(String string, boolean freeOnGC) {
        BytePointer pointer = new BytePointer(string.length() + 1, freeOnGC);
        pointer.setString(string);
        return pointer;
    }

    // TODO: 01.04.2025 Probably not belongs here
    public void setString(String string) {
        // TODO: 21.06.24 is that sane?
        assertBounds(string.length());
        CHandler.setPointerAsString(getPointer(), string);
    }

    public String getString() {
        if (isNull())
            return null;
        return CHandler.getPointerAsString(getPointer());
    }
}
