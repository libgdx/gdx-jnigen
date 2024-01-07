package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.Struct;
import com.badlogic.gdx.jnigen.util.DereferencingStructSupplier;

public class StructPointer<T extends Struct> extends Pointing {

    private DereferencingStructSupplier<T> supplier;

    public StructPointer(long pointer, boolean freeOnGC, DereferencingStructSupplier<T> supplier) {
        super(pointer, freeOnGC);
        this.supplier = supplier;
    }

    public StructPointer(DereferencingStructSupplier<T> supplier) {
        super(8); // Determine pointer size per system?
        this.supplier = supplier;
    }

    public void set(T struct) {
        Global.memcpy(getPointer(), struct.getPointer(), struct.getSize());
    }

    public T get() {
        T struct = supplier.create();
        Global.memcpy(struct.getPointer(), getPointer(), struct.getSize()); // I would prefer to solve this in one JNI call
        return struct;
    }
}
