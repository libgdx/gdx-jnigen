package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

public class SBytePointer extends VoidPointer {
    private static final int BYTE_SIZE = 1;

    public SBytePointer(int count, boolean freeOnGC, boolean guard) {
        super(count * BYTE_SIZE, freeOnGC, guard);
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

    public SBytePointer guardCount(long count) {
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

    public byte getByte() {
        return getByte(0);
    }

    public byte getByte(int index) {
        return (byte)CHandler.getPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index));
    }

    public void setByte(byte value) {
        setByte(value, 0);
    }

    public void setByte(byte value, int index) {
        CHandler.setPointerPart(getPointer(), BYTE_SIZE, calculateOffset(index), value);
    }

    public static SBytePointer fromString(String string, boolean freeOnGC) {
        SBytePointer pointer = new SBytePointer(string.length() + 1, freeOnGC, true);
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
