package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.gdx.jnigen.runtime.util.Utils;

/**
 * This represents a pointer to an unsigned pointer-wide integer: `size_t`, `uintptr_t` and typedefs of them.
 * Elements are 4 bytes on 32bit platforms and 8 bytes on 64bit platforms.
 * Java cannot represent unsigned longs natively, the closest is a signed long.
 */
public class UWordPointer extends VoidPointer {

    private static final int BYTE_SIZE = CHandler.POINTER_SIZE;

    public UWordPointer(VoidPointer pointer) {
        super(pointer);
    }

    public UWordPointer(int count, boolean freeOnGC) {
        super(count * BYTE_SIZE, freeOnGC);
    }

    public UWordPointer() {
        this(1);
    }

    public UWordPointer(int count) {
        super(count * BYTE_SIZE);
    }

    public UWordPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public UWordPointer(long pointer, boolean freeOnGC, int capacity) {
        super(pointer, freeOnGC, capacity * BYTE_SIZE);
    }

    public long getLong() {
        return getLong(0);
    }

    public long getLong(int index) {
        return getBufPtr().getNativeUWord(index * BYTE_SIZE);
    }

    public void setLong(long value) {
        setLong(value, 0);
    }

    public void setLong(long value, int index) {
        if (!Utils.checkBoundsForNumber(value, BYTE_SIZE, false))
            throw new IllegalArgumentException("UWord out of range: " + value);
        getBufPtr().setNativeUWord(index * BYTE_SIZE, value);
    }
}
