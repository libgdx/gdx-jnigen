package com.badlogic.jnigen.generated;

import com.badlogic.gdx.jnigen.runtime.c.CXXException;
import com.badlogic.jnigen.generated.structs.GlobalArg;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.BytePointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.UBytePointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SBytePointer;
import com.badlogic.jnigen.generated.enums.SpecialEnum;
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
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SIntPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.FloatPointer;
import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.jnigen.generated.structs.SpecialStruct;
import com.badlogic.jnigen.generated.structs.TestUnion;
import com.badlogic.gdx.jnigen.runtime.closure.Closure;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackBooleanArg_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackTestEnumPointerArg_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackDoubleReturn_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithThrowingCallback_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackIntArg_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackTestStructPointerReturn_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackShortReturn_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackTestEnumPointerReturn_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackTestStructArg_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackIntPointerReturn_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackLongArg_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackFloatArg_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackDoubleArg_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackIntPointerArg_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackTestEnumArg_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackCallThrowingCallback_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackTestStructPointerArg_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackTestUnionPointerReturn_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackByteReturn_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackCharReturn_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackTestEnumReturn_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackAllArgs_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallback_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithIntPtrPtrRet_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackTestUnionPointerArg_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackShortArg_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackByteArg_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackBooleanReturn_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackIntReturn_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackLongReturn_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackCharArg_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithIntPtrPtrArg_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.thread_callback_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackTestStructReturn_Internal;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithCallbackFloatReturn_Internal;

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
    private static native void init(Class<?> illegalArgumentException, Class<?> cxxException);/*
    	illegalArgumentExceptionClass = (jclass)env->NewGlobalRef(illegalArgumentException);
    	cxxExceptionClass = (jclass)env->NewGlobalRef(cxxException);
    */

    public static GlobalArg getGlobalArgState() {
        return new GlobalArg(getGlobalArgState_internal(0), true);
    }

    public static void getGlobalArgState(GlobalArg _retPar) {
        getGlobalArgState_internal(_retPar.getPointer());
    }

    public static native long getGlobalArgState_internal(long _retPar);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	GlobalArg* _ret = (GlobalArg*) (_retPar == 0 ? malloc(sizeof(GlobalArg)) : (void*)_retPar);
    	*_ret = getGlobalArgState();
    	return (jlong)_ret;
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    /**
     * This method does great stuff, trust me
     */
    public static void commentedMethod() {
        commentedMethod_internal();
    }

    public static native void commentedMethod_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	commentedMethod();
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void randomFunc(byte arg0, char arg1, byte arg2, BytePointer arg3, UBytePointer arg4, SBytePointer arg5) {
        randomFunc_internal(arg0, arg1, arg2, arg3.getPointer(), arg4.getPointer(), arg5.getPointer());
    }

    public static native void randomFunc_internal(byte arg0, char arg1, byte arg2, long arg3, long arg4, long arg5);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, signed char, arg2, 2, return);
    	CHECK_AND_THROW_C_TYPE(env, unsigned char, arg1, 1, return);
    	CHECK_AND_THROW_C_TYPE(env, char, arg0, 0, return);
    	randomFunc((char)arg0, (unsigned char)arg1, (signed char)arg2, (char *)arg3, (unsigned char *)arg4, (signed char *)arg5);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void ensureParsed(SpecialEnum arg0, AnonymousStructNoField arg1, AnonymousStructField arg2, AnonymousStructFieldArray arg3, AnonymousClosure arg4, AnonymousStructNoFieldEnd arg5, AnonymousStructNoFieldConsecutive arg6, AnonymousStructNoFieldNested arg7, forwardDeclStruct.forwardDeclStructPointer arg8) {
        ensureParsed_internal(arg0.getIndex(), arg1.getPointer(), arg2.getPointer(), arg3.getPointer(), arg4.getPointer(), arg5.getPointer(), arg6.getPointer(), arg7.getPointer(), arg8.getPointer());
    }

    public static native void ensureParsed_internal(int arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	ensureParsed((SpecialEnum)arg0, *(AnonymousStructNoField*)arg1, *(AnonymousStructField*)arg2, *(AnonymousStructFieldArray*)arg3, *(struct AnonymousClosure*)arg4, *(AnonymousStructNoFieldEnd*)arg5, *(AnonymousStructNoFieldConsecutive*)arg6, *(AnonymousStructNoFieldNested*)arg7, (struct forwardDeclStruct *)arg8);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void weirdPointer(VoidPointer _file) {
        weirdPointer_internal(_file.getPointer());
    }

    public static native void weirdPointer_internal(long _file);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	weirdPointer((FILE *)_file);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void constArrayParameter(TestStruct.TestStructPointer structs) {
        constArrayParameter_internal(structs.getPointer());
    }

    public static native void constArrayParameter_internal(long structs);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	constArrayParameter((const TestStruct*)structs);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static PointerPointer<VoidPointer> voidPointerPointer(PointerPointer<VoidPointer> test) {
        return new PointerPointer<>(voidPointerPointer_internal(test.getPointer()), false, VoidPointer::new);
    }

    public static void voidPointerPointer(PointerPointer<VoidPointer> test, PointerPointer<VoidPointer> _retPar) {
        _retPar.setPointer(voidPointerPointer_internal(test.getPointer()));
        _retPar.setPointerSupplier(VoidPointer::new);
    }

    public static native long voidPointerPointer_internal(long test);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)voidPointerPointer((void **)test);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static PointerPointer<TestEnum.TestEnumPointer> enumPointerPointer(PointerPointer<TestEnum.TestEnumPointer> test) {
        return new PointerPointer<>(enumPointerPointer_internal(test.getPointer()), false, TestEnum.TestEnumPointer::new);
    }

    public static void enumPointerPointer(PointerPointer<TestEnum.TestEnumPointer> test, PointerPointer<TestEnum.TestEnumPointer> _retPar) {
        _retPar.setPointer(enumPointerPointer_internal(test.getPointer()));
        _retPar.setPointerSupplier(TestEnum.TestEnumPointer::new);
    }

    public static native long enumPointerPointer_internal(long test);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)enumPointerPointer((TestEnum **)test);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static PointerPointer<TestStruct.TestStructPointer> structPointerPointer(PointerPointer<TestStruct.TestStructPointer> test) {
        return new PointerPointer<>(structPointerPointer_internal(test.getPointer()), false, TestStruct.TestStructPointer::new);
    }

    public static void structPointerPointer(PointerPointer<TestStruct.TestStructPointer> test, PointerPointer<TestStruct.TestStructPointer> _retPar) {
        _retPar.setPointer(structPointerPointer_internal(test.getPointer()));
        _retPar.setPointerSupplier(TestStruct.TestStructPointer::new);
    }

    public static native long structPointerPointer_internal(long test);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)structPointerPointer((TestStruct **)test);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static PointerPointer<SIntPointer> intPointerPointer(PointerPointer<SIntPointer> test) {
        return new PointerPointer<>(intPointerPointer_internal(test.getPointer()), false, SIntPointer::new);
    }

    public static void intPointerPointer(PointerPointer<SIntPointer> test, PointerPointer<SIntPointer> _retPar) {
        _retPar.setPointer(intPointerPointer_internal(test.getPointer()));
        _retPar.setPointerSupplier(SIntPointer::new);
    }

    public static native long intPointerPointer_internal(long test);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)intPointerPointer((int **)test);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static PointerPointer<FloatPointer> floatPointerPointer(PointerPointer<FloatPointer> test) {
        return new PointerPointer<>(floatPointerPointer_internal(test.getPointer()), false, FloatPointer::new);
    }

    public static void floatPointerPointer(PointerPointer<FloatPointer> test, PointerPointer<FloatPointer> _retPar) {
        _retPar.setPointer(floatPointerPointer_internal(test.getPointer()));
        _retPar.setPointerSupplier(FloatPointer::new);
    }

    public static native long floatPointerPointer_internal(long test);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)floatPointerPointer((float **)test);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static PointerPointer<PointerPointer<PointerPointer<PointerPointer<VoidPointer>>>> pointerPointerManyyy(PointerPointer<PointerPointer<PointerPointer<PointerPointer<VoidPointer>>>> test) {
        return new PointerPointer<>(pointerPointerManyyy_internal(test.getPointer()), false, (long peer5, boolean owned5) -> new PointerPointer<>(peer5, owned5, (long peer4, boolean owned4) -> new PointerPointer<>(peer4, owned4, (long peer3, boolean owned3) -> new PointerPointer<>(peer3, owned3, VoidPointer::new))));
    }

    public static void pointerPointerManyyy(PointerPointer<PointerPointer<PointerPointer<PointerPointer<VoidPointer>>>> test, PointerPointer<PointerPointer<PointerPointer<PointerPointer<VoidPointer>>>> _retPar) {
        _retPar.setPointer(pointerPointerManyyy_internal(test.getPointer()));
        _retPar.setPointerSupplier((long peer5, boolean owned5) -> new PointerPointer<>(peer5, owned5, (long peer4, boolean owned4) -> new PointerPointer<>(peer4, owned4, (long peer3, boolean owned3) -> new PointerPointer<>(peer3, owned3, VoidPointer::new))));
    }

    public static native long pointerPointerManyyy_internal(long test);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)pointerPointerManyyy((void *****)test);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void call_methodWithCallback(ClosureObject<methodWithCallback> fnPtr) {
        call_methodWithCallback_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithCallback_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallback((methodWithCallback)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackLongArg(ClosureObject<methodWithCallbackLongArg> fnPtr) {
        call_methodWithCallbackLongArg_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithCallbackLongArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackLongArg((methodWithCallbackLongArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackIntArg(ClosureObject<methodWithCallbackIntArg> fnPtr) {
        call_methodWithCallbackIntArg_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithCallbackIntArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackIntArg((methodWithCallbackIntArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackShortArg(ClosureObject<methodWithCallbackShortArg> fnPtr) {
        call_methodWithCallbackShortArg_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithCallbackShortArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackShortArg((methodWithCallbackShortArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackByteArg(ClosureObject<methodWithCallbackByteArg> fnPtr) {
        call_methodWithCallbackByteArg_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithCallbackByteArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackByteArg((methodWithCallbackByteArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackCharArg(ClosureObject<methodWithCallbackCharArg> fnPtr) {
        call_methodWithCallbackCharArg_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithCallbackCharArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackCharArg((methodWithCallbackCharArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackBooleanArg(ClosureObject<methodWithCallbackBooleanArg> fnPtr) {
        call_methodWithCallbackBooleanArg_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithCallbackBooleanArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackBooleanArg((methodWithCallbackBooleanArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackFloatArg(ClosureObject<methodWithCallbackFloatArg> fnPtr) {
        call_methodWithCallbackFloatArg_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithCallbackFloatArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackFloatArg((methodWithCallbackFloatArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackDoubleArg(ClosureObject<methodWithCallbackDoubleArg> fnPtr) {
        call_methodWithCallbackDoubleArg_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithCallbackDoubleArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackDoubleArg((methodWithCallbackDoubleArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackAllArgs(ClosureObject<methodWithCallbackAllArgs> fnPtr) {
        call_methodWithCallbackAllArgs_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithCallbackAllArgs_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackAllArgs((methodWithCallbackAllArgs)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static long call_methodWithCallbackLongReturn(ClosureObject<methodWithCallbackLongReturn> fnPtr) {
        return call_methodWithCallbackLongReturn_internal(fnPtr.getPointer());
    }

    public static native long call_methodWithCallbackLongReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)call_methodWithCallbackLongReturn((methodWithCallbackLongReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static int call_methodWithCallbackIntReturn(ClosureObject<methodWithCallbackIntReturn> fnPtr) {
        return call_methodWithCallbackIntReturn_internal(fnPtr.getPointer());
    }

    public static native int call_methodWithCallbackIntReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)call_methodWithCallbackIntReturn((methodWithCallbackIntReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static short call_methodWithCallbackShortReturn(ClosureObject<methodWithCallbackShortReturn> fnPtr) {
        return call_methodWithCallbackShortReturn_internal(fnPtr.getPointer());
    }

    public static native short call_methodWithCallbackShortReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jshort)call_methodWithCallbackShortReturn((methodWithCallbackShortReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static char call_methodWithCallbackCharReturn(ClosureObject<methodWithCallbackCharReturn> fnPtr) {
        return call_methodWithCallbackCharReturn_internal(fnPtr.getPointer());
    }

    public static native char call_methodWithCallbackCharReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jchar)call_methodWithCallbackCharReturn((methodWithCallbackCharReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static byte call_methodWithCallbackByteReturn(ClosureObject<methodWithCallbackByteReturn> fnPtr) {
        return call_methodWithCallbackByteReturn_internal(fnPtr.getPointer());
    }

    public static native byte call_methodWithCallbackByteReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jbyte)call_methodWithCallbackByteReturn((methodWithCallbackByteReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static boolean call_methodWithCallbackBooleanReturn(ClosureObject<methodWithCallbackBooleanReturn> fnPtr) {
        return call_methodWithCallbackBooleanReturn_internal(fnPtr.getPointer());
    }

    public static native boolean call_methodWithCallbackBooleanReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jboolean)call_methodWithCallbackBooleanReturn((methodWithCallbackBooleanReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static float call_methodWithCallbackFloatReturn(ClosureObject<methodWithCallbackFloatReturn> fnPtr) {
        return call_methodWithCallbackFloatReturn_internal(fnPtr.getPointer());
    }

    public static native float call_methodWithCallbackFloatReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jfloat)call_methodWithCallbackFloatReturn((methodWithCallbackFloatReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static double call_methodWithCallbackDoubleReturn(ClosureObject<methodWithCallbackDoubleReturn> fnPtr) {
        return call_methodWithCallbackDoubleReturn_internal(fnPtr.getPointer());
    }

    public static native double call_methodWithCallbackDoubleReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jdouble)call_methodWithCallbackDoubleReturn((methodWithCallbackDoubleReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void call_methodWithThrowingCallback(ClosureObject<methodWithThrowingCallback> fnPtr) {
        call_methodWithThrowingCallback_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithThrowingCallback_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithThrowingCallback((methodWithThrowingCallback)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithIntPtrPtrArg(ClosureObject<methodWithIntPtrPtrArg> fnPtr) {
        call_methodWithIntPtrPtrArg_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithIntPtrPtrArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithIntPtrPtrArg((methodWithIntPtrPtrArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static PointerPointer<SIntPointer> call_methodWithIntPtrPtrRet(ClosureObject<methodWithIntPtrPtrRet> fnPtr) {
        return new PointerPointer<>(call_methodWithIntPtrPtrRet_internal(fnPtr.getPointer()), false, SIntPointer::new);
    }

    public static void call_methodWithIntPtrPtrRet(ClosureObject<methodWithIntPtrPtrRet> fnPtr, PointerPointer<SIntPointer> _retPar) {
        _retPar.setPointer(call_methodWithIntPtrPtrRet_internal(fnPtr.getPointer()));
        _retPar.setPointerSupplier(SIntPointer::new);
    }

    public static native long call_methodWithIntPtrPtrRet_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)call_methodWithIntPtrPtrRet((methodWithIntPtrPtrRet)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestStruct.TestStructPointer returnTestStructPointer() {
        return new TestStruct.TestStructPointer(returnTestStructPointer_internal(), false);
    }

    public static void returnTestStructPointer(TestStruct.TestStructPointer _retPar) {
        _retPar.setPointer(returnTestStructPointer_internal());
    }

    public static native long returnTestStructPointer_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)returnTestStructPointer();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestStruct returnTestStruct() {
        return new TestStruct(returnTestStruct_internal(0), true);
    }

    public static void returnTestStruct(TestStruct _retPar) {
        returnTestStruct_internal(_retPar.getPointer());
    }

    public static native long returnTestStruct_internal(long _retPar);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	TestStruct* _ret = (TestStruct*) (_retPar == 0 ? malloc(sizeof(TestStruct)) : (void*)_retPar);
    	*_ret = returnTestStruct();
    	return (jlong)_ret;
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static long passByValue(TestStruct testStruct) {
        return passByValue_internal(testStruct.getPointer());
    }

    public static native long passByValue_internal(long testStruct);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)passByValue(*(TestStruct*)testStruct);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static long passByPointer(TestStruct.TestStructPointer testStruct) {
        return passByPointer_internal(testStruct.getPointer());
    }

    public static native long passByPointer_internal(long testStruct);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)passByPointer((TestStruct *)testStruct);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestStruct call_methodWithCallbackTestStructReturn(ClosureObject<methodWithCallbackTestStructReturn> fnPtr) {
        return new TestStruct(call_methodWithCallbackTestStructReturn_internal(fnPtr.getPointer(), 0), true);
    }

    public static void call_methodWithCallbackTestStructReturn(ClosureObject<methodWithCallbackTestStructReturn> fnPtr, TestStruct _retPar) {
        call_methodWithCallbackTestStructReturn_internal(fnPtr.getPointer(), _retPar.getPointer());
    }

    public static native long call_methodWithCallbackTestStructReturn_internal(long fnPtr, long _retPar);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	TestStruct* _ret = (TestStruct*) (_retPar == 0 ? malloc(sizeof(TestStruct)) : (void*)_retPar);
    	*_ret = call_methodWithCallbackTestStructReturn((methodWithCallbackTestStructReturn)fnPtr);
    	return (jlong)_ret;
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestStruct.TestStructPointer call_methodWithCallbackTestStructPointerReturn(ClosureObject<methodWithCallbackTestStructPointerReturn> fnPtr) {
        return new TestStruct.TestStructPointer(call_methodWithCallbackTestStructPointerReturn_internal(fnPtr.getPointer()), false);
    }

    public static void call_methodWithCallbackTestStructPointerReturn(ClosureObject<methodWithCallbackTestStructPointerReturn> fnPtr, TestStruct.TestStructPointer _retPar) {
        _retPar.setPointer(call_methodWithCallbackTestStructPointerReturn_internal(fnPtr.getPointer()));
    }

    public static native long call_methodWithCallbackTestStructPointerReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)call_methodWithCallbackTestStructPointerReturn((methodWithCallbackTestStructPointerReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void call_methodWithCallbackTestStructArg(ClosureObject<methodWithCallbackTestStructArg> fnPtr) {
        call_methodWithCallbackTestStructArg_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithCallbackTestStructArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackTestStructArg((methodWithCallbackTestStructArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void call_methodWithCallbackTestStructPointerArg(ClosureObject<methodWithCallbackTestStructPointerArg> fnPtr) {
        call_methodWithCallbackTestStructPointerArg_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithCallbackTestStructPointerArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackTestStructPointerArg((methodWithCallbackTestStructPointerArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static int passTestEnum(TestEnum enumValue) {
        return passTestEnum_internal(enumValue.getIndex());
    }

    public static native int passTestEnum_internal(int enumValue);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)passTestEnum((TestEnum)enumValue);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestEnum returnTestEnum() {
        return TestEnum.getByIndex((int) returnTestEnum_internal());
    }

    public static native int returnTestEnum_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)returnTestEnum();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    /**
     * Combined attempt
     * @see TestData#returnTestEnum
     */
    public static TestEnum passAndReturnTestEnum(TestEnum enumValue) {
        return TestEnum.getByIndex((int) passAndReturnTestEnum_internal(enumValue.getIndex()));
    }

    public static native int passAndReturnTestEnum_internal(int enumValue);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)passAndReturnTestEnum((TestEnum)enumValue);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static int passTestEnumPointer(TestEnum.TestEnumPointer enumValue) {
        return passTestEnumPointer_internal(enumValue.getPointer());
    }

    public static native int passTestEnumPointer_internal(long enumValue);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)passTestEnumPointer((TestEnum *)enumValue);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestEnum.TestEnumPointer returnTestEnumPointer() {
        return new TestEnum.TestEnumPointer(returnTestEnumPointer_internal(), false);
    }

    public static void returnTestEnumPointer(TestEnum.TestEnumPointer _retPar) {
        _retPar.setPointer(returnTestEnumPointer_internal());
    }

    public static native long returnTestEnumPointer_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)returnTestEnumPointer();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestEnum passAndReturnTestEnumPointer(TestEnum.TestEnumPointer enumValue) {
        return TestEnum.getByIndex((int) passAndReturnTestEnumPointer_internal(enumValue.getPointer()));
    }

    public static native int passAndReturnTestEnumPointer_internal(long enumValue);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)passAndReturnTestEnumPointer((TestEnum *)enumValue);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestEnum call_methodWithCallbackTestEnumReturn(ClosureObject<methodWithCallbackTestEnumReturn> fnPtr) {
        return TestEnum.getByIndex((int) call_methodWithCallbackTestEnumReturn_internal(fnPtr.getPointer()));
    }

    public static native int call_methodWithCallbackTestEnumReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)call_methodWithCallbackTestEnumReturn((methodWithCallbackTestEnumReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void call_methodWithCallbackTestEnumArg(ClosureObject<methodWithCallbackTestEnumArg> fnPtr) {
        call_methodWithCallbackTestEnumArg_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithCallbackTestEnumArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackTestEnumArg((methodWithCallbackTestEnumArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static TestEnum.TestEnumPointer call_methodWithCallbackTestEnumPointerReturn(ClosureObject<methodWithCallbackTestEnumPointerReturn> fnPtr) {
        return new TestEnum.TestEnumPointer(call_methodWithCallbackTestEnumPointerReturn_internal(fnPtr.getPointer()), false);
    }

    public static void call_methodWithCallbackTestEnumPointerReturn(ClosureObject<methodWithCallbackTestEnumPointerReturn> fnPtr, TestEnum.TestEnumPointer _retPar) {
        _retPar.setPointer(call_methodWithCallbackTestEnumPointerReturn_internal(fnPtr.getPointer()));
    }

    public static native long call_methodWithCallbackTestEnumPointerReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)call_methodWithCallbackTestEnumPointerReturn((methodWithCallbackTestEnumPointerReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void call_methodWithCallbackTestEnumPointerArg(ClosureObject<methodWithCallbackTestEnumPointerArg> fnPtr) {
        call_methodWithCallbackTestEnumPointerArg_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithCallbackTestEnumPointerArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackTestEnumPointerArg((methodWithCallbackTestEnumPointerArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static int passIntPointer(SIntPointer arg0) {
        return passIntPointer_internal(arg0.getPointer());
    }

    public static native int passIntPointer_internal(long arg0);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)passIntPointer((int *)arg0);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static SIntPointer returnIntPointer(int arg0) {
        return new SIntPointer(returnIntPointer_internal(arg0), false);
    }

    public static void returnIntPointer(int arg0, SIntPointer _retPar) {
        _retPar.setPointer(returnIntPointer_internal(arg0));
    }

    public static native long returnIntPointer_internal(int arg0);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, int, arg0, 0, return 0);
    	return (jlong)returnIntPointer((int)arg0);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static SIntPointer call_methodWithCallbackIntPointerReturn(ClosureObject<methodWithCallbackIntPointerReturn> fnPtr, int val) {
        return new SIntPointer(call_methodWithCallbackIntPointerReturn_internal(fnPtr.getPointer(), val), false);
    }

    public static void call_methodWithCallbackIntPointerReturn(ClosureObject<methodWithCallbackIntPointerReturn> fnPtr, int val, SIntPointer _retPar) {
        _retPar.setPointer(call_methodWithCallbackIntPointerReturn_internal(fnPtr.getPointer(), val));
    }

    public static native long call_methodWithCallbackIntPointerReturn_internal(long fnPtr, int val);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, int, val, 1, return 0);
    	return (jlong)call_methodWithCallbackIntPointerReturn((methodWithCallbackIntPointerReturn)fnPtr, (int)val);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static int call_methodWithCallbackIntPointerArg(ClosureObject<methodWithCallbackIntPointerArg> fnPtr) {
        return call_methodWithCallbackIntPointerArg_internal(fnPtr.getPointer());
    }

    public static native int call_methodWithCallbackIntPointerArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)call_methodWithCallbackIntPointerArg((methodWithCallbackIntPointerArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static float getFloatPtrFieldValue(SpecialStruct specialStruct) {
        return getFloatPtrFieldValue_internal(specialStruct.getPointer());
    }

    public static native float getFloatPtrFieldValue_internal(long specialStruct);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jfloat)getFloatPtrFieldValue(*(SpecialStruct*)specialStruct);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static int getFixedSizeArrayFieldValue(SpecialStruct specialStruct, int index) {
        return getFixedSizeArrayFieldValue_internal(specialStruct.getPointer(), index);
    }

    public static native int getFixedSizeArrayFieldValue_internal(long specialStruct, int index);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, int, index, 1, return 0);
    	return (jint)getFixedSizeArrayFieldValue(*(SpecialStruct*)specialStruct, (int)index);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static int getIntPtrFieldValue(SpecialStruct specialStruct) {
        return getIntPtrFieldValue_internal(specialStruct.getPointer());
    }

    public static native int getIntPtrFieldValue_internal(long specialStruct);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jint)getIntPtrFieldValue(*(SpecialStruct*)specialStruct);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void setFloatPtrFieldValue(SpecialStruct specialStruct, float value) {
        setFloatPtrFieldValue_internal(specialStruct.getPointer(), value);
    }

    public static native void setFloatPtrFieldValue_internal(long specialStruct, float value);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	setFloatPtrFieldValue(*(SpecialStruct*)specialStruct, (float)value);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void setFixedSizeArrayFieldValue(SpecialStruct.SpecialStructPointer specialStruct, int index, int value) {
        setFixedSizeArrayFieldValue_internal(specialStruct.getPointer(), index, value);
    }

    public static native void setFixedSizeArrayFieldValue_internal(long specialStruct, int index, int value);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, int, value, 2, return);
    	CHECK_AND_THROW_C_TYPE(env, int, index, 1, return);
    	setFixedSizeArrayFieldValue((SpecialStruct *)specialStruct, (int)index, (int)value);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void setIntPtrFieldValue(SpecialStruct specialStruct, int value) {
        setIntPtrFieldValue_internal(specialStruct.getPointer(), value);
    }

    public static native void setIntPtrFieldValue_internal(long specialStruct, int value);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, int, value, 1, return);
    	setIntPtrFieldValue(*(SpecialStruct*)specialStruct, (int)value);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static TestUnion.TestUnionPointer returnTestUnionPointer() {
        return new TestUnion.TestUnionPointer(returnTestUnionPointer_internal(), false);
    }

    public static void returnTestUnionPointer(TestUnion.TestUnionPointer _retPar) {
        _retPar.setPointer(returnTestUnionPointer_internal());
    }

    public static native long returnTestUnionPointer_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)returnTestUnionPointer();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static TestUnion returnTestUnion() {
        return new TestUnion(returnTestUnion_internal(0), true);
    }

    public static void returnTestUnion(TestUnion _retPar) {
        returnTestUnion_internal(_retPar.getPointer());
    }

    public static native long returnTestUnion_internal(long _retPar);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	TestUnion* _ret = (TestUnion*) (_retPar == 0 ? malloc(sizeof(TestUnion)) : (void*)_retPar);
    	*_ret = returnTestUnion();
    	return (jlong)_ret;
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static long getUnionUintTypeByValue(TestUnion testUnion) {
        return getUnionUintTypeByValue_internal(testUnion.getPointer());
    }

    public static native long getUnionUintTypeByValue_internal(long testUnion);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getUnionUintTypeByValue(*(TestUnion*)testUnion);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void setUnionUintTypeByPointer(TestUnion.TestUnionPointer testUnion, long value) {
        setUnionUintTypeByPointer_internal(testUnion.getPointer(), value);
    }

    public static native void setUnionUintTypeByPointer_internal(long testUnion, long value);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, uint64_t, value, 1, return);
    	setUnionUintTypeByPointer((TestUnion *)testUnion, (uint64_t)value);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static double getUnionDoubleTypeByValue(TestUnion testUnion) {
        return getUnionDoubleTypeByValue_internal(testUnion.getPointer());
    }

    public static native double getUnionDoubleTypeByValue_internal(long testUnion);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jdouble)getUnionDoubleTypeByValue(*(TestUnion*)testUnion);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void setUnionDoubleTypeByPointer(TestUnion.TestUnionPointer testUnion, double value) {
        setUnionDoubleTypeByPointer_internal(testUnion.getPointer(), value);
    }

    public static native void setUnionDoubleTypeByPointer_internal(long testUnion, double value);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	setUnionDoubleTypeByPointer((TestUnion *)testUnion, (double)value);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static SIntPointer getUnionFixedSizeIntByPointer(TestUnion.TestUnionPointer testUnion) {
        return new SIntPointer(getUnionFixedSizeIntByPointer_internal(testUnion.getPointer()), false);
    }

    public static void getUnionFixedSizeIntByPointer(TestUnion.TestUnionPointer testUnion, SIntPointer _retPar) {
        _retPar.setPointer(getUnionFixedSizeIntByPointer_internal(testUnion.getPointer()));
    }

    public static native long getUnionFixedSizeIntByPointer_internal(long testUnion);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getUnionFixedSizeIntByPointer((TestUnion *)testUnion);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static int getUnionFixedSizeIntByValue(TestUnion testUnion, int index) {
        return getUnionFixedSizeIntByValue_internal(testUnion.getPointer(), index);
    }

    public static native int getUnionFixedSizeIntByValue_internal(long testUnion, int index);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, int, index, 1, return 0);
    	return (jint)getUnionFixedSizeIntByValue(*(TestUnion*)testUnion, (int)index);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void setUnionFixedSizeIntByPointer(TestUnion.TestUnionPointer testUnion, int index, int value) {
        setUnionFixedSizeIntByPointer_internal(testUnion.getPointer(), index, value);
    }

    public static native void setUnionFixedSizeIntByPointer_internal(long testUnion, int index, int value);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, int, value, 2, return);
    	CHECK_AND_THROW_C_TYPE(env, int, index, 1, return);
    	setUnionFixedSizeIntByPointer((TestUnion *)testUnion, (int)index, (int)value);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static TestStruct getUnionStructTypeByValue(TestUnion testUnion) {
        return new TestStruct(getUnionStructTypeByValue_internal(testUnion.getPointer(), 0), true);
    }

    public static void getUnionStructTypeByValue(TestUnion testUnion, TestStruct _retPar) {
        getUnionStructTypeByValue_internal(testUnion.getPointer(), _retPar.getPointer());
    }

    public static native long getUnionStructTypeByValue_internal(long testUnion, long _retPar);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	TestStruct* _ret = (TestStruct*) (_retPar == 0 ? malloc(sizeof(TestStruct)) : (void*)_retPar);
    	*_ret = getUnionStructTypeByValue(*(TestUnion*)testUnion);
    	return (jlong)_ret;
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void setUnionStructTypeByPointer(TestUnion.TestUnionPointer testUnion, TestStruct value) {
        setUnionStructTypeByPointer_internal(testUnion.getPointer(), value.getPointer());
    }

    public static native void setUnionStructTypeByPointer_internal(long testUnion, long value);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	setUnionStructTypeByPointer((TestUnion *)testUnion, *(TestStruct*)value);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static TestUnion.TestUnionPointer call_methodWithCallbackTestUnionPointerReturn(ClosureObject<methodWithCallbackTestUnionPointerReturn> fnPtr) {
        return new TestUnion.TestUnionPointer(call_methodWithCallbackTestUnionPointerReturn_internal(fnPtr.getPointer()), false);
    }

    public static void call_methodWithCallbackTestUnionPointerReturn(ClosureObject<methodWithCallbackTestUnionPointerReturn> fnPtr, TestUnion.TestUnionPointer _retPar) {
        _retPar.setPointer(call_methodWithCallbackTestUnionPointerReturn_internal(fnPtr.getPointer()));
    }

    public static native long call_methodWithCallbackTestUnionPointerReturn_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)call_methodWithCallbackTestUnionPointerReturn((methodWithCallbackTestUnionPointerReturn)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void call_methodWithCallbackTestUnionPointerArg(ClosureObject<methodWithCallbackTestUnionPointerArg> fnPtr) {
        call_methodWithCallbackTestUnionPointerArg_internal(fnPtr.getPointer());
    }

    public static native void call_methodWithCallbackTestUnionPointerArg_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_methodWithCallbackTestUnionPointerArg((methodWithCallbackTestUnionPointerArg)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void outOfBoundArg(long par) {
        outOfBoundArg_internal(par);
    }

    public static native void outOfBoundArg_internal(long par);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	CHECK_AND_THROW_C_TYPE(env, uint32_t, par, 0, return);
    	outOfBoundArg((uint32_t)par);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void throwOrdinaryException() {
        throwOrdinaryException_internal();
    }

    public static native void throwOrdinaryException_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	throwOrdinaryException();
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static void throwNumberException() {
        throwNumberException_internal();
    }

    public static native void throwNumberException_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	throwNumberException();
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static BytePointer returnThrownCauseMessage(ClosureObject<methodWithThrowingCallback> fnPtr) {
        return new BytePointer(returnThrownCauseMessage_internal(fnPtr.getPointer()), false);
    }

    public static void returnThrownCauseMessage(ClosureObject<methodWithThrowingCallback> fnPtr, BytePointer _retPar) {
        _retPar.setPointer(returnThrownCauseMessage_internal(fnPtr.getPointer()));
    }

    public static native long returnThrownCauseMessage_internal(long fnPtr);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)returnThrownCauseMessage((methodWithThrowingCallback)fnPtr);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static BytePointer returnString() {
        return new BytePointer(returnString_internal(), false);
    }

    public static void returnString(BytePointer _retPar) {
        _retPar.setPointer(returnString_internal());
    }

    public static native long returnString_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)returnString();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static boolean validateString(BytePointer str) {
        return validateString_internal(str.getPointer());
    }

    public static native boolean validateString_internal(long str);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jboolean)validateString((char *)str);
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static void call_callback_in_thread(ClosureObject<thread_callback> thread_callback) {
        call_callback_in_thread_internal(thread_callback.getPointer());
    }

    public static native void call_callback_in_thread_internal(long thread_callback);/*
    	HANDLE_JAVA_EXCEPTION_START()
    	call_callback_in_thread((void *(*)(void *))thread_callback);
    	HANDLE_JAVA_EXCEPTION_END()
    */

    public static ClosureObject<methodWithCallback> getVoidCallback() {
        return CHandler.getClosureObject(getVoidCallback_internal(), methodWithCallback_Internal::methodWithCallback_downcall);
    }

    public static native long getVoidCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getVoidCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackLongArg> getLongArgCallback() {
        return CHandler.getClosureObject(getLongArgCallback_internal(), methodWithCallbackLongArg_Internal::methodWithCallbackLongArg_downcall);
    }

    public static native long getLongArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getLongArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackIntArg> getIntArgCallback() {
        return CHandler.getClosureObject(getIntArgCallback_internal(), methodWithCallbackIntArg_Internal::methodWithCallbackIntArg_downcall);
    }

    public static native long getIntArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getIntArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackShortArg> getShortArgCallback() {
        return CHandler.getClosureObject(getShortArgCallback_internal(), methodWithCallbackShortArg_Internal::methodWithCallbackShortArg_downcall);
    }

    public static native long getShortArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getShortArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackByteArg> getByteArgCallback() {
        return CHandler.getClosureObject(getByteArgCallback_internal(), methodWithCallbackByteArg_Internal::methodWithCallbackByteArg_downcall);
    }

    public static native long getByteArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getByteArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackCharArg> getCharArgCallback() {
        return CHandler.getClosureObject(getCharArgCallback_internal(), methodWithCallbackCharArg_Internal::methodWithCallbackCharArg_downcall);
    }

    public static native long getCharArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getCharArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackBooleanArg> getBooleanArgCallback() {
        return CHandler.getClosureObject(getBooleanArgCallback_internal(), methodWithCallbackBooleanArg_Internal::methodWithCallbackBooleanArg_downcall);
    }

    public static native long getBooleanArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getBooleanArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackFloatArg> getFloatArgCallback() {
        return CHandler.getClosureObject(getFloatArgCallback_internal(), methodWithCallbackFloatArg_Internal::methodWithCallbackFloatArg_downcall);
    }

    public static native long getFloatArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getFloatArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackDoubleArg> getDoubleArgCallback() {
        return CHandler.getClosureObject(getDoubleArgCallback_internal(), methodWithCallbackDoubleArg_Internal::methodWithCallbackDoubleArg_downcall);
    }

    public static native long getDoubleArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getDoubleArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackAllArgs> getAllArgsCallback() {
        return CHandler.getClosureObject(getAllArgsCallback_internal(), methodWithCallbackAllArgs_Internal::methodWithCallbackAllArgs_downcall);
    }

    public static native long getAllArgsCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getAllArgsCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackLongReturn> getLongReturnCallback() {
        return CHandler.getClosureObject(getLongReturnCallback_internal(), methodWithCallbackLongReturn_Internal::methodWithCallbackLongReturn_downcall);
    }

    public static native long getLongReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getLongReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackIntReturn> getIntReturnCallback() {
        return CHandler.getClosureObject(getIntReturnCallback_internal(), methodWithCallbackIntReturn_Internal::methodWithCallbackIntReturn_downcall);
    }

    public static native long getIntReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getIntReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackShortReturn> getShortReturnCallback() {
        return CHandler.getClosureObject(getShortReturnCallback_internal(), methodWithCallbackShortReturn_Internal::methodWithCallbackShortReturn_downcall);
    }

    public static native long getShortReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getShortReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackCharReturn> getCharReturnCallback() {
        return CHandler.getClosureObject(getCharReturnCallback_internal(), methodWithCallbackCharReturn_Internal::methodWithCallbackCharReturn_downcall);
    }

    public static native long getCharReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getCharReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackByteReturn> getByteReturnCallback() {
        return CHandler.getClosureObject(getByteReturnCallback_internal(), methodWithCallbackByteReturn_Internal::methodWithCallbackByteReturn_downcall);
    }

    public static native long getByteReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getByteReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackBooleanReturn> getBooleanReturnCallback() {
        return CHandler.getClosureObject(getBooleanReturnCallback_internal(), methodWithCallbackBooleanReturn_Internal::methodWithCallbackBooleanReturn_downcall);
    }

    public static native long getBooleanReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getBooleanReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackFloatReturn> getFloatReturnCallback() {
        return CHandler.getClosureObject(getFloatReturnCallback_internal(), methodWithCallbackFloatReturn_Internal::methodWithCallbackFloatReturn_downcall);
    }

    public static native long getFloatReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getFloatReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackDoubleReturn> getDoubleReturnCallback() {
        return CHandler.getClosureObject(getDoubleReturnCallback_internal(), methodWithCallbackDoubleReturn_Internal::methodWithCallbackDoubleReturn_downcall);
    }

    public static native long getDoubleReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getDoubleReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithIntPtrPtrArg> getIntPtrPtrArgCallback() {
        return CHandler.getClosureObject(getIntPtrPtrArgCallback_internal(), methodWithIntPtrPtrArg_Internal::methodWithIntPtrPtrArg_downcall);
    }

    public static native long getIntPtrPtrArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getIntPtrPtrArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithIntPtrPtrRet> getIntPtrPtrRetCallback() {
        return CHandler.getClosureObject(getIntPtrPtrRetCallback_internal(), methodWithIntPtrPtrRet_Internal::methodWithIntPtrPtrRet_downcall);
    }

    public static native long getIntPtrPtrRetCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getIntPtrPtrRetCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestStructReturn> getTestStructReturnCallback() {
        return CHandler.getClosureObject(getTestStructReturnCallback_internal(), methodWithCallbackTestStructReturn_Internal::methodWithCallbackTestStructReturn_downcall);
    }

    public static native long getTestStructReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestStructReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestStructPointerReturn> getTestStructPointerReturnCallback() {
        return CHandler.getClosureObject(getTestStructPointerReturnCallback_internal(), methodWithCallbackTestStructPointerReturn_Internal::methodWithCallbackTestStructPointerReturn_downcall);
    }

    public static native long getTestStructPointerReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestStructPointerReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestStructArg> getTestStructArgCallback() {
        return CHandler.getClosureObject(getTestStructArgCallback_internal(), methodWithCallbackTestStructArg_Internal::methodWithCallbackTestStructArg_downcall);
    }

    public static native long getTestStructArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestStructArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestStructPointerArg> getTestStructPointerArgCallback() {
        return CHandler.getClosureObject(getTestStructPointerArgCallback_internal(), methodWithCallbackTestStructPointerArg_Internal::methodWithCallbackTestStructPointerArg_downcall);
    }

    public static native long getTestStructPointerArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestStructPointerArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestEnumReturn> getTestEnumReturnCallback() {
        return CHandler.getClosureObject(getTestEnumReturnCallback_internal(), methodWithCallbackTestEnumReturn_Internal::methodWithCallbackTestEnumReturn_downcall);
    }

    public static native long getTestEnumReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestEnumReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestEnumArg> getTestEnumArgCallback() {
        return CHandler.getClosureObject(getTestEnumArgCallback_internal(), methodWithCallbackTestEnumArg_Internal::methodWithCallbackTestEnumArg_downcall);
    }

    public static native long getTestEnumArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestEnumArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestEnumPointerReturn> getTestEnumPointerReturnCallback() {
        return CHandler.getClosureObject(getTestEnumPointerReturnCallback_internal(), methodWithCallbackTestEnumPointerReturn_Internal::methodWithCallbackTestEnumPointerReturn_downcall);
    }

    public static native long getTestEnumPointerReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestEnumPointerReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestEnumPointerArg> getTestEnumPointerArgCallback() {
        return CHandler.getClosureObject(getTestEnumPointerArgCallback_internal(), methodWithCallbackTestEnumPointerArg_Internal::methodWithCallbackTestEnumPointerArg_downcall);
    }

    public static native long getTestEnumPointerArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestEnumPointerArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackIntPointerReturn> getIntPointerReturnCallback() {
        return CHandler.getClosureObject(getIntPointerReturnCallback_internal(), methodWithCallbackIntPointerReturn_Internal::methodWithCallbackIntPointerReturn_downcall);
    }

    public static native long getIntPointerReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getIntPointerReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackIntPointerArg> getIntPointerArgCallback() {
        return CHandler.getClosureObject(getIntPointerArgCallback_internal(), methodWithCallbackIntPointerArg_Internal::methodWithCallbackIntPointerArg_downcall);
    }

    public static native long getIntPointerArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getIntPointerArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestUnionPointerReturn> getTestUnionPointerReturnCallback() {
        return CHandler.getClosureObject(getTestUnionPointerReturnCallback_internal(), methodWithCallbackTestUnionPointerReturn_Internal::methodWithCallbackTestUnionPointerReturn_downcall);
    }

    public static native long getTestUnionPointerReturnCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestUnionPointerReturnCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackTestUnionPointerArg> getTestUnionPointerArgCallback() {
        return CHandler.getClosureObject(getTestUnionPointerArgCallback_internal(), methodWithCallbackTestUnionPointerArg_Internal::methodWithCallbackTestUnionPointerArg_downcall);
    }

    public static native long getTestUnionPointerArgCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getTestUnionPointerArgCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public static ClosureObject<methodWithCallbackCallThrowingCallback> getCallThrowingCallbackCallback() {
        return CHandler.getClosureObject(getCallThrowingCallbackCallback_internal(), methodWithCallbackCallThrowingCallback_Internal::methodWithCallbackCallThrowingCallback_downcall);
    }

    public static native long getCallThrowingCallbackCallback_internal();/*
    	HANDLE_JAVA_EXCEPTION_START()
    	return (jlong)getCallThrowingCallbackCallback();
    	HANDLE_JAVA_EXCEPTION_END()
    	return 0;
    */

    public interface methodWithCallbackBooleanArg extends Closure, methodWithCallbackBooleanArg_Internal {

        void methodWithCallbackBooleanArg_call(boolean arg0);
    }

    public interface methodWithCallbackTestEnumPointerArg extends Closure, methodWithCallbackTestEnumPointerArg_Internal {

        void methodWithCallbackTestEnumPointerArg_call(TestEnum.TestEnumPointer arg0);
    }

    public interface methodWithCallbackDoubleReturn extends Closure, methodWithCallbackDoubleReturn_Internal {

        double methodWithCallbackDoubleReturn_call();
    }

    public interface methodWithThrowingCallback extends Closure, methodWithThrowingCallback_Internal {

        void methodWithThrowingCallback_call();
    }

    public interface methodWithCallbackIntArg extends Closure, methodWithCallbackIntArg_Internal {

        void methodWithCallbackIntArg_call(int arg0);
    }

    public interface methodWithCallbackTestStructPointerReturn extends Closure, methodWithCallbackTestStructPointerReturn_Internal {

        TestStruct.TestStructPointer methodWithCallbackTestStructPointerReturn_call();
    }

    public interface methodWithCallbackShortReturn extends Closure, methodWithCallbackShortReturn_Internal {

        short methodWithCallbackShortReturn_call();
    }

    public interface methodWithCallbackTestEnumPointerReturn extends Closure, methodWithCallbackTestEnumPointerReturn_Internal {

        TestEnum.TestEnumPointer methodWithCallbackTestEnumPointerReturn_call();
    }

    public interface methodWithCallbackTestStructArg extends Closure, methodWithCallbackTestStructArg_Internal {

        void methodWithCallbackTestStructArg_call(TestStruct arg0);
    }

    public interface methodWithCallbackIntPointerReturn extends Closure, methodWithCallbackIntPointerReturn_Internal {

        SIntPointer methodWithCallbackIntPointerReturn_call();
    }

    public interface methodWithCallbackLongArg extends Closure, methodWithCallbackLongArg_Internal {

        void methodWithCallbackLongArg_call(long test);
    }

    public interface methodWithCallbackFloatArg extends Closure, methodWithCallbackFloatArg_Internal {

        void methodWithCallbackFloatArg_call(float arg0);
    }

    public interface methodWithCallbackDoubleArg extends Closure, methodWithCallbackDoubleArg_Internal {

        void methodWithCallbackDoubleArg_call(double arg0);
    }

    public interface methodWithCallbackIntPointerArg extends Closure, methodWithCallbackIntPointerArg_Internal {

        int methodWithCallbackIntPointerArg_call(SIntPointer arg0);
    }

    public interface methodWithCallbackTestEnumArg extends Closure, methodWithCallbackTestEnumArg_Internal {

        void methodWithCallbackTestEnumArg_call(TestEnum arg0);
    }

    public interface methodWithCallbackCallThrowingCallback extends Closure, methodWithCallbackCallThrowingCallback_Internal {

        void methodWithCallbackCallThrowingCallback_call(ClosureObject<methodWithThrowingCallback> arg0);
    }

    public interface methodWithCallbackTestStructPointerArg extends Closure, methodWithCallbackTestStructPointerArg_Internal {

        void methodWithCallbackTestStructPointerArg_call(TestStruct.TestStructPointer arg0);
    }

    public interface methodWithCallbackTestUnionPointerReturn extends Closure, methodWithCallbackTestUnionPointerReturn_Internal {

        TestUnion.TestUnionPointer methodWithCallbackTestUnionPointerReturn_call();
    }

    public interface methodWithCallbackByteReturn extends Closure, methodWithCallbackByteReturn_Internal {

        byte methodWithCallbackByteReturn_call();
    }

    public interface methodWithCallbackCharReturn extends Closure, methodWithCallbackCharReturn_Internal {

        char methodWithCallbackCharReturn_call();
    }

    public interface methodWithCallbackTestEnumReturn extends Closure, methodWithCallbackTestEnumReturn_Internal {

        TestEnum methodWithCallbackTestEnumReturn_call();
    }

    public interface methodWithCallbackAllArgs extends Closure, methodWithCallbackAllArgs_Internal {

        void methodWithCallbackAllArgs_call(long arg0, int arg1, short arg2, byte arg3, char arg4, boolean arg5, float arg6, double arg7);
    }

    public interface methodWithCallback extends Closure, methodWithCallback_Internal {

        /**
         * Comment on callback
         */
        void methodWithCallback_call();
    }

    public interface methodWithIntPtrPtrRet extends Closure, methodWithIntPtrPtrRet_Internal {

        PointerPointer<SIntPointer> methodWithIntPtrPtrRet_call();
    }

    public interface methodWithCallbackTestUnionPointerArg extends Closure, methodWithCallbackTestUnionPointerArg_Internal {

        void methodWithCallbackTestUnionPointerArg_call(TestUnion.TestUnionPointer arg0);
    }

    public interface methodWithCallbackShortArg extends Closure, methodWithCallbackShortArg_Internal {

        void methodWithCallbackShortArg_call(short arg0);
    }

    public interface methodWithCallbackByteArg extends Closure, methodWithCallbackByteArg_Internal {

        void methodWithCallbackByteArg_call(byte arg0);
    }

    public interface methodWithCallbackBooleanReturn extends Closure, methodWithCallbackBooleanReturn_Internal {

        boolean methodWithCallbackBooleanReturn_call();
    }

    public interface methodWithCallbackIntReturn extends Closure, methodWithCallbackIntReturn_Internal {

        int methodWithCallbackIntReturn_call();
    }

    public interface methodWithCallbackLongReturn extends Closure, methodWithCallbackLongReturn_Internal {

        long methodWithCallbackLongReturn_call();
    }

    public interface methodWithCallbackCharArg extends Closure, methodWithCallbackCharArg_Internal {

        void methodWithCallbackCharArg_call(char arg0);
    }

    public interface methodWithIntPtrPtrArg extends Closure, methodWithIntPtrPtrArg_Internal {

        void methodWithIntPtrPtrArg_call(PointerPointer<SIntPointer> arg0);
    }

    public interface thread_callback extends Closure, thread_callback_Internal {

        VoidPointer thread_callback_call(VoidPointer arg0);
    }

    public interface methodWithCallbackTestStructReturn extends Closure, methodWithCallbackTestStructReturn_Internal {

        TestStruct methodWithCallbackTestStructReturn_call();
    }

    public interface methodWithCallbackFloatReturn extends Closure, methodWithCallbackFloatReturn_Internal {

        float methodWithCallbackFloatReturn_call();
    }
}
