package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.gc.GCHandler;

public class Pointing {
    private final long pointer;
    protected boolean freed;
    private final boolean freeOnGC;

    public Pointing(long pointer, boolean freeOnGC) {
        this.pointer = pointer;
        this.freeOnGC = freeOnGC;
        if (freeOnGC)
            GCHandler.enqueuePointer(this);
    }

    public Pointing(long size) {
        this(CHandler.calloc(size), true);
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
