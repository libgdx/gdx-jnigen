package com.badlogic.gdx.jnigen.runtime.mem;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.gc.GCHandler;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class BufferPtr {

    private final ByteBuffer buffer;
    private final long pointer;
    private final int capacity;
    private final boolean freeOnGC;
    private boolean freed = false;
    private BufferPtr parent;

    public BufferPtr(ByteBuffer buffer, long pointer, int capacity, boolean freeOnGC) {
        this.buffer = buffer;
        this.pointer = pointer;
        this.capacity = capacity;
        this.freeOnGC = freeOnGC;
        if (freeOnGC)
            GCHandler.enqueuePointer(this);
        if (capacity > buffer.capacity())
            throw new IllegalArgumentException("Buffer capacity (" + buffer.capacity() + ") exceeded by " + capacity + ". More then 1GB?");
    }

    public void free() {
        if (freed)
            throw new IllegalStateException("Double free on " + pointer);
        if (getsGCFreed())
            throw new IllegalStateException("Can't free a object, that gets freed by GC.");
        CHandler.free(pointer);
        freed = true;
    }

    public boolean isFreed() {
        return freed;
    }

    public void setParent(BufferPtr parent) {
        this.parent = parent;
    }

    public void assertBounds(int expectedCapacity) {
        if (capacity > 0 && (expectedCapacity < 0 || expectedCapacity > capacity))
            throw new IndexOutOfBoundsException("Index: " + expectedCapacity + ", Size: " + capacity);
    }

    public boolean getBoolean() {
        return buffer.get(0) != 0;
    }

    public boolean getBoolean(int index) {
        assertBounds(index + 1);
        return buffer.get(index) != 0;
    }

    public void setBoolean(boolean value) {
        assertBounds(1);
        buffer.put(0, (byte)(value ? 1 : 0));
    }

    public void setBoolean(int index, boolean value) {
        assertBounds(index + 1);
        buffer.put(index, (byte)(value ? 1 : 0));
    }

    public byte getByte() {
        assertBounds(1);
        return buffer.get(0);
    }

    public byte getByte(int index) {
        assertBounds(index + 1);
        return buffer.get(index);
    }

    public void setByte(byte value) {
        assertBounds(1);
        buffer.put(0, value);
    }

    public void setByte(int index, byte value) {
        assertBounds(index + 1);
        buffer.put(index, value);
    }

    public char getUByte() {
        assertBounds(1);
        return (char)(buffer.get(0) & 0xFF);
    }

    public char getUByte(int index) {
        assertBounds(index + 1);
        return (char)(buffer.get(index) & 0xFF);
    }

    public void setUByte(byte value) {
        assertBounds(1);
        buffer.put(0, value);
    }

    public void setUByte(int index, byte value) {
        assertBounds(index + 1);
        buffer.put(index, value);
    }

    public void setUByte(char value) {
        if (value >= 1L << 8)
            throw new IllegalArgumentException("UByte out of range: " + value);
        assertBounds(1);
        buffer.put(0, (byte)value);
    }

    public void setUByte(int index, char value) {
        if (value >= 1L << 8)
            throw new IllegalArgumentException("UByte out of range: " + value);
        assertBounds(index + 1);
        buffer.put(index, (byte)value);
    }

    public char getChar() {
        assertBounds(2);
        return buffer.getChar(0);
    }

    public char getChar(int index) {
        assertBounds(index + 2);
        return buffer.getChar(index);
    }

    public void setChar(char value) {
        assertBounds(2);
        buffer.putChar(0, value);
    }

    public void setChar(int index, char value) {
        assertBounds(index + 2);
        buffer.putChar(index, value);
    }

    public short getShort() {
        assertBounds(2);
        return buffer.getShort(0);
    }

    public short getShort(int index) {
        assertBounds(index + 2);
        return buffer.getShort(index);
    }

    public void setShort(short value) {
        assertBounds(2);
        buffer.putShort(0, value);
    }

    public void setShort(int index, short value) {
        assertBounds(index + 2);
        buffer.putShort(index, value);
    }

    public int getInt() {
        assertBounds(4);
        return buffer.getInt(0);
    }

    public int getInt(int index) {
        assertBounds(index + 4);
        return buffer.getInt(index);
    }

    public void setInt(int value) {
        assertBounds(4);
        buffer.putInt(0, value);
    }

    public void setInt(int index, int value) {
        assertBounds(index + 4);
        buffer.putInt(index, value);
    }

    public long getUInt() {
        assertBounds(4);
        return buffer.getInt(0) & 0xFFFFFFFFL;
    }

    public long getUInt(int index) {
        assertBounds(index + 4);
        return buffer.getInt(index) & 0xFFFFFFFFL;
    }

    public void setUInt(int value) {
        assertBounds(4);
        buffer.putInt(0, value);
    }

    public void setUInt(int index, int value) {
        assertBounds(index + 4);
        buffer.putInt(index, value);
    }

    public void setUInt(long value) {
        if (value >= 1L << 32)
            throw new IllegalArgumentException("UInt out of range: " + value);
        assertBounds(4);
        buffer.putInt(0, (int)value);
    }

    public void setUInt(int index, long value) {
        if (value >= 1L << 32)
            throw new IllegalArgumentException("UInt out of range: " + value);
        assertBounds(index + 4);
        buffer.putInt(index, (int)value);
    }

    public long getLong() {
        assertBounds(8);
        return buffer.getLong(0);
    }

    public long getLong(int index) {
        assertBounds(index + 8);
        return buffer.getLong(index);
    }

    public void setLong(long value) {
        assertBounds(8);
        buffer.putLong(0, value);
    }

    public void setLong(int index, long value) {
        assertBounds(index + 8);
        buffer.putLong(index, value);
    }

    public float getFloat() {
        assertBounds(4);
        return buffer.getFloat(0);
    }

    public float getFloat(int index) {
        assertBounds(index + 4);
        return buffer.getFloat(index);
    }

    public void setFloat(float value) {
        assertBounds(4);
        buffer.putFloat(0, value);
    }

    public void setFloat(int index, float value) {
        assertBounds(index + 4);
        buffer.putFloat(index, value);
    }

    public double getDouble() {
        assertBounds(8);
        return buffer.getDouble(0);
    }

    public double getDouble(int index) {
        assertBounds(index + 8);
        return buffer.getDouble(index);
    }

    public void setDouble(double value) {
        assertBounds(8);
        buffer.putDouble(0, value);
    }

    public void setDouble(int index, double value) {
        assertBounds(index + 8);
        buffer.putDouble(index, value);
    }

    public long getNativePointer() {
        assertBounds(CHandler.POINTER_SIZE);
        if (CHandler.POINTER_SIZE == 4)
            return buffer.getInt(0);
        else
            return buffer.getLong(0);
    }

    public long getNativePointer(int index) {
        assertBounds(index + CHandler.POINTER_SIZE);
        if (CHandler.POINTER_SIZE == 4) {
            return buffer.getInt(index);
        } else {
            return buffer.getLong(index);
        }
    }

    public void setNativePointer(long value) {
        assertBounds(CHandler.POINTER_SIZE);
        if (CHandler.POINTER_SIZE == 4)
            buffer.putInt(0, (int)value);
        else
            buffer.putLong(0, value);
    }

    public void setNativePointer(int index, long value) {
        assertBounds(index + CHandler.POINTER_SIZE);
        if (CHandler.POINTER_SIZE == 4) {
            buffer.putInt(index, (int)value);
        } else {
            buffer.putLong(index, value);
        }
    }

    public long getNativeLong() {
        assertBounds(CHandler.LONG_SIZE);
        if (CHandler.LONG_SIZE == 4)
            return buffer.getInt(0);
        else
            return buffer.getLong(0);
    }

    public long getNativeLong(int index) {
        assertBounds(index + CHandler.LONG_SIZE);
        if (CHandler.LONG_SIZE == 4) {
            return buffer.getInt(index);
        } else {
            return buffer.getLong(index);
        }
    }

    public void setNativeLong(long value) {
        assertBounds(CHandler.LONG_SIZE);
        if (CHandler.LONG_SIZE == 4)
            buffer.putInt(0, (int)value);
        else
            buffer.putLong(0, value);
    }

    public void setNativeLong(int index, long value) {
        assertBounds(index + CHandler.LONG_SIZE);
        if (CHandler.LONG_SIZE == 4) {
            buffer.putInt(index, (int)value);
        } else {
            buffer.putLong(index, value);
        }
    }

    public long getNativeULong() {
        assertBounds(CHandler.LONG_SIZE);
        if (CHandler.LONG_SIZE == 4)
            return buffer.getInt(0) & 0xFFFFFFFFL;
        else
            return buffer.getLong(0);
    }

    public long getNativeULong(int index) {
        assertBounds(index + CHandler.LONG_SIZE);
        if (CHandler.LONG_SIZE == 4) {
            return buffer.getInt(index) & 0xFFFFFFFFL;
        } else {
            return buffer.getLong(index);
        }
    }

    public void setNativeULong(long value) {
        assertBounds(CHandler.LONG_SIZE);
        if (CHandler.LONG_SIZE == 4)
            buffer.putInt(0, (int)value);
        else
            buffer.putLong(0, value);
    }

    public void setNativeULong(int index, long value) {
        assertBounds(index + CHandler.LONG_SIZE);
        if (CHandler.LONG_SIZE == 4) {
            buffer.putInt(index, (int)value);
        } else {
            buffer.putLong(index, value);
        }
    }

    public String getString()
    {
        return getString(StandardCharsets.UTF_8);
    }

    public String getString(Charset charset)
    {
        int length = 0;
        while (buffer.get(length) != 0)
            length++;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = buffer.get(i);
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
            buffer.put(i, bytes[i]);
        }
        buffer.put(bytes.length, (byte) 0);
    }

    public boolean getsGCFreed() {
        if (parent != null)
            return parent.getsGCFreed();
        return freeOnGC;
    }

    public boolean isNull() {
        return pointer == 0;
    }

    public long getPointer() {
        return pointer;
    }

    public int getCapacity() {
        return capacity;
    }
}
