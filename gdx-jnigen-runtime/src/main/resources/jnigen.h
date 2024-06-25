#include <stdint.h>
#include <stdbool.h>
#include <stdlib.h>
#include <stdexcept>
#include <string>
#include <jni.h>

#define HANDLE_JAVA_EXCEPTION_START() try {

#define HANDLE_JAVA_EXCEPTION_END() } catch (const JavaExceptionMarker& e) { env->Throw(e.javaExc); } \
        catch (const std::exception& ex) { env->ThrowNew(cxxExceptionClass, ex.what()); } \
        catch (...) { env->ThrowNew(cxxExceptionClass, "An unknown error occurred"); }

struct JavaExceptionMarker : public std::runtime_error {
    jthrowable javaExc;
    JavaExceptionMarker(jthrowable exc, const std::string& message);
    ~JavaExceptionMarker() _NOEXCEPT;
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

#define GET_NATIVE_TYPE(type, nat_type) \
    _Generic((type){0}, \
        double: set_native_type(nat_type, DOUBLE_TYPE, 8, true), \
        float: set_native_type(nat_type, FLOAT_TYPE, 4, true), \
        default: set_native_type(nat_type, INT_TYPE, sizeof(type), IS_SIGNED_TYPE(type)) \
    )

#define IS_SIGNED_TYPE(type) (((type)-1) < 0)

#define CHECK_AND_THROW_C_TYPE(env, type, value, argument_index, returnAction) \
    bool _signed##argument_index = IS_SIGNED_TYPE(type); \
    int _size##argument_index = sizeof(type); \
    if (!CHECK_BOUNDS_FOR_NUMBER(value, _size##argument_index, _signed##argument_index)) { \
        char buffer[1024]; \
        snprintf(buffer, sizeof(buffer), "Value %ld is out of bound for size %d and signess %d on argument %d", (jlong)value, _size##argument_index, _signed##argument_index, argument_index); \
        env->ThrowNew(illegalArgumentExceptionClass, buffer); \
        returnAction; \
    }

#define CHECK_BOUNDS_FOR_NUMBER(value, size, is_signed) \
    ((size) == 8 ? true : \
    (!(is_signed)) ? ((uint64_t)(value) < (1ULL << ((size) << 3))) : \
    ((int64_t)(value) >= (-1LL << (((size) << 3) - 1)) && (int64_t)(value) <= ((1LL << (((size) << 3) - 1)) - 1)))