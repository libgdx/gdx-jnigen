package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.Global;

public abstract class StructPointer<T extends Struct> extends Pointing {

    protected StructPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public StructPointer(long size) {
        super(size);
    }

    public static <T extends Struct> StructPointer<T> of(Class<T> of) {
        // Make better, by being able to reference the default constructir
        return Global.getStructPointer(of).create(Global.malloc(Global.getStructSize(of)), true);
    }

    public T get() {
        long newPtr = Global.clone(getPointer(), getSize());
        return Global.getPointingSupplier(getStructClass()).create(newPtr, true);
    }

    public T asStruct() {
        // Shiiit tooooo, we somehow need to prevent freeing to early
        // Also, cache this
        return Global.getPointingSupplier(getStructClass()).create(getPointer(), false);
    }

    public void set(T struct) {
        Global.memcpy(getPointer(), struct.getPointer(), getSize());
    }

    public abstract Class<T> getStructClass();

    public abstract long getSize();
}
