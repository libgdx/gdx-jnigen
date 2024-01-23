package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.util.WrappingPointingSupplier;

public abstract class StructPointer<T extends Struct> extends Pointing {

    private final long size;
    private final WrappingPointingSupplier<T> supplier;

    protected StructPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
        this.size = Global.getStructSize(getStructClass());
        this.supplier = Global.getPointingSupplier(getStructClass());
    }

    public StructPointer(long size) {
        super(size);
        this.size = Global.getStructSize(getStructClass());
        this.supplier = Global.getPointingSupplier(getStructClass());
    }

    public static <T extends Struct> StructPointer<T> of(Class<T> of) {
        // Make better, by being able to reference the default constructir
        return Global.getStructPointer(of).create(Global.malloc(Global.getStructSize(of)), true);
    }

    public T get() {
        long newPtr = Global.clone(getPointer(), size);
        return supplier.create(newPtr, true);
    }

    public T asStruct() {
        return supplier.create(getPointer(), true);
    }

    public void set(T struct) {
        Global.memcpy(getPointer(), struct.getPointer(), size);
    }

    public abstract Class<T> getStructClass();

    public long getSize() {
        return size;
    }
}
