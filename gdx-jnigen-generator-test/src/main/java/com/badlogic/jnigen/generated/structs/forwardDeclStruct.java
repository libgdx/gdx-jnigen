package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.jnigen.generated.FFITypes;

public final class forwardDeclStruct extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(25).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public forwardDeclStruct(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public forwardDeclStruct() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public forwardDeclStruct.forwardDeclStructPointer asPointer() {
        return new forwardDeclStruct.forwardDeclStructPointer(getPointer(), false, this);
    }

    public static final class forwardDeclStructPointer extends StackElementPointer<forwardDeclStruct> {

        public forwardDeclStructPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public forwardDeclStructPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public forwardDeclStructPointer() {
            this(1, true, true);
        }

        public forwardDeclStructPointer(int count, boolean freeOnGC, boolean guard) {
            super(__size, count, freeOnGC, guard);
        }

        public forwardDeclStruct.forwardDeclStructPointer guardCount(long count) {
            super.guardCount(count);
            return this;
        }

        public int getSize() {
            return __size;
        }

        protected forwardDeclStruct createStackElement(long ptr, boolean freeOnGC) {
            return new forwardDeclStruct(ptr, freeOnGC);
        }
    }
}
