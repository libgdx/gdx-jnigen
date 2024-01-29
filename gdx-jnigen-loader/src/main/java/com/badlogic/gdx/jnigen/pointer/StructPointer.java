package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.util.WrappingPointingSupplier;

public abstract class StructPointer<T extends Struct> extends Pointing {

    private final WrappingPointingSupplier<T> supplier;

    protected StructPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
        this.supplier = Global.getPointingSupplier(getStructClass());
    }

    public StructPointer(long size) {
        super(size);
        this.supplier = Global.getPointingSupplier(getStructClass());
    }

    public static <T extends Struct> StructPointer<T> of(Class<T> of) {
        return Global.getNewStructPointerSupplier(of).create();
    }

    public T get() {
        long newPtr = Global.clone(getPointer(), getSize());
        return supplier.create(newPtr, true);
    }

    public T asStruct() {
        return supplier.create(getPointer(), getsGCFreed());
    }

    public void set(T struct) {
        Global.memcpy(getPointer(), struct.getPointer(), struct.getSize());
    }

    public abstract Class<T> getStructClass();

    public abstract long getSize();
}
