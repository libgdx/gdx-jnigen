package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.Struct;
import com.badlogic.gdx.jnigen.pointer.StructPointer;

public class TestStruct extends Struct {

    /*JNI
        #include <stdint.h>
        #include <stdlib.h>
        typedef struct TestStruct {
            uint64_t field1;
            uint32_t field2;
            uint16_t field3;
            uint8_t field4;
        } TestStruct;
    */

    // GLOBAL FUNCTIONS
    public static int passByValueTest(TestStruct testStruct) {
        return passByValueTest(testStruct.getPointer());
    }

    private static native int passByValueTest(long pointer);/*
        TestStruct tStruct = *((TestStruct*) pointer);

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

    public static StructPointer<TestStruct> returnStructPointerTest() {
        return new StructPointer<>(returnStructPointerTestN(), false, TestStruct::new);
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
        __size = calculateSize();
        __ffi_type = generateFFIType();
    }

    private static native long calculateSize();/*
        return (jlong)sizeof(TestStruct);
    */

    private static native long generateFFIType();/*
        return (jlong)sizeof(TestStruct);
    */

    protected TestStruct() {
        super(__size);
    }

    @Override
    public StructPointer<TestStruct> asPointer() {
        // Okay, this might be shit, because we now get a new view on the Struct as a StructPointer.
        // This might lead to problems, where the original Struct gets freed, while the StructPointer view still exists.
        // So, we might need an efficient way to determine, if another Pointing exists, that targets the same address.
        // This way we can Queue the StructPointer and wait with release, until both went out of reach.
        // Another way might be, that the StructPointer holds a strong ref to the Struct, but I don't like that.
        return new StructPointer<>(getPointer(), false, TestStruct::new);
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
        return getField(getPointer(), (byte)1);
    }

    public void setField1(long value) {
        setField(getPointer(), value, (byte) 1);
    }

    public int getField2() {
        return (int)getField(getPointer(), (byte)2);
    }

    public void setField2(int value) {
        setField(getPointer(), value, (byte) 2);
    }

    public short getField3() {
        return (short)getField(getPointer(), (byte)3);
    }

    public void setField3(short value) {
        setField(getPointer(), value, (byte) 3);
    }


    public byte getField4() {
        return (byte)getField(getPointer(), (byte)4);
    }

    public void setField4(byte value) {
        setField(getPointer(), value, (byte) 4);
    }

    private static native long getField(long pointer, byte field); /*
        TestStruct* passedStruct = (TestStruct*) pointer;
        switch (field) {
            case 1:
                return passedStruct->field1;
            case 2:
                return passedStruct->field2;
            case 3:
                return passedStruct->field3;
            case 4:
                return passedStruct->field4;
            default:
                return -1;
        }
    */

    private static native void setField(long pointer, long value, byte field); /*
        TestStruct* passedStruct = (TestStruct*) pointer;
        switch (field) {
            case 1:
                passedStruct->field1 = value;
                break;
            case 2:
                passedStruct->field2 = value;
                break;
            case 3:
                passedStruct->field3 = value;
                break;
            case 4:
                passedStruct->field4 = value;
                break;
        }
    */
}
