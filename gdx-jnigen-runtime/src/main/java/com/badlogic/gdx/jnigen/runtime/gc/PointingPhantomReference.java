package com.badlogic.gdx.jnigen.runtime.gc;

import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;

import java.lang.ref.PhantomReference;

public class PointingPhantomReference extends PhantomReference<Object> {

    private final long pointer;
    public PointingPhantomReference(Object referent, long pointer) {
        super(referent, GCHandler.REFERENCE_QUEUE);
        this.pointer = pointer;
    }

    public long getPointer() {
        return pointer;
    }
}
