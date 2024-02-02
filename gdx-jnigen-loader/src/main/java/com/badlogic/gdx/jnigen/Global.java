package com.badlogic.gdx.jnigen;

import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.gdx.jnigen.ffi.ClosureInfo;
import com.badlogic.gdx.jnigen.ffi.ParameterTypes;
import com.badlogic.gdx.jnigen.pointer.Pointing;
import com.badlogic.gdx.jnigen.pointer.Struct;
import com.badlogic.gdx.jnigen.pointer.StructPointer;
import com.badlogic.gdx.jnigen.util.NewPointingSupplier;
import com.badlogic.gdx.jnigen.util.WrappingPointingSupplier;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
        POINTER_SIZE = getPointerSize();
    }

    public static void init() {
        // To force static initializer
    }

    public static final int POINTER_SIZE;

    public static native int getPointerSize();/*
        return sizeof(void*);
    */

    private static final HashMap<Class<? extends Closure>, Long> classCifMap = new HashMap<>();

    private static final HashMap<Class<? extends Struct>, Long> classStructFFITypeMap = new HashMap<>();

    private static final HashMap<Class<? extends Struct>, NewPointingSupplier<? extends StructPointer<?>>> classNewStructPointerMap = new HashMap<>();

    private static final HashMap<Class<? extends Pointing>, WrappingPointingSupplier<? extends Pointing>> classPointingSupplierMap = new HashMap<>();

    private static final HashMap<Class<? extends Struct>, WrappingPointingSupplier<? extends StructPointer<?>>> classStructPointerMap = new HashMap<>();

    private static final HashMap<String, Integer> cTypeSizeMap = new HashMap<>();

    /*JNI
    #include <stdlib.h>
    #include <string.h>
    #include <ffi.h>
    #include "definitions.h"


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
                    memcpy(backingBuffer + i, args[i],  type->size);
                }
            }
        }
        jlong ret = env->CallStaticLongMethod(globalClass, dispatchCallbackMethod, closureInfo, jBuffer);
        ffi_type* rtype = cif->rtype;
        if(rtype->type == FFI_TYPE_STRUCT) {
            memcpy(result, reinterpret_cast<void*>(ret), rtype->size);
        } else {
            memcpy(result, &ret, rtype->size); // Why does that work? That should not work?
        }
        DETACH_ENV()
    }

    */

    private static native boolean init(Method dispatchCallbackReflectedMethod);/*
        env->GetJavaVM(&gJVM);
        globalClass = (jclass)env->NewGlobalRef(clazz);
        dispatchCallbackMethod = env->FromReflectedMethod(dispatchCallbackReflectedMethod);
        if (dispatchCallbackMethod == NULL) {
            fprintf(stderr, "com.badlogic.gdx.jnigen.Global#dispatchCallback is not reachable via JNI\n");
            return JNI_FALSE;
        }
        return JNI_TRUE;
    */

    public static <T extends Closure> long dispatchCallback(ClosureInfo<T> toCallOn, ByteBuffer parameter) {
        try {
            return toCallOn.invoke(parameter);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getCTypeSize(String name) {
        synchronized (cTypeSizeMap) {
            Integer size = cTypeSizeMap.get(name);
            if (size == null)
                throw new IllegalArgumentException("CType " + name + " is not registered.");
            return size;
        }
    }

    public static void registerCTypeSize(String name, int size) {
        synchronized (cTypeSizeMap) {
            cTypeSizeMap.put(name, size);
        }
    }


    public static <T extends Struct, S extends StructPointer<T>> void registerStructPointer(Class<T> structClass, WrappingPointingSupplier<S> pointerClass) {
        synchronized (classStructPointerMap) {
            classStructPointerMap.put(structClass, pointerClass);
        }
    }

    public static <T extends Struct, S extends StructPointer<T>> WrappingPointingSupplier<S> getStructPointer(Class<T> structClass) {
        synchronized (classStructPointerMap) {
            //noinspection unchecked
            return (WrappingPointingSupplier<S>)classStructPointerMap.get(structClass);
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

    public static <T extends Struct, S extends StructPointer<T>> void registerNewStructPointerSupplier(Class<T> structClass, NewPointingSupplier<S> supplier) {
        synchronized (classNewStructPointerMap) {
            classNewStructPointerMap.put(structClass, supplier);
        }
    }

    public static <T extends Struct, S extends StructPointer<T>> NewPointingSupplier<S> getNewStructPointerSupplier(Class<T> structClass) {
        synchronized (classNewStructPointerMap) {
            //noinspection unchecked
            return (NewPointingSupplier<S>)classNewStructPointerMap.get(structClass);
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
        ffi_type** parameterFFITypes = (ffi_type**)malloc(sizeof(ffi_type*) * size);
        memcpy(parameterFFITypes, parameters, sizeof(ffi_type*) * size);

        ffi_cif* cif = (ffi_cif*)malloc(sizeof(ffi_cif));
        ffi_prep_cif(cif, FFI_DEFAULT_ABI, size, reinterpret_cast<ffi_type*>(returnType), parameterFFITypes);
        return reinterpret_cast<jlong>(cif);
    */

    private static long generateFFICifForClass(Class<? extends Closure> closureClass) {
        Method callingMethod = getMethodForClosure(closureClass);
        Class<?> returnType = callingMethod.getReturnType();

        Parameter[] parameters = callingMethod.getParameters();

        // Yes, I'm extremely lazy and don't want to deal with JNI array handling
        ByteBuffer mappedParameter = ByteBuffer.allocateDirect(parameters.length * 8);
        mappedParameter.order(ByteOrder.nativeOrder());
        for (Parameter parameter : parameters) {
            mappedParameter.putLong(ParameterTypes.mapToFFIType(parameter.getType(), parameter));
        }

        return nativeCreateCif(ParameterTypes.mapToFFIType(returnType, callingMethod), mappedParameter, parameters.length);
    }

    public static long getFFICifForClass(Class<? extends Closure> closureClass) {
        synchronized (classCifMap) {
            return classCifMap.computeIfAbsent(closureClass, Global::generateFFICifForClass);
        }
    }

    private static Method getMethodForClosure(Class<? extends Closure> closureClass) {
        Class<?>[] interfaces = closureClass.getInterfaces();
        if (interfaces.length != 1)
            throw new IllegalArgumentException("Closures are only allowed to implement one interface");

        Class<?> superClass = interfaces[0];
        Method[] methods = superClass.getDeclaredMethods();
        if (methods.length != 2)
            throw new IllegalArgumentException("Closures are only allowed to implement two methods");

        if (!methods[0].isDefault())
            return methods[0];
        else
            return methods[1];
    }

    public static <T extends Closure> ClosureObject<T> createClosureForObject(T object) {
        long cif = getFFICifForClass(object.getClass());
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8);
        byteBuffer.order(ByteOrder.nativeOrder());

        Method closureMethod = getMethodForClosure(object.getClass());
        ClosureInfo<T> closureInfo = new ClosureInfo<>(cif, closureMethod, object);
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

    /*JNI

    size_t getOffsetForField(ffi_type* struct_type, uint32_t index) {
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

    public static native long getStructField(long pointer, long type_ptr, int index);/*
        char* ptr = reinterpret_cast<char*>(pointer);
        ffi_type* struct_type = reinterpret_cast<ffi_type*>(type_ptr);
        uint32_t field = (uint32_t) index;

        size_t offset = getOffsetForField(struct_type, field);

        jlong ret = 0;
        memcpy(&ret, ptr + offset, struct_type->elements[field]->size); // Why does that work? That should not work?
        return ret;
    */

    public static native long setStructField(long pointer, long type_ptr, int index, long value);/*
        char* ptr = reinterpret_cast<char*>(pointer);
        ffi_type* struct_type = reinterpret_cast<ffi_type*>(type_ptr);
        uint32_t field = (uint32_t) index;
        uint64_t toSet = (uint64_t)value;

        size_t offset = getOffsetForField(struct_type, field);

        jlong ret = 0;
        memcpy(ptr + offset, &toSet, struct_type->elements[field]->size); // Why does that work? That should not work?
        return ret;
    */

    public static native long getSizeFromFFIType(long type);/*
        return reinterpret_cast<ffi_type*>(type)->size;
    */

    // TODO: Add support for specific alignment
    public static native void calculateAlignmentAndSizeForType(long type);/*
        ffi_type* struct_type = reinterpret_cast<ffi_type*>(type);
        int index = 0;
        ffi_type* current_element = struct_type->elements[index];
        size_t struct_size = 0;
        size_t struct_alignment = 0;
        while (current_element != NULL) {
            size_t alignment = current_element->alignment;
            if (alignment > struct_alignment)
                struct_alignment = alignment;

            if (struct_size % alignment != 0) {
                struct_size += alignment - (struct_size % alignment);
            }

            struct_size += current_element->size;

            index++;
            current_element = struct_type->elements[index];
        }

        if (struct_size % struct_alignment != 0) {
           struct_size += struct_alignment - (struct_size % struct_alignment);
        }

        struct_type->alignment = struct_alignment;
        struct_type->size = struct_size;
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

    public static native long clone(long src, long size);/*
        void* dst = malloc(size);
        memcpy((void*)dst, (void*)src, size);
        return reinterpret_cast<jlong>(dst);
    */
}
