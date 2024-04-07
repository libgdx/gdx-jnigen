package com.badlogic.gdx.jnigen;

import com.badlogic.gdx.jnigen.c.CTypeInfo;
import com.badlogic.gdx.jnigen.c.CXXException;
import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.gdx.jnigen.ffi.ClosureInfo;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

public class CHandler {

    static {
        new SharedLibraryLoader().load("jnigen-native");
        try {
            boolean res = init(CHandler.class.getDeclaredMethod("dispatchCallback", ClosureInfo.class, ByteBuffer.class),
                    IllegalArgumentException.class, CXXException.class);
            if (!res)
                throw new RuntimeException("JNI initialization failed.");
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

    private static native int getPointerSize();/*
        return sizeof(void*);
    */

    private static final HashMap<CTypeInfo[], Long> classCifMap = new HashMap<>();

    private static final HashMap<String, CTypeInfo> cTypeInfoMap = new HashMap<>();

    private static final HashMap<Long, ClosureObject<?>> fnPtrClosureMap = new HashMap<>();

    /*JNI
    #include <stdlib.h>
    #include <string.h>
    #include <ffi.h>
    #include <jnigen.h>
#ifdef __linux__
    #include <dlfcn.h>
#endif // __linux__


    #define ATTACH_ENV()                                                    \
        bool _hadToAttach = false;                                          \
        JNIEnv* env;                                                        \
        if (gJVM->GetEnv((void**)&env, JNI_VERSION_1_6) == JNI_EDETACHED) { \
            gJVM->AttachCurrentThread((void**)&env, NULL);                  \
            _hadToAttach = true;                                            \
        }

    #define DETACH_ENV()                 \
        if (_hadToAttach) {              \
            gJVM->DetachCurrentThread(); \
        }

    #ifdef __BYTE_ORDER__
        #if __BYTE_ORDER__ == __ORDER_LITTLE_ENDIAN__
            #define ENDIAN_INTCPY(dest, dest_size, src, src_size) memcpy(dest, src, dest_size > src_size ? src_size : dest_size)
        #elif __BYTE_ORDER__ == __ORDER_BIG_ENDIAN__
            #error Big endian is currently unsupported
        #else
            #error Could not determine byte order
        #endif
    #else
        #error Could not determine byte order
    #endif

    jmethodID dispatchCallbackMethod = NULL;
    jclass globalClass = NULL;
    jclass illegalArgumentExceptionClass = NULL;
    jclass cxxExceptionClass = NULL;
    JavaVM* gJVM = NULL;

    void callbackHandler(ffi_cif* cif, void* result, void** args, void* user) {
        ATTACH_ENV()
        jobject closureInfo = (jobject) user;
        void* backingBuffer[cif->nargs * sizeof(void*)];
        jobject jBuffer = NULL;
        if (cif->nargs != 0) {
            jBuffer = env->NewDirectByteBuffer(backingBuffer, cif->nargs * sizeof(void*));
            for (size_t i = 0; i < cif->nargs; i++) {
                ffi_type* type = cif->arg_types[i];
                if(type->type == FFI_TYPE_STRUCT) {
                    void* structBuf = malloc(type->size);
                    memcpy(structBuf, args[i], type->size);
                    backingBuffer[i] = structBuf;
                } else {
                    ENDIAN_INTCPY(backingBuffer + i, sizeof(void*), args[i], type->size);
                }
            }
        }
        jlong ret = env->CallStaticLongMethod(globalClass, dispatchCallbackMethod, closureInfo, jBuffer);

        if(env->ExceptionCheck() == JNI_TRUE)
            throw JavaExceptionMarker();

        ffi_type* rtype = cif->rtype;
        if(rtype->type == FFI_TYPE_STRUCT) {
            memcpy(result, reinterpret_cast<void*>(ret), rtype->size);
        } else {
            ENDIAN_INTCPY(result, rtype->size, &ret, sizeof(jlong));
        }
        DETACH_ENV()
    }

    JavaExceptionMarker::JavaExceptionMarker() : std::runtime_error("Java Exception") {}

    const char* JavaExceptionMarker::what() const noexcept {
        ATTACH_ENV()
        env->ExceptionDescribe();
        DETACH_ENV()
        // TODO: Implement at some point properly
        return std::runtime_error::what();
    }

    void throwIllegalArgumentException(JNIEnv* env, const char* message) {
        env->ThrowNew(illegalArgumentExceptionClass, message);
    }

    void throwCXXException(JNIEnv* env, const char* message) {
        env->ThrowNew(cxxExceptionClass, message);
    }
    */

    private static native boolean init(Method dispatchCallbackReflectedMethod, Class illegalArgumentException, Class cxxException);/*
        env->GetJavaVM(&gJVM);
        globalClass = (jclass)env->NewGlobalRef(clazz);
        dispatchCallbackMethod = env->FromReflectedMethod(dispatchCallbackReflectedMethod);
        if (dispatchCallbackMethod == NULL) {
            fprintf(stderr, "com.badlogic.gdx.jnigen.Global#dispatchCallback is not reachable via JNI\n");
            return JNI_FALSE;
        }
        illegalArgumentExceptionClass = (jclass)env->NewGlobalRef(illegalArgumentException);
        cxxExceptionClass = (jclass)env->NewGlobalRef(cxxException);
        return JNI_TRUE;
    */

    private static void testNativeSetup() {
        try {
            testIllegalArgumentExceptionThrowable();
            throw new RuntimeException("Unable to throw IllegalArgumentException from JNI.");
        }catch (IllegalArgumentException ignored) {}
        try {
            testCXXExceptionThrowable();
            throw new RuntimeException("Unable to throw CXXException from JNI.");
        }catch (CXXException ignored) {}
    }

    private static native void testIllegalArgumentExceptionThrowable();/*
        throwIllegalArgumentException(env, "Test");
    */

    private static native void testCXXExceptionThrowable();/*
        throwCXXException(env, "Test");
    */

    public static native boolean reExportSymbolsGlobally(String libPath);/*
#ifdef __linux__
        void* handle = dlopen(libPath, RTLD_NOW | RTLD_GLOBAL);
        if (handle == NULL)
            printf("Error: %s\n", dlerror());
        return handle != NULL;
#else
        return JNI_TRUE;
#endif // __linux__
    */

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

    public static CTypeInfo constructStackElementCTypeFromFFIType(String name, long ffiType, boolean isStruct) {
        return new CTypeInfo(name, ffiType, CHandler.getSizeFromFFIType(ffiType), CHandler.getSignFromFFIType(ffiType), true, CHandler.isVoid(ffiType));
    }

    public static CTypeInfo constructCTypeFromFFIType(String name, long ffiType) {
        return new CTypeInfo(name, ffiType, CHandler.getSizeFromFFIType(ffiType), CHandler.getSignFromFFIType(ffiType), false, CHandler.isVoid(ffiType));
    }

    private static native long nativeCreateCif(long returnType, ByteBuffer parameters, int size); /*
        ffi_type** parameterFFITypes = (ffi_type**)malloc(sizeof(ffi_type*) * size);
        memcpy(parameterFFITypes, parameters, sizeof(ffi_type*) * size);

        ffi_cif* cif = (ffi_cif*)malloc(sizeof(ffi_cif));
        ffi_prep_cif(cif, FFI_DEFAULT_ABI, size, reinterpret_cast<ffi_type*>(returnType), parameterFFITypes);
        return reinterpret_cast<jlong>(cif);
    */

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

    public static native <T extends Closure> long createClosureForObject(long cif, ClosureInfo<T> object, ByteBuffer closureRet);/*
        jobject toCallOn = env->NewGlobalRef(object);
        void* fnPtr;
        ffi_closure* closure = (ffi_closure*)ffi_closure_alloc(sizeof(ffi_closure), &fnPtr);

        ffi_prep_closure_loc(closure, (ffi_cif*)cif, callbackHandler, toCallOn, fnPtr);
        *((ffi_closure**) closureRet) = closure;
        return reinterpret_cast<jlong>(fnPtr);
    */

    public static <T extends Closure> ClosureObject<T> getClosureObject(long fnPtr) {
        synchronized (fnPtrClosureMap) {
            //noinspection unchecked
            return (ClosureObject<T>)fnPtrClosureMap.get(fnPtr);
        }
    }

    /*JNI
    static size_t getOffsetForField(ffi_type* struct_type, uint32_t index) {
        size_t offset = 0;

        for (size_t i = 0; i <= index; i++) {
            ffi_type* current_element = struct_type->elements[i];
            size_t alignment = current_element->alignment;
            if (offset % alignment != 0) {
                offset += alignment - (offset % alignment);
            }
            if (i != index)
                offset += current_element->size;
        }

        return offset;
    }
    */

    public static native int getOffsetForField(long type_ptr, int index);/*
        return getOffsetForField(reinterpret_cast<ffi_type*>(type_ptr), (uint32_t) index);
    */

    public static native long getStackElementField(long pointer, long type_ptr, int index, boolean calculateOffset);/*
        char* ptr = reinterpret_cast<char*>(pointer);
        ffi_type* struct_type = reinterpret_cast<ffi_type*>(type_ptr);
        uint32_t field = (uint32_t) index;

        size_t offset = calculateOffset ? getOffsetForField(struct_type, field) : 0;

        jlong ret = 0;
        ENDIAN_INTCPY(&ret, sizeof(jlong), ptr + offset, struct_type->elements[field]->size);
        return ret;
    */

    public static void setStackElementField(long pointer, long type_ptr, int index, long value, boolean calculateOffset) {
        boolean res = setStackElement_internal(pointer, type_ptr, index, value, calculateOffset);
        if (!res)
            throw new IllegalArgumentException("Type " + value + " is out of valid bounds");
    }

    private static native boolean setStackElement_internal(long pointer, long type_ptr, int index, long value, boolean calculateOffset);/*
        char* ptr = reinterpret_cast<char*>(pointer);
        ffi_type* struct_type = reinterpret_cast<ffi_type*>(type_ptr);
        uint32_t field = (uint32_t) index;

        bool valid_bounds = CHECK_BOUNDS_FFI_TYPE(struct_type->elements[field], value);
        if(!valid_bounds) {
            return false;
        }

        size_t offset = calculateOffset ? getOffsetForField(struct_type, field) : 0;

        ENDIAN_INTCPY(ptr + offset, struct_type->elements[field]->size, &value, sizeof(jlong));
        return true;
    */

    public static native long getPointerPart(long pointer, int size, int offset);/*
        char* ptr = reinterpret_cast<char*>(pointer);
        jlong ret = 0;
        ENDIAN_INTCPY(&ret, sizeof(jlong), ptr + offset, size);
        return ret;
    */

    public static native void setPointerPart(long pointer, int size, int offset, long value);/*
        char* ptr = reinterpret_cast<char*>(pointer);
        ENDIAN_INTCPY(ptr + offset, size, &value, sizeof(jlong));
    */

    public static native int getSizeFromFFIType(long type);/*
        return reinterpret_cast<ffi_type*>(type)->size;
    */

    public static native boolean getSignFromFFIType(long type);/*
        return (jboolean)(GET_FFI_TYPE_SIGN((ffi_type*)type));
    */

    public static native boolean isStruct(long type);/*
        return ((ffi_type*)type)->type == FFI_TYPE_STRUCT;
    */

    public static native boolean isVoid(long type);/*
        return ((ffi_type*)type)->type == FFI_TYPE_VOID;
    */

    public static void freeClosure(ClosureObject<?> closureObject) {
        synchronized (fnPtrClosureMap) {
            fnPtrClosureMap.remove(closureObject.getFnPtr());
        }
        freeClosure(closureObject.getPointer());
    }


    private static native void freeClosure(long closurePtr);/*
        ffi_closure* closure = (ffi_closure*) closurePtr;
        env->DeleteGlobalRef((jobject)closure->user_data);
        ffi_closure_free(closure);
    */

    public static native long malloc(long size);/*
        return reinterpret_cast<jlong>(malloc(size));
    */

    public static native void free(long pointer);/*
        free((void*)pointer);
    */

    public static native void memcpy(long dst, long src, long size);/*
        memcpy((void*)dst, (void*)src, size);
    */

    public static native long clone(long src, long size);/*
        void* dst = malloc(size);
        memcpy((void*)dst, (void*)src, size);
        return reinterpret_cast<jlong>(dst);
    */
}
