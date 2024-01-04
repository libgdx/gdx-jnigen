package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.Struct;

import java.lang.ref.PhantomReference;

public class StructPhantomReference extends PhantomReference<Struct> {

    private final long pointer;
    public StructPhantomReference(Struct referent) {
        super(referent, GCHandler.REFERENCE_QUEUE);
        this.pointer = referent.getPointer();
    }

    public long getPointer() {
        return pointer;
    }
}
