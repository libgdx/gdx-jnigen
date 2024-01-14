package com.badlogic.gdx.jnigen;

import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.gdx.jnigen.ffi.ClosureInfo;
import com.badlogic.gdx.jnigen.ffi.ParameterTypes;
import com.badlogic.gdx.jnigen.pointer.Pointing;
import com.badlogic.gdx.jnigen.util.WrappingPointingSupplier;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

public class Global {

    static {
        new SharedLibraryLoader("build/libs/gdx-jnigen-loader-2.5.1-SNAPSHOT-natives-desktop.jar").load("jnigen-native");
        try {
            boolean res = init(Global.class.getDeclaredMethod("dispatchCallback", ClosureInfo.class, ByteBuffer.class));
            if (!res)
                throw new RuntimeException("JNI initialization failed.");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static final HashMap<Class<? extends Closure>, Long> classCifMap = new HashMap<>();

    private static final HashMap<Class<? extends Struct>, Long> classStructFFITypeMap = new HashMap<>();

    private static final HashMap<Class<? extends Pointing>, WrappingPointingSupplier<? extends Pointing>> classPointingSupplierMap = new HashMap<>();

    /*JNI
    #include <stdlib.h>
    #include <string.h>
    #include <ffi.h>

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

    jmethodID dispatchCallbackMethod = NULL;
    jclass globalClass = NULL;
    JavaVM* gJVM = NULL;

    void callbackHandler(ffi_cif* cif, void* result, void** args, void* user) {
        ATTACH_ENV()
        jobject closureInfo = (jobject) user;
        void* backingBuffer[cif->nargs * 8];
        jobject jBuffer = NULL;
        if (cif->nargs != 0) {
            jBuffer = env->NewDirectByteBuffer(backingBuffer, cif->nargs * 8);
            for (int i = 0; i < cif->nargs; i++) {
                ffi_type* type = cif->arg_types[i];
                if(type->type == FFI_TYPE_STRUCT) {
                    void* structBuf = malloc(type->size);
                    memcpy(structBuf, args[i], type->size);
                    backingBuffer[i] = structBuf;
                } else {
                    memcpy(backingBuffer + i, args[i],  type->size);
                }
            }
        }
        env->CallStaticVoidMethod(globalClass, dispatchCallbackMethod, closureInfo, jBuffer);
        DETACH_ENV()
    }

    */

    public static native boolean init(Method dispatchCallbackReflectedMethod);/*
        env->GetJavaVM(&gJVM);
        globalClass = (jclass)env->NewGlobalRef(clazz);
        dispatchCallbackMethod = env->FromReflectedMethod(dispatchCallbackReflectedMethod);
        if (dispatchCallbackMethod == NULL) {
            fprintf(stderr, "com.badlogic.gdx.jnigen.Global#dispatchCallback is not reachable via JNI\n");
            return JNI_FALSE;
        }
        return JNI_TRUE;
    */

    public static <T extends Closure> void dispatchCallback(ClosureInfo<T> toCallOn, ByteBuffer parameter) {
        try {
            toCallOn.invoke(parameter);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerStructFFIType(Class<? extends Struct> structClass, long ffiType) {
        synchronized (classStructFFITypeMap) {
            classStructFFITypeMap.put(structClass, ffiType);
        }
    }

    public static long getStructFFIType(Class<? extends Struct> structClass) {
        synchronized (classStructFFITypeMap) {
            return classStructFFITypeMap.getOrDefault(structClass, 0L);
        }
    }

    public static <T extends Pointing> void registerPointingSupplier(Class<T> toRegister, WrappingPointingSupplier<T> supplier) {
        synchronized (classPointingSupplierMap) {
            classPointingSupplierMap.put(toRegister, supplier);
        }
    }

    public static <T extends Pointing> WrappingPointingSupplier<T> getPointingSupplier(Class<T> toGet) {
        synchronized (classPointingSupplierMap) {
            //noinspection unchecked
            return (WrappingPointingSupplier<T>)classPointingSupplierMap.get(toGet);
        }
    }

    private static native long nativeCreateCif(long returnType, ByteBuffer parameters, int size); /*
        int64_t* params = (int64_t*) parameters;
        ffi_type** parameterFFITypes = (ffi_type**)malloc(sizeof(ffi_type*) * size);
        for (int i = 0; i < size; i++) {
            switch (params[i]) {
                case -1:
                    parameterFFITypes[i] = &ffi_type_uint8;
                    break;
                case -2:
                    parameterFFITypes[i] = &ffi_type_uint16;
                    break;
                case -3:
                    parameterFFITypes[i] = &ffi_type_uint32;
                    break;
                case -4:
                    parameterFFITypes[i] = &ffi_type_uint64;
                    break;
                case -5:
                    parameterFFITypes[i] = &ffi_type_float;
                    break;
                case -6:
                    parameterFFITypes[i] = &ffi_type_double;
                    break;
                default:
                    parameterFFITypes[i] = ((ffi_type**)params)[i];
            }
        }

        ffi_cif* cif = (ffi_cif*)malloc(sizeof(ffi_cif));
        ffi_prep_cif(cif, FFI_DEFAULT_ABI, size, &ffi_type_void, parameterFFITypes);
        return reinterpret_cast<jlong>(cif);
    */

    private static long generateFFICifForClass(Class<? extends Closure> closureClass) {
        Method[] methods = closureClass.getDeclaredMethods();
        if (methods.length != 1)
            throw new IllegalArgumentException("Closures are only allowed to implement one function");
        Method callingMethod = methods[0];
        if (callingMethod.getReturnType() != void.class)
            throw new IllegalArgumentException("Closures are currently only allowed to have a Void return");
        Class<?>[] parameters = callingMethod.getParameterTypes();

        // Yes, I'm extremely lazy and don't want to deal with JNI array handling
        ByteBuffer mappedParameter = ByteBuffer.allocateDirect(parameters.length * 8);
        mappedParameter.order(ByteOrder.nativeOrder());
        for (Class<?> parameter : parameters) {
            mappedParameter.putLong(ParameterTypes.mapObjectToID(parameter));
        }

        return nativeCreateCif(0, mappedParameter, parameters.length);
    }

    private static long getFFICifForClass(Class<? extends Closure> closureClass) {
        synchronized (classCifMap) {
            return classCifMap.computeIfAbsent(closureClass, Global::generateFFICifForClass);
        }
    }

    public static <T extends Closure> ClosureObject<T> createClosureForObject(T object) {
        long cif = getFFICifForClass(object.getClass());
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8);
        byteBuffer.order(ByteOrder.nativeOrder());
        ClosureInfo<T> closureInfo = new ClosureInfo<>(cif, object.getClass().getDeclaredMethods()[0], object);
        long fnPtr = createClosureForObject(cif, closureInfo, byteBuffer);
        long closurePtr = byteBuffer.getLong();

        return new ClosureObject<>(fnPtr, closurePtr, false);
    }

    public static native <T extends Closure> long createClosureForObject(long cif, ClosureInfo<T> object, ByteBuffer closureRet);/*
        jobject toCallOn = env->NewGlobalRef(object);
        void* fnPtr;
        ffi_closure* closure = (ffi_closure*)ffi_closure_alloc(sizeof(ffi_closure), &fnPtr);

        ffi_prep_closure_loc(closure, (ffi_cif*)cif, callbackHandler, toCallOn, fnPtr);
        *((ffi_closure**) closureRet) = closure;
        return reinterpret_cast<jlong>(fnPtr);
    */

    public static native void freeClosure(long closurePtr);/*
        ffi_closure* closure = (ffi_closure*) closurePtr;
        env->DeleteGlobalRef((jobject)closure->user_data);
        ffi_closure_free(closure);
    */

    public static native long malloc(long size);/*
        return reinterpret_cast<jlong>(malloc(size));
    */

    public static native long calloc(long size);/*
        return reinterpret_cast<jlong>(calloc(1, size));
    */

    public static native void free(long pointer);/*
        free((void*)pointer);
    */

    public static native void memcpy(long dst, long src, long size);/*
        memcpy((void*)dst, (void*)src, size);
    */
}
