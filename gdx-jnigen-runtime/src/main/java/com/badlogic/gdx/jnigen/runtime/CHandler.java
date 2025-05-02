package com.badlogic.gdx.jnigen.runtime;

import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.c.CXXException;
import com.badlogic.gdx.jnigen.runtime.closure.Closure;
import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.gdx.jnigen.runtime.closure.JavaClosureObject;
import com.badlogic.gdx.jnigen.runtime.closure.ClosureDecoder;
import com.badlogic.gdx.jnigen.loader.SharedLibraryLoader;
import com.badlogic.gdx.jnigen.runtime.mem.AllocationManager;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtrManager;
import com.badlogic.gdx.jnigen.runtime.pointer.PointerPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.gdx.jnigen.runtime.util.DowncallCClosureObjectSupplier;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class CHandler {

    static {
        new SharedLibraryLoader().load("jnigen-runtime");
        try {
            boolean res = init(CHandler.class.getDeclaredMethod("dispatchCallback", ClosureDecoder.class, long.class),
                    CHandler.class.getDeclaredMethod("getExceptionString", Throwable.class));
            if (!res)
                throw new RuntimeException("JNI initialization failed, either CHandler#dispatchCallback or CHandler#getExceptionString are not JNI accessible.");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        POINTER_SIZE = is32Bit() ? 4 : 8;
        IS_32_BIT = is32Bit();
        IS_64_BIT = !is32Bit();
        IS_COMPILED_UNIX = !isCompiledWin();
        IS_COMPILED_WIN = isCompiledWin();
        IS_COMPILED_ANDROID_X86 = isCompiledAndroidX86();
        IS_CHAR_SIGNED = isCharSigned();
        LONG_SIZE = is32Bit() || isCompiledWin() ? 4 : 8;
        testNativeSetup();
    }

    public static void init() {
        // To force static initializer
    }

    public static final int POINTER_SIZE;
    public static final boolean IS_32_BIT;
    public static final boolean IS_64_BIT;
    public static final boolean IS_COMPILED_UNIX;
    public static final boolean IS_COMPILED_WIN;
    public static final boolean IS_COMPILED_ANDROID_X86;
    public static final boolean IS_CHAR_SIGNED;
    public static final int LONG_SIZE;

    private static native boolean is32Bit();
    private static native boolean isCompiledWin();
    private static native boolean isCompiledAndroidX86();
    private static native boolean isCharSigned();

    private static final HashMap<CTypeInfo[], Long> classCifMap = new HashMap<>();

    private static final HashMap<Long, ClosureObject<?>> fnPtrClosureMap = new HashMap<>();

    private static native boolean init(Method dispatchCallbackReflectedMethod, Method getExceptionStringReflectedMethod);

    public static String getExceptionString(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    private static void testNativeSetup() {
        try {
            testIllegalArgumentExceptionThrowable(IllegalArgumentException.class);
            throw new RuntimeException("Unable to throw IllegalArgumentException from JNI.");
        }catch (IllegalArgumentException ignored) {}
        try {
            testCXXExceptionThrowable(CXXException.class);
            throw new RuntimeException("Unable to throw CXXException from JNI.");
        }catch (CXXException ignored) {}
    }

    private static native void testIllegalArgumentExceptionThrowable(Class<?> illegalArgumentException);

    private static native void testCXXExceptionThrowable(Class<?> cxxException);

    /**
     * If java code throws an exception into native, this will disable setting a descriptor for the wrapping CXX exception
     * This can hold a performance boost, if a lot of CXX exceptions are created
     */
    public static native void setDisableCXXExceptionMessage(boolean disable);

    public static native boolean reExportSymbolsGlobally(String libPath);

    public static <T extends Closure> void dispatchCallback(ClosureDecoder<T> toCallOn, long parameter) {
        BufferPtr ptr = AllocationManager.wrap(parameter);
        toCallOn.invoke(ptr);
        BufferPtrManager.insertPool(ptr);
    }

    public static native void dispatchCCall(long fnPtr, long cif, long parameter);

    public static native long convertNativeTypeToFFIType(long nativeType);

    public static CTypeInfo constructCTypeFromNativeType(long nativeType) {
        if (nativeType == 0)
            throw new IllegalArgumentException("CType maps to zero.");
        long ffiType = convertNativeTypeToFFIType(nativeType);
        return new CTypeInfo(ffiType, CHandler.getSizeFromFFIType(ffiType));
    }

    private static native long nativeCreateCif(long returnType, long parameters, int size);

    private static long generateFFICifForSignature(CTypeInfo[] signature) {
        int parameterCount = signature.length - 1;
        PointerPointer<VoidPointer> mappedParameters = new PointerPointer<>(parameterCount, VoidPointer::new);

        for (int i = 1; i < signature.length; i++)
            mappedParameters.setValue(new VoidPointer(signature[i].getFfiType(), false), i - 1);

        return nativeCreateCif(signature[0].getFfiType(), mappedParameters.getPointer(), parameterCount);
    }

    public static long getFFICifForSignature(CTypeInfo[] signature) {
        synchronized (classCifMap) {
            Long cif = classCifMap.get(signature);
            if (cif == null) {
                cif = CHandler.generateFFICifForSignature(signature);
                classCifMap.put(signature, cif);
            }
            return cif;
        }
    }

    public static <T extends Closure> ClosureObject<T> createClosureForObject(T object) {
        long cif = getFFICifForSignature(object.functionSignature());
        PointerPointer<VoidPointer> ret = new PointerPointer<>(VoidPointer::new);

        ClosureDecoder<T> closureDecoder = new ClosureDecoder<>(object);
        long fnPtr = createClosureForObject(cif, closureDecoder, ret.getPointer());
        long closurePtr = ret.getValue().getPointer();

        ClosureObject<T> closureObject = new JavaClosureObject<>(object, fnPtr, closurePtr, closureDecoder);
        synchronized (fnPtrClosureMap) {
            fnPtrClosureMap.put(fnPtr, closureObject);
        }
        return closureObject;
    }

    public static native long createClosureForObject(long cifArg, ClosureDecoder<?> object, long closureRet);

    public static <T extends Closure> ClosureObject<T> getClosureObject(long fnPtr, DowncallCClosureObjectSupplier<T> closureFallback) {
        synchronized (fnPtrClosureMap) {
            @SuppressWarnings("unchecked")
            ClosureObject<T> closureObject = (ClosureObject<T>) fnPtrClosureMap.get(fnPtr);
            if (closureObject == null) {
                if (closureFallback == null)
                    throw new RuntimeException("No Closure object found for " + fnPtr);
                closureObject = closureFallback.get(fnPtr);
                fnPtrClosureMap.put(fnPtr, closureObject);
            }
            return closureObject;
        }
    }

    public static native ByteBuffer wrapPointer(long pointer, int size);

    public static native int getSizeFromFFIType(long type);

    public static void deregisterFunctionPointer(long fnPtr) {
        synchronized (fnPtrClosureMap) {
            fnPtrClosureMap.remove(fnPtr);
        }
    }

    public static void clearRegisteredFunctionPointer() {
        synchronized (fnPtrClosureMap) {
            fnPtrClosureMap.clear();
        }
    }


    public static native void freeClosure(long closurePtr);

    public static native long malloc(long size);

    public static native long calloc(long count, long size);

    public static native void free(long pointer);

    public static native void memcpy(long dst, long src, long size);

    public static native long clone(long src, long size);
}
