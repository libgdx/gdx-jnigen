package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.jnigen.generated.FFITypes;
import com.badlogic.gdx.jnigen.runtime.pointer.FloatPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.CSizedIntPointer;

public final class SpecialStruct extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(19).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public SpecialStruct(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public SpecialStruct() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public SpecialStruct.SpecialStructPointer asPointer() {
        return new SpecialStruct.SpecialStructPointer(getPointer(), getsGCFreed());
    }

    public FloatPointer floatPtrField() {
        return new FloatPointer(getValue(0), false);
    }

    public void floatPtrField(FloatPointer floatPtrField) {
        setValue(floatPtrField.getPointer(), 0);
    }

    public CSizedIntPointer arrayField() {
        return __arrayField;
    }

    private static final int __arrayField_offset = CHandler.getOffsetForField(__ffi_type, 1);

    private final CSizedIntPointer __arrayField = new CSizedIntPointer(getPointer() + __arrayField_offset, false, "int").guardCount(5);

    public CSizedIntPointer intPtrField() {
        return new CSizedIntPointer(getValue(6), false, "int");
    }

    public void intPtrField(CSizedIntPointer intPtrField) {
        setValue(intPtrField.getPointer(), 6);
    }

    public static final class SpecialStructPointer extends StackElementPointer<SpecialStruct> {

        public SpecialStructPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public SpecialStructPointer() {
            this(1, true, true);
        }

        public SpecialStructPointer(int count, boolean freeOnGC, boolean guard) {
            super(__size, count, freeOnGC, guard);
        }

        public SpecialStruct.SpecialStructPointer guardCount(long count) {
            super.guardCount(count);
            return this;
        }

        public int getSize() {
            return __size;
        }

        protected SpecialStruct createStackElement(long ptr, boolean freeOnGC) {
            return new SpecialStruct(ptr, freeOnGC);
        }
    }
}
