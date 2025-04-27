package com.badlogic.gdx.jnigen.runtime.pointer;

/**
 * A base pointer class, where all pointer types extend from, that can be represented as "void*" in C.<br>
 * No data can/should be accessed directly. Rather construct the appropriate pointer type with {@link VoidPointer#VoidPointer(VoidPointer)}
 */
public class VoidPointer extends Pointing {

    public static final VoidPointer NULL = new VoidPointer(0, false, 0);

    public VoidPointer(VoidPointer pointer) {
        super(pointer);
    }

    public VoidPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public VoidPointer(long pointer, boolean freeOnGC, int capacity) {
        super(pointer, freeOnGC, capacity);
    }

    public VoidPointer(int size) {
        this(size, true);
    }

    public VoidPointer(int size, boolean freeOnGC) {
        super(size, freeOnGC);
    }
}
