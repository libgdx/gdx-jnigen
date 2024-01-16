package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.Global;

public interface StructPointer<T extends Struct> {

    public static <T extends Struct> StructPointer<T> of(Class<T> of) {
        // Make better, by being able to reference the default constructir
        return Global.getStructPointer(of).create(Global.malloc(Global.getStructSize(of)), true);
    }

    default T get() {
        long newPtr = Global.clone(getPointer(), getSize());
        return Global.getPointingSupplier(getStructClass()).create(newPtr, true);
    }

    default void set(T struct) {
        Global.memcpy(getPointer(), struct.getPointer(), getSize());
    }

    Class<T> getStructClass();

    long getSize();

    long getPointer();

    void free();
}
