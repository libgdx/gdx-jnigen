package com.badlogic.gdx.jnigen.runtime.pointer;

/**
 * A StackElement is a representation of a Struct/Union in C, which only lives on the stack. StackElements are passed by value.
 * If they are not a view to an element of a {@link StackElementPointer}, it makes usually sense to let them be freed by GC.
 */
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

    /**
     * This method reinterprets the StackElement as a {@link StackElementPointer}. Note, that no data is copied.
     * If this StackElement gets free'd, the pointer will be invalid too.
     * @return The new pointer
     */
    public abstract StackElementPointer<? extends StackElement> asPointer();
    public abstract long getSize();
    public abstract long getFFIType();
}
