#include <stdint.h>
#include <stdbool.h>
#include <stdlib.h>
#include <ffi.h>

#define CONVERT_TO_64_BIT(x) static_cast<uint64_t>(reinterpret_cast<uintptr_t>(x));
#define CONVERT_TO_VOID_POINTER(x) reinterpret_cast<void*>(static_cast<uintptr_t>(x));

#define CONVERT_TO_JLONG(x) reinterpret_cast<jlong>(static_cast<uint64_t>(reinterpret_cast<uintptr_t>(x)));
#define CONVERT_FROM_JLONG(x, type) reinterpret_cast<type>(static_cast<uintptr_t>(reinterpret_cast<uint64_t>(x)));

#define GET_FFI_TYPE(type) \
    _Generic((type){0}, \
        double: &ffi_type_double, \
        float: &ffi_type_float, \
        default: CONVERT_SIZE_TO_FFI_TYPE(type) \
    )

#define CONVERT_SIZE_TO_FFI_TYPE(type) \
    ((sizeof(type) == 1) ? &ffi_type_uint8 : \
    (sizeof(type) == 2) ? &ffi_type_uint16 : \
    (sizeof(type) == 4) ? &ffi_type_uint32 : \
    (sizeof(type) == 8) ? &ffi_type_uint64 : \
    NULL)

typedef struct TestStruct {
    uint64_t field1;
    uint32_t field2;
    uint16_t field3;
    uint8_t field4;
} TestStruct;