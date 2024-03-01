#include <stdint.h>
#include <stdbool.h>
#include <stdlib.h>
#include <ffi.h>

#define GET_FFI_TYPE(type) \
    _Generic((type){0}, \
        double: &ffi_type_double, \
        float: &ffi_type_float, \
        default: CONVERT_SIZE_TO_FFI_TYPE(type) \
    )

#define CONVERT_SIZE_TO_FFI_TYPE(type) \
    ((sizeof(type) == 1) ? (IS_SIGNED_TYPE(type) ? &ffi_type_sint8 : &ffi_type_uint8) : \
    (sizeof(type) == 2) ? (IS_SIGNED_TYPE(type) ? &ffi_type_sint16 : &ffi_type_uint16) : \
    (sizeof(type) == 4) ? (IS_SIGNED_TYPE(type) ? &ffi_type_sint32 : &ffi_type_uint32) : \
    (sizeof(type) == 8) ? (IS_SIGNED_TYPE(type) ? &ffi_type_sint64 : &ffi_type_uint64) : \
    NULL)

#define GET_FFI_TYPE_SIGN(type) \
    &ffi_type_sint8 == type || &ffi_type_sint16 == type || &ffi_type_sint32 == type || &ffi_type_sint64 == type || &ffi_type_double == type || &ffi_type_float == type

#define CHECK_BOUNDS_FFI_TYPE(type, value) \
    CHECK_BOUNDS_FOR_NUMBER(value, type->size, GET_FFI_TYPE_SIGN(type));

#define IS_SIGNED_TYPE(type) (((type)-1) < 0)

#define CHECK_AND_THROW_C_TYPE(type, value, set_error) \
    bool _check = CHECK_BOUNDS_FOR_C_TYPE(type, value); \
    if (!_check) { \
        env->Throw(typeBoundCheckFailed); \
        error_code = set_error; \
    }

#define CHECK_BOUNDS_FOR_C_TYPE(type, value) \
    CHECK_BOUNDS_FOR_NUMBER(value, sizeof(type), IS_SIGNED_TYPE(type))

#define CHECK_BOUNDS_FOR_NUMBER(value, size, is_signed) \
    ((size) == 8 ? true : \
    (!(is_signed)) ? ((uint64_t)(value) < (1ULL << ((size) << 3))) : \
    ((int64_t)(value) >= (-1LL << (((size) << 3) - 1)) && (int64_t)(value) <= ((1LL << (((size) << 3) - 1)) - 1)))

static void calculateAlignmentAndOffset(ffi_type* type, bool isStruct) {
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

    if (isStruct && struct_size % struct_alignment != 0) {
       struct_size += struct_alignment - (struct_size % struct_alignment);
    }

    type->alignment = struct_alignment;
    type->size = struct_size;
}