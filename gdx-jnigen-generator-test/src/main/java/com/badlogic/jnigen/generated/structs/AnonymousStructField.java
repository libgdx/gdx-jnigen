package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.jnigen.generated.FFITypes;
import com.badlogic.jnigen.generated.structs.AnonymousStructField.inner;

public final class AnonymousStructField extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(16).getFfiType();
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
        return new AnonymousStructField.AnonymousStructFieldPointer(getPointer(), false, this);
    }

    public inner inner() {
        return __inner;
    }

    private static final int __inner_offset = CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0);

    private final inner __inner = new inner(getPointer() + __inner_offset, false);

    public int externalValue() {
        return (int) getBufPtr().getInt(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 8 : 8) : (CHandler.IS_COMPILED_WIN ? 8 : 8));
    }

    public void externalValue(int externalValue) {
        getBufPtr().setInt(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 8 : 8) : (CHandler.IS_COMPILED_WIN ? 8 : 8), externalValue);
    }

    public static final class AnonymousStructFieldPointer extends StackElementPointer<AnonymousStructField> {

        public AnonymousStructFieldPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public AnonymousStructFieldPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public AnonymousStructFieldPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public AnonymousStructFieldPointer() {
            this(1, true);
        }

        public AnonymousStructFieldPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected AnonymousStructField createStackElement(long ptr, boolean freeOnGC) {
            return new AnonymousStructField(ptr, freeOnGC);
        }
    }

    /**
     * Inner struct name
     */
    public final static class inner extends Struct {

        private final static int __size;

        private final static long __ffi_type;

        static {
            __ffi_type = FFITypes.getCTypeInfo(28).getFfiType();
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
            return new inner.innerPointer(getPointer(), false, this);
        }

        /**
         * Innerr struct field
         */
        public int intValue() {
            return (int) getBufPtr().getInt(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0));
        }

        /**
         * Innerr struct field
         */
        public void intValue(int intValue) {
            getBufPtr().setInt(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), intValue);
        }

        public float floatValue() {
            return (float) getBufPtr().getFloat(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 4 : 4) : (CHandler.IS_COMPILED_WIN ? 4 : 4));
        }

        public void floatValue(float floatValue) {
            getBufPtr().setFloat(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 4 : 4) : (CHandler.IS_COMPILED_WIN ? 4 : 4), floatValue);
        }

        public static final class innerPointer extends StackElementPointer<inner> {

            public innerPointer(long pointer, boolean freeOnGC) {
                super(pointer, freeOnGC);
            }

            public innerPointer(long pointer, boolean freeOnGC, int capacity) {
                super(pointer, freeOnGC, capacity * __size);
            }

            public innerPointer(long pointer, boolean freeOnGC, Pointing parent) {
                super(pointer, freeOnGC);
                setParent(parent);
            }

            public innerPointer() {
                this(1, true);
            }

            public innerPointer(int count, boolean freeOnGC) {
                super(__size, count, freeOnGC);
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
