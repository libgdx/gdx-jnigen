package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.jnigen.generated.FFITypes;

/**
 * This is a test struct
 */
public final class TestStruct extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(24).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public TestStruct(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public TestStruct() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public TestStruct.TestStructPointer asPointer() {
        return new TestStruct.TestStructPointer(getPointer(), false, this);
    }

    /**
     * Field Comment 1
     */
    public long field1() {
        return (long) getBufPtr().getLong(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0));
    }

    /**
     * Field Comment 1
     */
    public void field1(long field1) {
        getBufPtr().setLong(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), field1);
    }

    /**
     * Field Comment 2
     */
    public long field2() {
        return (long) getBufPtr().getUInt(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 8 : 8) : (CHandler.IS_COMPILED_WIN ? 8 : 8));
    }

    /**
     * Field Comment 2
     */
    public void field2(long field2) {
        getBufPtr().setUInt(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 8 : 8) : (CHandler.IS_COMPILED_WIN ? 8 : 8), field2);
    }

    /**
     * Field Comment 3
     */
    public char field3() {
        return (char) getBufPtr().getChar(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 12 : 12) : (CHandler.IS_COMPILED_WIN ? 12 : 12));
    }

    /**
     * Field Comment 3
     */
    public void field3(char field3) {
        getBufPtr().setChar(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 12 : 12) : (CHandler.IS_COMPILED_WIN ? 12 : 12), field3);
    }

    /**
     * Field Comment 4
     */
    public char field4() {
        return (char) getBufPtr().getUByte(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 14 : 14) : (CHandler.IS_COMPILED_WIN ? 14 : 14));
    }

    /**
     * Field Comment 4
     */
    public void field4(char field4) {
        getBufPtr().setUByte(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 14 : 14) : (CHandler.IS_COMPILED_WIN ? 14 : 14), field4);
    }

    public static final class TestStructPointer extends StackElementPointer<TestStruct> {

        public TestStructPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public TestStructPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public TestStructPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public TestStructPointer() {
            this(1, true);
        }

        public TestStructPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected TestStruct createStackElement(long ptr, boolean freeOnGC) {
            return new TestStruct(ptr, freeOnGC);
        }
    }
}
