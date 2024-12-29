package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.jnigen.generated.FFITypes;
import com.badlogic.jnigen.generated.structs.AnonymousStructField.inner;

public final class AnonymousStructField extends com.badlogic.gdx.jnigen.runtime.pointer.Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(12).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public AnonymousStructField(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public AnonymousStructField() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public AnonymousStructField.AnonymousStructFieldPointer asPointer() {
        return new AnonymousStructField.AnonymousStructFieldPointer(getPointer(), getsGCFreed());
    }

    public inner inner() {
        return __inner;
    }

    private static final int __inner_offset = CHandler.getOffsetForField(__ffi_type, 0);

    private final inner __inner = new inner(getPointer() + __inner_offset, false);

    public int externalValue() {
        return (int) getValue(1);
    }

    public void externalValue(int externalValue) {
        setValue(externalValue, 1);
    }

    public static final class AnonymousStructFieldPointer extends StackElementPointer<AnonymousStructField> {

        public AnonymousStructFieldPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public AnonymousStructFieldPointer() {
            this(1, true, true);
        }

        public AnonymousStructFieldPointer(int count, boolean freeOnGC, boolean guard) {
            super(__size, count, freeOnGC, guard);
        }

        public AnonymousStructField.AnonymousStructFieldPointer guardCount(long count) {
            super.guardCount(count);
            return this;
        }

        public int getSize() {
            return __size;
        }

        protected AnonymousStructField createStackElement(long ptr, boolean freeOnGC) {
            return new AnonymousStructField(ptr, freeOnGC);
        }
    }

    public final static class inner extends com.badlogic.gdx.jnigen.runtime.pointer.Struct {

        private final static int __size;

        private final static long __ffi_type;

        static {
            __ffi_type = FFITypes.getCTypeInfo(24).getFfiType();
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
