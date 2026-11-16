package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;

public final class forwardDeclStruct {

    private forwardDeclStruct() {
    }

    public static final class forwardDeclStructPointer extends VoidPointer {

        public forwardDeclStructPointer(VoidPointer pointer) {
            super(pointer);
        }

        public forwardDeclStructPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public forwardDeclStructPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }
    }
}
