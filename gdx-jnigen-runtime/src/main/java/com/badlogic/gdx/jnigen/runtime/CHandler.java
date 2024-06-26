package com.badlogic.gdx.jnigen.runtime;

import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.c.CXXException;
import com.badlogic.gdx.jnigen.runtime.closure.Closure;
import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.gdx.jnigen.runtime.ffi.ClosureInfo;
import com.badlogic.gdx.jnigen.loader.SharedLibraryLoader;

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
            boolean res = init(CHandler.class.getDeclaredMethod("dispatchCallback", ClosureInfo.class, ByteBuffer.class),
                    CHandler.class.getDeclaredMethod("getExceptionString", Throwable.class));
            if (!res)
                throw new RuntimeException("JNI initialization failed, either CHandler#dispatchCallback or CHandler#getExceptionString are not JNI accessible.");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        POINTER_SIZE = getPointerSize();
        testNativeSetup();
    }

    public static void init() {
        // To force static initializer
    }

    public static final int POINTER_SIZE;

    private static native int getPointerSize();

    private static final HashMap<CTypeInfo[], Long> classCifMap = new HashMap<>();

    private static final HashMap<String, CTypeInfo> cTypeInfoMap = new HashMap<>();

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

    private static native void testIllegalArgumentExceptionThrowable(Class illegalArgumentException);

    private static native void testCXXExceptionThrowable(Class cxxException);

    /**
     * If java code throws an exception into native, this will disable setting a descriptor for the wrapping CXX exception
     * This can hold a performance boost, if a lot of CXX exceptions are created
     */
    public static native void setDisableCXXExceptionMessage(boolean disable);

    public static native boolean reExportSymbolsGlobally(String libPath);

    public static <T extends Closure> long dispatchCallback(ClosureInfo<T> toCallOn, ByteBuffer parameter) {
        return toCallOn.invoke(parameter);
    }

    public static CTypeInfo getCTypeInfo(String name) {
        synchronized (cTypeInfoMap) {
            CTypeInfo cTypeInfo = cTypeInfoMap.get(name);
            if (cTypeInfo == null)
                throw new IllegalArgumentException("CType " + name + " is not registered.");
            return cTypeInfo;
        }
    }

    public static void registerCType(CTypeInfo cTypeInfo) {
        synchronized (cTypeInfoMap) {
            cTypeInfoMap.put(cTypeInfo.getName(), cTypeInfo);
        }
    }

    public static native long convertNativeTypeToFFIType(long nativeType);

    public static CTypeInfo constructStackElementCTypeFromNativeType(String name, long nativeType) {
        if (nativeType == 0)
            throw new IllegalArgumentException("CType " + name + " maps to zero.");
        long ffiType = convertNativeTypeToFFIType(nativeType);
        return new CTypeInfo(name, ffiType, CHandler.getSizeFromFFIType(ffiType), CHandler.getSignFromFFIType(ffiType), true, CHandler.isVoid(ffiType));
    }

    public static CTypeInfo constructCTypeFromNativeType(String name, long nativeType) {
        if (nativeType == 0)
            throw new IllegalArgumentException("CType " + name + " maps to zero.");
        long ffiType = convertNativeTypeToFFIType(nativeType);
        return new CTypeInfo(name, ffiType, CHandler.getSizeFromFFIType(ffiType), CHandler.getSignFromFFIType(ffiType), false, CHandler.isVoid(ffiType));
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
            return classCifMap.computeIfAbsent(signature, CHandler::generateFFICifForSignature);
        }
    }

    public static <T extends Closure> ClosureObject<T> createClosureForObject(T object) {
        long cif = getFFICifForSignature(object.functionSignature());
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8);
        byteBuffer.order(ByteOrder.nativeOrder());

        ClosureInfo<T> closureInfo = new ClosureInfo<>(cif, object);
        long fnPtr = createClosureForObject(cif, closureInfo, byteBuffer);
        long closurePtr = byteBuffer.getLong();

        ClosureObject<T> closureObject = new ClosureObject<>(fnPtr, closurePtr, false);
        synchronized (fnPtrClosureMap) {
            fnPtrClosureMap.put(fnPtr, closureObject);
        }
        return closureObject;
    }

    public static native <T extends Closure> long createClosureForObject(long cifArg, ClosureInfo<T> object, ByteBuffer closureRet);

    public static <T extends Closure> ClosureObject<T> getClosureObject(long fnPtr) {
        synchronized (fnPtrClosureMap) {
            //noinspection unchecked
            return (ClosureObject<T>)fnPtrClosureMap.get(fnPtr);
        }
    }

    public static native int getOffsetForField(long type_ptr, int index);

    public static native long getStackElementField(long pointer, long type_ptr, int index, boolean calculateOffset);

    public static void setStackElementField(long pointer, long type_ptr, int index, long value, boolean calculateOffset) {
        boolean res = setStackElement_internal(pointer, type_ptr, index, value, calculateOffset);
        if (!res)
            throw new IllegalArgumentException("Type " + value + " is out of valid bounds");
    }

    private static native boolean setStackElement_internal(long pointer, long type_ptr, int index, long value, boolean calculateOffset);

    public static native long getPointerPart(long pointer, int size, int offset);

    public static native void setPointerPart(long pointer, int size, int offset, long value);

    public static native void setPointerAsString(long pointer, String string);

    public static native String getPointerAsString(long pointer);

    public static native int getSizeFromFFIType(long type);

    public static native boolean getSignFromFFIType(long type);

    public static native boolean isStruct(long type);

    public static native boolean isVoid(long type);

    public static void freeClosure(ClosureObject<?> closureObject) {
        synchronized (fnPtrClosureMap) {
            fnPtrClosureMap.remove(closureObject.getFnPtr());
        }
        freeClosure(closureObject.getPointer());
    }


    private static native void freeClosure(long closurePtr);

    public static native long malloc(long size);

    public static native void free(long pointer);

    public static native void memcpy(long dst, long src, long size);

    public static native long clone(long src, long size);
}
