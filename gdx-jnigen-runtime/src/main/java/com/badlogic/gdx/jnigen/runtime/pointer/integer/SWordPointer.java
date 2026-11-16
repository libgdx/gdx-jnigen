package com.badlogic.gdx.jnigen.runtime.pointer.integer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.gdx.jnigen.runtime.util.Utils;

/**
 * This represents a pointer to a signed pointer-wide integer: `ssize_t`, `ptrdiff_t`, `intptr_t` and typedefs of them.
 * Elements are 4 bytes on 32bit platforms and 8 bytes on 64bit platforms.
 */
public class SWordPointer extends VoidPointer {

    private static final int BYTE_SIZE = CHandler.POINTER_SIZE;

    public SWordPointer(VoidPointer pointer) {
        super(pointer);
    }

    public SWordPointer(int count, boolean freeOnGC) {
        super(count * BYTE_SIZE, freeOnGC);
    }

    public SWordPointer() {
        this(1);
    }

    public SWordPointer(int count) {
        super(count * BYTE_SIZE);
    }

    public SWordPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public SWordPointer(long pointer, boolean freeOnGC, int capacity) {
        super(pointer, freeOnGC, capacity * BYTE_SIZE);
    }

    public long getLong() {
        return getLong(0);
    }

    public long getLong(int index) {
        return getBufPtr().getNativeWord(index * BYTE_SIZE);
    }

    public void setLong(long value) {
        setLong(value, 0);
    }

    public void setLong(long value, int index) {
        if (!Utils.checkBoundsForNumber(value, BYTE_SIZE, true))
            throw new IllegalArgumentException("SWord out of range: " + value);
        getBufPtr().setNativeWord(index * BYTE_SIZE, value);
    }
}
