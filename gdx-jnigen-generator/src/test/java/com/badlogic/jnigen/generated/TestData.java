package com.badlogic.jnigen.generated;

import com.badlogic.gdx.jnigen.CHandler;
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
import com.badlogic.gdx.jnigen.pointer.StructPointer;
import com.badlogic.jnigen.generated.structs.TestStruct;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestStructReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestStructPointerReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestStructArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestStructPointerArg;
import com.badlogic.jnigen.generated.enums.TestEnum;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestEnumReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestEnumArg;
import com.badlogic.gdx.jnigen.pointer.CSizedIntPointer;
import com.badlogic.jnigen.generated.structs.SpecialStruct;
import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.ffi.JavaTypeWrapper;

public class TestData {

    /*JNI
#include <jnigen.h>
#include <test_data.h>
*/
    public static void call_methodWithCallback(ClosureObject<methodWithCallback> arg0) {
        call_methodWithCallback_internal(arg0.getFnPtr());
    }

    static private native void call_methodWithCallback_internal(long arg0);/*
    	call_methodWithCallback((methodWithCallback)arg0);
    */

    public static void call_methodWithCallbackLongArg(ClosureObject<methodWithCallbackLongArg> arg0) {
        call_methodWithCallbackLongArg_internal(arg0.getFnPtr());
    }

    static private native void call_methodWithCallbackLongArg_internal(long arg0);/*
    	call_methodWithCallbackLongArg((methodWithCallbackLongArg)arg0);
    */

    public static void call_methodWithCallbackIntArg(ClosureObject<methodWithCallbackIntArg> arg0) {
        call_methodWithCallbackIntArg_internal(arg0.getFnPtr());
    }

    static private native void call_methodWithCallbackIntArg_internal(long arg0);/*
    	call_methodWithCallbackIntArg((methodWithCallbackIntArg)arg0);
    */

    public static void call_methodWithCallbackShortArg(ClosureObject<methodWithCallbackShortArg> arg0) {
        call_methodWithCallbackShortArg_internal(arg0.getFnPtr());
    }

    static private native void call_methodWithCallbackShortArg_internal(long arg0);/*
    	call_methodWithCallbackShortArg((methodWithCallbackShortArg)arg0);
    */

    public static void call_methodWithCallbackByteArg(ClosureObject<methodWithCallbackByteArg> arg0) {
        call_methodWithCallbackByteArg_internal(arg0.getFnPtr());
    }

    static private native void call_methodWithCallbackByteArg_internal(long arg0);/*
    	call_methodWithCallbackByteArg((methodWithCallbackByteArg)arg0);
    */

    public static void call_methodWithCallbackCharArg(ClosureObject<methodWithCallbackCharArg> arg0) {
        call_methodWithCallbackCharArg_internal(arg0.getFnPtr());
    }

    static private native void call_methodWithCallbackCharArg_internal(long arg0);/*
    	call_methodWithCallbackCharArg((methodWithCallbackCharArg)arg0);
    */

    public static void call_methodWithCallbackBooleanArg(ClosureObject<methodWithCallbackBooleanArg> arg0) {
        call_methodWithCallbackBooleanArg_internal(arg0.getFnPtr());
    }

    static private native void call_methodWithCallbackBooleanArg_internal(long arg0);/*
    	call_methodWithCallbackBooleanArg((methodWithCallbackBooleanArg)arg0);
    */

    public static void call_methodWithCallbackFloatArg(ClosureObject<methodWithCallbackFloatArg> arg0) {
        call_methodWithCallbackFloatArg_internal(arg0.getFnPtr());
    }

    static private native void call_methodWithCallbackFloatArg_internal(long arg0);/*
    	call_methodWithCallbackFloatArg((methodWithCallbackFloatArg)arg0);
    */

    public static void call_methodWithCallbackDoubleArg(ClosureObject<methodWithCallbackDoubleArg> arg0) {
        call_methodWithCallbackDoubleArg_internal(arg0.getFnPtr());
    }

    static private native void call_methodWithCallbackDoubleArg_internal(long arg0);/*
    	call_methodWithCallbackDoubleArg((methodWithCallbackDoubleArg)arg0);
    */

    public static void call_methodWithCallbackAllArgs(ClosureObject<methodWithCallbackAllArgs> arg0) {
        call_methodWithCallbackAllArgs_internal(arg0.getFnPtr());
    }

    static private native void call_methodWithCallbackAllArgs_internal(long arg0);/*
    	call_methodWithCallbackAllArgs((methodWithCallbackAllArgs)arg0);
    */

    public static long call_methodWithCallbackLongReturn(ClosureObject<methodWithCallbackLongReturn> arg0) {
        return call_methodWithCallbackLongReturn_internal(arg0.getFnPtr());
    }

    static private native long call_methodWithCallbackLongReturn_internal(long arg0);/*
    	return (jlong)call_methodWithCallbackLongReturn((methodWithCallbackLongReturn)arg0);
    */

    public static int call_methodWithCallbackIntReturn(ClosureObject<methodWithCallbackIntReturn> arg0) {
        return call_methodWithCallbackIntReturn_internal(arg0.getFnPtr());
    }

    static private native int call_methodWithCallbackIntReturn_internal(long arg0);/*
    	return (jint)call_methodWithCallbackIntReturn((methodWithCallbackIntReturn)arg0);
    */

    public static short call_methodWithCallbackShortReturn(ClosureObject<methodWithCallbackShortReturn> arg0) {
        return call_methodWithCallbackShortReturn_internal(arg0.getFnPtr());
    }

    static private native short call_methodWithCallbackShortReturn_internal(long arg0);/*
    	return (jshort)call_methodWithCallbackShortReturn((methodWithCallbackShortReturn)arg0);
    */

    public static char call_methodWithCallbackCharReturn(ClosureObject<methodWithCallbackCharReturn> arg0) {
        return call_methodWithCallbackCharReturn_internal(arg0.getFnPtr());
    }

    static private native char call_methodWithCallbackCharReturn_internal(long arg0);/*
    	return (jchar)call_methodWithCallbackCharReturn((methodWithCallbackCharReturn)arg0);
    */

    public static byte call_methodWithCallbackByteReturn(ClosureObject<methodWithCallbackByteReturn> arg0) {
        return call_methodWithCallbackByteReturn_internal(arg0.getFnPtr());
    }

    static private native byte call_methodWithCallbackByteReturn_internal(long arg0);/*
    	return (jbyte)call_methodWithCallbackByteReturn((methodWithCallbackByteReturn)arg0);
    */

    public static boolean call_methodWithCallbackBooleanReturn(ClosureObject<methodWithCallbackBooleanReturn> arg0) {
        return call_methodWithCallbackBooleanReturn_internal(arg0.getFnPtr());
    }

    static private native boolean call_methodWithCallbackBooleanReturn_internal(long arg0);/*
    	return (jboolean)call_methodWithCallbackBooleanReturn((methodWithCallbackBooleanReturn)arg0);
    */

    public static float call_methodWithCallbackFloatReturn(ClosureObject<methodWithCallbackFloatReturn> arg0) {
        return call_methodWithCallbackFloatReturn_internal(arg0.getFnPtr());
    }

    static private native float call_methodWithCallbackFloatReturn_internal(long arg0);/*
    	return (jfloat)call_methodWithCallbackFloatReturn((methodWithCallbackFloatReturn)arg0);
    */

    public static double call_methodWithCallbackDoubleReturn(ClosureObject<methodWithCallbackDoubleReturn> arg0) {
        return call_methodWithCallbackDoubleReturn_internal(arg0.getFnPtr());
    }

    static private native double call_methodWithCallbackDoubleReturn_internal(long arg0);/*
    	return (jdouble)call_methodWithCallbackDoubleReturn((methodWithCallbackDoubleReturn)arg0);
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

    public static long passByValue(TestStruct arg0) {
        return passByValue_internal(arg0.getPointer());
    }

    static private native long passByValue_internal(long arg0);/*
    	return (jlong)passByValue(*(TestStruct*)arg0);
    */

    public static long passByPointer(TestStruct.TestStructPointer arg0) {
        return passByPointer_internal(arg0.getPointer());
    }

    static private native long passByPointer_internal(long arg0);/*
    	return (jlong)passByPointer((TestStruct *)arg0);
    */

    public static TestStruct call_methodWithCallbackTestStructReturn(ClosureObject<methodWithCallbackTestStructReturn> arg0) {
        return new TestStruct(call_methodWithCallbackTestStructReturn_internal(arg0.getFnPtr()), true);
    }

    static private native long call_methodWithCallbackTestStructReturn_internal(long arg0);/*
    	TestStruct* _ret = (TestStruct*)malloc(sizeof(TestStruct));
    	*_ret = call_methodWithCallbackTestStructReturn((methodWithCallbackTestStructReturn)arg0);
    	return (jlong)_ret;
    */

    public static TestStruct.TestStructPointer call_methodWithCallbackTestStructPointerReturn(ClosureObject<methodWithCallbackTestStructPointerReturn> arg0) {
        return new TestStruct.TestStructPointer(call_methodWithCallbackTestStructPointerReturn_internal(arg0.getFnPtr()), false);
    }

    static private native long call_methodWithCallbackTestStructPointerReturn_internal(long arg0);/*
    	return (jlong)call_methodWithCallbackTestStructPointerReturn((methodWithCallbackTestStructPointerReturn)arg0);
    */

    public static void call_methodWithCallbackTestStructArg(ClosureObject<methodWithCallbackTestStructArg> arg0) {
        call_methodWithCallbackTestStructArg_internal(arg0.getFnPtr());
    }

    static private native void call_methodWithCallbackTestStructArg_internal(long arg0);/*
    	call_methodWithCallbackTestStructArg((methodWithCallbackTestStructArg)arg0);
    */

    public static void call_methodWithCallbackTestStructPointerArg(ClosureObject<methodWithCallbackTestStructPointerArg> arg0) {
        call_methodWithCallbackTestStructPointerArg_internal(arg0.getFnPtr());
    }

    static private native void call_methodWithCallbackTestStructPointerArg_internal(long arg0);/*
    	call_methodWithCallbackTestStructPointerArg((methodWithCallbackTestStructPointerArg)arg0);
    */

    public static int passTestEnum(TestEnum arg0) {
        return passTestEnum_internal(arg0.getIndex());
    }

    static private native int passTestEnum_internal(int arg0);/*
    	return (jint)passTestEnum((TestEnum)arg0);
    */

    public static TestEnum returnTestEnum() {
        return TestEnum.getByIndex((int) returnTestEnum_internal());
    }

    static private native int returnTestEnum_internal();/*
    	return (jint)returnTestEnum();
    */

    public static TestEnum passAndReturnTestEnum(TestEnum arg0) {
        return TestEnum.getByIndex((int) passAndReturnTestEnum_internal(arg0.getIndex()));
    }

    static private native int passAndReturnTestEnum_internal(int arg0);/*
    	return (jint)passAndReturnTestEnum((TestEnum)arg0);
    */

    public static TestEnum call_methodWithCallbackTestEnumReturn(ClosureObject<methodWithCallbackTestEnumReturn> arg0) {
        return TestEnum.getByIndex((int) call_methodWithCallbackTestEnumReturn_internal(arg0.getFnPtr()));
    }

    static private native int call_methodWithCallbackTestEnumReturn_internal(long arg0);/*
    	return (jint)call_methodWithCallbackTestEnumReturn((methodWithCallbackTestEnumReturn)arg0);
    */

    public static void call_methodWithCallbackTestEnumArg(ClosureObject<methodWithCallbackTestEnumArg> arg0) {
        call_methodWithCallbackTestEnumArg_internal(arg0.getFnPtr());
    }

    static private native void call_methodWithCallbackTestEnumArg_internal(long arg0);/*
    	call_methodWithCallbackTestEnumArg((methodWithCallbackTestEnumArg)arg0);
    */

    public static int passIntPointer(CSizedIntPointer arg0) {
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

    public static float getFloatPtrFieldValue(SpecialStruct arg0) {
        return getFloatPtrFieldValue_internal(arg0.getPointer());
    }

    static private native float getFloatPtrFieldValue_internal(long arg0);/*
    	return (jfloat)getFloatPtrFieldValue(*(SpecialStruct*)arg0);
    */

    public static int getFixedSizeArrayFieldValue(SpecialStruct arg0, int arg1) {
        return getFixedSizeArrayFieldValue_internal(arg0.getPointer(), arg1);
    }

    static private native int getFixedSizeArrayFieldValue_internal(long arg0, int arg1);/*
    	return (jint)getFixedSizeArrayFieldValue(*(SpecialStruct*)arg0, (int)arg1);
    */

    public static int getIntPtrFieldValue(SpecialStruct arg0) {
        return getIntPtrFieldValue_internal(arg0.getPointer());
    }

    static private native int getIntPtrFieldValue_internal(long arg0);/*
    	return (jint)getIntPtrFieldValue(*(SpecialStruct*)arg0);
    */

    public static void setFloatPtrFieldValue(SpecialStruct arg0, float arg1) {
        setFloatPtrFieldValue_internal(arg0.getPointer(), arg1);
    }

    static private native void setFloatPtrFieldValue_internal(long arg0, float arg1);/*
    	setFloatPtrFieldValue(*(SpecialStruct*)arg0, (float)arg1);
    */

    public static void setFixedSizeArrayFieldValue(SpecialStruct.SpecialStructPointer arg0, int arg1, int arg2) {
        setFixedSizeArrayFieldValue_internal(arg0.getPointer(), arg1, arg2);
    }

    static private native void setFixedSizeArrayFieldValue_internal(long arg0, int arg1, int arg2);/*
    	setFixedSizeArrayFieldValue((SpecialStruct *)arg0, (int)arg1, (int)arg2);
    */

    public static void setIntPtrFieldValue(SpecialStruct arg0, int arg1) {
        setIntPtrFieldValue_internal(arg0.getPointer(), arg1);
    }

    static private native void setIntPtrFieldValue_internal(long arg0, int arg1);/*
    	setIntPtrFieldValue(*(SpecialStruct*)arg0, (int)arg1);
    */

    public interface methodWithCallbackAllArgs extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(-2), FFITypes.getFFIType(0), FFITypes.getFFIType(5), FFITypes.getFFIType(6), FFITypes.getFFIType(7), FFITypes.getFFIType(2), FFITypes.getFFIType(8), FFITypes.getFFIType(4), FFITypes.getFFIType(9) };

        void methodWithCallbackAllArgs_call(long arg0, int arg1, short arg2, byte arg3, char arg4, boolean arg5, float arg6, double arg7);

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackAllArgs_call((long) parameters[0].asLong(), (int) parameters[1].asLong(), (short) parameters[2].asLong(), (byte) parameters[3].asLong(), (char) parameters[4].asLong(), parameters[5].asLong() != 0, (float) parameters[6].asFloat(), (double) parameters[7].asDouble());
        }
    }

    public interface methodWithCallback extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(-2) };

        void methodWithCallback_call();

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallback_call();
        }
    }

    public interface methodWithCallbackBooleanArg extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(-2), FFITypes.getFFIType(8) };

        void methodWithCallbackBooleanArg_call(boolean arg0);

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackBooleanArg_call(parameters[0].asLong() != 0);
        }
    }

    public interface methodWithCallbackDoubleReturn extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(9) };

        double methodWithCallbackDoubleReturn_call();

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackDoubleReturn_call());
        }
    }

    public interface methodWithCallbackIntArg extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(-2), FFITypes.getFFIType(5) };

        void methodWithCallbackIntArg_call(int arg0);

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackIntArg_call((int) parameters[0].asLong());
        }
    }

    public interface methodWithCallbackShortArg extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(-2), FFITypes.getFFIType(6) };

        void methodWithCallbackShortArg_call(short arg0);

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackShortArg_call((short) parameters[0].asLong());
        }
    }

    public interface methodWithCallbackTestStructPointerReturn extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(-1) };

        TestStruct.TestStructPointer methodWithCallbackTestStructPointerReturn_call();

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestStructPointerReturn_call());
        }
    }

    public interface methodWithCallbackByteArg extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(-2), FFITypes.getFFIType(7) };

        void methodWithCallbackByteArg_call(byte arg0);

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackByteArg_call((byte) parameters[0].asLong());
        }
    }

    public interface methodWithCallbackShortReturn extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(6) };

        short methodWithCallbackShortReturn_call();

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackShortReturn_call());
        }
    }

    public interface methodWithCallbackBooleanReturn extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(8) };

        boolean methodWithCallbackBooleanReturn_call();

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackBooleanReturn_call());
        }
    }

    public interface methodWithCallbackTestStructArg extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(-2), FFITypes.getFFIType(10) };

        void methodWithCallbackTestStructArg_call(TestStruct arg0);

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackTestStructArg_call(new TestStruct(parameters[0].asLong(), true));
        }
    }

    public interface methodWithCallbackLongArg extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(-2), FFITypes.getFFIType(0) };

        void methodWithCallbackLongArg_call(long arg0);

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackLongArg_call((long) parameters[0].asLong());
        }
    }

    public interface methodWithCallbackFloatArg extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(-2), FFITypes.getFFIType(4) };

        void methodWithCallbackFloatArg_call(float arg0);

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackFloatArg_call((float) parameters[0].asFloat());
        }
    }

    public interface methodWithCallbackDoubleArg extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(-2), FFITypes.getFFIType(9) };

        void methodWithCallbackDoubleArg_call(double arg0);

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackDoubleArg_call((double) parameters[0].asDouble());
        }
    }

    public interface methodWithCallbackIntReturn extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(5) };

        int methodWithCallbackIntReturn_call();

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackIntReturn_call());
        }
    }

    public interface methodWithCallbackTestEnumArg extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(-2), FFITypes.getFFIType(5) };

        void methodWithCallbackTestEnumArg_call(TestEnum arg0);

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackTestEnumArg_call(TestEnum.getByIndex((int) parameters[0].asLong()));
        }
    }

    public interface methodWithCallbackLongReturn extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(0) };

        long methodWithCallbackLongReturn_call();

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackLongReturn_call());
        }
    }

    public interface methodWithCallbackTestStructPointerArg extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(-2), FFITypes.getFFIType(-1) };

        void methodWithCallbackTestStructPointerArg_call(TestStruct.TestStructPointer arg0);

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackTestStructPointerArg_call(new TestStruct.TestStructPointer(parameters[0].asLong(), false));
        }
    }

    public interface methodWithCallbackCharArg extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(-2), FFITypes.getFFIType(2) };

        void methodWithCallbackCharArg_call(char arg0);

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackCharArg_call((char) parameters[0].asLong());
        }
    }

    public interface methodWithCallbackByteReturn extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(7) };

        byte methodWithCallbackByteReturn_call();

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackByteReturn_call());
        }
    }

    public interface methodWithCallbackCharReturn extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(2) };

        char methodWithCallbackCharReturn_call();

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackCharReturn_call());
        }
    }

    public interface methodWithCallbackTestStructReturn extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(10) };

        TestStruct methodWithCallbackTestStructReturn_call();

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestStructReturn_call());
        }
    }

    public interface methodWithCallbackTestEnumReturn extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(5) };

        TestEnum methodWithCallbackTestEnumReturn_call();

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestEnumReturn_call());
        }
    }

    public interface methodWithCallbackFloatReturn extends Closure {

        long[] __ffi_cache = new long[] { FFITypes.getFFIType(4) };

        float methodWithCallbackFloatReturn_call();

        default long[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackFloatReturn_call());
        }
    }
}
