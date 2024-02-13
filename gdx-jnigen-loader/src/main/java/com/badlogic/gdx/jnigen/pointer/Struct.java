package com.badlogic.gdx.jnigen.pointer;

public abstract class Struct extends Pointing {

    protected Struct(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    protected Struct(int size) {
        super(size, true, true);
    }

    public abstract StructPointer<?> asPointer();

    public abstract long getSize();

    public abstract long getFFIType();
}
