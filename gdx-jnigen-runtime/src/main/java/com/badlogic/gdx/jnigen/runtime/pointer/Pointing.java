package com.badlogic.gdx.jnigen.runtime.pointer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtrAllocator;

public class Pointing {

    private final BufferPtr bufPtr;
    private boolean freed = false;

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
        if (freed)
            throw new IllegalStateException("Double free on " + bufPtr.getPointer());
        bufPtr.free();
        freed = true;
    }

    public boolean isFreed() {
        return freed;
    }

    public void setParent(Pointing parent) {
        bufPtr.setParent(parent.getBufPtr());
    }

    public boolean getsGCFreed() {
        return bufPtr.getsGCFreed();
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
