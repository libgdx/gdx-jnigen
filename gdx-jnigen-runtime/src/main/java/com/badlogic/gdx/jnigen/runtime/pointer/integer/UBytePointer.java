package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * This represents an `unsigned char` pointer.
 * Java cannot represent unsigned char (1 byte) natively, the closest is an unsigned java char.
 * Caution is needed
 */
public class UBytePointer extends VoidPointer {

    private static final int BYTE_SIZE = 1;

    public UBytePointer(VoidPointer pointer) {
        super(pointer);
    }

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
        return getBufPtr().getUByte(index * BYTE_SIZE);
    }

    public void setUByte(byte value) {
        setUByte(value, 0);
    }

    public void setUByte(byte value, int index) {
        getBufPtr().setUByte(index * BYTE_SIZE, value);
    }

    public void setUByte(char value) {
        setUByte(value, 0);
    }

    public void setUByte(char value, int index) {
        getBufPtr().setUByte(index * BYTE_SIZE, (byte)value);
    }

    public static UBytePointer fromString(String string, boolean freeOnGC) {
        return fromString(string, StandardCharsets.UTF_8, freeOnGC);
    }

    public static UBytePointer fromString(String string, Charset charset, boolean freeOnGC) {
        byte[] bytes = string.getBytes(charset);
        UBytePointer pointer = new UBytePointer(bytes.length + 1, freeOnGC);
        BufferPtr bufPtr = pointer.getBufPtr();
        bufPtr.setBytes(bytes);
        bufPtr.setByte(bytes.length, (byte)0);
        return pointer;
    }

    public void setString(String string) {
        getBufPtr().setString(string);
    }

    public void setString(String string, Charset charset) {
        getBufPtr().setString(string, charset);
    }

    public String getString() {
        return getBufPtr().getString();
    }

    public String getString(Charset charset) {
        return getBufPtr().getString(charset);
    }
}
