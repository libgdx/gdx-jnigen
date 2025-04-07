package com.badlogic.gdx.jnigen.runtime.pointer;

public abstract class Struct extends StackElement {

    protected Struct(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    protected Struct(int size) {
        super(size);
    }
}
