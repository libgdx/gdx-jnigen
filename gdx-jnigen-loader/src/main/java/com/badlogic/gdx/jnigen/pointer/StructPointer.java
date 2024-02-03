package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.util.WrappingPointingSupplier;

public abstract class StructPointer<T extends Struct> extends Pointing {

    private final WrappingPointingSupplier<T> supplier;

    protected StructPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
        this.supplier = CHandler.getPointingSupplier(getStructClass());
    }

    public StructPointer(long size) {
        super(size);
        this.supplier = CHandler.getPointingSupplier(getStructClass());
    }

    public static <T extends Struct> StructPointer<T> of(Class<T> of) {
        return CHandler.getNewStructPointerSupplier(of).create();
    }

    public T get() {
        long newPtr = CHandler.clone(getPointer(), getSize());
        return supplier.create(newPtr, true);
    }

    public T asStruct() {
        return supplier.create(getPointer(), getsGCFreed());
    }

    public void set(T struct) {
        CHandler.memcpy(getPointer(), struct.getPointer(), struct.getSize());
    }

    public abstract Class<T> getStructClass();

    public abstract long getSize();
}
