package com.badlogic.gdx.jnigen.runtime.pointer;

import com.badlogic.gdx.jnigen.runtime.gc.GCHandler;
import com.badlogic.gdx.jnigen.runtime.mem.AllocationManager;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtrManager;

/**
 * This is the base class for everything that wraps a `pointer`. A pointer understood in the broadest sense, as memory allocated by C.
 * A pointer can be freed, a pointer can be null. A pointer can have a parent. The parent is used to track relations of reinterpreted addresses.
 * So that if e.g. a Struct is allocated in java, and then {@link Struct#asPointer()} is called, the original struct can't go out of scope and collected by GC.
 * <br>
 * A Pointing can be reset and the underlying address being changed. This is not advised, unless for memory pressure reasons.
 */
public abstract class Pointing {

    private final BufferPtr bufPtr;
    private final boolean freeOnGC;
    private Pointing parent;
    protected boolean freed = false;

    public Pointing(Pointing pointing) {
        this.bufPtr = AllocationManager.wrap(pointing.getPointer(), pointing.getCapacity());
        this.freeOnGC = false;
        this.parent = pointing;
    }

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

    /**
     * Whether the Pointing refers to a "NULL"
     */
    public boolean isNull() {
        if (freed)
            throw new IllegalStateException("Pointer is freed: " + bufPtr.getPointer());
        return bufPtr.isNull();
    }

    /**
     * Whether the byte at the `index` is part of the pointer.
     */
    public void assertBounds(int index) {
        if (freed)
            throw new IllegalStateException("Pointer is freed: " + bufPtr.getPointer());
        if (isNull())
            throw new NullPointerException("Pointer is null");
        bufPtr.assertBounds(index);
    }

    /**
     * Free's the specified pointer. You cannot free pointer with a parent or that are already registered for freeing by GC.
     */
    public void free() {
        if (freed)
            throw new IllegalStateException("Double free on " + bufPtr.getPointer());
        if (freeOnGC)
            throw new IllegalStateException("Can only free unmanaged objects");
        if (parent != null)
            throw new IllegalStateException("Can't free object that has parent");
        if (isNull())
            throw new NullPointerException("Pointer is null");
        bufPtr.free();
        freed = true;
    }

    /**
     * Whether the Pointing is already freed.
     */
    public boolean isFreed() {
        return freed;
    }

    /**
     * Sets the parent of this Pointing. The intention is to keep the parent object reachable, as long as this Pointing is reachable.
     * This can be used to avoid pre-cleaning of java allocated pointer
     */
    public void setParent(Pointing parent) {
        this.parent = parent;
    }

    /**
     * Exposes the underlying BufferPtr. Manually handling the BufferPtr is highly discouraged.
     *
     * Do not call this method, unless you explicitly know what you are doing.
     */
    public BufferPtr getBufPtr() {
        if (freed)
            throw new IllegalStateException("Pointer is freed: " + bufPtr.getPointer());
        if (isNull())
            throw new NullPointerException("Pointer is null");
        return bufPtr;
    }

    /**
     * This method swaps out the underlying pointer of this object. Use with caution.
     */
    public void setPointer(long pointer) {
        if (freeOnGC)
            throw new IllegalStateException("Can't change address of GC registered pointer");
        BufferPtrManager.setBufferPtrPointer(bufPtr, pointer);
        this.freed = false;
        this.parent = null;
    }

    /**
     * This method swaps out the underlying pointer of this object. Use with caution.
     */
    public void setPointer(long pointer, Pointing parent) {
        if (freeOnGC)
            throw new IllegalStateException("Can't change address of GC registered pointer");
        BufferPtrManager.setBufferPtrPointer(bufPtr, pointer);
        this.freed = false;
        this.parent = parent;
    }

    /**
     * This method swaps out the underlying pointer of this object. Use with caution.
     */
    public void setPointer(long pointer, int capacity) {
        if (freeOnGC)
            throw new IllegalStateException("Can't change address of GC registered pointer");
        BufferPtrManager.setBufferPtrPointer(bufPtr, pointer, capacity);
        this.freed = false;
        this.parent = null;
    }

    /**
     * This method swaps out the underlying pointer of this object. Use with caution.
     */
    public void setPointer(long pointer, int capacity, Pointing parent) {
        if (freeOnGC)
            throw new IllegalStateException("Can't change address of GC registered pointer");
        BufferPtrManager.setBufferPtrPointer(bufPtr, pointer, capacity);
        this.freed = false;
        this.parent = parent;
    }

    /**
     * This method swaps out the underlying pointer of this object. Use with caution.
     */
    public void setPointer(Pointing pointer) {
        if (freeOnGC)
            throw new IllegalStateException("Can't change address of GC registered pointer");
        BufferPtrManager.setBufferPtrPointer(bufPtr, pointer.getPointer(), pointer.getCapacity());
        this.freed = false;
        this.parent = pointer;
    }

    /**
     * The address of this object
     */
    public long getPointer() {
        if (freed)
            throw new IllegalStateException("Pointer is freed: " + bufPtr.getPointer());
        return bufPtr.getPointer();
    }

    /**
     * How many bytes this pointer allocated. -1 means no capacity
     */
    public int getCapacity() {
        if (freed)
            throw new IllegalStateException("Pointer is freed: " + bufPtr.getPointer());
        return bufPtr.getCapacity();
    }
}
