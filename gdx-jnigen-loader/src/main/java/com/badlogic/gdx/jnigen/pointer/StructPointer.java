package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.Struct;
import com.badlogic.gdx.jnigen.util.DereferencingStructSupplier;

public class StructPointer<T extends Struct> extends Pointing {

    static {
        Global.registerPointingSupplier(StructPointer.class, StructPointer::new);
    }


    public StructPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public StructPointer() {
        super(8); // Determine pointer size per system?
    }

    public void set(T struct) {
        Global.memcpy(getPointer(), struct.getPointer(), struct.getSize());
    }

    public T get(Class<T> classToGet) {
        long ptr = Global.clone(getPointer(), Global.getStructSize(classToGet));
        return Global.getPointingSupplier(classToGet).create(ptr, true);
    }
}
