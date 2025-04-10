package com.badlogic.gdx.jnigen.runtime.pointer;

import com.badlogic.gdx.jnigen.runtime.gc.GCHandler;
import com.badlogic.gdx.jnigen.runtime.mem.AllocationManager;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.mem.MemoryManagementStrategy;

public class Pointing {

    private final BufferPtr bufPtr;
    private Pointing parent;
    protected boolean freed = false;

    public Pointing(long pointer, boolean freeOnGC) {
        this.bufPtr = AllocationManager.wrap(pointer, freeOnGC);
        if (bufPtr != null && bufPtr.getManagementStrategy() == MemoryManagementStrategy.GC)
            GCHandler.enqueuePointer(this);
    }

    public Pointing(long pointer, boolean freeOnGC, int capacity) {
        this.bufPtr = AllocationManager.wrap(pointer, capacity, freeOnGC);
        if (bufPtr != null && bufPtr.getManagementStrategy() == MemoryManagementStrategy.GC)
            GCHandler.enqueuePointer(this);
    }

    public Pointing(int size, boolean freeOnGC) {
        this.bufPtr = AllocationManager.allocate(size, freeOnGC);
        if (bufPtr.getManagementStrategy() == MemoryManagementStrategy.GC)
            GCHandler.enqueuePointer(this);
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
        if (getManagementStrategy() != MemoryManagementStrategy.UNMANAGED)
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

    public MemoryManagementStrategy getManagementStrategy() {
        if (parent != null)
            return parent.getManagementStrategy();
        return bufPtr.getManagementStrategy();
    }

    public BufferPtr getBufPtr() {
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
