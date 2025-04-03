package com.badlogic.gdx.jnigen.runtime.pointer;

public class VoidPointer extends Pointing {

    public static final VoidPointer NULL = new VoidPointer(0, false).guardSize(0);

    public VoidPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public VoidPointer(int size) {
        this(size, true, true);
    }

    public VoidPointer(int size, boolean freeOnGC, boolean guard) {
        super(size, freeOnGC, guard);
    }

    public VoidPointer guardSize(long size) {
        super.guardBytes(size);
        return this;
    }
}
