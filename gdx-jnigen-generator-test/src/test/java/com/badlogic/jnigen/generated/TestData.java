package com.badlogic.jnigen.generated;

import com.badlogic.gdx.jnigen.runtime.c.CXXException;
import com.badlogic.jnigen.generated.structs.GlobalArg;
import com.badlogic.jnigen.generated.structs.AnonymousStructNoField;
import com.badlogic.jnigen.generated.structs.AnonymousStructField;
import com.badlogic.jnigen.generated.structs.AnonymousStructFieldArray;
import com.badlogic.jnigen.generated.structs.AnonymousClosure;
import com.badlogic.jnigen.generated.structs.AnonymousStructNoFieldEnd;
import com.badlogic.jnigen.generated.structs.AnonymousStructNoFieldConsecutive;
import com.badlogic.jnigen.generated.structs.AnonymousStructNoFieldNested;
import com.badlogic.jnigen.generated.structs.forwardDeclStruct;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.jnigen.generated.structs.TestStruct;
import com.badlogic.gdx.jnigen.runtime.pointer.PointerPointer;
import com.badlogic.jnigen.generated.enums.TestEnum;
import com.badlogic.gdx.jnigen.runtime.pointer.CSizedIntPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.FloatPointer;
import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.jnigen.generated.structs.SpecialStruct;
import com.badlogic.jnigen.generated.structs.TestUnion;
import com.badlogic.gdx.jnigen.runtime.closure.Closure;
import com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper;
import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.ffi.ClosureEncoder;

public final class TestData {

    static {
        CHandler.init();
        FFITypes.init();
        init(IllegalArgumentException.class, CXXException.class);
    }

    public static void initialize() {
    }

    /*JNI
#include <jnigen.h>
#include <test_data.h>

static jclass illegalArgumentExceptionClass = NULL;
static jclass cxxExceptionClass = NULL;
*/
    private static native void init(Class illegalArgumentException, Class cxxException);/*
    	illegalArgumentExceptionClass = (jclass)env->NewGlobalRef(illegalArgumentException);
    	cxxExceptionClass = (jclass)env->NewGlobalRef(cxxException);
    */

    public static GlobalArg getGlobalArgState() {
        return new GlobalArg(getGlobalArgState_internal(), true);
    }

    static private native long getGlobalArgState_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	GlobalArg* _ret = (GlobalArg*)malloc(sizeof(GlobalArg));
    	*_ret = getGlobalArgState();
    	return (jlong)_ret;
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void ensureParsed(AnonymousStructNoField arg0, AnonymousStructField arg1, AnonymousStructFieldArray arg2, AnonymousClosure arg3, AnonymousStructNoFieldEnd arg4, AnonymousStructNoFieldConsecutive arg5, AnonymousStructNoFieldNested arg6, forwardDeclStruct.forwardDeclStructPointer arg7) {
        ensureParsed_internal(arg0.getPointer(), arg1.getPointer(), arg2.getPointer(), arg3.getPointer(), arg4.getPointer(), arg5.getPointer(), arg6.getPointer(), arg7.getPointer());
    }

    static private native void ensureParsed_internal(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	ensureParsed(*(AnonymousStructNoField*)arg0, *(AnonymousStructField*)arg1, *(AnonymousStructFieldArray*)arg2, *(struct AnonymousClosure*)arg3, *(AnonymousStructNoFieldEnd*)arg4, *(AnonymousStructNoFieldConsecutive*)arg5, *(AnonymousStructNoFieldNested*)arg6, (struct forwardDeclStruct *)arg7);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void weirdPointer(VoidPointer _file) {
        weirdPointer_internal(_file.getPointer());
    }

    static private native void weirdPointer_internal(long _file);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	weirdPointer((FILE *)_file);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void constArrayParameter(TestStruct.TestStructPointer structs) {
        constArrayParameter_internal(structs.getPointer());
    }

    static private native void constArrayParameter_internal(long structs);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	constArrayParameter((const TestStruct*)structs);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static PointerPointer<VoidPointer> voidPointerPointer(PointerPointer<VoidPointer> test) {
        return new PointerPointer<>(voidPointerPointer_internal(test.getPointer()), false, VoidPointer::new);
    }

    static private native long voidPointerPointer_internal(long test);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)voidPointerPointer((void **)test);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static PointerPointer<TestEnum.TestEnumPointer> enumPointerPointer(PointerPointer<TestEnum.TestEnumPointer> test) {
        return new PointerPointer<>(enumPointerPointer_internal(test.getPointer()), false, TestEnum.TestEnumPointer::new);
    }

    static private native long enumPointerPointer_internal(long test);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)enumPointerPointer((TestEnum **)test);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static PointerPointer<TestStruct.TestStructPointer> structPointerPointer(PointerPointer<TestStruct.TestStructPointer> test) {
        return new PointerPointer<>(structPointerPointer_internal(test.getPointer()), false, TestStruct.TestStructPointer::new);
    }

    static private native long structPointerPointer_internal(long test);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)structPointerPointer((TestStruct **)test);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static PointerPointer<CSizedIntPointer> intPointerPointer(PointerPointer<CSizedIntPointer> test) {
        test.assertCTypeBacking("int");
        return new PointerPointer<>(intPointerPointer_internal(test.getPointer()), false, (long peer2, boolean owned2) -> new CSizedIntPointer(peer2, owned2, "int")).setBackingCType("int");
    }

    static private native long intPointerPointer_internal(long test);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)intPointerPointer((int **)test);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static PointerPointer<FloatPointer> floatPointerPointer(PointerPointer<FloatPointer> test) {
        return new PointerPointer<>(floatPointerPointer_internal(test.getPointer()), false, FloatPointer::new);
    }

    static private native long floatPointerPointer_internal(long test);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)floatPointerPointer((float **)test);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static PointerPointer<PointerPointer<PointerPointer<PointerPointer<VoidPointer>>>> pointerPointerManyyy(PointerPointer<PointerPointer<PointerPointer<PointerPointer<VoidPointer>>>> test) {
        return new PointerPointer<>(pointerPointerManyyy_internal(test.getPointer()), false, (long peer5, boolean owned5) -> new PointerPointer<>(peer5, owned5, (long peer4, boolean owned4) -> new PointerPointer<>(peer4, owned4, (long peer3, boolean owned3) -> new PointerPointer<>(peer3, owned3, VoidPointer::new))));
    }

    static private native long pointerPointerManyyy_internal(long test);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)pointerPointerManyyy((void *****)test);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void call_methodWithCallback(ClosureObject<methodWithCallback> fnPtr) {
        call_methodWithCallback_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithCallback_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallback((methodWithCallback)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackLongArg(ClosureObject<methodWithCallbackLongArg> fnPtr) {
        call_methodWithCallbackLongArg_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithCallbackLongArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackLongArg((methodWithCallbackLongArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackIntArg(ClosureObject<methodWithCallbackIntArg> fnPtr) {
        call_methodWithCallbackIntArg_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithCallbackIntArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackIntArg((methodWithCallbackIntArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackShortArg(ClosureObject<methodWithCallbackShortArg> fnPtr) {
        call_methodWithCallbackShortArg_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithCallbackShortArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackShortArg((methodWithCallbackShortArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackByteArg(ClosureObject<methodWithCallbackByteArg> fnPtr) {
        call_methodWithCallbackByteArg_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithCallbackByteArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackByteArg((methodWithCallbackByteArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackCharArg(ClosureObject<methodWithCallbackCharArg> fnPtr) {
        call_methodWithCallbackCharArg_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithCallbackCharArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackCharArg((methodWithCallbackCharArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackBooleanArg(ClosureObject<methodWithCallbackBooleanArg> fnPtr) {
        call_methodWithCallbackBooleanArg_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithCallbackBooleanArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackBooleanArg((methodWithCallbackBooleanArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackFloatArg(ClosureObject<methodWithCallbackFloatArg> fnPtr) {
        call_methodWithCallbackFloatArg_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithCallbackFloatArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackFloatArg((methodWithCallbackFloatArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackDoubleArg(ClosureObject<methodWithCallbackDoubleArg> fnPtr) {
        call_methodWithCallbackDoubleArg_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithCallbackDoubleArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackDoubleArg((methodWithCallbackDoubleArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackAllArgs(ClosureObject<methodWithCallbackAllArgs> fnPtr) {
        call_methodWithCallbackAllArgs_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithCallbackAllArgs_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackAllArgs((methodWithCallbackAllArgs)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static long call_methodWithCallbackLongReturn(ClosureObject<methodWithCallbackLongReturn> fnPtr) {
        return call_methodWithCallbackLongReturn_internal(fnPtr.getPointer());
    }

    static private native long call_methodWithCallbackLongReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)call_methodWithCallbackLongReturn((methodWithCallbackLongReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static int call_methodWithCallbackIntReturn(ClosureObject<methodWithCallbackIntReturn> fnPtr) {
        return call_methodWithCallbackIntReturn_internal(fnPtr.getPointer());
    }

    static private native int call_methodWithCallbackIntReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)call_methodWithCallbackIntReturn((methodWithCallbackIntReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static short call_methodWithCallbackShortReturn(ClosureObject<methodWithCallbackShortReturn> fnPtr) {
        return call_methodWithCallbackShortReturn_internal(fnPtr.getPointer());
    }

    static private native short call_methodWithCallbackShortReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jshort)call_methodWithCallbackShortReturn((methodWithCallbackShortReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static char call_methodWithCallbackCharReturn(ClosureObject<methodWithCallbackCharReturn> fnPtr) {
        return call_methodWithCallbackCharReturn_internal(fnPtr.getPointer());
    }

    static private native char call_methodWithCallbackCharReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jchar)call_methodWithCallbackCharReturn((methodWithCallbackCharReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static byte call_methodWithCallbackByteReturn(ClosureObject<methodWithCallbackByteReturn> fnPtr) {
        return call_methodWithCallbackByteReturn_internal(fnPtr.getPointer());
    }

    static private native byte call_methodWithCallbackByteReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jbyte)call_methodWithCallbackByteReturn((methodWithCallbackByteReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static boolean call_methodWithCallbackBooleanReturn(ClosureObject<methodWithCallbackBooleanReturn> fnPtr) {
        return call_methodWithCallbackBooleanReturn_internal(fnPtr.getPointer());
    }

    static private native boolean call_methodWithCallbackBooleanReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jboolean)call_methodWithCallbackBooleanReturn((methodWithCallbackBooleanReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static float call_methodWithCallbackFloatReturn(ClosureObject<methodWithCallbackFloatReturn> fnPtr) {
        return call_methodWithCallbackFloatReturn_internal(fnPtr.getPointer());
    }

    static private native float call_methodWithCallbackFloatReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jfloat)call_methodWithCallbackFloatReturn((methodWithCallbackFloatReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static double call_methodWithCallbackDoubleReturn(ClosureObject<methodWithCallbackDoubleReturn> fnPtr) {
        return call_methodWithCallbackDoubleReturn_internal(fnPtr.getPointer());
    }

    static private native double call_methodWithCallbackDoubleReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jdouble)call_methodWithCallbackDoubleReturn((methodWithCallbackDoubleReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void call_methodWithThrowingCallback(ClosureObject<methodWithThrowingCallback> fnPtr) {
        call_methodWithThrowingCallback_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithThrowingCallback_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithThrowingCallback((methodWithThrowingCallback)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithIntPtrPtrArg(ClosureObject<methodWithIntPtrPtrArg> fnPtr) {
        call_methodWithIntPtrPtrArg_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithIntPtrPtrArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithIntPtrPtrArg((methodWithIntPtrPtrArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static PointerPointer<CSizedIntPointer> call_methodWithIntPtrPtrRet(ClosureObject<methodWithIntPtrPtrRet> fnPtr) {
        return new PointerPointer<>(call_methodWithIntPtrPtrRet_internal(fnPtr.getPointer()), false, (long peer2, boolean owned2) -> new CSizedIntPointer(peer2, owned2, "int")).setBackingCType("int");
    }

    static private native long call_methodWithIntPtrPtrRet_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)call_methodWithIntPtrPtrRet((methodWithIntPtrPtrRet)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestStruct.TestStructPointer returnTestStructPointer() {
        return new TestStruct.TestStructPointer(returnTestStructPointer_internal(), false);
    }

    static private native long returnTestStructPointer_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)returnTestStructPointer();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestStruct returnTestStruct() {
        return new TestStruct(returnTestStruct_internal(), true);
    }

    static private native long returnTestStruct_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	TestStruct* _ret = (TestStruct*)malloc(sizeof(TestStruct));
    	*_ret = returnTestStruct();
    	return (jlong)_ret;
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static long passByValue(TestStruct testStruct) {
        return passByValue_internal(testStruct.getPointer());
    }

    static private native long passByValue_internal(long testStruct);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)passByValue(*(TestStruct*)testStruct);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static long passByPointer(TestStruct.TestStructPointer testStruct) {
        return passByPointer_internal(testStruct.getPointer());
    }

    static private native long passByPointer_internal(long testStruct);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)passByPointer((TestStruct *)testStruct);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestStruct call_methodWithCallbackTestStructReturn(ClosureObject<methodWithCallbackTestStructReturn> fnPtr) {
        return new TestStruct(call_methodWithCallbackTestStructReturn_internal(fnPtr.getPointer()), true);
    }

    static private native long call_methodWithCallbackTestStructReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	TestStruct* _ret = (TestStruct*)malloc(sizeof(TestStruct));
    	*_ret = call_methodWithCallbackTestStructReturn((methodWithCallbackTestStructReturn)fnPtr);
    	return (jlong)_ret;
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestStruct.TestStructPointer call_methodWithCallbackTestStructPointerReturn(ClosureObject<methodWithCallbackTestStructPointerReturn> fnPtr) {
        return new TestStruct.TestStructPointer(call_methodWithCallbackTestStructPointerReturn_internal(fnPtr.getPointer()), false);
    }

    static private native long call_methodWithCallbackTestStructPointerReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)call_methodWithCallbackTestStructPointerReturn((methodWithCallbackTestStructPointerReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void call_methodWithCallbackTestStructArg(ClosureObject<methodWithCallbackTestStructArg> fnPtr) {
        call_methodWithCallbackTestStructArg_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithCallbackTestStructArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackTestStructArg((methodWithCallbackTestStructArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackTestStructPointerArg(ClosureObject<methodWithCallbackTestStructPointerArg> fnPtr) {
        call_methodWithCallbackTestStructPointerArg_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithCallbackTestStructPointerArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackTestStructPointerArg((methodWithCallbackTestStructPointerArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static int passTestEnum(TestEnum enumValue) {
        return passTestEnum_internal(enumValue.getIndex());
    }

    static private native int passTestEnum_internal(int enumValue);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)passTestEnum((TestEnum)enumValue);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestEnum returnTestEnum() {
        return TestEnum.getByIndex((int) returnTestEnum_internal());
    }

    static private native int returnTestEnum_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)returnTestEnum();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestEnum passAndReturnTestEnum(TestEnum enumValue) {
        return TestEnum.getByIndex((int) passAndReturnTestEnum_internal(enumValue.getIndex()));
    }

    static private native int passAndReturnTestEnum_internal(int enumValue);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)passAndReturnTestEnum((TestEnum)enumValue);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static int passTestEnumPointer(TestEnum.TestEnumPointer enumValue) {
        return passTestEnumPointer_internal(enumValue.getPointer());
    }

    static private native int passTestEnumPointer_internal(long enumValue);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)passTestEnumPointer((TestEnum *)enumValue);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestEnum.TestEnumPointer returnTestEnumPointer() {
        return new TestEnum.TestEnumPointer(returnTestEnumPointer_internal(), false);
    }

    static private native long returnTestEnumPointer_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)returnTestEnumPointer();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestEnum passAndReturnTestEnumPointer(TestEnum.TestEnumPointer enumValue) {
        return TestEnum.getByIndex((int) passAndReturnTestEnumPointer_internal(enumValue.getPointer()));
    }

    static private native int passAndReturnTestEnumPointer_internal(long enumValue);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)passAndReturnTestEnumPointer((TestEnum *)enumValue);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestEnum call_methodWithCallbackTestEnumReturn(ClosureObject<methodWithCallbackTestEnumReturn> fnPtr) {
        return TestEnum.getByIndex((int) call_methodWithCallbackTestEnumReturn_internal(fnPtr.getPointer()));
    }

    static private native int call_methodWithCallbackTestEnumReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)call_methodWithCallbackTestEnumReturn((methodWithCallbackTestEnumReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void call_methodWithCallbackTestEnumArg(ClosureObject<methodWithCallbackTestEnumArg> fnPtr) {
        call_methodWithCallbackTestEnumArg_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithCallbackTestEnumArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackTestEnumArg((methodWithCallbackTestEnumArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static TestEnum.TestEnumPointer call_methodWithCallbackTestEnumPointerReturn(ClosureObject<methodWithCallbackTestEnumPointerReturn> fnPtr) {
        return new TestEnum.TestEnumPointer(call_methodWithCallbackTestEnumPointerReturn_internal(fnPtr.getPointer()), false);
    }

    static private native long call_methodWithCallbackTestEnumPointerReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)call_methodWithCallbackTestEnumPointerReturn((methodWithCallbackTestEnumPointerReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void call_methodWithCallbackTestEnumPointerArg(ClosureObject<methodWithCallbackTestEnumPointerArg> fnPtr) {
        call_methodWithCallbackTestEnumPointerArg_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithCallbackTestEnumPointerArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackTestEnumPointerArg((methodWithCallbackTestEnumPointerArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static int passIntPointer(CSizedIntPointer arg0) {
        arg0.assertHasCTypeBacking("int");
        return passIntPointer_internal(arg0.getPointer());
    }

    static private native int passIntPointer_internal(long arg0);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)passIntPointer((int *)arg0);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static CSizedIntPointer returnIntPointer(int arg0) {
        return new CSizedIntPointer(returnIntPointer_internal(arg0), false, "int");
    }

    static private native long returnIntPointer_internal(int arg0);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, int, arg0, 0, return 0);
    	return (jlong)returnIntPointer((int)arg0);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static CSizedIntPointer call_methodWithCallbackIntPointerReturn(ClosureObject<methodWithCallbackIntPointerReturn> fnPtr, int val) {
        return new CSizedIntPointer(call_methodWithCallbackIntPointerReturn_internal(fnPtr.getPointer(), val), false, "int");
    }

    static private native long call_methodWithCallbackIntPointerReturn_internal(long fnPtr, int val);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, int, val, 1, return 0);
    	return (jlong)call_methodWithCallbackIntPointerReturn((methodWithCallbackIntPointerReturn)fnPtr, (int)val);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static int call_methodWithCallbackIntPointerArg(ClosureObject<methodWithCallbackIntPointerArg> fnPtr) {
        return call_methodWithCallbackIntPointerArg_internal(fnPtr.getPointer());
    }

    static private native int call_methodWithCallbackIntPointerArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)call_methodWithCallbackIntPointerArg((methodWithCallbackIntPointerArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static float getFloatPtrFieldValue(SpecialStruct specialStruct) {
        return getFloatPtrFieldValue_internal(specialStruct.getPointer());
    }

    static private native float getFloatPtrFieldValue_internal(long specialStruct);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jfloat)getFloatPtrFieldValue(*(SpecialStruct*)specialStruct);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static int getFixedSizeArrayFieldValue(SpecialStruct specialStruct, int index) {
        return getFixedSizeArrayFieldValue_internal(specialStruct.getPointer(), index);
    }

    static private native int getFixedSizeArrayFieldValue_internal(long specialStruct, int index);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, int, index, 1, return 0);
    	return (jint)getFixedSizeArrayFieldValue(*(SpecialStruct*)specialStruct, (int)index);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static int getIntPtrFieldValue(SpecialStruct specialStruct) {
        return getIntPtrFieldValue_internal(specialStruct.getPointer());
    }

    static private native int getIntPtrFieldValue_internal(long specialStruct);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)getIntPtrFieldValue(*(SpecialStruct*)specialStruct);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void setFloatPtrFieldValue(SpecialStruct specialStruct, float value) {
        setFloatPtrFieldValue_internal(specialStruct.getPointer(), value);
    }

    static private native void setFloatPtrFieldValue_internal(long specialStruct, float value);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	setFloatPtrFieldValue(*(SpecialStruct*)specialStruct, (float)value);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void setFixedSizeArrayFieldValue(SpecialStruct.SpecialStructPointer specialStruct, int index, int value) {
        setFixedSizeArrayFieldValue_internal(specialStruct.getPointer(), index, value);
    }

    static private native void setFixedSizeArrayFieldValue_internal(long specialStruct, int index, int value);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, int, value, 2, return);
    	CHECK_AND_THROW_C_TYPE(env, int, index, 1, return);
    	setFixedSizeArrayFieldValue((SpecialStruct *)specialStruct, (int)index, (int)value);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void setIntPtrFieldValue(SpecialStruct specialStruct, int value) {
        setIntPtrFieldValue_internal(specialStruct.getPointer(), value);
    }

    static private native void setIntPtrFieldValue_internal(long specialStruct, int value);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, int, value, 1, return);
    	setIntPtrFieldValue(*(SpecialStruct*)specialStruct, (int)value);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static TestUnion.TestUnionPointer returnTestUnionPointer() {
        return new TestUnion.TestUnionPointer(returnTestUnionPointer_internal(), false);
    }

    static private native long returnTestUnionPointer_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)returnTestUnionPointer();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestUnion returnTestUnion() {
        return new TestUnion(returnTestUnion_internal(), true);
    }

    static private native long returnTestUnion_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	TestUnion* _ret = (TestUnion*)malloc(sizeof(TestUnion));
    	*_ret = returnTestUnion();
    	return (jlong)_ret;
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static long getUnionUintTypeByValue(TestUnion testUnion) {
        return getUnionUintTypeByValue_internal(testUnion.getPointer());
    }

    static private native long getUnionUintTypeByValue_internal(long testUnion);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getUnionUintTypeByValue(*(TestUnion*)testUnion);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void setUnionUintTypeByPointer(TestUnion.TestUnionPointer testUnion, long value) {
        setUnionUintTypeByPointer_internal(testUnion.getPointer(), value);
    }

    static private native void setUnionUintTypeByPointer_internal(long testUnion, long value);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, uint64_t, value, 1, return);
    	setUnionUintTypeByPointer((TestUnion *)testUnion, (uint64_t)value);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static double getUnionDoubleTypeByValue(TestUnion testUnion) {
        return getUnionDoubleTypeByValue_internal(testUnion.getPointer());
    }

    static private native double getUnionDoubleTypeByValue_internal(long testUnion);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jdouble)getUnionDoubleTypeByValue(*(TestUnion*)testUnion);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void setUnionDoubleTypeByPointer(TestUnion.TestUnionPointer testUnion, double value) {
        setUnionDoubleTypeByPointer_internal(testUnion.getPointer(), value);
    }

    static private native void setUnionDoubleTypeByPointer_internal(long testUnion, double value);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	setUnionDoubleTypeByPointer((TestUnion *)testUnion, (double)value);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static CSizedIntPointer getUnionFixedSizeIntByPointer(TestUnion.TestUnionPointer testUnion) {
        return new CSizedIntPointer(getUnionFixedSizeIntByPointer_internal(testUnion.getPointer()), false, "int");
    }

    static private native long getUnionFixedSizeIntByPointer_internal(long testUnion);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getUnionFixedSizeIntByPointer((TestUnion *)testUnion);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static int getUnionFixedSizeIntByValue(TestUnion testUnion, int index) {
        return getUnionFixedSizeIntByValue_internal(testUnion.getPointer(), index);
    }

    static private native int getUnionFixedSizeIntByValue_internal(long testUnion, int index);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, int, index, 1, return 0);
    	return (jint)getUnionFixedSizeIntByValue(*(TestUnion*)testUnion, (int)index);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void setUnionFixedSizeIntByPointer(TestUnion.TestUnionPointer testUnion, int index, int value) {
        setUnionFixedSizeIntByPointer_internal(testUnion.getPointer(), index, value);
    }

    static private native void setUnionFixedSizeIntByPointer_internal(long testUnion, int index, int value);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, int, value, 2, return);
    	CHECK_AND_THROW_C_TYPE(env, int, index, 1, return);
    	setUnionFixedSizeIntByPointer((TestUnion *)testUnion, (int)index, (int)value);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static TestStruct getUnionStructTypeByValue(TestUnion testUnion) {
        return new TestStruct(getUnionStructTypeByValue_internal(testUnion.getPointer()), true);
    }

    static private native long getUnionStructTypeByValue_internal(long testUnion);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	TestStruct* _ret = (TestStruct*)malloc(sizeof(TestStruct));
    	*_ret = getUnionStructTypeByValue(*(TestUnion*)testUnion);
    	return (jlong)_ret;
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void setUnionStructTypeByPointer(TestUnion.TestUnionPointer testUnion, TestStruct value) {
        setUnionStructTypeByPointer_internal(testUnion.getPointer(), value.getPointer());
    }

    static private native void setUnionStructTypeByPointer_internal(long testUnion, long value);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	setUnionStructTypeByPointer((TestUnion *)testUnion, *(TestStruct*)value);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static TestUnion.TestUnionPointer call_methodWithCallbackTestUnionPointerReturn(ClosureObject<methodWithCallbackTestUnionPointerReturn> fnPtr) {
        return new TestUnion.TestUnionPointer(call_methodWithCallbackTestUnionPointerReturn_internal(fnPtr.getPointer()), false);
    }

    static private native long call_methodWithCallbackTestUnionPointerReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)call_methodWithCallbackTestUnionPointerReturn((methodWithCallbackTestUnionPointerReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void call_methodWithCallbackTestUnionPointerArg(ClosureObject<methodWithCallbackTestUnionPointerArg> fnPtr) {
        call_methodWithCallbackTestUnionPointerArg_internal(fnPtr.getPointer());
    }

    static private native void call_methodWithCallbackTestUnionPointerArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackTestUnionPointerArg((methodWithCallbackTestUnionPointerArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void outOfBoundArg(long par) {
        outOfBoundArg_internal(par);
    }

    static private native void outOfBoundArg_internal(long par);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, uint32_t, par, 0, return);
    	outOfBoundArg((uint32_t)par);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void throwOrdinaryException() {
        throwOrdinaryException_internal();
    }

    static private native void throwOrdinaryException_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	throwOrdinaryException();
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void throwNumberException() {
        throwNumberException_internal();
    }

    static private native void throwNumberException_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	throwNumberException();
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static CSizedIntPointer returnThrownCauseMessage(ClosureObject<methodWithThrowingCallback> fnPtr) {
        return new CSizedIntPointer(returnThrownCauseMessage_internal(fnPtr.getPointer()), false, "const char");
    }

    static private native long returnThrownCauseMessage_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)returnThrownCauseMessage((methodWithThrowingCallback)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static CSizedIntPointer returnString() {
        return new CSizedIntPointer(returnString_internal(), false, "char");
    }

    static private native long returnString_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)returnString();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static boolean validateString(CSizedIntPointer str) {
        str.assertHasCTypeBacking("char");
        return validateString_internal(str.getPointer());
    }

    static private native boolean validateString_internal(long str);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jboolean)validateString((char *)str);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void call_callback_in_thread(ClosureObject<thread_callback> thread_callback) {
        call_callback_in_thread_internal(thread_callback.getPointer());
    }

    static private native void call_callback_in_thread_internal(long thread_callback);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_callback_in_thread((void *(*)(void *))thread_callback);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static ClosureObject<methodWithCallback> getVoidCallback() {
        return CHandler.getClosureObject(getVoidCallback_internal(), methodWithCallback::methodWithCallback_downcall);
    }

    static private native long getVoidCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getVoidCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackLongArg> getLongArgCallback() {
        return CHandler.getClosureObject(getLongArgCallback_internal(), methodWithCallbackLongArg::methodWithCallbackLongArg_downcall);
    }

    static private native long getLongArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getLongArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackIntArg> getIntArgCallback() {
        return CHandler.getClosureObject(getIntArgCallback_internal(), methodWithCallbackIntArg::methodWithCallbackIntArg_downcall);
    }

    static private native long getIntArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getIntArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackShortArg> getShortArgCallback() {
        return CHandler.getClosureObject(getShortArgCallback_internal(), methodWithCallbackShortArg::methodWithCallbackShortArg_downcall);
    }

    static private native long getShortArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getShortArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackByteArg> getByteArgCallback() {
        return CHandler.getClosureObject(getByteArgCallback_internal(), methodWithCallbackByteArg::methodWithCallbackByteArg_downcall);
    }

    static private native long getByteArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getByteArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackCharArg> getCharArgCallback() {
        return CHandler.getClosureObject(getCharArgCallback_internal(), methodWithCallbackCharArg::methodWithCallbackCharArg_downcall);
    }

    static private native long getCharArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getCharArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackBooleanArg> getBooleanArgCallback() {
        return CHandler.getClosureObject(getBooleanArgCallback_internal(), methodWithCallbackBooleanArg::methodWithCallbackBooleanArg_downcall);
    }

    static private native long getBooleanArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getBooleanArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackFloatArg> getFloatArgCallback() {
        return CHandler.getClosureObject(getFloatArgCallback_internal(), methodWithCallbackFloatArg::methodWithCallbackFloatArg_downcall);
    }

    static private native long getFloatArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getFloatArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackDoubleArg> getDoubleArgCallback() {
        return CHandler.getClosureObject(getDoubleArgCallback_internal(), methodWithCallbackDoubleArg::methodWithCallbackDoubleArg_downcall);
    }

    static private native long getDoubleArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getDoubleArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackAllArgs> getAllArgsCallback() {
        return CHandler.getClosureObject(getAllArgsCallback_internal(), methodWithCallbackAllArgs::methodWithCallbackAllArgs_downcall);
    }

    static private native long getAllArgsCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getAllArgsCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackLongReturn> getLongReturnCallback() {
        return CHandler.getClosureObject(getLongReturnCallback_internal(), methodWithCallbackLongReturn::methodWithCallbackLongReturn_downcall);
    }

    static private native long getLongReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getLongReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackIntReturn> getIntReturnCallback() {
        return CHandler.getClosureObject(getIntReturnCallback_internal(), methodWithCallbackIntReturn::methodWithCallbackIntReturn_downcall);
    }

    static private native long getIntReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getIntReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackShortReturn> getShortReturnCallback() {
        return CHandler.getClosureObject(getShortReturnCallback_internal(), methodWithCallbackShortReturn::methodWithCallbackShortReturn_downcall);
    }

    static private native long getShortReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getShortReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackCharReturn> getCharReturnCallback() {
        return CHandler.getClosureObject(getCharReturnCallback_internal(), methodWithCallbackCharReturn::methodWithCallbackCharReturn_downcall);
    }

    static private native long getCharReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getCharReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackByteReturn> getByteReturnCallback() {
        return CHandler.getClosureObject(getByteReturnCallback_internal(), methodWithCallbackByteReturn::methodWithCallbackByteReturn_downcall);
    }

    static private native long getByteReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getByteReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackBooleanReturn> getBooleanReturnCallback() {
        return CHandler.getClosureObject(getBooleanReturnCallback_internal(), methodWithCallbackBooleanReturn::methodWithCallbackBooleanReturn_downcall);
    }

    static private native long getBooleanReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getBooleanReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackFloatReturn> getFloatReturnCallback() {
        return CHandler.getClosureObject(getFloatReturnCallback_internal(), methodWithCallbackFloatReturn::methodWithCallbackFloatReturn_downcall);
    }

    static private native long getFloatReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getFloatReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackDoubleReturn> getDoubleReturnCallback() {
        return CHandler.getClosureObject(getDoubleReturnCallback_internal(), methodWithCallbackDoubleReturn::methodWithCallbackDoubleReturn_downcall);
    }

    static private native long getDoubleReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getDoubleReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithIntPtrPtrArg> getIntPtrPtrArgCallback() {
        return CHandler.getClosureObject(getIntPtrPtrArgCallback_internal(), methodWithIntPtrPtrArg::methodWithIntPtrPtrArg_downcall);
    }

    static private native long getIntPtrPtrArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getIntPtrPtrArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithIntPtrPtrRet> getIntPtrPtrRetCallback() {
        return CHandler.getClosureObject(getIntPtrPtrRetCallback_internal(), methodWithIntPtrPtrRet::methodWithIntPtrPtrRet_downcall);
    }

    static private native long getIntPtrPtrRetCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getIntPtrPtrRetCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestStructReturn> getTestStructReturnCallback() {
        return CHandler.getClosureObject(getTestStructReturnCallback_internal(), methodWithCallbackTestStructReturn::methodWithCallbackTestStructReturn_downcall);
    }

    static private native long getTestStructReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestStructReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestStructPointerReturn> getTestStructPointerReturnCallback() {
        return CHandler.getClosureObject(getTestStructPointerReturnCallback_internal(), methodWithCallbackTestStructPointerReturn::methodWithCallbackTestStructPointerReturn_downcall);
    }

    static private native long getTestStructPointerReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestStructPointerReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestStructArg> getTestStructArgCallback() {
        return CHandler.getClosureObject(getTestStructArgCallback_internal(), methodWithCallbackTestStructArg::methodWithCallbackTestStructArg_downcall);
    }

    static private native long getTestStructArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestStructArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestStructPointerArg> getTestStructPointerArgCallback() {
        return CHandler.getClosureObject(getTestStructPointerArgCallback_internal(), methodWithCallbackTestStructPointerArg::methodWithCallbackTestStructPointerArg_downcall);
    }

    static private native long getTestStructPointerArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestStructPointerArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestEnumReturn> getTestEnumReturnCallback() {
        return CHandler.getClosureObject(getTestEnumReturnCallback_internal(), methodWithCallbackTestEnumReturn::methodWithCallbackTestEnumReturn_downcall);
    }

    static private native long getTestEnumReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestEnumReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestEnumArg> getTestEnumArgCallback() {
        return CHandler.getClosureObject(getTestEnumArgCallback_internal(), methodWithCallbackTestEnumArg::methodWithCallbackTestEnumArg_downcall);
    }

    static private native long getTestEnumArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestEnumArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestEnumPointerReturn> getTestEnumPointerReturnCallback() {
        return CHandler.getClosureObject(getTestEnumPointerReturnCallback_internal(), methodWithCallbackTestEnumPointerReturn::methodWithCallbackTestEnumPointerReturn_downcall);
    }

    static private native long getTestEnumPointerReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestEnumPointerReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestEnumPointerArg> getTestEnumPointerArgCallback() {
        return CHandler.getClosureObject(getTestEnumPointerArgCallback_internal(), methodWithCallbackTestEnumPointerArg::methodWithCallbackTestEnumPointerArg_downcall);
    }

    static private native long getTestEnumPointerArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestEnumPointerArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackIntPointerReturn> getIntPointerReturnCallback() {
        return CHandler.getClosureObject(getIntPointerReturnCallback_internal(), methodWithCallbackIntPointerReturn::methodWithCallbackIntPointerReturn_downcall);
    }

    static private native long getIntPointerReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getIntPointerReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackIntPointerArg> getIntPointerArgCallback() {
        return CHandler.getClosureObject(getIntPointerArgCallback_internal(), methodWithCallbackIntPointerArg::methodWithCallbackIntPointerArg_downcall);
    }

    static private native long getIntPointerArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getIntPointerArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestUnionPointerReturn> getTestUnionPointerReturnCallback() {
        return CHandler.getClosureObject(getTestUnionPointerReturnCallback_internal(), methodWithCallbackTestUnionPointerReturn::methodWithCallbackTestUnionPointerReturn_downcall);
    }

    static private native long getTestUnionPointerReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestUnionPointerReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestUnionPointerArg> getTestUnionPointerArgCallback() {
        return CHandler.getClosureObject(getTestUnionPointerArgCallback_internal(), methodWithCallbackTestUnionPointerArg::methodWithCallbackTestUnionPointerArg_downcall);
    }

    static private native long getTestUnionPointerArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestUnionPointerArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public interface methodWithCallbackBooleanArg extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(0) };

        void methodWithCallbackBooleanArg_call(boolean arg0);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithCallbackBooleanArg_call(parameters[0].asLong() != 0);
        }

        public static methodWithCallbackBooleanArg methodWithCallbackBooleanArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackBooleanArg.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackBooleanArg.__ffi_cache[methodWithCallbackBooleanArg.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface methodWithCallbackTestEnumPointerArg extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(-1) };

        void methodWithCallbackTestEnumPointerArg_call(TestEnum.TestEnumPointer arg0);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithCallbackTestEnumPointerArg_call(new TestEnum.TestEnumPointer(parameters[0].asLong(), false));
        }

        public static methodWithCallbackTestEnumPointerArg methodWithCallbackTestEnumPointerArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestEnumPointerArg.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackTestEnumPointerArg.__ffi_cache[methodWithCallbackTestEnumPointerArg.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface methodWithCallbackDoubleReturn extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(3) };

        double methodWithCallbackDoubleReturn_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackDoubleReturn_call());
        }

        public static methodWithCallbackDoubleReturn methodWithCallbackDoubleReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackDoubleReturn.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackDoubleReturn.__ffi_cache[methodWithCallbackDoubleReturn.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return (double) returnConvert.asDouble();
            };
        }
    }

    public interface methodWithThrowingCallback extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2) };

        void methodWithThrowingCallback_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithThrowingCallback_call();
        }

        public static methodWithThrowingCallback methodWithThrowingCallback_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithThrowingCallback.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithThrowingCallback.__ffi_cache[methodWithThrowingCallback.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface methodWithCallbackIntArg extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(5) };

        void methodWithCallbackIntArg_call(int arg0);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithCallbackIntArg_call((int) parameters[0].asLong());
        }

        public static methodWithCallbackIntArg methodWithCallbackIntArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackIntArg.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackIntArg.__ffi_cache[methodWithCallbackIntArg.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface methodWithCallbackTestStructPointerReturn extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-1) };

        TestStruct.TestStructPointer methodWithCallbackTestStructPointerReturn_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestStructPointerReturn_call());
        }

        public static methodWithCallbackTestStructPointerReturn methodWithCallbackTestStructPointerReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestStructPointerReturn.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackTestStructPointerReturn.__ffi_cache[methodWithCallbackTestStructPointerReturn.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return new TestStruct.TestStructPointer(returnConvert.asLong(), false);
            };
        }
    }

    public interface methodWithCallbackShortReturn extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(6) };

        short methodWithCallbackShortReturn_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackShortReturn_call());
        }

        public static methodWithCallbackShortReturn methodWithCallbackShortReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackShortReturn.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackShortReturn.__ffi_cache[methodWithCallbackShortReturn.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return (short) returnConvert.asLong();
            };
        }
    }

    public interface methodWithCallbackTestEnumPointerReturn extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-1) };

        TestEnum.TestEnumPointer methodWithCallbackTestEnumPointerReturn_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestEnumPointerReturn_call());
        }

        public static methodWithCallbackTestEnumPointerReturn methodWithCallbackTestEnumPointerReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestEnumPointerReturn.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackTestEnumPointerReturn.__ffi_cache[methodWithCallbackTestEnumPointerReturn.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return new TestEnum.TestEnumPointer(returnConvert.asLong(), false);
            };
        }
    }

    public interface methodWithCallbackTestStructArg extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(20) };

        void methodWithCallbackTestStructArg_call(TestStruct arg0);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithCallbackTestStructArg_call(new TestStruct(parameters[0].asLong(), true));
        }

        public static methodWithCallbackTestStructArg methodWithCallbackTestStructArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestStructArg.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackTestStructArg.__ffi_cache[methodWithCallbackTestStructArg.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface methodWithCallbackIntPointerReturn extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-1) };

        CSizedIntPointer methodWithCallbackIntPointerReturn_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            CSizedIntPointer _ret = methodWithCallbackIntPointerReturn_call();
            _ret.assertHasCTypeBacking("int");
            returnType.setValue(_ret);
        }

        public static methodWithCallbackIntPointerReturn methodWithCallbackIntPointerReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackIntPointerReturn.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackIntPointerReturn.__ffi_cache[methodWithCallbackIntPointerReturn.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return new CSizedIntPointer(returnConvert.asLong(), false, "int");
            };
        }
    }

    public interface methodWithCallbackLongArg extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(9) };

        void methodWithCallbackLongArg_call(long arg0);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithCallbackLongArg_call((long) parameters[0].asLong());
        }

        public static methodWithCallbackLongArg methodWithCallbackLongArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackLongArg.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackLongArg.__ffi_cache[methodWithCallbackLongArg.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface methodWithCallbackFloatArg extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(4) };

        void methodWithCallbackFloatArg_call(float arg0);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithCallbackFloatArg_call((float) parameters[0].asFloat());
        }

        public static methodWithCallbackFloatArg methodWithCallbackFloatArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackFloatArg.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackFloatArg.__ffi_cache[methodWithCallbackFloatArg.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface methodWithCallbackDoubleArg extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(3) };

        void methodWithCallbackDoubleArg_call(double arg0);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithCallbackDoubleArg_call((double) parameters[0].asDouble());
        }

        public static methodWithCallbackDoubleArg methodWithCallbackDoubleArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackDoubleArg.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackDoubleArg.__ffi_cache[methodWithCallbackDoubleArg.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface methodWithCallbackIntPointerArg extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(5), FFITypes.getCTypeInfo(-1) };

        int methodWithCallbackIntPointerArg_call(CSizedIntPointer arg0);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackIntPointerArg_call(new CSizedIntPointer(parameters[0].asLong(), false, "int")));
        }

        public static methodWithCallbackIntPointerArg methodWithCallbackIntPointerArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackIntPointerArg.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackIntPointerArg.__ffi_cache[methodWithCallbackIntPointerArg.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return (int) returnConvert.asLong();
            };
        }
    }

    public interface methodWithCallbackTestEnumArg extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(5) };

        void methodWithCallbackTestEnumArg_call(TestEnum arg0);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithCallbackTestEnumArg_call(TestEnum.getByIndex((int) parameters[0].asLong()));
        }

        public static methodWithCallbackTestEnumArg methodWithCallbackTestEnumArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestEnumArg.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackTestEnumArg.__ffi_cache[methodWithCallbackTestEnumArg.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface methodWithCallbackTestStructPointerArg extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(-1) };

        void methodWithCallbackTestStructPointerArg_call(TestStruct.TestStructPointer arg0);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithCallbackTestStructPointerArg_call(new TestStruct.TestStructPointer(parameters[0].asLong(), false));
        }

        public static methodWithCallbackTestStructPointerArg methodWithCallbackTestStructPointerArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestStructPointerArg.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackTestStructPointerArg.__ffi_cache[methodWithCallbackTestStructPointerArg.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface methodWithCallbackTestUnionPointerReturn extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-1) };

        TestUnion.TestUnionPointer methodWithCallbackTestUnionPointerReturn_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestUnionPointerReturn_call());
        }

        public static methodWithCallbackTestUnionPointerReturn methodWithCallbackTestUnionPointerReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestUnionPointerReturn.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackTestUnionPointerReturn.__ffi_cache[methodWithCallbackTestUnionPointerReturn.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return new TestUnion.TestUnionPointer(returnConvert.asLong(), false);
            };
        }
    }

    public interface methodWithCallbackByteReturn extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(1) };

        byte methodWithCallbackByteReturn_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackByteReturn_call());
        }

        public static methodWithCallbackByteReturn methodWithCallbackByteReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackByteReturn.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackByteReturn.__ffi_cache[methodWithCallbackByteReturn.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return (byte) returnConvert.asLong();
            };
        }
    }

    public interface methodWithCallbackCharReturn extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(7) };

        char methodWithCallbackCharReturn_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackCharReturn_call());
        }

        public static methodWithCallbackCharReturn methodWithCallbackCharReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackCharReturn.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackCharReturn.__ffi_cache[methodWithCallbackCharReturn.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return (char) returnConvert.asLong();
            };
        }
    }

    public interface methodWithCallbackTestEnumReturn extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(5) };

        TestEnum methodWithCallbackTestEnumReturn_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestEnumReturn_call());
        }

        public static methodWithCallbackTestEnumReturn methodWithCallbackTestEnumReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestEnumReturn.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackTestEnumReturn.__ffi_cache[methodWithCallbackTestEnumReturn.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return TestEnum.getByIndex((int) returnConvert.asLong());
            };
        }
    }

    public interface methodWithCallbackAllArgs extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(9), FFITypes.getCTypeInfo(5), FFITypes.getCTypeInfo(6), FFITypes.getCTypeInfo(1), FFITypes.getCTypeInfo(7), FFITypes.getCTypeInfo(0), FFITypes.getCTypeInfo(4), FFITypes.getCTypeInfo(3) };

        void methodWithCallbackAllArgs_call(long arg0, int arg1, short arg2, byte arg3, char arg4, boolean arg5, float arg6, double arg7);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithCallbackAllArgs_call((long) parameters[0].asLong(), (int) parameters[1].asLong(), (short) parameters[2].asLong(), (byte) parameters[3].asLong(), (char) parameters[4].asLong(), parameters[5].asLong() != 0, (float) parameters[6].asFloat(), (double) parameters[7].asDouble());
        }

        public static methodWithCallbackAllArgs methodWithCallbackAllArgs_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackAllArgs.__ffi_cache);
            return (arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                    useEncoder.setValue(1, arg1);
                    useEncoder.setValue(2, arg2);
                    useEncoder.setValue(3, arg3);
                    useEncoder.setValue(4, arg4);
                    useEncoder.setValue(5, arg5);
                    useEncoder.setValue(6, arg6);
                    useEncoder.setValue(7, arg7);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackAllArgs.__ffi_cache[methodWithCallbackAllArgs.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface methodWithCallback extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2) };

        void methodWithCallback_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithCallback_call();
        }

        public static methodWithCallback methodWithCallback_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallback.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallback.__ffi_cache[methodWithCallback.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface methodWithIntPtrPtrRet extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-1) };

        PointerPointer<CSizedIntPointer> methodWithIntPtrPtrRet_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            PointerPointer<CSizedIntPointer> _ret = methodWithIntPtrPtrRet_call();
            _ret.assertCTypeBacking("int");
            returnType.setValue(_ret);
        }

        public static methodWithIntPtrPtrRet methodWithIntPtrPtrRet_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithIntPtrPtrRet.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithIntPtrPtrRet.__ffi_cache[methodWithIntPtrPtrRet.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return new PointerPointer<>(returnConvert.asLong(), false, (long peer2, boolean owned2) -> new CSizedIntPointer(peer2, owned2, "int")).setBackingCType("int");
            };
        }
    }

    public interface methodWithCallbackTestUnionPointerArg extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(-1) };

        void methodWithCallbackTestUnionPointerArg_call(TestUnion.TestUnionPointer arg0);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithCallbackTestUnionPointerArg_call(new TestUnion.TestUnionPointer(parameters[0].asLong(), false));
        }

        public static methodWithCallbackTestUnionPointerArg methodWithCallbackTestUnionPointerArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestUnionPointerArg.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackTestUnionPointerArg.__ffi_cache[methodWithCallbackTestUnionPointerArg.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface methodWithCallbackShortArg extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(6) };

        void methodWithCallbackShortArg_call(short arg0);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithCallbackShortArg_call((short) parameters[0].asLong());
        }

        public static methodWithCallbackShortArg methodWithCallbackShortArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackShortArg.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackShortArg.__ffi_cache[methodWithCallbackShortArg.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface methodWithCallbackByteArg extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(1) };

        void methodWithCallbackByteArg_call(byte arg0);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithCallbackByteArg_call((byte) parameters[0].asLong());
        }

        public static methodWithCallbackByteArg methodWithCallbackByteArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackByteArg.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackByteArg.__ffi_cache[methodWithCallbackByteArg.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface methodWithCallbackBooleanReturn extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(0) };

        boolean methodWithCallbackBooleanReturn_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackBooleanReturn_call());
        }

        public static methodWithCallbackBooleanReturn methodWithCallbackBooleanReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackBooleanReturn.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackBooleanReturn.__ffi_cache[methodWithCallbackBooleanReturn.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return returnConvert.asLong() != 0;
            };
        }
    }

    public interface methodWithCallbackIntReturn extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(5) };

        int methodWithCallbackIntReturn_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackIntReturn_call());
        }

        public static methodWithCallbackIntReturn methodWithCallbackIntReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackIntReturn.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackIntReturn.__ffi_cache[methodWithCallbackIntReturn.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return (int) returnConvert.asLong();
            };
        }
    }

    public interface methodWithCallbackLongReturn extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(9) };

        long methodWithCallbackLongReturn_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackLongReturn_call());
        }

        public static methodWithCallbackLongReturn methodWithCallbackLongReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackLongReturn.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackLongReturn.__ffi_cache[methodWithCallbackLongReturn.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return (long) returnConvert.asLong();
            };
        }
    }

    public interface methodWithCallbackCharArg extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(7) };

        void methodWithCallbackCharArg_call(char arg0);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithCallbackCharArg_call((char) parameters[0].asLong());
        }

        public static methodWithCallbackCharArg methodWithCallbackCharArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackCharArg.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackCharArg.__ffi_cache[methodWithCallbackCharArg.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface methodWithIntPtrPtrArg extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(-1) };

        void methodWithIntPtrPtrArg_call(PointerPointer<CSizedIntPointer> arg0);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            methodWithIntPtrPtrArg_call(new PointerPointer<>(parameters[0].asLong(), false, (long peer2, boolean owned2) -> new CSizedIntPointer(peer2, owned2, "int")).setBackingCType("int"));
        }

        public static methodWithIntPtrPtrArg methodWithIntPtrPtrArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithIntPtrPtrArg.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithIntPtrPtrArg.__ffi_cache[methodWithIntPtrPtrArg.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return;
            };
        }
    }

    public interface thread_callback extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(-1), FFITypes.getCTypeInfo(-1) };

        VoidPointer thread_callback_call(VoidPointer arg0);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(thread_callback_call(new VoidPointer(parameters[0].asLong(), false)));
        }

        public static thread_callback thread_callback_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, thread_callback.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(thread_callback.__ffi_cache[thread_callback.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return new VoidPointer(returnConvert.asLong(), false);
            };
        }
    }

    public interface methodWithCallbackTestStructReturn extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(20) };

        TestStruct methodWithCallbackTestStructReturn_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestStructReturn_call());
        }

        public static methodWithCallbackTestStructReturn methodWithCallbackTestStructReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestStructReturn.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackTestStructReturn.__ffi_cache[methodWithCallbackTestStructReturn.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return new TestStruct(returnConvert.asLong(), true);
            };
        }
    }

    public interface methodWithCallbackFloatReturn extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(4) };

        float methodWithCallbackFloatReturn_call();

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackFloatReturn_call());
        }

        public static methodWithCallbackFloatReturn methodWithCallbackFloatReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackFloatReturn.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackFloatReturn.__ffi_cache[methodWithCallbackFloatReturn.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return (float) returnConvert.asFloat();
            };
        }
    }
}
