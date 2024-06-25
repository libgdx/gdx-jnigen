#include <stdlib.h>
#include <string.h>
#include <ffi.h>
#include <jnigen.h>

#ifdef __linux__
#include <dlfcn.h>
#endif // __linux__

#include <CHandler.h>


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
jmethodID getExceptionStringMethod = NULL;
jclass globalClass = NULL;
JavaVM* gJVM = NULL;
bool ignoreCXXExceptionMessage = false;

typedef struct _closure_info {
    jobject javaInfo;
    size_t argumentSize;
} closure_info;


static inline size_t getOffsetForField(ffi_type* struct_type, uint32_t index) {
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

void callbackHandler(ffi_cif* cif, void* result, void** args, void* user) {
    ATTACH_ENV()
    closure_info* info = (closure_info*) user;
    char backingBuffer[info->argumentSize];
    jobject jBuffer = NULL;
    if (cif->nargs != 0) {
        jBuffer = env->NewDirectByteBuffer(backingBuffer, info->argumentSize);
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
    jlong ret = env->CallStaticLongMethod(globalClass, dispatchCallbackMethod, info->javaInfo, jBuffer);

    jthrowable exc = env->ExceptionOccurred();
    if(exc != NULL) {
        env->ExceptionClear();
        if (ignoreCXXExceptionMessage)
            throw JavaExceptionMarker(exc, "Java-Side exception");
        jstring message = (jstring)env->CallStaticObjectMethod(globalClass, getExceptionStringMethod, exc);
        const char* cStringMessage = env->GetStringUTFChars(message, NULL);
        std::string cxxMessage(cStringMessage);
        env->ReleaseStringUTFChars(message, cStringMessage);
        throw JavaExceptionMarker(exc, cxxMessage);
    }

    ffi_type* rtype = cif->rtype;
    if(rtype->type == FFI_TYPE_STRUCT) {
        memcpy(result, reinterpret_cast<void*>(ret), rtype->size);
    } else {
        ENDIAN_INTCPY(result, rtype->size, &ret, sizeof(jlong));
    }
    DETACH_ENV()
}

JavaExceptionMarker::JavaExceptionMarker(jthrowable exc, const std::string& message) : std::runtime_error(message) {
    ATTACH_ENV() // TODO: We could save this by doing this in the caller, but that seems overoptimization?
    javaExc = (jthrowable)env->NewGlobalRef(exc);
    DETACH_ENV()
}

JavaExceptionMarker::~JavaExceptionMarker() _NOEXCEPT {
    ATTACH_ENV() // TODO: Figure out, whether this is an issue during full-crash
    env->DeleteGlobalRef(javaExc);
    DETACH_ENV()
}

JNIEXPORT jint JNICALL Java_com_badlogic_gdx_jnigen_CHandler_getPointerSize(JNIEnv* env, jclass clazz) {
    return sizeof(void*);
}

JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_CHandler_init(JNIEnv* env, jclass clazz, jobject dispatchCallbackReflectedMethod, jobject getExceptionStringReflectedMethod) {
    env->GetJavaVM(&gJVM);
    globalClass = (jclass)env->NewGlobalRef(clazz);
    dispatchCallbackMethod = env->FromReflectedMethod(dispatchCallbackReflectedMethod);
    if (dispatchCallbackMethod == NULL) {
        fprintf(stderr, "com.badlogic.gdx.jnigen.Global#dispatchCallback is not reachable via JNI\n");
        return JNI_FALSE;
    }
    getExceptionStringMethod = env->FromReflectedMethod(getExceptionStringReflectedMethod);
    if (getExceptionStringMethod == NULL) {
        fprintf(stderr, "com.badlogic.gdx.jnigen.Global#getExceptionStringMethod is not reachable via JNI\n");
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_CHandler_testIllegalArgumentExceptionThrowable(JNIEnv* env, jclass clazz, jclass illegalArgumentException) {
    env->ThrowNew(illegalArgumentException, "Test");
}

JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_CHandler_testCXXExceptionThrowable(JNIEnv* env, jclass clazz, jclass cxxException) {
    env->ThrowNew(cxxException, "Test");
}

JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_CHandler_setDisableCXXExceptionMessage(JNIEnv* env, jclass clazz, jboolean disable) {
    ignoreCXXExceptionMessage = disable;
}

JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_CHandler_reExportSymbolsGlobally(JNIEnv* env, jclass clazz, jstring obj_libPath) {
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

JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_CHandler_nativeCreateCif(JNIEnv* env, jclass clazz, jlong returnType, jobject obj_parameters, jint size) {
	char* parameters = (char*)(obj_parameters?env->GetDirectBufferAddress(obj_parameters):0);

    ffi_type** parameterFFITypes = (ffi_type**)malloc(sizeof(ffi_type*) * size);
    memcpy(parameterFFITypes, parameters, sizeof(ffi_type*) * size);

    ffi_cif* cif = (ffi_cif*)malloc(sizeof(ffi_cif));
    ffi_prep_cif(cif, FFI_DEFAULT_ABI, size, reinterpret_cast<ffi_type*>(returnType), parameterFFITypes);
    return reinterpret_cast<jlong>(cif);
}

JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_CHandler_createClosureForObject(JNIEnv* env, jclass clazz, jlong cifArg, jobject object, jobject obj_closureRet) {
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

JNIEXPORT jint JNICALL Java_com_badlogic_gdx_jnigen_CHandler_getOffsetForField(JNIEnv* env, jclass clazz, jlong type_ptr, jint index) {
    return getOffsetForField(reinterpret_cast<ffi_type*>(type_ptr), (uint32_t) index);
}

JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_CHandler_getStackElementField(JNIEnv* env, jclass clazz, jlong pointer, jlong type_ptr, jint index, jboolean calculateOffset) {
    char* ptr = reinterpret_cast<char*>(pointer);
    ffi_type* struct_type = reinterpret_cast<ffi_type*>(type_ptr);
    uint32_t field = (uint32_t) index;

    size_t offset = calculateOffset ? getOffsetForField(struct_type, field) : 0;

    jlong ret = 0;
    ENDIAN_INTCPY(&ret, sizeof(jlong), ptr + offset, struct_type->elements[field]->size);
    return ret;
}

JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_CHandler_setStackElement_1internal(JNIEnv* env, jclass clazz, jlong pointer, jlong type_ptr, jint index, jlong value, jboolean calculateOffset) {
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
}

JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_CHandler_getPointerPart(JNIEnv* env, jclass clazz, jlong pointer, jint size, jint offset) {
    char* ptr = reinterpret_cast<char*>(pointer);
    jlong ret = 0;
    ENDIAN_INTCPY(&ret, sizeof(jlong), ptr + offset, size);
    return ret;
}

JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_CHandler_setPointerPart(JNIEnv* env, jclass clazz, jlong pointer, jint size, jint offset, jlong value) {
    char* ptr = reinterpret_cast<char*>(pointer);
    ENDIAN_INTCPY(ptr + offset, size, &value, sizeof(jlong));
}

JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_CHandler_setPointerAsString(JNIEnv* env, jclass clazz, jlong pointer, jstring obj_string) {
	char* string = (char*)env->GetStringUTFChars(obj_string, 0);

    strcpy((char*)pointer, string);
    
	env->ReleaseStringUTFChars(obj_string, string);
}

JNIEXPORT jstring JNICALL Java_com_badlogic_gdx_jnigen_CHandler_getPointerAsString(JNIEnv* env, jclass clazz, jlong pointer) {
    return env->NewStringUTF(reinterpret_cast<const char*>(pointer));
}

JNIEXPORT jint JNICALL Java_com_badlogic_gdx_jnigen_CHandler_getSizeFromFFIType(JNIEnv* env, jclass clazz, jlong type) {
    return reinterpret_cast<ffi_type*>(type)->size;
}

JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_CHandler_getSignFromFFIType(JNIEnv* env, jclass clazz, jlong type) {
    return (jboolean)(GET_FFI_TYPE_SIGN((ffi_type*)type));
}

JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_CHandler_isStruct(JNIEnv* env, jclass clazz, jlong type) {
    return ((ffi_type*)type)->type == FFI_TYPE_STRUCT;
}

JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_CHandler_isVoid(JNIEnv* env, jclass clazz, jlong type) {
    return ((ffi_type*)type)->type == FFI_TYPE_VOID;
}

JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_CHandler_freeClosure(JNIEnv* env, jclass clazz, jlong closurePtr) {
    ffi_closure* closure = (ffi_closure*) closurePtr;
    env->DeleteGlobalRef(((closure_info*)closure->user_data)->javaInfo);
    free(closure->user_data);
    ffi_closure_free(closure);
}

JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_CHandler_malloc(JNIEnv* env, jclass clazz, jlong size) {
    return reinterpret_cast<jlong>(malloc(size));
}

JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_CHandler_free(JNIEnv* env, jclass clazz, jlong pointer) {
    free((void*)pointer);
}

JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_CHandler_memcpy(JNIEnv* env, jclass clazz, jlong dst, jlong src, jlong size) {
    memcpy((void*)dst, (void*)src, size);
}

JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_CHandler_clone(JNIEnv* env, jclass clazz, jlong src, jlong size) {
    void* dst = malloc(size);
    memcpy((void*)dst, (void*)src, size);
    return reinterpret_cast<jlong>(dst);
}

