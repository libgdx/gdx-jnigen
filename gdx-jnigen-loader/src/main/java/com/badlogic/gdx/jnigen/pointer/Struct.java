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
        // Okay, this might be shit, because we now get a new view on the Struct as a StructPointer.
        // This might lead to problems, where the original Struct gets freed, while the StructPointer view still exists.
        // So, we might need an efficient way to determine, if another Pointing exists, that targets the same address.
        // This way we can Queue the StructPointer and wait with release, until both went out of reach.
        // Another way might be, that the StructPointer holds a strong ref to the Struct, but I don't like that.
        return (StructPointer<T>)Global.getStructPointer(getClass()).create(getPointer(), false);
    }

    public abstract long getSize();

    public abstract long getFFIType();
}
