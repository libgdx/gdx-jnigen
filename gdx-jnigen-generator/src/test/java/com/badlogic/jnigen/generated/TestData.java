package com.badlogic.jnigen.generated;

import com.badlogic.gdx.jnigen.pointer.CType;
import com.badlogic.gdx.jnigen.pointer.Signed;
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

    public static StructPointer<TestStruct> returnTestStructPointer() {
        return new TestStruct.Pointer(returnTestStructPointer_internal(), false);
    }

    static private native long returnTestStructPointer_internal();/*
    	return (jlong)returnTestStructPointer();
    */

    public static TestStruct returnTestStruct() {
        TestStruct _ret = new TestStruct();
        returnTestStruct_internal(_ret.getPointer());
        return _ret;
    }

    static private native void returnTestStruct_internal(long _ret);/*
    	*(TestStruct *)_ret  = returnTestStruct();
    */

    public static long passByValue(TestStruct arg0) {
        return passByValue_internal(arg0.getPointer());
    }

    static private native long passByValue_internal(long arg0);/*
    	return (jlong)passByValue(*(TestStruct*)arg0);
    */

    public static long passByPointer(StructPointer<TestStruct> arg0) {
        return passByPointer_internal(arg0.getPointer());
    }

    static private native long passByPointer_internal(long arg0);/*
    	return (jlong)passByPointer((TestStruct *)arg0);
    */

    public static TestStruct call_methodWithCallbackTestStructReturn(ClosureObject<methodWithCallbackTestStructReturn> arg0) {
        TestStruct _ret = new TestStruct();
        call_methodWithCallbackTestStructReturn_internal(arg0.getFnPtr(), _ret.getPointer());
        return _ret;
    }

    static private native void call_methodWithCallbackTestStructReturn_internal(long arg0, long _ret);/*
    	*(TestStruct *)_ret  = call_methodWithCallbackTestStructReturn((methodWithCallbackTestStructReturn)arg0);
    */

    public static StructPointer<TestStruct> call_methodWithCallbackTestStructPointerReturn(ClosureObject<methodWithCallbackTestStructPointerReturn> arg0) {
        return new TestStruct.Pointer(call_methodWithCallbackTestStructPointerReturn_internal(arg0.getFnPtr()), false);
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
        return TestEnum.getByIndex(returnTestEnum_internal());
    }

    static private native int returnTestEnum_internal();/*
    	return (jint)returnTestEnum();
    */

    public static TestEnum passAndReturnTestEnum(TestEnum arg0) {
        return TestEnum.getByIndex(passAndReturnTestEnum_internal(arg0.getIndex()));
    }

    static private native int passAndReturnTestEnum_internal(int arg0);/*
    	return (jint)passAndReturnTestEnum((TestEnum)arg0);
    */

    public interface methodWithCallbackAllArgs extends Closure {

        void methodWithCallbackAllArgs_call(@CType(value = "uint64_t") long arg0, @CType(value = "int") @Signed() int arg1, @CType(value = "short") @Signed() short arg2, @CType(value = "char") @Signed() byte arg3, @CType(value = "uint16_t") char arg4, @CType(value = "bool") boolean arg5, @CType(value = "float") @Signed() float arg6, @CType(value = "double") @Signed() double arg7);

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackAllArgs_call(parameters[0].asLong(), parameters[1].asInt(), parameters[2].asShort(), parameters[3].asByte(), parameters[4].asChar(), parameters[5].asBoolean(), parameters[6].asFloat(), parameters[7].asDouble());
        }
    }

    public interface methodWithCallback extends Closure {

        void methodWithCallback_call();

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallback_call();
        }
    }

    public interface methodWithCallbackBooleanArg extends Closure {

        void methodWithCallbackBooleanArg_call(@CType(value = "bool") boolean arg0);

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackBooleanArg_call(parameters[0].asBoolean());
        }
    }

    public interface methodWithCallbackDoubleReturn extends Closure {

        @CType(value = "double")
        @Signed()
        double methodWithCallbackDoubleReturn_call();

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackDoubleReturn_call());
        }
    }

    public interface methodWithCallbackIntArg extends Closure {

        void methodWithCallbackIntArg_call(@CType(value = "int") @Signed() int arg0);

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackIntArg_call(parameters[0].asInt());
        }
    }

    public interface methodWithCallbackShortArg extends Closure {

        void methodWithCallbackShortArg_call(@CType(value = "short") @Signed() short arg0);

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackShortArg_call(parameters[0].asShort());
        }
    }

    public interface methodWithCallbackTestStructPointerReturn extends Closure {

        StructPointer<TestStruct> methodWithCallbackTestStructPointerReturn_call();

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestStructPointerReturn_call());
        }
    }

    public interface methodWithCallbackByteArg extends Closure {

        void methodWithCallbackByteArg_call(@CType(value = "char") @Signed() byte arg0);

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackByteArg_call(parameters[0].asByte());
        }
    }

    public interface methodWithCallbackShortReturn extends Closure {

        @CType(value = "short")
        @Signed()
        short methodWithCallbackShortReturn_call();

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackShortReturn_call());
        }
    }

    public interface methodWithCallbackBooleanReturn extends Closure {

        @CType(value = "bool")
        boolean methodWithCallbackBooleanReturn_call();

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackBooleanReturn_call());
        }
    }

    public interface methodWithCallbackTestStructArg extends Closure {

        void methodWithCallbackTestStructArg_call(TestStruct arg0);

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackTestStructArg_call((TestStruct) parameters[0].asPointing());
        }
    }

    public interface methodWithCallbackLongArg extends Closure {

        void methodWithCallbackLongArg_call(@CType(value = "uint64_t") long arg0);

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackLongArg_call(parameters[0].asLong());
        }
    }

    public interface methodWithCallbackFloatArg extends Closure {

        void methodWithCallbackFloatArg_call(@CType(value = "float") @Signed() float arg0);

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackFloatArg_call(parameters[0].asFloat());
        }
    }

    public interface methodWithCallbackDoubleArg extends Closure {

        void methodWithCallbackDoubleArg_call(@CType(value = "double") @Signed() double arg0);

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackDoubleArg_call(parameters[0].asDouble());
        }
    }

    public interface methodWithCallbackIntReturn extends Closure {

        @CType(value = "int")
        @Signed()
        int methodWithCallbackIntReturn_call();

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackIntReturn_call());
        }
    }

    public interface methodWithCallbackLongReturn extends Closure {

        @CType(value = "uint64_t")
        long methodWithCallbackLongReturn_call();

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackLongReturn_call());
        }
    }

    public interface methodWithCallbackTestStructPointerArg extends Closure {

        void methodWithCallbackTestStructPointerArg_call(TestStruct.Pointer arg0);

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackTestStructPointerArg_call((TestStruct.Pointer) parameters[0].asPointing());
        }
    }

    public interface methodWithCallbackCharArg extends Closure {

        void methodWithCallbackCharArg_call(@CType(value = "uint16_t") char arg0);

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackCharArg_call(parameters[0].asChar());
        }
    }

    public interface methodWithCallbackByteReturn extends Closure {

        @CType(value = "char")
        @Signed()
        byte methodWithCallbackByteReturn_call();

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackByteReturn_call());
        }
    }

    public interface methodWithCallbackCharReturn extends Closure {

        @CType(value = "uint16_t")
        char methodWithCallbackCharReturn_call();

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackCharReturn_call());
        }
    }

    public interface methodWithCallbackTestStructReturn extends Closure {

        TestStruct methodWithCallbackTestStructReturn_call();

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestStructReturn_call());
        }
    }

    public interface methodWithCallbackFloatReturn extends Closure {

        @CType(value = "float")
        @Signed()
        float methodWithCallbackFloatReturn_call();

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackFloatReturn_call());
        }
    }
}
