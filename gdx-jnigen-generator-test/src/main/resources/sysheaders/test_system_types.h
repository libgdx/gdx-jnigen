#ifndef TEST_SYSTEM_TYPES_H
#define TEST_SYSTEM_TYPES_H

// Simulated system header: included via -isystem in the generator config so clang treats these
// declarations as system types. Each public typedef nests through an internal (__-prefixed) name that
// must NOT leak into the generated bindings - the generator must stop resolving at the outer alias.

typedef void (*__internal_callback)(int value);
typedef __internal_callback public_callback;

enum __internal_color {
    COLOR_RED = 1,
    COLOR_GREEN = 2,
    COLOR_BLUE = 3
};
typedef enum __internal_color public_color;

#endif // TEST_SYSTEM_TYPES_H
