#include <stdint.h>
#define __STDC_FORMAT_MACROS
#include <inttypes.h>
#include <stdbool.h>
#include <stdlib.h>
#include <stdexcept>
#include <string>
#include <jni.h>

// Helper macro for platform-specific thread attachment
// Stolen from https://github.com/rednblackgames/gdx-miniaudio
#ifdef __ANDROID__
#define THREAD_ATTACH_MACRO gJVM->AttachCurrentThread(&env, NULL);
#else
#define THREAD_ATTACH_MACRO gJVM->AttachCurrentThread((void**)&env, NULL);
#endif

#define ATTACH_ENV()                                                    \
    bool _hadToAttach = false;                                          \
    JNIEnv* env;                                                        \
    if (gJVM->GetEnv((void**)&env, JNI_VERSION_1_6) == JNI_EDETACHED) { \
        THREAD_ATTACH_MACRO                                             \
        _hadToAttach = true;                                            \
    }

#define DETACH_ENV()                 \
    if (_hadToAttach) {              \
        gJVM->DetachCurrentThread(); \
    }

#define HANDLE_JAVA_EXCEPTION_START() try {

#define HANDLE_JAVA_EXCEPTION_END() } catch (const JavaExceptionMarker& e) { env->Throw(e.javaExc); } \
        catch (const std::exception& ex) { env->ThrowNew(cxxExceptionClass, ex.what()); } \
        catch (...) { env->ThrowNew(cxxExceptionClass, "An unknown error occurred"); }

class JavaExceptionMarker : public std::runtime_error {
    public:
        jthrowable javaExc;
        JavaVM* gJVM;
        JavaExceptionMarker(JavaVM* passedJVM, jthrowable exc, const std::string& message) : std::runtime_error(message) {
            gJVM = passedJVM;
            ATTACH_ENV() // TODO: We could save this by doing this in the caller, but that seems overoptimization?
            javaExc = (jthrowable)env->NewGlobalRef(exc);
            DETACH_ENV()
        }

        ~JavaExceptionMarker() throw() {
            ATTACH_ENV() // TODO: Figure out, whether this is an issue during full-crash
            env->DeleteGlobalRef(javaExc);
            DETACH_ENV()
        }
};

typedef enum _native_type_id {
    VOID_TYPE = 1,
    INT_TYPE,
    FLOAT_TYPE,
    DOUBLE_TYPE,
    STRUCT_TYPE,
    UNION_TYPE,
    POINTER_TYPE
} native_type_id;

typedef struct _native_type {
    native_type_id type;
    int size;
    bool sign;
    // If we are a struct/union
    int field_count;
    _native_type** fields;
} native_type;

static inline void set_native_type(native_type* nat_type, native_type_id id, size_t size, bool sign) {
    nat_type->type = id;
    nat_type->size = size;
    nat_type->sign = sign;
}

#define IS_SIGNED_TYPE(type) (((type)-1) < 0)

#ifdef __cplusplus
template<typename T>
struct native_type_traits;

template<>
struct native_type_traits<double> {
    static void get(native_type* nat_type) {
        set_native_type(nat_type, DOUBLE_TYPE, sizeof(double), true);
    }
};

template<>
struct native_type_traits<float> {
    static void get(native_type* nat_type) {
        set_native_type(nat_type, FLOAT_TYPE, sizeof(float), true);
    }
};

template<typename T>
struct native_type_traits {
    static void get(native_type* nat_type) {
        set_native_type(nat_type, INT_TYPE, sizeof(T), std::is_signed<T>::value);
    }
};

template<typename T>
void get_native_type(native_type* nat_type) {
    native_type_traits<T>::get(nat_type);
}

#define GET_NATIVE_TYPE(type, nat_type) get_native_type<type>(nat_type)

#else // C code

#define GET_NATIVE_TYPE(type, nat_type) \
    _Generic((type){0}, \
        double: set_native_type(nat_type, DOUBLE_TYPE, 8, true), \
        float: set_native_type(nat_type, FLOAT_TYPE, 4, true), \
        default: set_native_type(nat_type, INT_TYPE, sizeof(type), IS_SIGNED_TYPE(type)) \
    )

#endif //__cplusplus

#define CHECK_AND_THROW_C_TYPE(env, type, value, argument_index, returnAction) \
    bool _signed##argument_index = IS_SIGNED_TYPE(type); \
    int _size##argument_index = sizeof(type); \
    if (!CHECK_BOUNDS_FOR_NUMBER(value, _size##argument_index, _signed##argument_index)) { \
        char buffer[1024]; \
        snprintf(buffer, sizeof(buffer), "Value %" PRId64 " is out of bound for size %d and signess %d on argument %d", (int64_t)value, _size##argument_index, _signed##argument_index, argument_index); \
        env->ThrowNew(illegalArgumentExceptionClass, buffer); \
        returnAction; \
    }

#define CHECK_BOUNDS_FOR_NUMBER(value, size, is_signed) \
    ((size) == 8 ? true : \
    (!(is_signed)) ? ((uint64_t)(value) < (1ULL << ((size) << 3))) : \
    ((int64_t)(value) >= (-1LL << (((size) << 3) - 1)) && (int64_t)(value) <= ((1LL << (((size) << 3) - 1)) - 1)))