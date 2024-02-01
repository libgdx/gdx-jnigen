package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.gdx.jnigen.ffi.JavaTypeWrapper;
import com.badlogic.gdx.jnigen.pointer.Struct;
import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.pointer.StructPointer;

public class TestStruct extends Struct {

    /*JNI
        #include "definitions.h"
        #include <ffi.h>
    */

    // GLOBAL FUNCTIONS
    public static int passByValueTest(TestStruct testStruct) {
        return passByValueTest(testStruct.getPointer());
    }

    private static native int passByValueTest(long pointer);/*
        TestStruct tStruct = *((TestStruct*) pointer);
        tStruct.field4 = 0;
        return tStruct.field2;
    */

    public static int passPointerTest(StructPointer<TestStruct> testStruct) {
        return passPointerTest(testStruct.getPointer());
    }

    private static native int passPointerTest(long pointer);/*
        TestStruct* tStruct = (TestStruct*) pointer;
        tStruct->field4 = 5;
        return tStruct->field2;
    */

    public interface CallbackNoReturnStructArg extends Closure {
        void toCall(TestStruct arg);

        @Override
        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            toCall((TestStruct)parameters[0].asPointing());
        }
    }

    public interface CallbackNoReturnStructPointerArg extends Closure {
        void toCall(TestStruct.TestStructPointer arg);

        @Override
        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            toCall((TestStruct.TestStructPointer)parameters[0].asPointing());
        }
    }

    public interface CallbackStructReturnNoArg extends Closure {
        TestStruct toCall();

        @Override
        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(toCall());
        }
    }

    public interface CallbackStructPointerReturnNoArg extends Closure {
        StructPointer<TestStruct> toCall();

        @Override
        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(toCall());
        }
    }

    public static StructPointer<TestStruct> methodWithStructPointerReturn(
            ClosureObject<CallbackStructPointerReturnNoArg> closure) {
        long peer = methodWithCallbackStructPointerReturn(closure.getFnPtr());
        return new TestStructPointer(peer, false);
    }

    public static TestStruct methodWithStructReturn(ClosureObject<CallbackStructReturnNoArg> closure) {
        TestStruct testStruct = new TestStruct();
        methodWithCallbackStructReturn(closure.getFnPtr(), testStruct.getPointer());
        return testStruct;
    }

    private static native void methodWithCallbackStructReturn(long fnPtr, long pointer);/*
        *reinterpret_cast<TestStruct*>(pointer) = ((TestStruct (*)())fnPtr)();
     */


    public static native long methodWithCallbackStructPointerReturn(long fnPtr);/*
         return reinterpret_cast<jlong>(((TestStruct* (*)())fnPtr)());
    */

    public static TestStruct returnStructTest() {
        TestStruct testStruct = new TestStruct();
        returnStructTest(testStruct.getPointer());
        return testStruct;
    }

    private static native void returnStructTest(long retPointer);/*
        TestStruct* ret = (TestStruct*) retPointer;

        TestStruct str = {
            .field1 = 1,
            .field2 = 2,
            .field3 = 3,
            .field4 = 4
        };

        *ret = str;
    */

    public static TestStruct.TestStructPointer returnStructPointerTest() {
        return new TestStructPointer(returnStructPointerTestN(), false);
    }

    private static native long returnStructPointerTestN();/*
        TestStruct* ptr = (TestStruct*)malloc(sizeof(TestStruct));
        TestStruct str = {
            .field1 = 1,
            .field2 = 2,
            .field3 = 3,
            .field4 = 4
        };

        *ptr = str;
        return (jlong)ptr;
    */

    private static final long __size;
    private static final long __ffi_type;

    static {
        __ffi_type = generateFFIType();
        __size = Global.getSizeFromFFIType(__ffi_type);
        Global.registerStructFFIType(TestStruct.class, __ffi_type);
        Global.registerPointingSupplier(TestStruct.class, TestStruct::new);
        Global.registerNewStructPointerSupplier(TestStruct.class, TestStructPointer::new);
        Global.registerStructPointer(TestStruct.class, TestStructPointer::new);
        Global.registerPointingSupplier(TestStructPointer.class, TestStructPointer::new);
    }

    private static native long generateFFIType();/*
        ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
        type->type = FFI_TYPE_STRUCT;
        type->size = sizeof(TestStruct);
        type->alignment = alignof(TestStruct);
        type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * 5);
        type->elements[0] = GET_FFI_TYPE(uint64_t);
        type->elements[1] = GET_FFI_TYPE(uint32_t);
        type->elements[2] = GET_FFI_TYPE(uint16_t);
        type->elements[3] = GET_FFI_TYPE(uint8_t);
        type->elements[4] = NULL;

        return reinterpret_cast<jlong>(type);
    */

    protected TestStruct(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    protected TestStruct() {
        super(__size);
    }

    @Override
    public long getSize() {
        return __size;
    }

    @Override
    public long getFFIType() {
        return __ffi_type;
    }

    public long getField1() {
        return Global.getStructField(getPointer(), __ffi_type, 0);
    }

    public void setField1(long value) {
        Global.setStructField(getPointer(), __ffi_type, 0, value);
    }

    public int getField2() {
        return (int)Global.getStructField(getPointer(), __ffi_type, 1);
    }

    public void setField2(int value) {
        Global.setStructField(getPointer(), __ffi_type, 1, value);
    }

    public short getField3() {
        return (short) Global.getStructField(getPointer(), __ffi_type, 2);
    }

    public void setField3(short value) {
        Global.setStructField(getPointer(), __ffi_type, 2, value);
    }


    public byte getField4() {
        return (byte)Global.getStructField(getPointer(), __ffi_type, 3);
    }

    public void setField4(byte value) {
        Global.setStructField(getPointer(), __ffi_type, 3, value);
    }


    public static final class TestStructPointer extends StructPointer<TestStruct> {

        public TestStructPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public TestStructPointer() {
            super(__size);
        }

        @Override
        public Class<TestStruct> getStructClass() {
            return TestStruct.class;
        }

        @Override
        public long getSize() {
            return __size;
        }
    }
}
