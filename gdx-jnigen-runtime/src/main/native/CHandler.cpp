#include <stdlib.h>
#include <string.h>
#include <ffi.h>
#include <jnigen.h>
#include <jni_env_tls.h>

#ifdef __linux__
#include <dlfcn.h>
#endif // __linux__

#include <CHandler.h>

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

#define GET_FFI_TYPE_SIGN(type) \
    &ffi_type_sint8 == type || &ffi_type_sint16 == type || &ffi_type_sint32 == type || &ffi_type_sint64 == type || &ffi_type_double == type || &ffi_type_float == type

#define CHECK_BOUNDS_FFI_TYPE(type, value) \
    CHECK_BOUNDS_FOR_NUMBER(value, type->size, GET_FFI_TYPE_SIGN(type));

static jmethodID dispatchCallbackMethod = NULL;
static jmethodID getExceptionStringMethod = NULL;
static jclass globalClass = NULL;
static jclass cxxExceptionClass = NULL;
static JavaVM* gJVM = NULL;
static bool ignoreCXXExceptionMessage = false;

typedef struct _closure_info {
    jobject javaInfo;
    size_t argumentSize;
} closure_info;

// We do inline to silence compiler warnings, but it shouldn't actually inline
static inline void calculateAlignmentAndOffset(ffi_type* type, bool isStruct) {
    int index = 0;
    ffi_type* current_element = type->elements[index];
    size_t struct_size = 0;
    size_t struct_alignment = 0;
    while (current_element != NULL) {
        size_t alignment = current_element->alignment;
        if (alignment > struct_alignment)
            struct_alignment = alignment;

        if (isStruct) {
            if (struct_size % alignment != 0) {
                struct_size += alignment - (struct_size % alignment);
            }
            struct_size += current_element->size;
        } else {
            if (current_element->size > struct_size) {
                struct_size = current_element->size;
            }
        }

        index++;
        current_element = type->elements[index];
    }

    if (isStruct && struct_alignment != 0 && struct_size % struct_alignment != 0) {
       struct_size += struct_alignment - (struct_size % struct_alignment);
    }

    type->alignment = struct_alignment;
    type->size = struct_size;
}

void callbackHandler(ffi_cif* cif, void* result, void** args, void* user) {
    JNIEnv* env;
    tls_attach_jni_env(gJVM, &env);
    closure_info* info = (closure_info*) user;
    ffi_type* rtype = cif->rtype;
    char backingBuffer[info->argumentSize > rtype->size ? info->argumentSize : rtype->size];
    if (cif->nargs != 0) {
        size_t offset = 0;
        for (size_t i = 0; i < cif->nargs; i++) {
            ffi_type* type = cif->arg_types[i];
            if(type->type == FFI_TYPE_STRUCT) {
                void* structBuf = malloc(type->size);
                memcpy(structBuf, args[i], type->size);
                *(void**)(backingBuffer + offset) = structBuf;
                offset += sizeof(void*);
            } else {
                memcpy(backingBuffer + offset, args[i], type->size);
                offset += type->size;
            }
        }
    }

    env->CallStaticVoidMethod(globalClass, dispatchCallbackMethod, info->javaInfo, backingBuffer);

    jthrowable exc = env->ExceptionOccurred();
    if(exc != NULL) {
        env->ExceptionClear();
        if (ignoreCXXExceptionMessage)
            throw JavaExceptionMarker(gJVM, exc, "Java-Side exception");
        jstring message = (jstring)env->CallStaticObjectMethod(globalClass, getExceptionStringMethod, exc);
        const char* cStringMessage = env->GetStringUTFChars(message, NULL);
        std::string cxxMessage(cStringMessage);
        env->ReleaseStringUTFChars(message, cStringMessage);
        throw JavaExceptionMarker(gJVM, exc, cxxMessage);
    }

    if(rtype->type == FFI_TYPE_STRUCT) {
        memcpy(result, *(void**)backingBuffer, rtype->size);
    } else {
        memcpy(result, &backingBuffer, rtype->size);
    }
}

JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_dispatchCCall(JNIEnv* env, jclass clazz, jlong fnPtr_j, jlong cif_j, jlong arg_buf) {
    HANDLE_JAVA_EXCEPTION_START()

    void** arguments = (void**)arg_buf;
    void* fnPtr = (void*) fnPtr_j;
    ffi_cif* cif = (ffi_cif*) cif_j;

    void** decodedArguments = (void**)alloca(cif->nargs * (sizeof(void*)));
    int offset = 0;
    for (int i = 0; i < cif->nargs; ++i) {
        ffi_type* arg = cif->arg_types[i];
        if (arg->type == FFI_TYPE_STRUCT) {
            decodedArguments[i] = arguments[i];
        } else {
            decodedArguments[i] = alloca(arg->size);
            memcpy(decodedArguments[i], (char*)arguments + offset, arg->size);
        }

        offset += arg->size;
    }

    void* result = (void*)alloca(cif->rtype->size);
    ffi_call(cif, (void (*)())fnPtr, result, decodedArguments);

    ffi_type* rtype = cif->rtype;

    if (rtype->type == FFI_TYPE_VOID)
        return;
    if(rtype->type == FFI_TYPE_STRUCT) {
        memcpy(*(void**)(arguments + offset), result, rtype->size);
    } else {
        memcpy(arguments + offset, result, rtype->size);
    }

    HANDLE_JAVA_EXCEPTION_END()
}

JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_is32Bit(JNIEnv* env, jclass clazz) {
    return ARCH_BITS == 32;
}

JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_isCompiledWin(JNIEnv* env, jclass clazz) {
    #if defined(_WIN32)
        return true;
    #else
        return false;
    #endif
}

JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_isCharSigned(JNIEnv* env, jclass clazz) {
    return IS_SIGNED_TYPE(char);
}

JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_isCompiledAndroidX86(JNIEnv* env, jclass clazz) {
    #if defined(__i386__) && defined(__ANDROID__)
        return true;
    #else
        return false;
    #endif
}

JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_init(JNIEnv* env, jclass clazz, jobject dispatchCallbackReflectedMethod, jobject getExceptionStringReflectedMethod) {
    env->GetJavaVM(&gJVM);
    globalClass = (jclass)env->NewGlobalRef(clazz);
    dispatchCallbackMethod = env->FromReflectedMethod(dispatchCallbackReflectedMethod);
    if (dispatchCallbackMethod == NULL) {
        fprintf(stderr, "com.badlogic.gdx.jnigen.runtime.Global#dispatchCallback is not reachable via JNI\n");
        return JNI_FALSE;
    }
    getExceptionStringMethod = env->FromReflectedMethod(getExceptionStringReflectedMethod);
    if (getExceptionStringMethod == NULL) {
        fprintf(stderr, "com.badlogic.gdx.jnigen.runtime.Global#getExceptionStringMethod is not reachable via JNI\n");
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_testIllegalArgumentExceptionThrowable(JNIEnv* env, jclass clazz, jclass illegalArgumentException) {
    env->ThrowNew(illegalArgumentException, "Test");
}

JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_testCXXExceptionThrowable(JNIEnv* env, jclass clazz, jclass cxxException) {
    env->ThrowNew(cxxException, "Test");
    cxxExceptionClass = (jclass)env->NewGlobalRef(cxxException);
}

JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_setDisableCXXExceptionMessage(JNIEnv* env, jclass clazz, jboolean disable) {
    ignoreCXXExceptionMessage = disable;
}

JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_reExportSymbolsGlobally(JNIEnv* env, jclass clazz, jstring obj_libPath) {
#ifdef __linux__
	char* libPath = (char*)env->GetStringUTFChars(obj_libPath, 0);

    void* handle = dlopen(libPath, RTLD_NOW | RTLD_GLOBAL);
    if (handle == NULL)
        printf("Error: %s\n", dlerror());
    env->ReleaseStringUTFChars(obj_libPath, libPath);

	return handle != NULL;
#else
    return JNI_TRUE;
#endif // __linux__
}

ffi_type* getFFITypeForNativeType(native_type* nativeType) {
    switch (nativeType->type) {
        case VOID_TYPE:
            return &ffi_type_void;
        case POINTER_TYPE:
            return &ffi_type_pointer;
        case FLOAT_TYPE:
            return &ffi_type_float;
        case DOUBLE_TYPE:
            return &ffi_type_double;
        case INT_TYPE:
            switch (nativeType->size) {
                case 1:
                    return nativeType->sign ? &ffi_type_sint8 : &ffi_type_uint8;
                case 2:
                    return nativeType->sign ? &ffi_type_sint16 : &ffi_type_uint16;
                case 4:
                    return nativeType->sign ? &ffi_type_sint32 : &ffi_type_uint32;
                case 8:
                    return nativeType->sign ? &ffi_type_sint64 : &ffi_type_uint64;
            }
            return NULL;
        case UNION_TYPE:
        case STRUCT_TYPE:
            ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
            type->type = FFI_TYPE_STRUCT;
            type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * (nativeType->field_count + 1));

            for (int i = 0; i < nativeType->field_count; i++) {
                type->elements[i] = getFFITypeForNativeType(nativeType->fields[i]);
            }

            type->elements[nativeType->field_count] = NULL;
            calculateAlignmentAndOffset(type, nativeType->type == STRUCT_TYPE);
            return type;
    }
    return NULL;
}

void freeNativeTypeRecursive(native_type* nativeType) {
    if (nativeType->type == UNION_TYPE || nativeType->type == STRUCT_TYPE) {
        for (int i = 0; i < nativeType->field_count; i++) {
            freeNativeTypeRecursive(nativeType->fields[i]);
        }
    }

    free(nativeType);
}

JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_convertNativeTypeToFFIType(JNIEnv* env, jclass clazz, jlong natTypeJ) {
    native_type* nativeType = (native_type*) natTypeJ;
    ffi_type* ffiType = getFFITypeForNativeType(nativeType);
    freeNativeTypeRecursive(nativeType);
    return (jlong) ffiType;
}

JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_nativeCreateCif(JNIEnv* env, jclass clazz, jlong returnType, jobject obj_parameters, jint size) {
	char* parameters = (char*)(obj_parameters?env->GetDirectBufferAddress(obj_parameters):0);

    ffi_type** parameterFFITypes = (ffi_type**)malloc(sizeof(ffi_type*) * size);
    memcpy(parameterFFITypes, parameters, sizeof(ffi_type*) * size);

    ffi_cif* cif = (ffi_cif*)malloc(sizeof(ffi_cif));
    ffi_prep_cif(cif, FFI_DEFAULT_ABI, size, reinterpret_cast<ffi_type*>(returnType), parameterFFITypes);
    return reinterpret_cast<jlong>(cif);
}

JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_createClosureForObject(JNIEnv* env, jclass clazz, jlong cifArg, jobject object, jobject obj_closureRet) {
	char* closureRet = (char*)(obj_closureRet?env->GetDirectBufferAddress(obj_closureRet):0);

    ffi_cif* cif = (ffi_cif*)cifArg;
    size_t argsSize = 0;
    for (size_t i = 0; i < cif->nargs; i++) {
        ffi_type* type = cif->arg_types[i];
        if(type->type == FFI_TYPE_STRUCT) {
            argsSize += sizeof(void*);
        } else {
            argsSize += type->size;
        }
    }


    jobject toCallOn = env->NewGlobalRef(object);
    closure_info* info = (closure_info*)malloc(sizeof(closure_info));
    info->javaInfo = toCallOn;
    info->argumentSize = argsSize;

    void* fnPtr;
    ffi_closure* closure = (ffi_closure*)ffi_closure_alloc(sizeof(ffi_closure), &fnPtr);

    ffi_prep_closure_loc(closure, cif, callbackHandler, info, fnPtr);
    *((ffi_closure**) closureRet) = closure;
    return reinterpret_cast<jlong>(fnPtr);
}

JNIEXPORT jint JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_getSizeFromFFIType(JNIEnv* env, jclass clazz, jlong type) {
    return reinterpret_cast<ffi_type*>(type)->size;
}

JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_freeClosure(JNIEnv* env, jclass clazz, jlong closurePtr) {
    ffi_closure* closure = (ffi_closure*) closurePtr;
    env->DeleteGlobalRef(((closure_info*)closure->user_data)->javaInfo);
    free(closure->user_data);
    ffi_closure_free(closure);
}

JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_malloc(JNIEnv* env, jclass clazz, jlong size) {
    return reinterpret_cast<jlong>(malloc(size));
}

JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_calloc(JNIEnv* env, jclass clazz, jlong count, jlong size) {
    return reinterpret_cast<jlong>(calloc(count, size));
}

JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_free(JNIEnv* env, jclass clazz, jlong pointer) {
    free((void*)pointer);
}

JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_memcpy(JNIEnv* env, jclass clazz, jlong dst, jlong src, jlong size) {
    memcpy((void*)dst, (void*)src, size);
}

JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_clone(JNIEnv* env, jclass clazz, jlong src, jlong size) {
    void* dst = malloc(size);
    memcpy((void*)dst, (void*)src, size);
    return reinterpret_cast<jlong>(dst);
}

JNIEXPORT jobject JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_wrapPointer
  (JNIEnv* env, jclass clazz, jlong ptr, jint capacity) {
    return env->NewDirectByteBuffer((void*)ptr, capacity);
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    init_tls();
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved) {
    JNIEnv* env;
    tls_attach_jni_env(gJVM, &env);
    if (globalClass != NULL) {
        env->DeleteGlobalRef(globalClass);
    }
    if (cxxExceptionClass != NULL) {
        env->DeleteGlobalRef(cxxExceptionClass);
    }
    cleanup_tls();
}