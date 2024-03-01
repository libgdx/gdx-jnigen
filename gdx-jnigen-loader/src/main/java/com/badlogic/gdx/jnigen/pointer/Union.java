package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;

public abstract class Union extends StackElement {

    protected Union(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    protected Union(int size) {
        super(size);
    }

    @Override
    public boolean hasElementOffsets() {
        return false;
    }
}
