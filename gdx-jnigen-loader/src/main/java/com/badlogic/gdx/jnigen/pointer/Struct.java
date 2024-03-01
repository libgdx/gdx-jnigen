package com.badlogic.gdx.jnigen.pointer;

public abstract class Struct extends StackElement {

    protected Struct(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    protected Struct(int size) {
        super(size);
    }

    public abstract long getFFIType();
}
