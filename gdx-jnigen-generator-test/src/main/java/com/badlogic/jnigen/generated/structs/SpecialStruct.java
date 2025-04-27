package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.jnigen.generated.FFITypes;
import com.badlogic.gdx.jnigen.runtime.pointer.FloatPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SIntPointer;

/**
 * Special Struct jaja
 */
public final class SpecialStruct extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(23).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public SpecialStruct(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public SpecialStruct(long pointer, boolean freeOnGC, Pointing parent) {
        super(pointer, freeOnGC);
        setParent(parent);
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
        return new SpecialStruct.SpecialStructPointer(getPointer(), false, 1, this);
    }

    public void asPointer(SpecialStruct.SpecialStructPointer ptr) {
        ptr.setPointer(this);
    }

    public FloatPointer floatPtrField() {
        return new FloatPointer(getBufPtr().getNativePointer(0), false);
    }

    public void floatPtrField(FloatPointer floatPtrField) {
        getBufPtr().setNativePointer(0, floatPtrField.getPointer());
    }

    public SIntPointer arrayField() {
        return new SIntPointer(getPointer() + (CHandler.IS_64_BIT ? 8 : 4), false, 5);
    }

    public void arrayField(SIntPointer toSetPtr) {
        toSetPtr.setPointer(getPointer() + (CHandler.IS_64_BIT ? 8 : 4), 20, this);
    }

    public SIntPointer getArrayField() {
        return new SIntPointer(getBufPtr().duplicate(CHandler.IS_64_BIT ? 8 : 4, 20), false, 5);
    }

    public void getArrayField(SIntPointer toCopyTo) {
        toCopyTo.getBufPtr().copyFrom(0, getBufPtr(), CHandler.IS_64_BIT ? 8 : 4, 20);
    }

    public void setArrayField(SIntPointer toCopyFrom) {
        getBufPtr().copyFrom(CHandler.IS_64_BIT ? 8 : 4, toCopyFrom.getBufPtr(), 0, 20);
    }

    public SIntPointer intPtrField() {
        return new SIntPointer(getBufPtr().getNativePointer(CHandler.IS_64_BIT ? 32 : 24), false);
    }

    public void intPtrField(SIntPointer intPtrField) {
        getBufPtr().setNativePointer(CHandler.IS_64_BIT ? 32 : 24, intPtrField.getPointer());
    }

    public static final class SpecialStructPointer extends StackElementPointer<SpecialStruct> {

        public SpecialStructPointer(VoidPointer pointer) {
            super(pointer);
        }

        public SpecialStructPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public SpecialStructPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public SpecialStructPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public SpecialStructPointer(long pointer, boolean freeOnGC, int capacity, Pointing parent) {
            super(pointer, freeOnGC, capacity * __size);
            setParent(parent);
        }

        public SpecialStructPointer() {
            this(1, true);
        }

        public SpecialStructPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected SpecialStruct createStackElement(long ptr, boolean freeOnGC) {
            return new SpecialStruct(ptr, freeOnGC);
        }
    }
}
