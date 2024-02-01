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
        void toCall(TestStruct.Pointer arg);

        @Override
        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            toCall((TestStruct.Pointer)parameters[0].asPointing());
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
        return new TestStruct.Pointer(peer, false);
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

    public static TestStruct.Pointer returnStructPointerTest() {
        return new TestStruct.Pointer(returnStructPointerTestN(), false);
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

    private final static long __size;

    private final static long __ffi_type;

    static {
        __ffi_type = generateFFIType();
        Global.calculateAlignmentAndSizeForType(__ffi_type);
        __size = Global.getSizeFromFFIType(__ffi_type);
        Global.registerStructFFIType(TestStruct.class, __ffi_type);
        Global.registerPointingSupplier(TestStruct.class, TestStruct::new);
        Global.registerNewStructPointerSupplier(TestStruct.class, TestStruct.Pointer::new);
        Global.registerStructPointer(TestStruct.class, TestStruct.Pointer::new);
        Global.registerPointingSupplier(TestStruct.Pointer.class, TestStruct.Pointer::new);
    }

    public static native long generateFFIType();/*
    	ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
    	type->type = FFI_TYPE_STRUCT;
    	type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * 5);
    	type->elements[0] = GET_FFI_TYPE(uint64_t);
    	type->elements[1] = GET_FFI_TYPE(uint32_t);
    	type->elements[2] = GET_FFI_TYPE(uint16_t);
    	type->elements[3] = GET_FFI_TYPE(uint8_t);
    	type->elements[4] = NULL;
    	return reinterpret_cast<jlong>(type);
    */

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

    public long field1() {
        return (long) Global.getStructField(getPointer(), __ffi_type, 0);
    }

    public void field1(long field1) {
        Global.setStructField(getPointer(), __ffi_type, 0, field1);
    }

    public long field2() {
        return (long) Global.getStructField(getPointer(), __ffi_type, 1);
    }

    public void field2(long field2) {
        Global.setStructField(getPointer(), __ffi_type, 1, field2);
    }

    public int field3() {
        return (int) Global.getStructField(getPointer(), __ffi_type, 2);
    }

    public void field3(int field3) {
        Global.setStructField(getPointer(), __ffi_type, 2, field3);
    }

    public short field4() {
        return (short) Global.getStructField(getPointer(), __ffi_type, 3);
    }

    public void field4(short field4) {
        Global.setStructField(getPointer(), __ffi_type, 3, field4);
    }

    public static final class Pointer extends StructPointer<TestStruct> {

        public Pointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public Pointer() {
            super(__size);
        }

        public long getSize() {
            return __size;
        }

        public Class<TestStruct> getStructClass() {
            return TestStruct.class;
        }
    }
}
