package com.badlogic.gdx.jnigen.runtime;

import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.c.CXXException;
import com.badlogic.gdx.jnigen.runtime.closure.Closure;
import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.gdx.jnigen.runtime.ffi.ClosureDecoder;
import com.badlogic.gdx.jnigen.loader.SharedLibraryLoader;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtrAllocator;
import com.badlogic.gdx.jnigen.runtime.util.DowncallClosureSupplier;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
        IS_COMPILED_WIN = isCompiledWin();
        IS_CHAR_SIGNED = isCharSigned();
        LONG_SIZE = is32Bit() || isCompiledWin() ? 4 : 8;
        testNativeSetup();
    }

    public static void init() {
        // To force static initializer
    }

    public static final int POINTER_SIZE;
    public static final boolean IS_32_BIT;
    public static final boolean IS_COMPILED_WIN;
    public static final boolean IS_CHAR_SIGNED;
    public static final int LONG_SIZE;

    private static native boolean is32Bit();
    private static native boolean isCompiledWin();
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
        toCallOn.invoke(BufferPtrAllocator.get(parameter));
    }

    public static native void dispatchCCall(long fnPtr, long cif, long parameter);

    public static native long convertNativeTypeToFFIType(long nativeType);

    public static CTypeInfo constructStackElementCTypeFromNativeType(long nativeType) {
        if (nativeType == 0)
            throw new IllegalArgumentException("CType maps to zero.");
        long ffiType = convertNativeTypeToFFIType(nativeType);
        return new CTypeInfo(ffiType, CHandler.getSizeFromFFIType(ffiType), CHandler.getSignFromFFIType(ffiType), true, CHandler.isVoid(ffiType));
    }

    public static CTypeInfo constructCTypeFromNativeType(long nativeType) {
        if (nativeType == 0)
            throw new IllegalArgumentException("CType maps to zero.");
        long ffiType = convertNativeTypeToFFIType(nativeType);
        return new CTypeInfo(ffiType, CHandler.getSizeFromFFIType(ffiType), CHandler.getSignFromFFIType(ffiType), false, CHandler.isVoid(ffiType));
    }

    private static native long nativeCreateCif(long returnType, ByteBuffer parameters, int size); 

    private static long generateFFICifForSignature(CTypeInfo[] signature) {

        int parameterCount = signature.length - 1;
        // Yes, I'm extremely lazy and don't want to deal with JNI array handling
        ByteBuffer mappedParameter = ByteBuffer.allocateDirect(parameterCount * 8);
        mappedParameter.order(ByteOrder.nativeOrder());
        for (int i = 1; i < signature.length; i++) {
            mappedParameter.putLong((i - 1) * 8, signature[i].getFfiType());
        }

        return nativeCreateCif(signature[0].getFfiType(), mappedParameter, parameterCount);
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
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8);
        byteBuffer.order(ByteOrder.nativeOrder());

        ClosureDecoder<T> closureDecoder = new ClosureDecoder<>(object);
        long fnPtr = createClosureForObject(cif, closureDecoder, byteBuffer);
        long closurePtr = byteBuffer.getLong();

        ClosureObject<T> closureObject = new ClosureObject<>(object, fnPtr, closurePtr, false);
        synchronized (fnPtrClosureMap) {
            fnPtrClosureMap.put(fnPtr, closureObject);
        }
        return closureObject;
    }

    public static native <T extends Closure> long createClosureForObject(long cifArg, ClosureDecoder<T> object, ByteBuffer closureRet);

    public static <T extends Closure> ClosureObject<T> getClosureObject(long fnPtr, DowncallClosureSupplier<T> closureFallback) {
        synchronized (fnPtrClosureMap) {
            @SuppressWarnings("unchecked")
            ClosureObject<T> closureObject = (ClosureObject<T>) fnPtrClosureMap.get(fnPtr);
            if (closureObject == null) {
                closureObject = new ClosureObject<>(closureFallback.get(fnPtr), fnPtr, 0, false);
                fnPtrClosureMap.put(fnPtr, closureObject);
            }
            return closureObject;
        }
    }

    public static native ByteBuffer wrapPointer(long pointer, int size);

    public static native int getSizeFromFFIType(long type);

    public static native boolean getSignFromFFIType(long type);

    public static native boolean isStruct(long type);

    public static native boolean isVoid(long type);

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
