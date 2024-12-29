package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.jnigen.generated.FFITypes;

public final class TestStruct extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(20).getFfiType();
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
        return new TestStruct.TestStructPointer(getPointer(), getsGCFreed());
    }

    public long field1() {
        return (long) getValue(0);
    }

    public void field1(long field1) {
        setValue(field1, 0);
    }

    public long field2() {
        return (long) getValue(1);
    }

    public void field2(long field2) {
        setValue(field2, 1);
    }

    public char field3() {
        return (char) getValue(2);
    }

    public void field3(char field3) {
        setValue(field3, 2);
    }

    public char field4() {
        return (char) getValue(3);
    }

    public void field4(char field4) {
        setValue(field4, 3);
    }

    public static final class TestStructPointer extends StackElementPointer<TestStruct> {

        public TestStructPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public TestStructPointer() {
            this(1, true, true);
        }

        public TestStructPointer(int count, boolean freeOnGC, boolean guard) {
            super(__size, count, freeOnGC, guard);
        }

        public TestStruct.TestStructPointer guardCount(long count) {
            super.guardCount(count);
            return this;
        }

        public int getSize() {
            return __size;
        }

        protected TestStruct createStackElement(long ptr, boolean freeOnGC) {
            return new TestStruct(ptr, freeOnGC);
        }
    }
}
