package com.badlogic.gdx.jnigen;

import com.badlogic.gdx.jnigen.gc.GCHandler;
import com.badlogic.gdx.jnigen.pointer.Pointing;

public abstract class Struct extends Pointing {

    protected Struct(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    protected Struct(long size) {
        super(size);
    }


    public abstract long getSize();
}
