package com.badlogic.gdx.jnigen;

import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.gdx.jnigen.ffi.ParameterTypes;
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
            init(Global.class.getDeclaredMethod("dispatchCallback", Object.class, Object[].class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static final HashMap<Class<? extends Closure>, Long> classCifMap = new HashMap<>();

    /*JNI
    #include <stdlib.h>
    #include <string.h>
    #include <ffi.h>

    #define ATTACH_ENV()                                                    \
        bool _hadToAttach = false;                                          \
        JNIEnv* env;                                                        \
        if (gJVM->GetEnv((void**)&env, JNI_VERSION_1_6) == JNI_EDETACHED) { \
            gJVM->AttachCurrentThread((void**)&env, NULL);                          \
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
        jobject toCallOn = (jobject) user;
        // jobjectArray array = env->NewObjectArray(0, );
        env->CallStaticVoidMethod(globalClass, dispatchCallbackMethod, toCallOn, NULL);
        DETACH_ENV()
    }

    */

    public static native void init(Method dispatchCallbackReflectedMethod);/*
        env->GetJavaVM(&gJVM);
        dispatchCallbackMethod = env->FromReflectedMethod(dispatchCallbackReflectedMethod);
        globalClass = (jclass)env->NewGlobalRef(clazz);
    */

    public static void dispatchCallback(Object toCallOn, Object[] parameter) {
        try {
            // Catch method
            Method toCall = toCallOn.getClass().getDeclaredMethods()[0];
            toCall.setAccessible(true);
            toCall.invoke(toCallOn, parameter);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static native long nativeCreateCif(long returnType, ByteBuffer parameters, int size); /*
        uint64_t* params = (uint64_t*) parameters;
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
        return (jlong)cif;
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
        long fnPtr = createClosureForObject(cif, object, byteBuffer);
        long closurePtr = byteBuffer.getLong();

        return new ClosureObject<>(fnPtr, closurePtr, false);
    }

    public static native <T extends Closure> long createClosureForObject(long cif, T object, ByteBuffer closureRet);/*
        jobject toCallOn = env->NewGlobalRef(object);
        void* fnPtr;
        ffi_closure* closure = (ffi_closure*)ffi_closure_alloc(sizeof(ffi_closure), &fnPtr);

        ffi_prep_closure_loc(closure, (ffi_cif*)cif, callbackHandler, toCallOn, fnPtr);
        *((ffi_closure**) closureRet) = closure;
        return (jlong)fnPtr;
    */

    public static native void freeClosure(long closurePtr);/*
        ffi_closure* closure = (ffi_closure*) closurePtr;
        env->DeleteGlobalRef((jobject)closure->user_data);
        ffi_closure_free(closure);
    */

    public static native long malloc(long size);/*
        return (jlong)malloc(size);
    */

    public static native long calloc(long size);/*
        return (jlong)calloc(1, size);
    */

    public static native void free(long pointer);/*
        free((void*)pointer);
    */

    public static native void memcpy(long dst, long src, long size);/*
        memcpy((void*)dst, (void*)src, size);
    */
}
