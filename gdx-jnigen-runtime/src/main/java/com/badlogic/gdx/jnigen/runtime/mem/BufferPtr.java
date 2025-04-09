package com.badlogic.gdx.jnigen.runtime.mem;

import com.badlogic.gdx.jnigen.runtime.CHandler;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class BufferPtr {

    private ByteBuffer buffer;
    private long pointer;
    private int offset;
    private int capacity;

    public BufferPtr(ByteBuffer buffer, long pointer, int offset, int capacity) {
        this.buffer = buffer;
        this.pointer = pointer;
        this.offset = offset;
        this.capacity = capacity;
        if (capacity > buffer.capacity())
            throw new IllegalArgumentException("Buffer capacity (" + buffer.capacity() + ") exceeded by " + capacity + ". More then 1GB?");
    }

    void reset(ByteBuffer buffer, long pointer, int offset, int capacity) {
        this.buffer = buffer;
        this.pointer = pointer;
        this.offset = offset;
        this.capacity = capacity;
    }

    public void free() {
        if (buffer == null)
            throw new IllegalStateException("Buffer invalid (use-after-free?)");
        CHandler.free(pointer);
    }

    public void assertBounds(int expectedCapacity) {
        if (buffer == null)
            throw new IllegalStateException("Buffer invalid (use-after-free?)");
        if (capacity > 0 && (expectedCapacity < 0 || expectedCapacity > capacity))
            throw new IndexOutOfBoundsException("Index: " + expectedCapacity + ", Size: " + capacity);
    }

    public boolean getBoolean() {
        assertBounds(1);
        return buffer.get(offset) != 0;
    }

    public boolean getBoolean(int index) {
        assertBounds(index + 1);
        return buffer.get(offset + index) != 0;
    }

    public void setBoolean(boolean value) {
        assertBounds(1);
        buffer.put(offset, (byte)(value ? 1 : 0));
    }

    public void setBoolean(int index, boolean value) {
        assertBounds(index + 1);
        buffer.put(offset + index, (byte)(value ? 1 : 0));
    }

    public byte getByte() {
        assertBounds(1);
        return buffer.get(offset);
    }

    public byte getByte(int index) {
        assertBounds(index + 1);
        return buffer.get(offset + index);
    }

    public void setByte(byte value) {
        assertBounds(1);
        buffer.put(offset, value);
    }

    public void setByte(int index, byte value) {
        assertBounds(index + 1);
        buffer.put(offset + index, value);
    }

    public char getUByte() {
        assertBounds(1);
        return (char)(buffer.get(offset) & 0xFF);
    }

    public char getUByte(int index) {
        assertBounds(index + 1);
        return (char)(buffer.get(offset + index) & 0xFF);
    }

    public void setUByte(byte value) {
        assertBounds(1);
        buffer.put(offset, value);
    }

    public void setUByte(int index, byte value) {
        assertBounds(index + 1);
        buffer.put(offset + index, value);
    }

    public void setUByte(char value) {
        if (value >= 1L << 8)
            throw new IllegalArgumentException("UByte out of range: " + value);
        assertBounds(1);
        buffer.put(offset, (byte)value);
    }

    public void setUByte(int index, char value) {
        if (value >= 1L << 8)
            throw new IllegalArgumentException("UByte out of range: " + value);
        assertBounds(index + 1);
        buffer.put(offset + index, (byte)value);
    }

    public char getChar() {
        assertBounds(2);
        return buffer.getChar(offset);
    }

    public char getChar(int index) {
        assertBounds(index + 2);
        return buffer.getChar(offset + index);
    }

    public void setChar(char value) {
        assertBounds(2);
        buffer.putChar(offset, value);
    }

    public void setChar(int index, char value) {
        assertBounds(index + 2);
        buffer.putChar(offset + index, value);
    }

    public short getShort() {
        assertBounds(2);
        return buffer.getShort(offset);
    }

    public short getShort(int index) {
        assertBounds(index + 2);
        return buffer.getShort(offset + index);
    }

    public void setShort(short value) {
        assertBounds(2);
        buffer.putShort(offset, value);
    }

    public void setShort(int index, short value) {
        assertBounds(index + 2);
        buffer.putShort(offset + index, value);
    }

    public int getInt() {
        assertBounds(4);
        return buffer.getInt(offset);
    }

    public int getInt(int index) {
        assertBounds(index + 4);
        return buffer.getInt(offset + index);
    }

    public void setInt(int value) {
        assertBounds(4);
        buffer.putInt(offset, value);
    }

    public void setInt(int index, int value) {
        assertBounds(index + 4);
        buffer.putInt(offset + index, value);
    }

    public long getUInt() {
        assertBounds(4);
        return buffer.getInt(offset) & 0xFFFFFFFFL;
    }

    public long getUInt(int index) {
        assertBounds(index + 4);
        return buffer.getInt(offset + index) & 0xFFFFFFFFL;
    }

    public void setUInt(int value) {
        assertBounds(4);
        buffer.putInt(offset, value);
    }

    public void setUInt(int index, int value) {
        assertBounds(index + 4);
        buffer.putInt(offset + index, value);
    }

    public void setUInt(long value) {
        if (value >= 1L << 32)
            throw new IllegalArgumentException("UInt out of range: " + value);
        assertBounds(4);
        buffer.putInt(offset, (int)value);
    }

    public void setUInt(int index, long value) {
        if (value >= 1L << 32)
            throw new IllegalArgumentException("UInt out of range: " + value);
        assertBounds(index + 4);
        buffer.putInt(offset + index, (int)value);
    }

    public long getLong() {
        assertBounds(8);
        return buffer.getLong(offset);
    }

    public long getLong(int index) {
        assertBounds(index + 8);
        return buffer.getLong(offset + index);
    }

    public void setLong(long value) {
        assertBounds(8);
        buffer.putLong(offset, value);
    }

    public void setLong(int index, long value) {
        assertBounds(index + 8);
        buffer.putLong(offset + index, value);
    }

    public float getFloat() {
        assertBounds(4);
        return buffer.getFloat(offset);
    }

    public float getFloat(int index) {
        assertBounds(index + 4);
        return buffer.getFloat(offset + index);
    }

    public void setFloat(float value) {
        assertBounds(4);
        buffer.putFloat(offset, value);
    }

    public void setFloat(int index, float value) {
        assertBounds(index + 4);
        buffer.putFloat(offset + index, value);
    }

    public double getDouble() {
        assertBounds(8);
        return buffer.getDouble(offset);
    }

    public double getDouble(int index) {
        assertBounds(index + 8);
        return buffer.getDouble(offset + index);
    }

    public void setDouble(double value) {
        assertBounds(8);
        buffer.putDouble(offset, value);
    }

    public void setDouble(int index, double value) {
        assertBounds(index + 8);
        buffer.putDouble(offset + index, value);
    }

    public long getNativePointer() {
        assertBounds(CHandler.POINTER_SIZE);
        if (CHandler.POINTER_SIZE == 4)
            return buffer.getInt(offset);
        else
            return buffer.getLong(offset);
    }

    public long getNativePointer(int index) {
        assertBounds(index + CHandler.POINTER_SIZE);
        if (CHandler.POINTER_SIZE == 4) {
            return buffer.getInt(offset + index);
        } else {
            return buffer.getLong(offset + index);
        }
    }

    public void setNativePointer(long value) {
        assertBounds(CHandler.POINTER_SIZE);
        if (CHandler.POINTER_SIZE == 4)
            buffer.putInt(offset, (int)value);
        else
            buffer.putLong(offset, value);
    }

    public void setNativePointer(int index, long value) {
        assertBounds(index + CHandler.POINTER_SIZE);
        if (CHandler.POINTER_SIZE == 4) {
            buffer.putInt(offset + index, (int)value);
        } else {
            buffer.putLong(offset + index, value);
        }
    }

    public long getNativeLong() {
        assertBounds(CHandler.LONG_SIZE);
        if (CHandler.LONG_SIZE == 4)
            return buffer.getInt(offset);
        else
            return buffer.getLong(offset);
    }

    public long getNativeLong(int index) {
        assertBounds(index + CHandler.LONG_SIZE);
        if (CHandler.LONG_SIZE == 4) {
            return buffer.getInt(offset + index);
        } else {
            return buffer.getLong(offset + index);
        }
    }

    public void setNativeLong(long value) {
        assertBounds(CHandler.LONG_SIZE);
        if (CHandler.LONG_SIZE == 4)
            buffer.putInt(offset, (int)value);
        else
            buffer.putLong(offset, value);
    }

    public void setNativeLong(int index, long value) {
        assertBounds(index + CHandler.LONG_SIZE);
        if (CHandler.LONG_SIZE == 4) {
            buffer.putInt(offset + index, (int)value);
        } else {
            buffer.putLong(offset + index, value);
        }
    }

    public long getNativeULong() {
        assertBounds(CHandler.LONG_SIZE);
        if (CHandler.LONG_SIZE == 4)
            return buffer.getInt(offset) & 0xFFFFFFFFL;
        else
            return buffer.getLong(offset);
    }

    public long getNativeULong(int index) {
        assertBounds(index + CHandler.LONG_SIZE);
        if (CHandler.LONG_SIZE == 4) {
            return buffer.getInt(offset + index) & 0xFFFFFFFFL;
        } else {
            return buffer.getLong(offset + index);
        }
    }

    public void setNativeULong(long value) {
        assertBounds(CHandler.LONG_SIZE);
        if (CHandler.LONG_SIZE == 4)
            buffer.putInt(offset, (int)value);
        else
            buffer.putLong(offset, value);
    }

    public void setNativeULong(int index, long value) {
        assertBounds(index + CHandler.LONG_SIZE);
        if (CHandler.LONG_SIZE == 4) {
            buffer.putInt(offset + index, (int)value);
        } else {
            buffer.putLong(offset + index, value);
        }
    }

    public String getString()
    {
        return getString(StandardCharsets.UTF_8);
    }

    public String getString(Charset charset)
    {
        int length = 0;
        while (buffer.get(offset + length) != 0)
            length++;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = buffer.get(offset + i);
        }
        return new String(bytes, charset);
    }

    public void setString(String string)
    {
        setString(string, StandardCharsets.UTF_8);
    }

    public void setString(String string, Charset charset)
    {
        byte[] bytes = string.getBytes(charset);
        assertBounds(bytes.length + 1);
        for (int i = 0; i < bytes.length; i++) {
            buffer.put(offset + i, bytes[i]);
        }
        buffer.put(offset + bytes.length, (byte) 0);
    }

    public void copyFrom(BufferPtr src, int size) {
        assertBounds(size);
        src.assertBounds(size);
        CHandler.memcpy(pointer, src.pointer, size);
    }

    public void copyFrom(int index, BufferPtr src, int srcOffset, int size) {
        assertBounds(index + size);
        src.assertBounds(srcOffset + size);
        CHandler.memcpy(pointer + index, src.pointer + srcOffset, size);
    }

    public long getPointer() {
        if (buffer == null)
            throw new IllegalStateException("Buffer invalid (use-after-free?)");
        return pointer;
    }

    public int getCapacity() {
        if (buffer == null)
            throw new IllegalStateException("Buffer invalid (use-after-free?)");
        return capacity;
    }
}
