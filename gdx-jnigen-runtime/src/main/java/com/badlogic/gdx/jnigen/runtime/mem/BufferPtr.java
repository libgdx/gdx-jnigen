package com.badlogic.gdx.jnigen.runtime.mem;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.gc.GCHandler;

import java.nio.ByteBuffer;

public final class BufferPtr {

    private final ByteBuffer buffer;
    private final ByteBuffer next;
    private final long pointer;
    private final int capacity;
    private final boolean freeOnGC;
    private boolean freed = false;
    private BufferPtr parent;

    public BufferPtr(ByteBuffer buffer, ByteBuffer next, long pointer, int capacity, boolean freeOnGC) {
        this.buffer = buffer;
        this.next = next;
        this.pointer = pointer;
        this.capacity = capacity;
        this.freeOnGC = freeOnGC;
        if (freeOnGC)
            GCHandler.enqueuePointer(this);
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

    public void assertBounds(int index) {
        if (capacity > 0 && (index < 0 || index >= capacity))
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + capacity);
    }

    public boolean getBoolean() {
        return buffer.get(0) != 0;
    }

    public boolean getBoolean(int index) {
        assertBounds(index);
        if (index >= buffer.capacity())
            return next.get(index - buffer.capacity()) != 0;
        return buffer.get(index) != 0;
    }

    public void setBoolean(boolean value) {
        buffer.put(0, (byte)(value ? 1 : 0));
    }

    public void setBoolean(int index, boolean value) {
        assertBounds(index);
        if (index >= buffer.capacity())
            next.put(index - buffer.capacity(), (byte)(value ? 1 : 0));
        else
            buffer.put(index, (byte)(value ? 1 : 0));
    }

    public byte getByte() {
        return buffer.get(0);
    }

    public byte getByte(int index) {
        assertBounds(index);
        if (index >= buffer.capacity())
            return next.get(index - buffer.capacity());
        return buffer.get(index);
    }

    public void setByte(byte value) {
        buffer.put(0, value);
    }

    public void setByte(int index, byte value) {
        assertBounds(index);
        if (index >= buffer.capacity())
            next.put(index - buffer.capacity(), value);
        else
            buffer.put(index, value);
    }

    public char getUByte() {
        return (char)(buffer.get(0) & 0xFF);
    }

    public char getUByte(int index) {
        assertBounds(index);
        if (index >= buffer.capacity())
            return (char)(next.get(index - buffer.capacity()) & 0xFF);
        return (char)(buffer.get(index) & 0xFF);
    }

    public void setUByte(byte value) {
        buffer.put(0, value);
    }

    public void setUByte(int index, byte value) {
        assertBounds(index);
        if (index >= buffer.capacity())
            next.put(index - buffer.capacity(), value);
        else
            buffer.put(index, value);
    }

    public void setUByte(char value) {
        if (value >= 1L << 8)
            throw new IllegalArgumentException("UByte out of range: " + value);
        buffer.put(0, (byte)value);
    }

    public void setUByte(int index, char value) {
        if (value >= 1L << 8)
            throw new IllegalArgumentException("UByte out of range: " + value);
        assertBounds(index);
        if (index >= buffer.capacity())
            next.put(index - buffer.capacity(), (byte)value);
        else
            buffer.put(index, (byte)value);
    }

    public char getChar() {
        return buffer.getChar(0);
    }

    public char getChar(int index) {
        assertBounds(index);
        if (index >= buffer.capacity())
            return next.getChar(index - buffer.capacity());
        return buffer.getChar(index);
    }

    public void setChar(char value) {
        buffer.putChar(0, value);
    }

    public void setChar(int index, char value) {
        assertBounds(index);
        if (index >= buffer.capacity())
            next.putChar(index - buffer.capacity(), value);
        else
            buffer.putChar(index, value);
    }

    public short getShort() {
        return buffer.getShort(0);
    }

    public short getShort(int index) {
        assertBounds(index);
        if (index >= buffer.capacity())
            return next.getShort(index - buffer.capacity());
        return buffer.getShort(index);
    }

    public void setShort(short value) {
        buffer.putShort(0, value);
    }

    public void setShort(int index, short value) {
        assertBounds(index);
        if (index >= buffer.capacity())
            next.putShort(index - buffer.capacity(), value);
        else
            buffer.putShort(index, value);
    }

    public int getInt() {
        return buffer.getInt(0);
    }

    public int getInt(int index) {
        assertBounds(index);
        if (index >= buffer.capacity())
            return next.getInt(index - buffer.capacity());
        return buffer.getInt(index);
    }

    public void setInt(int value) {
        buffer.putInt(0, value);
    }

    public void setInt(int index, int value) {
        assertBounds(index);
        if (index >= buffer.capacity())
            next.putInt(index - buffer.capacity(), value);
        else
            buffer.putInt(index, value);
    }

    public long getUInt() {
        return buffer.getInt(0) & 0xFFFFFFFFL;
    }

    public long getUInt(int index) {
        assertBounds(index);
        if (index >= buffer.capacity())
            return next.getInt(index - buffer.capacity()) & 0xFFFFFFFFL;
        return buffer.getInt(index) & 0xFFFFFFFFL;
    }

    public void setUInt(int value) {
        buffer.putInt(0, value);
    }

    public void setUInt(int index, int value) {
        assertBounds(index);
        if (index >= buffer.capacity())
            next.putInt(index - buffer.capacity(), value);
        else
            buffer.putInt(index, value);
    }

    public void setUInt(long value) {
        if (value >= 1L << 32)
            throw new IllegalArgumentException("UInt out of range: " + value);
        buffer.putInt(0, (int)value);
    }

    public void setUInt(int index, long value) {
        if (value >= 1L << 32)
            throw new IllegalArgumentException("UInt out of range: " + value);
        assertBounds(index);
        if (index >= buffer.capacity())
            next.putInt(index - buffer.capacity(), (int)value);
        else
            buffer.putInt(index, (int)value);
    }

    public long getLong() {
        return buffer.getLong(0);
    }

    public long getLong(int index) {
        assertBounds(index);
        if (index >= buffer.capacity())
            return next.getLong(index - buffer.capacity());
        return buffer.getLong(index);
    }

    public void setLong(long value) {
        buffer.putLong(0, value);
    }

    public void setLong(int index, long value) {
        assertBounds(index);
        if (index >= buffer.capacity())
            next.putLong(index - buffer.capacity(), value);
        else
            buffer.putLong(index, value);
    }

    public float getFloat() {
        return buffer.getFloat(0);
    }

    public float getFloat(int index) {
        assertBounds(index);
        if (index >= buffer.capacity())
            return next.getFloat(index - buffer.capacity());
        return buffer.getFloat(index);
    }

    public void setFloat(float value) {
        buffer.putFloat(0, value);
    }

    public void setFloat(int index, float value) {
        assertBounds(index);
        if (index >= buffer.capacity())
            next.putFloat(index - buffer.capacity(), value);
        else
            buffer.putFloat(index, value);
    }

    public double getDouble() {
        return buffer.getDouble(0);
    }

    public double getDouble(int index) {
        assertBounds(index);
        if (index >= buffer.capacity())
            return next.getDouble(index - buffer.capacity());
        return buffer.getDouble(index);
    }

    public void setDouble(double value) {
        buffer.putDouble(0, value);
    }

    public void setDouble(int index, double value) {
        assertBounds(index);
        if (index >= buffer.capacity())
            next.putDouble(index - buffer.capacity(), value);
        else
            buffer.putDouble(index, value);
    }

    public long getNativePointer() {
        if (CHandler.POINTER_SIZE == 4)
            return buffer.getInt(0);
        else
            return buffer.getLong(0);
    }

    public long getNativePointer(int index) {
        assertBounds(index);
        if (CHandler.POINTER_SIZE == 4) {
            if (index >= buffer.capacity()) {
                return next.getInt(index - buffer.capacity());
            } else {
                return buffer.getInt(index);
            }
        } else {
            if (index >= buffer.capacity()) {
                return next.getLong(index - buffer.capacity());
            } else {
                return buffer.getLong(index);
            }
        }
    }

    public void setNativePointer(long value) {
        if (CHandler.POINTER_SIZE == 4)
            buffer.putInt(0, (int)value);
        else
            buffer.putLong(0, value);
    }

    public void setNativePointer(int index, long value) {
        assertBounds(index);
        if (CHandler.LONG_SIZE == 4) {
            if (index >= buffer.capacity()) {
                next.putInt(index - buffer.capacity(), (int)value);
            } else {
                buffer.putInt(index, (int)value);
            }
        } else {
            if (index >= buffer.capacity()) {
                next.putLong(index - buffer.capacity(), value);
            } else {
                buffer.putLong(index, value);
            }
        }
    }

    public long getNativeLong() {
        if (CHandler.LONG_SIZE == 4)
            return buffer.getInt(0);
        else
            return buffer.getLong(0);
    }

    public long getNativeLong(int index) {
        assertBounds(index);
        if (CHandler.LONG_SIZE == 4) {
            if (index >= buffer.capacity()) {
                return next.getInt(index - buffer.capacity());
            } else {
                return buffer.getInt(index);
            }
        } else {
            if (index >= buffer.capacity()) {
                return next.getLong(index - buffer.capacity());
            } else {
                return buffer.getLong(index);
            }
        }
    }

    public void setNativeLong(long value) {
        if (CHandler.LONG_SIZE == 4)
            buffer.putInt(0, (int)value);
        else
            buffer.putLong(0, value);
    }

    public void setNativeLong(int index, long value) {
        assertBounds(index);
        if (CHandler.LONG_SIZE == 4) {
            if (index >= buffer.capacity()) {
                next.putInt(index - buffer.capacity(), (int)value);
            } else {
                buffer.putInt(index, (int)value);
            }
        } else {
            if (index >= buffer.capacity()) {
                next.putLong(index - buffer.capacity(), value);
            } else {
                buffer.putLong(index, value);
            }
        }
    }

    public long getNativeULong() {
        if (CHandler.LONG_SIZE == 4)
            return buffer.getInt(0) & 0xFFFFFFFFL;
        else
            return buffer.getLong(0);
    }

    public long getNativeULong(int index) {
        assertBounds(index);
        if (CHandler.LONG_SIZE == 4) {
            if (index >= buffer.capacity()) {
                return next.getInt(index - buffer.capacity()) & 0xFFFFFFFFL;
            } else {
                return buffer.getInt(index) & 0xFFFFFFFFL;
            }
        } else {
            if (index >= buffer.capacity()) {
                return next.getLong(index - buffer.capacity());
            } else {
                return buffer.getLong(index);
            }
        }
    }

    public void setNativeULong(long value) {
        if (CHandler.LONG_SIZE == 4)
            buffer.putInt(0, (int)value);
        else
            buffer.putLong(0, value);
    }

    public void setNativeULong(int index, long value) {
        assertBounds(index);
        if (CHandler.LONG_SIZE == 4) {
            if (index >= buffer.capacity()) {
                next.putInt(index - buffer.capacity(), (int)value);
            } else {
                buffer.putInt(index, (int)value);
            }
        } else {
            if (index >= buffer.capacity()) {
                next.putLong(index - buffer.capacity(), value);
            } else {
                buffer.putLong(index, value);
            }
        }
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
}
