#include <stdint.h>
#include <stdbool.h>
#include <stdlib.h>

#define CONVERT_TO_64_BIT(x) static_cast<uint64_t>(reinterpret_cast<uintptr_t>(x));
#define CONVERT_TO_VOID_POINTER(x) reinterpret_cast<void*>(static_cast<uintptr_t>(x));

#define CONVERT_TO_JLONG(x) reinterpret_cast<jlong>(static_cast<uint64_t>(reinterpret_cast<uintptr_t>(x)));
#define CONVERT_FROM_JLONG(x, type) reinterpret_cast<type>(static_cast<uintptr_t>(reinterpret_cast<uint64_t>(x)));


typedef struct TestStruct {
    uint64_t field1;
    uint32_t field2;
    uint16_t field3;
    uint8_t field4;
} TestStruct;