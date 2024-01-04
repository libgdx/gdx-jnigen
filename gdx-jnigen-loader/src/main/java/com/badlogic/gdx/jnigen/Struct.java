package com.badlogic.gdx.jnigen;

import com.badlogic.gdx.jnigen.gc.GCHandler;

public class Struct {

    private final long pointer;

    protected Struct(long pointer) {
        if (pointer <= -1)
            throw new IllegalArgumentException("Pointer must be positive.");
        this.pointer = pointer;
    }

    public Struct(int size) {
        this.pointer = Global.calloc(size);
        GCHandler.enqueueMallocedStruct(this);
    }

    public long getPointer() {
        return pointer;
    }
}
