package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.Global;

public abstract class Struct extends Pointing {

    protected Struct(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    protected Struct(long size) {
        super(size);
    }

    public <T extends Struct> StructPointer<T> asPointer() {
        //noinspection unchecked
        return (StructPointer<T>)Global.getStructPointer(getClass()).create(getPointer(), getsGCFreed());
    }

    public abstract long getSize();

    public abstract long getFFIType();
}
