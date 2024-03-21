package com.badlogic.jnigen.generated;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.jnigen.generated.structs.AnonymousStructNoField;
import com.badlogic.jnigen.generated.structs.AnonymousStructField;
import com.badlogic.jnigen.generated.structs.AnonymousStructFieldArray;
import com.badlogic.jnigen.generated.structs.AnonymousClosure;
import com.badlogic.jnigen.generated.structs.AnonymousStructNoFieldEnd;
import com.badlogic.jnigen.generated.structs.AnonymousStructNoFieldConsecutive;
import com.badlogic.jnigen.generated.structs.AnonymousStructNoFieldNested;
import com.badlogic.gdx.jnigen.pointer.StackElementPointer;
import com.badlogic.jnigen.generated.structs.TestStruct;
import com.badlogic.gdx.jnigen.pointer.PointerPointer;
import com.badlogic.gdx.jnigen.pointer.VoidPointer;
import com.badlogic.gdx.jnigen.pointer.EnumPointer;
import com.badlogic.jnigen.generated.enums.TestEnum;
import com.badlogic.gdx.jnigen.pointer.CSizedIntPointer;
import com.badlogic.gdx.jnigen.pointer.FloatPointer;
import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.jnigen.generated.TestData.methodWithCallback;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackLongArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackIntArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackShortArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackByteArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackCharArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackBooleanArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackFloatArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackDoubleArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackAllArgs;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackLongReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackIntReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackShortReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackCharReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackByteReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackBooleanReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackFloatReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackDoubleReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestStructReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestStructPointerReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestStructArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestStructPointerArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestEnumReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestEnumArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestEnumPointerReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestEnumPointerArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackIntPointerReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackIntPointerArg;
import com.badlogic.jnigen.generated.structs.SpecialStruct;
import com.badlogic.jnigen.generated.structs.TestUnion;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestUnionPointerReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestUnionPointerArg;
import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.ffi.JavaTypeWrapper;
import com.badlogic.gdx.jnigen.c.CTypeInfo;

public final class TestData {

    /*JNI
#include <jnigen.h>
#include <test_data.h>
*/
    public static void ensureAnonymousStructParsed(AnonymousStructNoField arg0, AnonymousStructField arg1, AnonymousStructFieldArray arg2, AnonymousClosure arg3, AnonymousStructNoFieldEnd arg4, AnonymousStructNoFieldConsecutive arg5, AnonymousStructNoFieldNested arg6) {
        ensureAnonymousStructParsed_internal(arg0.getPointer(), arg1.getPointer(), arg2.getPointer(), arg3.getPointer(), arg4.getPointer(), arg5.getPointer(), arg6.getPointer());
    }

    static private native void ensureAnonymousStructParsed_internal(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6);/*
    	ensureAnonymousStructParsed(*(AnonymousStructNoField*)arg0, *(AnonymousStructField*)arg1, *(AnonymousStructFieldArray*)arg2, *(struct AnonymousClosure*)arg3, *(AnonymousStructNoFieldEnd*)arg4, *(AnonymousStructNoFieldConsecutive*)arg5, *(AnonymousStructNoFieldNested*)arg6);
    */

    public static void constArrayParameter(TestStruct.TestStructPointer structs) {
        constArrayParameter_internal(structs.getPointer());
    }

    static private native void constArrayParameter_internal(long structs);/*
    	constArrayParameter((const TestStruct*)structs);
    */

    public static PointerPointer<VoidPointer> voidPointerPointer(PointerPointer<VoidPointer> test) {
        test.assertDepth(2);
        return new PointerPointer<VoidPointer>(voidPointerPointer_internal(test.getPointer()), false, VoidPointer::new, 2);
    }

    static private native long voidPointerPointer_internal(long test);/*
    	return (jlong)voidPointerPointer((void **)test);
    */

    public static PointerPointer<EnumPointer<TestEnum>> enumPointerPointer(PointerPointer<EnumPointer<TestEnum>> test) {
        test.assertDepth(2);
        return new PointerPointer<EnumPointer<TestEnum>>(enumPointerPointer_internal(test.getPointer()), false, EnumPointer.getPointerPointerSupplier(TestEnum::getByIndex), 2);
    }

    static private native long enumPointerPointer_internal(long test);/*
    	return (jlong)enumPointerPointer((TestEnum **)test);
    */

    public static PointerPointer<TestStruct.TestStructPointer> structPointerPointer(PointerPointer<TestStruct.TestStructPointer> test) {
        test.assertDepth(2);
        return new PointerPointer<TestStruct.TestStructPointer>(structPointerPointer_internal(test.getPointer()), false, TestStruct.TestStructPointer::new, 2);
    }

    static private native long structPointerPointer_internal(long test);/*
    	return (jlong)structPointerPointer((TestStruct **)test);
    */

    public static PointerPointer<CSizedIntPointer> intPointerPointer(PointerPointer<CSizedIntPointer> test) {
        test.assertDepth(2);
        return new PointerPointer<CSizedIntPointer>(intPointerPointer_internal(test.getPointer()), false, CSizedIntPointer.getPointerPointerSupplier("int"), 2);
    }

    static private native long intPointerPointer_internal(long test);/*
    	return (jlong)intPointerPointer((int **)test);
    */

    public static PointerPointer<FloatPointer> floatPointerPointer(PointerPointer<FloatPointer> test) {
        test.assertDepth(2);
        return new PointerPointer<FloatPointer>(floatPointerPointer_internal(test.getPointer()), false, FloatPointer::new, 2);
    }

    static private native long floatPointerPointer_internal(long test);/*
    	return (jlong)floatPointerPointer((float **)test);
    */

    public static PointerPointer<PointerPointer<PointerPointer<PointerPointer<VoidPointer>>>> pointerPointerManyyy(PointerPointer<PointerPointer<PointerPointer<PointerPointer<VoidPointer>>>> test) {
        test.assertDepth(5);
        return new PointerPointer<PointerPointer<PointerPointer<PointerPointer<VoidPointer>>>>(pointerPointerManyyy_internal(test.getPointer()), false, VoidPointer::new, 5);
    }

    static private native long pointerPointerManyyy_internal(long test);/*
    	return (jlong)pointerPointerManyyy((void *****)test);
    */

    public static void call_methodWithCallback(ClosureObject<methodWithCallback> fnPtr) {
        call_methodWithCallback_internal(fnPtr.getFnPtr());
    }

    static private native void call_methodWithCallback_internal(long fnPtr);/*
    	call_methodWithCallback((methodWithCallback)fnPtr);
    */

    public static void call_methodWithCallbackLongArg(ClosureObject<methodWithCallbackLongArg> fnPtr) {
        call_methodWithCallbackLongArg_internal(fnPtr.getFnPtr());
    }

    static private native void call_methodWithCallbackLongArg_internal(long fnPtr);/*
    	call_methodWithCallbackLongArg((methodWithCallbackLongArg)fnPtr);
    */

    public static void call_methodWithCallbackIntArg(ClosureObject<methodWithCallbackIntArg> fnPtr) {
        call_methodWithCallbackIntArg_internal(fnPtr.getFnPtr());
    }

    static private native void call_methodWithCallbackIntArg_internal(long fnPtr);/*
    	call_methodWithCallbackIntArg((methodWithCallbackIntArg)fnPtr);
    */

    public static void call_methodWithCallbackShortArg(ClosureObject<methodWithCallbackShortArg> fnPtr) {
        call_methodWithCallbackShortArg_internal(fnPtr.getFnPtr());
    }

    static private native void call_methodWithCallbackShortArg_internal(long fnPtr);/*
    	call_methodWithCallbackShortArg((methodWithCallbackShortArg)fnPtr);
    */

    public static void call_methodWithCallbackByteArg(ClosureObject<methodWithCallbackByteArg> fnPtr) {
        call_methodWithCallbackByteArg_internal(fnPtr.getFnPtr());
    }

    static private native void call_methodWithCallbackByteArg_internal(long fnPtr);/*
    	call_methodWithCallbackByteArg((methodWithCallbackByteArg)fnPtr);
    */

    public static void call_methodWithCallbackCharArg(ClosureObject<methodWithCallbackCharArg> fnPtr) {
        call_methodWithCallbackCharArg_internal(fnPtr.getFnPtr());
    }

    static private native void call_methodWithCallbackCharArg_internal(long fnPtr);/*
    	call_methodWithCallbackCharArg((methodWithCallbackCharArg)fnPtr);
    */

    public static void call_methodWithCallbackBooleanArg(ClosureObject<methodWithCallbackBooleanArg> fnPtr) {
        call_methodWithCallbackBooleanArg_internal(fnPtr.getFnPtr());
    }

    static private native void call_methodWithCallbackBooleanArg_internal(long fnPtr);/*
    	call_methodWithCallbackBooleanArg((methodWithCallbackBooleanArg)fnPtr);
    */

    public static void call_methodWithCallbackFloatArg(ClosureObject<methodWithCallbackFloatArg> fnPtr) {
        call_methodWithCallbackFloatArg_internal(fnPtr.getFnPtr());
    }

    static private native void call_methodWithCallbackFloatArg_internal(long fnPtr);/*
    	call_methodWithCallbackFloatArg((methodWithCallbackFloatArg)fnPtr);
    */

    public static void call_methodWithCallbackDoubleArg(ClosureObject<methodWithCallbackDoubleArg> fnPtr) {
        call_methodWithCallbackDoubleArg_internal(fnPtr.getFnPtr());
    }

    static private native void call_methodWithCallbackDoubleArg_internal(long fnPtr);/*
    	call_methodWithCallbackDoubleArg((methodWithCallbackDoubleArg)fnPtr);
    */

    public static void call_methodWithCallbackAllArgs(ClosureObject<methodWithCallbackAllArgs> fnPtr) {
        call_methodWithCallbackAllArgs_internal(fnPtr.getFnPtr());
    }

    static private native void call_methodWithCallbackAllArgs_internal(long fnPtr);/*
    	call_methodWithCallbackAllArgs((methodWithCallbackAllArgs)fnPtr);
    */

    public static long call_methodWithCallbackLongReturn(ClosureObject<methodWithCallbackLongReturn> fnPtr) {
        return call_methodWithCallbackLongReturn_internal(fnPtr.getFnPtr());
    }

    static private native long call_methodWithCallbackLongReturn_internal(long fnPtr);/*
    	return (jlong)call_methodWithCallbackLongReturn((methodWithCallbackLongReturn)fnPtr);
    */

    public static int call_methodWithCallbackIntReturn(ClosureObject<methodWithCallbackIntReturn> fnPtr) {
        return call_methodWithCallbackIntReturn_internal(fnPtr.getFnPtr());
    }

    static private native int call_methodWithCallbackIntReturn_internal(long fnPtr);/*
    	return (jint)call_methodWithCallbackIntReturn((methodWithCallbackIntReturn)fnPtr);
    */

    public static short call_methodWithCallbackShortReturn(ClosureObject<methodWithCallbackShortReturn> fnPtr) {
        return call_methodWithCallbackShortReturn_internal(fnPtr.getFnPtr());
    }

    static private native short call_methodWithCallbackShortReturn_internal(long fnPtr);/*
    	return (jshort)call_methodWithCallbackShortReturn((methodWithCallbackShortReturn)fnPtr);
    */

    public static char call_methodWithCallbackCharReturn(ClosureObject<methodWithCallbackCharReturn> fnPtr) {
        return call_methodWithCallbackCharReturn_internal(fnPtr.getFnPtr());
    }

    static private native char call_methodWithCallbackCharReturn_internal(long fnPtr);/*
    	return (jchar)call_methodWithCallbackCharReturn((methodWithCallbackCharReturn)fnPtr);
    */

    public static byte call_methodWithCallbackByteReturn(ClosureObject<methodWithCallbackByteReturn> fnPtr) {
        return call_methodWithCallbackByteReturn_internal(fnPtr.getFnPtr());
    }

    static private native byte call_methodWithCallbackByteReturn_internal(long fnPtr);/*
    	return (jbyte)call_methodWithCallbackByteReturn((methodWithCallbackByteReturn)fnPtr);
    */

    public static boolean call_methodWithCallbackBooleanReturn(ClosureObject<methodWithCallbackBooleanReturn> fnPtr) {
        return call_methodWithCallbackBooleanReturn_internal(fnPtr.getFnPtr());
    }

    static private native boolean call_methodWithCallbackBooleanReturn_internal(long fnPtr);/*
    	return (jboolean)call_methodWithCallbackBooleanReturn((methodWithCallbackBooleanReturn)fnPtr);
    */

    public static float call_methodWithCallbackFloatReturn(ClosureObject<methodWithCallbackFloatReturn> fnPtr) {
        return call_methodWithCallbackFloatReturn_internal(fnPtr.getFnPtr());
    }

    static private native float call_methodWithCallbackFloatReturn_internal(long fnPtr);/*
    	return (jfloat)call_methodWithCallbackFloatReturn((methodWithCallbackFloatReturn)fnPtr);
    */

    public static double call_methodWithCallbackDoubleReturn(ClosureObject<methodWithCallbackDoubleReturn> fnPtr) {
        return call_methodWithCallbackDoubleReturn_internal(fnPtr.getFnPtr());
    }

    static private native double call_methodWithCallbackDoubleReturn_internal(long fnPtr);/*
    	return (jdouble)call_methodWithCallbackDoubleReturn((methodWithCallbackDoubleReturn)fnPtr);
    */

    public static TestStruct.TestStructPointer returnTestStructPointer() {
        return new TestStruct.TestStructPointer(returnTestStructPointer_internal(), false);
    }

    static private native long returnTestStructPointer_internal();/*
    	return (jlong)returnTestStructPointer();
    */

    public static TestStruct returnTestStruct() {
        return new TestStruct(returnTestStruct_internal(), true);
    }

    static private native long returnTestStruct_internal();/*
    	TestStruct* _ret = (TestStruct*)malloc(sizeof(TestStruct));
    	*_ret = returnTestStruct();
    	return (jlong)_ret;
    */

    public static long passByValue(TestStruct testStruct) {
        return passByValue_internal(testStruct.getPointer());
    }

    static private native long passByValue_internal(long testStruct);/*
    	return (jlong)passByValue(*(TestStruct*)testStruct);
    */

    public static long passByPointer(TestStruct.TestStructPointer testStruct) {
        return passByPointer_internal(testStruct.getPointer());
    }

    static private native long passByPointer_internal(long testStruct);/*
    	return (jlong)passByPointer((TestStruct *)testStruct);
    */

    public static TestStruct call_methodWithCallbackTestStructReturn(ClosureObject<methodWithCallbackTestStructReturn> fnPtr) {
        return new TestStruct(call_methodWithCallbackTestStructReturn_internal(fnPtr.getFnPtr()), true);
    }

    static private native long call_methodWithCallbackTestStructReturn_internal(long fnPtr);/*
    	TestStruct* _ret = (TestStruct*)malloc(sizeof(TestStruct));
    	*_ret = call_methodWithCallbackTestStructReturn((methodWithCallbackTestStructReturn)fnPtr);
    	return (jlong)_ret;
    */

    public static TestStruct.TestStructPointer call_methodWithCallbackTestStructPointerReturn(ClosureObject<methodWithCallbackTestStructPointerReturn> fnPtr) {
        return new TestStruct.TestStructPointer(call_methodWithCallbackTestStructPointerReturn_internal(fnPtr.getFnPtr()), false);
    }

    static private native long call_methodWithCallbackTestStructPointerReturn_internal(long fnPtr);/*
    	return (jlong)call_methodWithCallbackTestStructPointerReturn((methodWithCallbackTestStructPointerReturn)fnPtr);
    */

    public static void call_methodWithCallbackTestStructArg(ClosureObject<methodWithCallbackTestStructArg> fnPtr) {
        call_methodWithCallbackTestStructArg_internal(fnPtr.getFnPtr());
    }

    static private native void call_methodWithCallbackTestStructArg_internal(long fnPtr);/*
    	call_methodWithCallbackTestStructArg((methodWithCallbackTestStructArg)fnPtr);
    */

    public static void call_methodWithCallbackTestStructPointerArg(ClosureObject<methodWithCallbackTestStructPointerArg> fnPtr) {
        call_methodWithCallbackTestStructPointerArg_internal(fnPtr.getFnPtr());
    }

    static private native void call_methodWithCallbackTestStructPointerArg_internal(long fnPtr);/*
    	call_methodWithCallbackTestStructPointerArg((methodWithCallbackTestStructPointerArg)fnPtr);
    */

    public static int passTestEnum(TestEnum enumValue) {
        return passTestEnum_internal(enumValue.getIndex());
    }

    static private native int passTestEnum_internal(int enumValue);/*
    	return (jint)passTestEnum((TestEnum)enumValue);
    */

    public static TestEnum returnTestEnum() {
        return TestEnum.getByIndex((int) returnTestEnum_internal());
    }

    static private native int returnTestEnum_internal();/*
    	return (jint)returnTestEnum();
    */

    public static TestEnum passAndReturnTestEnum(TestEnum enumValue) {
        return TestEnum.getByIndex((int) passAndReturnTestEnum_internal(enumValue.getIndex()));
    }

    static private native int passAndReturnTestEnum_internal(int enumValue);/*
    	return (jint)passAndReturnTestEnum((TestEnum)enumValue);
    */

    public static int passTestEnumPointer(EnumPointer<TestEnum> enumValue) {
        return passTestEnumPointer_internal(enumValue.getPointer());
    }

    static private native int passTestEnumPointer_internal(long enumValue);/*
    	return (jint)passTestEnumPointer((TestEnum *)enumValue);
    */

    public static EnumPointer<TestEnum> returnTestEnumPointer() {
        return new EnumPointer<TestEnum>(returnTestEnumPointer_internal(), false, TestEnum::getByIndex);
    }

    static private native long returnTestEnumPointer_internal();/*
    	return (jlong)returnTestEnumPointer();
    */

    public static TestEnum passAndReturnTestEnumPointer(EnumPointer<TestEnum> enumValue) {
        return TestEnum.getByIndex((int) passAndReturnTestEnumPointer_internal(enumValue.getPointer()));
    }

    static private native int passAndReturnTestEnumPointer_internal(long enumValue);/*
    	return (jint)passAndReturnTestEnumPointer((TestEnum *)enumValue);
    */

    public static TestEnum call_methodWithCallbackTestEnumReturn(ClosureObject<methodWithCallbackTestEnumReturn> fnPtr) {
        return TestEnum.getByIndex((int) call_methodWithCallbackTestEnumReturn_internal(fnPtr.getFnPtr()));
    }

    static private native int call_methodWithCallbackTestEnumReturn_internal(long fnPtr);/*
    	return (jint)call_methodWithCallbackTestEnumReturn((methodWithCallbackTestEnumReturn)fnPtr);
    */

    public static void call_methodWithCallbackTestEnumArg(ClosureObject<methodWithCallbackTestEnumArg> fnPtr) {
        call_methodWithCallbackTestEnumArg_internal(fnPtr.getFnPtr());
    }

    static private native void call_methodWithCallbackTestEnumArg_internal(long fnPtr);/*
    	call_methodWithCallbackTestEnumArg((methodWithCallbackTestEnumArg)fnPtr);
    */

    public static EnumPointer<TestEnum> call_methodWithCallbackTestEnumPointerReturn(ClosureObject<methodWithCallbackTestEnumPointerReturn> fnPtr) {
        return new EnumPointer<TestEnum>(call_methodWithCallbackTestEnumPointerReturn_internal(fnPtr.getFnPtr()), false, TestEnum::getByIndex);
    }

    static private native long call_methodWithCallbackTestEnumPointerReturn_internal(long fnPtr);/*
    	return (jlong)call_methodWithCallbackTestEnumPointerReturn((methodWithCallbackTestEnumPointerReturn)fnPtr);
    */

    public static void call_methodWithCallbackTestEnumPointerArg(ClosureObject<methodWithCallbackTestEnumPointerArg> fnPtr) {
        call_methodWithCallbackTestEnumPointerArg_internal(fnPtr.getFnPtr());
    }

    static private native void call_methodWithCallbackTestEnumPointerArg_internal(long fnPtr);/*
    	call_methodWithCallbackTestEnumPointerArg((methodWithCallbackTestEnumPointerArg)fnPtr);
    */

    public static int passIntPointer(CSizedIntPointer arg0) {
        arg0.assertHasCTypeBacking("int");
        return passIntPointer_internal(arg0.getPointer());
    }

    static private native int passIntPointer_internal(long arg0);/*
    	return (jint)passIntPointer((int *)arg0);
    */

    public static CSizedIntPointer returnIntPointer(int arg0) {
        return new CSizedIntPointer(returnIntPointer_internal(arg0), false, "int");
    }

    static private native long returnIntPointer_internal(int arg0);/*
    	return (jlong)returnIntPointer((int)arg0);
    */

    public static CSizedIntPointer call_methodWithCallbackIntPointerReturn(ClosureObject<methodWithCallbackIntPointerReturn> fnPtr, int val) {
        return new CSizedIntPointer(call_methodWithCallbackIntPointerReturn_internal(fnPtr.getFnPtr(), val), false, "int");
    }

    static private native long call_methodWithCallbackIntPointerReturn_internal(long fnPtr, int val);/*
    	return (jlong)call_methodWithCallbackIntPointerReturn((methodWithCallbackIntPointerReturn)fnPtr, (int)val);
    */

    public static int call_methodWithCallbackIntPointerArg(ClosureObject<methodWithCallbackIntPointerArg> fnPtr) {
        return call_methodWithCallbackIntPointerArg_internal(fnPtr.getFnPtr());
    }

    static private native int call_methodWithCallbackIntPointerArg_internal(long fnPtr);/*
    	return (jint)call_methodWithCallbackIntPointerArg((methodWithCallbackIntPointerArg)fnPtr);
    */

    public static float getFloatPtrFieldValue(SpecialStruct specialStruct) {
        return getFloatPtrFieldValue_internal(specialStruct.getPointer());
    }

    static private native float getFloatPtrFieldValue_internal(long specialStruct);/*
    	return (jfloat)getFloatPtrFieldValue(*(SpecialStruct*)specialStruct);
    */

    public static int getFixedSizeArrayFieldValue(SpecialStruct specialStruct, int index) {
        return getFixedSizeArrayFieldValue_internal(specialStruct.getPointer(), index);
    }

    static private native int getFixedSizeArrayFieldValue_internal(long specialStruct, int index);/*
    	return (jint)getFixedSizeArrayFieldValue(*(SpecialStruct*)specialStruct, (int)index);
    */

    public static int getIntPtrFieldValue(SpecialStruct specialStruct) {
        return getIntPtrFieldValue_internal(specialStruct.getPointer());
    }

    static private native int getIntPtrFieldValue_internal(long specialStruct);/*
    	return (jint)getIntPtrFieldValue(*(SpecialStruct*)specialStruct);
    */

    public static void setFloatPtrFieldValue(SpecialStruct specialStruct, float value) {
        setFloatPtrFieldValue_internal(specialStruct.getPointer(), value);
    }

    static private native void setFloatPtrFieldValue_internal(long specialStruct, float value);/*
    	setFloatPtrFieldValue(*(SpecialStruct*)specialStruct, (float)value);
    */

    public static void setFixedSizeArrayFieldValue(SpecialStruct.SpecialStructPointer specialStruct, int index, int value) {
        setFixedSizeArrayFieldValue_internal(specialStruct.getPointer(), index, value);
    }

    static private native void setFixedSizeArrayFieldValue_internal(long specialStruct, int index, int value);/*
    	setFixedSizeArrayFieldValue((SpecialStruct *)specialStruct, (int)index, (int)value);
    */

    public static void setIntPtrFieldValue(SpecialStruct specialStruct, int value) {
        setIntPtrFieldValue_internal(specialStruct.getPointer(), value);
    }

    static private native void setIntPtrFieldValue_internal(long specialStruct, int value);/*
    	setIntPtrFieldValue(*(SpecialStruct*)specialStruct, (int)value);
    */

    public static TestUnion.TestUnionPointer returnTestUnionPointer() {
        return new TestUnion.TestUnionPointer(returnTestUnionPointer_internal(), false);
    }

    static private native long returnTestUnionPointer_internal();/*
    	return (jlong)returnTestUnionPointer();
    */

    public static TestUnion returnTestUnion() {
        return new TestUnion(returnTestUnion_internal(), true);
    }

    static private native long returnTestUnion_internal();/*
    	TestUnion* _ret = (TestUnion*)malloc(sizeof(TestUnion));
    	*_ret = returnTestUnion();
    	return (jlong)_ret;
    */

    public static long getUnionUintTypeByValue(TestUnion testUnion) {
        return getUnionUintTypeByValue_internal(testUnion.getPointer());
    }

    static private native long getUnionUintTypeByValue_internal(long testUnion);/*
    	return (jlong)getUnionUintTypeByValue(*(TestUnion*)testUnion);
    */

    public static void setUnionUintTypeByPointer(TestUnion.TestUnionPointer testUnion, long value) {
        setUnionUintTypeByPointer_internal(testUnion.getPointer(), value);
    }

    static private native void setUnionUintTypeByPointer_internal(long testUnion, long value);/*
    	setUnionUintTypeByPointer((TestUnion *)testUnion, (uint64_t)value);
    */

    public static double getUnionDoubleTypeByValue(TestUnion testUnion) {
        return getUnionDoubleTypeByValue_internal(testUnion.getPointer());
    }

    static private native double getUnionDoubleTypeByValue_internal(long testUnion);/*
    	return (jdouble)getUnionDoubleTypeByValue(*(TestUnion*)testUnion);
    */

    public static void setUnionDoubleTypeByPointer(TestUnion.TestUnionPointer testUnion, double value) {
        setUnionDoubleTypeByPointer_internal(testUnion.getPointer(), value);
    }

    static private native void setUnionDoubleTypeByPointer_internal(long testUnion, double value);/*
    	setUnionDoubleTypeByPointer((TestUnion *)testUnion, (double)value);
    */

    public static CSizedIntPointer getUnionFixedSizeIntByPointer(TestUnion.TestUnionPointer testUnion) {
        return new CSizedIntPointer(getUnionFixedSizeIntByPointer_internal(testUnion.getPointer()), false, "int");
    }

    static private native long getUnionFixedSizeIntByPointer_internal(long testUnion);/*
    	return (jlong)getUnionFixedSizeIntByPointer((TestUnion *)testUnion);
    */

    public static int getUnionFixedSizeIntByValue(TestUnion testUnion, int index) {
        return getUnionFixedSizeIntByValue_internal(testUnion.getPointer(), index);
    }

    static private native int getUnionFixedSizeIntByValue_internal(long testUnion, int index);/*
    	return (jint)getUnionFixedSizeIntByValue(*(TestUnion*)testUnion, (int)index);
    */

    public static void setUnionFixedSizeIntByPointer(TestUnion.TestUnionPointer testUnion, int index, int value) {
        setUnionFixedSizeIntByPointer_internal(testUnion.getPointer(), index, value);
    }

    static private native void setUnionFixedSizeIntByPointer_internal(long testUnion, int index, int value);/*
    	setUnionFixedSizeIntByPointer((TestUnion *)testUnion, (int)index, (int)value);
    */

    public static TestStruct getUnionStructTypeByValue(TestUnion testUnion) {
        return new TestStruct(getUnionStructTypeByValue_internal(testUnion.getPointer()), true);
    }

    static private native long getUnionStructTypeByValue_internal(long testUnion);/*
    	TestStruct* _ret = (TestStruct*)malloc(sizeof(TestStruct));
    	*_ret = getUnionStructTypeByValue(*(TestUnion*)testUnion);
    	return (jlong)_ret;
    */

    public static void setUnionStructTypeByPointer(TestUnion.TestUnionPointer testUnion, TestStruct value) {
        setUnionStructTypeByPointer_internal(testUnion.getPointer(), value.getPointer());
    }

    static private native void setUnionStructTypeByPointer_internal(long testUnion, long value);/*
    	setUnionStructTypeByPointer((TestUnion *)testUnion, *(TestStruct*)value);
    */

    public static TestUnion.TestUnionPointer call_methodWithCallbackTestUnionPointerReturn(ClosureObject<methodWithCallbackTestUnionPointerReturn> fnPtr) {
        return new TestUnion.TestUnionPointer(call_methodWithCallbackTestUnionPointerReturn_internal(fnPtr.getFnPtr()), false);
    }

    static private native long call_methodWithCallbackTestUnionPointerReturn_internal(long fnPtr);/*
    	return (jlong)call_methodWithCallbackTestUnionPointerReturn((methodWithCallbackTestUnionPointerReturn)fnPtr);
    */

    public static void call_methodWithCallbackTestUnionPointerArg(ClosureObject<methodWithCallbackTestUnionPointerArg> fnPtr) {
        call_methodWithCallbackTestUnionPointerArg_internal(fnPtr.getFnPtr());
    }

    static private native void call_methodWithCallbackTestUnionPointerArg_internal(long fnPtr);/*
    	call_methodWithCallbackTestUnionPointerArg((methodWithCallbackTestUnionPointerArg)fnPtr);
    */

    public interface methodWithCallbackBooleanArg extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(0) };

        void methodWithCallbackBooleanArg_call(boolean arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackBooleanArg_call(parameters[0].asLong() != 0);
        }
    }

    public interface methodWithCallbackTestEnumPointerArg extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(-1) };

        void methodWithCallbackTestEnumPointerArg_call(EnumPointer<TestEnum> arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackTestEnumPointerArg_call(new EnumPointer<TestEnum>(parameters[0].asLong(), false, TestEnum::getByIndex));
        }
    }

    public interface methodWithCallbackDoubleReturn extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(2) };

        double methodWithCallbackDoubleReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackDoubleReturn_call());
        }
    }

    public interface methodWithCallbackIntArg extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(4) };

        void methodWithCallbackIntArg_call(int arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackIntArg_call((int) parameters[0].asLong());
        }
    }

    public interface methodWithCallbackTestStructPointerReturn extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-1) };

        TestStruct.TestStructPointer methodWithCallbackTestStructPointerReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestStructPointerReturn_call());
        }
    }

    public interface methodWithCallbackShortReturn extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(5) };

        short methodWithCallbackShortReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackShortReturn_call());
        }
    }

    public interface methodWithCallbackTestEnumPointerReturn extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-1) };

        EnumPointer<TestEnum> methodWithCallbackTestEnumPointerReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestEnumPointerReturn_call());
        }
    }

    public interface methodWithCallbackTestStructArg extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(18) };

        void methodWithCallbackTestStructArg_call(TestStruct arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackTestStructArg_call(new TestStruct(parameters[0].asLong(), true));
        }
    }

    public interface methodWithCallbackIntPointerReturn extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-1) };

        CSizedIntPointer methodWithCallbackIntPointerReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackIntPointerReturn_call());
        }
    }

    public interface methodWithCallbackLongArg extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(8) };

        void methodWithCallbackLongArg_call(long arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackLongArg_call((long) parameters[0].asLong());
        }
    }

    public interface methodWithCallbackFloatArg extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(3) };

        void methodWithCallbackFloatArg_call(float arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackFloatArg_call((float) parameters[0].asFloat());
        }
    }

    public interface methodWithCallbackDoubleArg extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(2) };

        void methodWithCallbackDoubleArg_call(double arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackDoubleArg_call((double) parameters[0].asDouble());
        }
    }

    public interface methodWithCallbackIntPointerArg extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(4), FFITypes.getCTypeInfo(-1) };

        int methodWithCallbackIntPointerArg_call(CSizedIntPointer arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackIntPointerArg_call(new CSizedIntPointer(parameters[0].asLong(), false, "int")));
        }
    }

    public interface methodWithCallbackTestEnumArg extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(4) };

        void methodWithCallbackTestEnumArg_call(TestEnum arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackTestEnumArg_call(TestEnum.getByIndex((int) parameters[0].asLong()));
        }
    }

    public interface methodWithCallbackTestStructPointerArg extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(-1) };

        void methodWithCallbackTestStructPointerArg_call(TestStruct.TestStructPointer arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackTestStructPointerArg_call(new TestStruct.TestStructPointer(parameters[0].asLong(), false));
        }
    }

    public interface methodWithCallbackTestUnionPointerReturn extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-1) };

        TestUnion.TestUnionPointer methodWithCallbackTestUnionPointerReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestUnionPointerReturn_call());
        }
    }

    public interface methodWithCallbackByteReturn extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(1) };

        byte methodWithCallbackByteReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackByteReturn_call());
        }
    }

    public interface methodWithCallbackCharReturn extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(6) };

        char methodWithCallbackCharReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackCharReturn_call());
        }
    }

    public interface methodWithCallbackTestEnumReturn extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(4) };

        TestEnum methodWithCallbackTestEnumReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestEnumReturn_call());
        }
    }

    public interface methodWithCallbackAllArgs extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(8), FFITypes.getCTypeInfo(4), FFITypes.getCTypeInfo(5), FFITypes.getCTypeInfo(1), FFITypes.getCTypeInfo(6), FFITypes.getCTypeInfo(0), FFITypes.getCTypeInfo(3), FFITypes.getCTypeInfo(2) };

        void methodWithCallbackAllArgs_call(long arg0, int arg1, short arg2, byte arg3, char arg4, boolean arg5, float arg6, double arg7);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackAllArgs_call((long) parameters[0].asLong(), (int) parameters[1].asLong(), (short) parameters[2].asLong(), (byte) parameters[3].asLong(), (char) parameters[4].asLong(), parameters[5].asLong() != 0, (float) parameters[6].asFloat(), (double) parameters[7].asDouble());
        }
    }

    public interface methodWithCallback extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2) };

        void methodWithCallback_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallback_call();
        }
    }

    public interface methodWithCallbackTestUnionPointerArg extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(-1) };

        void methodWithCallbackTestUnionPointerArg_call(TestUnion.TestUnionPointer arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackTestUnionPointerArg_call(new TestUnion.TestUnionPointer(parameters[0].asLong(), false));
        }
    }

    public interface methodWithCallbackShortArg extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(5) };

        void methodWithCallbackShortArg_call(short arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackShortArg_call((short) parameters[0].asLong());
        }
    }

    public interface methodWithCallbackByteArg extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(1) };

        void methodWithCallbackByteArg_call(byte arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackByteArg_call((byte) parameters[0].asLong());
        }
    }

    public interface methodWithCallbackBooleanReturn extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(0) };

        boolean methodWithCallbackBooleanReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackBooleanReturn_call());
        }
    }

    public interface methodWithCallbackIntReturn extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(4) };

        int methodWithCallbackIntReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackIntReturn_call());
        }
    }

    public interface methodWithCallbackLongReturn extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(8) };

        long methodWithCallbackLongReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackLongReturn_call());
        }
    }

    public interface methodWithCallbackCharArg extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(6) };

        void methodWithCallbackCharArg_call(char arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackCharArg_call((char) parameters[0].asLong());
        }
    }

    public interface methodWithCallbackTestStructReturn extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(18) };

        TestStruct methodWithCallbackTestStructReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestStructReturn_call());
        }
    }

    public interface methodWithCallbackFloatReturn extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(3) };

        float methodWithCallbackFloatReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackFloatReturn_call());
        }
    }
}
