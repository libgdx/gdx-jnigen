package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.jnigen.generated.FFITypes;

public final class forwardDeclStruct extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(27).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public forwardDeclStruct(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public forwardDeclStruct(long pointer, boolean freeOnGC, Pointing parent) {
        super(pointer, freeOnGC);
        setParent(parent);
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
        return new forwardDeclStruct.forwardDeclStructPointer(getPointer(), false, 1, this);
    }

    public void asPointer(forwardDeclStruct.forwardDeclStructPointer ptr) {
        ptr.setPointer(this);
    }

    public static final class forwardDeclStructPointer extends StackElementPointer<forwardDeclStruct> {

        public forwardDeclStructPointer(VoidPointer pointer) {
            super(pointer);
        }

        public forwardDeclStructPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public forwardDeclStructPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public forwardDeclStructPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public forwardDeclStructPointer(long pointer, boolean freeOnGC, int capacity, Pointing parent) {
            super(pointer, freeOnGC, capacity * __size);
            setParent(parent);
        }

        public forwardDeclStructPointer() {
            this(1, true);
        }

        public forwardDeclStructPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected forwardDeclStruct createStackElement(long ptr, boolean freeOnGC) {
            return new forwardDeclStruct(ptr, freeOnGC);
        }
    }
}
