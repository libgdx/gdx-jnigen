package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.pointer.Struct;
import com.badlogic.gdx.jnigen.pointer.StackElementPointer;
import com.badlogic.jnigen.generated.FFITypes;
import com.badlogic.jnigen.generated.structs.AnonymousStructFieldArray.inner;

public final class AnonymousStructFieldArray extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(12).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public AnonymousStructFieldArray(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public AnonymousStructFieldArray() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public AnonymousStructFieldArray.AnonymousStructFieldArrayPointer asPointer() {
        return new AnonymousStructFieldArray.AnonymousStructFieldArrayPointer(getPointer(), getsGCFreed());
    }

    public inner.innerPointer inner() {
        return __inner;
    }

    private static final int __inner_offset = CHandler.getOffsetForField(__ffi_type, 0);

    private final inner.innerPointer __inner = new inner.innerPointer(getPointer() + __inner_offset, false).guardCount(2);

    public int externalValue() {
        return (int) getValue(2);
    }

    public void externalValue(int externalValue) {
        setValue(externalValue, 2);
    }

    public static final class AnonymousStructFieldArrayPointer extends StackElementPointer<AnonymousStructFieldArray> {

        public AnonymousStructFieldArrayPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public AnonymousStructFieldArrayPointer() {
            this(1, true, true);
        }

        public AnonymousStructFieldArrayPointer(int count, boolean freeOnGC, boolean guard) {
            super(__size, count, freeOnGC, guard);
        }

        public AnonymousStructFieldArray.AnonymousStructFieldArrayPointer guardCount(long count) {
            super.guardCount(count);
            return this;
        }

        public int getSize() {
            return __size;
        }

        protected AnonymousStructFieldArray createStackElement(long ptr, boolean freeOnGC) {
            return new AnonymousStructFieldArray(ptr, freeOnGC);
        }
    }

    public final static class inner extends Struct {

        private final static int __size;

        private final static long __ffi_type;

        static {
            __ffi_type = FFITypes.getCTypeInfo(18).getFfiType();
            __size = CHandler.getSizeFromFFIType(__ffi_type);
        }

        public inner(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public inner() {
            super(__size);
        }

        public long getSize() {
            return __size;
        }

        public long getFFIType() {
            return __ffi_type;
        }

        public inner.innerPointer asPointer() {
            return new inner.innerPointer(getPointer(), getsGCFreed());
        }

        public int intValue() {
            return (int) getValue(0);
        }

        public void intValue(int intValue) {
            setValue(intValue, 0);
        }

        public float floatValue() {
            return (float) getValueFloat(1);
        }

        public void floatValue(float floatValue) {
            setValue(floatValue, 1);
        }

        public static final class innerPointer extends StackElementPointer<inner> {

            public innerPointer(long pointer, boolean freeOnGC) {
                super(pointer, freeOnGC);
            }

            public innerPointer() {
                this(1, true, true);
            }

            public innerPointer(int count, boolean freeOnGC, boolean guard) {
                super(__size, count, freeOnGC, guard);
            }

            public inner.innerPointer guardCount(long count) {
                super.guardCount(count);
                return this;
            }

            public int getSize() {
                return __size;
            }

            protected inner createStackElement(long ptr, boolean freeOnGC) {
                return new inner(ptr, freeOnGC);
            }
        }
    }
}
