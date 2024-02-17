package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.gc.GCHandler;

public class Pointing {
    private final long pointer;
    protected boolean freed;
    private final boolean freeOnGC;

    /**
     * This is just a hint to an implementor of Pointing to respect a bound, but it doesn't have too
     */
    private long sizeGuard = -1;

    public Pointing(long pointer, boolean freeOnGC) {
        this.pointer = pointer;
        this.freeOnGC = freeOnGC;
        if (freeOnGC)
            GCHandler.enqueuePointer(this);
    }

    public Pointing(int size) {
        this(size, true, true);
    }

    public Pointing(int size, boolean freeOnGC, boolean guard) {
        this(CHandler.malloc(size), freeOnGC);
        if (guard)
            guardBytes(size);
    }

    public void guardBytes(long size) {
        this.sizeGuard = size;
    }

    public long getSizeGuard() {
        return sizeGuard;
    }

    public void assertBounds(long index) {
        if (sizeGuard != -1 && index >= sizeGuard)
            throw new IllegalArgumentException("Byte " + index + " overshoots guard " + sizeGuard);
    }

    public void free() {
        if (freed)
            throw new IllegalStateException("Double free on " + pointer);
        if (freeOnGC)
            throw new IllegalStateException("Can't free a object, that gets freed by GC.");
        CHandler.free(pointer);
        freed = true;
    }

    public boolean isFreed() {
        return freed;
    }

    public boolean getsGCFreed() {
        return freeOnGC;
    }

    public long getPointer() {
        return pointer;
    }
}
