package com.badlogic.gdx.jnigen.pointer;

public abstract class StackElement extends Pointing {

    protected StackElement(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    protected StackElement(int size) {
        super(size, true, true);
    }

    public abstract StackElementPointer<? extends StackElement> asPointer();

    public abstract long getSize();

}
