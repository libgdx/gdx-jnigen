package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.gdx.jnigen.runtime.util.Utils;

/**
 * This class represents a `char` pointer.
 * The C standard doesn't specify, if a `char` is signed or not.
 * Caution is needed.
 */
public class BytePointer extends VoidPointer {

    private static final int BYTE_SIZE = 1;
    private static final boolean IS_CHAR_SIGNED = CHandler.IS_CHAR_SIGNED;

    public BytePointer(VoidPointer pointer) {
        super(pointer);
    }

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

    public BytePointer(long pointer, boolean freeOnGC, int capacity) {
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
        BytePointer pointer = new BytePointer(string.getBytes().length + 1, freeOnGC);
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
