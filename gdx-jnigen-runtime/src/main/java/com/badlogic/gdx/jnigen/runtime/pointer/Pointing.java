package com.badlogic.gdx.jnigen.runtime.pointer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtrAllocator;

public class Pointing {
    private final BufferPtr bufPtr;
    private Pointing parent;

    /**
     * This is just a hint to an implementor of Pointing to respect a bound, but it doesn't have too
     */
    private long sizeGuard = -1;

    public Pointing(long pointer, boolean freeOnGC) {
        this.bufPtr = BufferPtrAllocator.get(pointer, -1, freeOnGC);
    }

    public Pointing(int size, boolean freeOnGC) {
        this.bufPtr = BufferPtrAllocator.get(CHandler.calloc(1, size), size, freeOnGC);
        this.sizeGuard = size;
    }

    public void guardBytes(long size) {
        this.sizeGuard = size;
    }

    public boolean isNull() {
        return bufPtr == null;
    }

    public void assertBounds(long index) {
        if (isNull())
            throw new NullPointerException("Pointer is null");
        if (sizeGuard != -1 && index >= sizeGuard)
            throw new IllegalArgumentException("Byte " + index + " overshoots guard " + sizeGuard);
    }

    public void free() {
        if (getsGCFreed())
            throw new IllegalStateException("Can't free a object, that gets freed by GC.");

        bufPtr.free();
    }

    public boolean isFreed() {
        return bufPtr.isFreed();
    }

    public void setParent(Pointing parent) {
        this.parent = parent;
    }

    public boolean getsGCFreed() {
        if (parent != null)
            return parent.getsGCFreed();
        return getBufPtr().getsGCFreed();
    }

    protected BufferPtr getBufPtr() {
        return bufPtr;
    }

    public long getPointer() {
        if (bufPtr == null)
            return 0;
        return bufPtr.getPointer();
    }
}
