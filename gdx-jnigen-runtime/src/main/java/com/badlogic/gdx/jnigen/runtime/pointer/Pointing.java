package com.badlogic.gdx.jnigen.runtime.pointer;

import com.badlogic.gdx.jnigen.runtime.gc.GCHandler;
import com.badlogic.gdx.jnigen.runtime.mem.AllocationManager;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;

public class Pointing {

    private final BufferPtr bufPtr;
    private final boolean freeOnGC;
    private Pointing parent;
    protected boolean freed = false;

    public Pointing(long pointer, boolean freeOnGC) {
        this.bufPtr = AllocationManager.wrap(pointer);
        this.freeOnGC = freeOnGC;
        if (freeOnGC)
            GCHandler.enqueuePointer(this, bufPtr);
    }

    public Pointing(long pointer, boolean freeOnGC, int capacity) {
        this.bufPtr = AllocationManager.wrap(pointer, capacity);
        this.freeOnGC = freeOnGC;
        if (freeOnGC)
            GCHandler.enqueuePointer(this, bufPtr);
    }

    public Pointing(int size, boolean freeOnGC) {
        this.bufPtr = AllocationManager.allocate(size);
        this.freeOnGC = freeOnGC;
        if (freeOnGC)
            GCHandler.enqueuePointer(this, bufPtr);
    }

    public boolean isNull() {
        if (freed)
            throw new IllegalStateException("Pointer is freed: " + bufPtr.getPointer());
        return bufPtr == null;
    }

    public void assertBounds(int index) {
        if (freed)
            throw new IllegalStateException("Pointer is freed: " + bufPtr.getPointer());
        if (isNull())
            throw new NullPointerException("Pointer is null");
        bufPtr.assertBounds(index);
    }

    public void free() {
        if (freed)
            throw new IllegalStateException("Double free on " + bufPtr.getPointer());
        if (freeOnGC)
            throw new IllegalStateException("Can only free unmanaged objects");
        if (parent != null)
            throw new IllegalStateException("Can't free object that has parent");
        bufPtr.free();
        freed = true;
    }

    public boolean isFreed() {
        return freed;
    }

    public void setParent(Pointing parent) {
        this.parent = parent;
    }

    protected BufferPtr getBufPtr() {
        if (freed)
            throw new IllegalStateException("Pointer is freed: " + bufPtr.getPointer());
        return bufPtr;
    }

    public long getPointer() {
        if (freed)
            throw new IllegalStateException("Pointer is freed: " + bufPtr.getPointer());
        if (bufPtr == null)
            return 0;
        return bufPtr.getPointer();
    }
}
