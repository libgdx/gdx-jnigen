package com.badlogic.gdx.jnigen.runtime.pointer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtrAllocator;

public class Pointing {
    private final BufferPtr bufPtr;
    private Pointing parent;

    public Pointing(long pointer, boolean freeOnGC) {
        this.bufPtr = BufferPtrAllocator.get(pointer, -1, freeOnGC);
    }

    public Pointing(long pointer, boolean freeOnGC, int capacity) {
        this.bufPtr = BufferPtrAllocator.get(pointer, capacity, freeOnGC);
    }

    public Pointing(int size, boolean freeOnGC) {
        this.bufPtr = BufferPtrAllocator.get(CHandler.calloc(1, size), size, freeOnGC);
    }

    public boolean isNull() {
        return bufPtr == null;
    }

    public void assertBounds(int index) {
        if (isNull())
            throw new NullPointerException("Pointer is null");
        bufPtr.assertBounds(index);
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
