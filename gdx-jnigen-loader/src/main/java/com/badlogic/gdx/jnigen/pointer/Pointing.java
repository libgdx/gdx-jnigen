package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.gc.GCHandler;

public class Pointing {
    private final long pointer;
    private boolean freed;
    private boolean freeOnGC;

    protected Pointing(long pointer, boolean freeOnGC) {
        this.pointer = pointer;
        this.freeOnGC = freeOnGC;
        if (freeOnGC)
            GCHandler.enqueuePointer(this);
    }

    public Pointing(long size) {
        this(Global.calloc(size), true);
    }

    public void free() {
        if (freed)
            throw new IllegalStateException("Double free on " + pointer);
        if (freeOnGC)
            throw new IllegalStateException("Can't free a object, that gets freed by GC.");
        Global.free(pointer);
        freed = true;
    }

    public long getPointer() {
        return pointer;
    }
}
