package com.badlogic.gdx.jnigen;

import com.badlogic.gdx.jnigen.pointer.Pointing;
import com.badlogic.gdx.jnigen.pointer.StructPointer;

public abstract class Struct extends Pointing {

    protected Struct(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    protected Struct(long size) {
        super(size);
    }

    public abstract <T extends Struct> StructPointer<T> asPointer();

    public abstract long getSize();
}
