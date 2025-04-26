package com.badlogic.gdx.jnigen.runtime.pointer;

public abstract class StackElement extends Pointing {

    protected StackElement(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    protected StackElement(int size) {
        super(size, true);
    }

    @Override
    public void setPointer(long pointer) {
        super.setPointer(pointer, (int) getSize());
    }

    @Override
    public void setPointer(long pointer, Pointing parent) {
        super.setPointer(pointer, (int) getSize(), parent);
    }

    public abstract StackElementPointer<? extends StackElement> asPointer();

    // TODO: All these methods could be removed with the generator. Performance tests need to show, whether virtual dispatch has any significant penalty
    //  However, I doubt it.
    public abstract long getSize();

    public abstract long getFFIType();
}
