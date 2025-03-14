package com.badlogic.gdx.jnigen.runtime.pointer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.gc.GCHandler;
import com.badlogic.gdx.jnigen.runtime.gc.PointingPhantomReference;

public class Pointing {
    private final long pointer;
    protected boolean freed;
    private final boolean freeOnGC;
    private Pointing parent;

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

    public Pointing(int size, boolean freeOnGC, boolean guard) {
        this(CHandler.calloc(1, size), freeOnGC);
        if (guard)
            guardBytes(size);
    }

    public void guardBytes(long size) {
        this.sizeGuard = size;
    }

    public boolean isNull() {
        return pointer == 0;
    }

    public long getSizeGuard() {
        return sizeGuard;
    }

    public void assertBounds(long index) {
        if (isNull())
            throw new NullPointerException("Pointer is null");
        if (sizeGuard != -1 && index >= sizeGuard)
            throw new IllegalArgumentException("Byte " + index + " overshoots guard " + sizeGuard);
    }

    public void free() {
        if (freed)
            throw new IllegalStateException("Double free on " + pointer);
        if (getsGCFreed())
            throw new IllegalStateException("Can't free a object, that gets freed by GC.");
        CHandler.free(pointer);
        freed = true;
    }

    public void setParent(Pointing parent) {
        this.parent = parent;
    }

    public boolean isFreed() {
        return freed;
    }

    public boolean getsGCFreed() {
        if (parent != null)
            return parent.getsGCFreed();
        return freeOnGC;
    }

    public long getPointer() {
        return pointer;
    }
}
