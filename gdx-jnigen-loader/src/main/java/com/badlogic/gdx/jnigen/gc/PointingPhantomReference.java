package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.pointer.Pointing;

import java.lang.ref.PhantomReference;

public class PointingPhantomReference extends PhantomReference<Pointing> {

    private final long pointer;
    public PointingPhantomReference(Pointing referent) {
        super(referent, GCHandler.REFERENCE_QUEUE);
        this.pointer = referent.getPointer();
    }

    public long getPointer() {
        return pointer;
    }
}
