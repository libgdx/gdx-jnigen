#include <stdint.h>
#include <stdlib.h>
#include <stdbool.h>
#include <stdarg.h>

#define RANDOM_MACRO 5
#ifdef __cplusplus
extern "C" {
#endif

#define UNSIGNED_INT 10
#define SIGNED_INT -10
#define UNSIGNED_DOUBLE 10.5
#define SIGNED_DOUBLE -10.5
#define UNSIGNED_HEX_INT 0x5B
#define SIGNED_HEX_INT -0x5B
#define boolean 10
#define special1 10L
#define special2 10LLU
#define special3 10.5
#define special4 10.5f
#define special5 10.5F
#define special6 10.5L
#define special7 10.5l

typedef struct TestStruct {
    uint64_t field1;
    uint32_t field2;
    uint16_t field3;
    uint8_t field4;
} TestStruct;

typedef struct SpecialStruct {
    float* floatPtrField;
    int arrayField[5];
    int* intPtrField;
} SpecialStruct;

typedef enum TestEnum {
    FIRST,
    SECOND,
    THIRD = 4
} TestEnum;

typedef union TestUnion
{
    uint64_t uintType;
    double doubleType;
    int fixedSizeInt[3];
    TestStruct structType;
} TestUnion;

typedef struct AnonymousStructNoField {
    struct {
        int intValue;
        float floatValue;
    };
    int externalValue;
} AnonymousStructNoField;

typedef struct AnonymousStructNoFieldEnd {
    int externalValue;
    struct {
        int intValue;
        float floatValue;
    };
} AnonymousStructNoFieldEnd;

typedef struct AnonymousStructNoFieldConsecutive {
    int externalValue;
    struct {
        int intValue1;
        float floatValue1;
    };
    struct {
        int intValue2;
        float floatValue2;
    };
} AnonymousStructNoFieldConsecutive;

typedef struct AnonymousStructNoFieldNested {
    struct {
        int intValue1;
        struct {
            float floatValue2;
        };
    };
    int externalValue;
} AnonymousStructNoFieldNested;

typedef struct AnonymousStructField {
    struct {
        int intValue;
        float floatValue;
    } inner;
    int externalValue;
} AnonymousStructField;

typedef struct AnonymousStructFieldArray {
    struct {
        int intValue;
        float floatValue;
    } inner[2];
    int externalValue;
} AnonymousStructFieldArray;

struct AnonymousClosure
{
    int (* someClosure)(int* t, double p);
    float (* anotherClosure)(int t, double p);
};

void ensureAnonymousStructParsed(AnonymousStructNoField, AnonymousStructField, AnonymousStructFieldArray, struct AnonymousClosure, AnonymousStructNoFieldEnd, AnonymousStructNoFieldConsecutive, AnonymousStructNoFieldNested);
void constArrayParameter(const TestStruct structs[]);
void** voidPointerPointer(void** test);
TestEnum** enumPointerPointer(TestEnum** test);
TestStruct** structPointerPointer(TestStruct** test);
int** intPointerPointer(int** test);
float** floatPointerPointer(float** test);
void***** pointerPointerManyyy(void***** test);

//variadic
int variadic(int count, ...);
int variadic_va_list(int count, va_list list);

// Typedefs
typedef void (*methodWithCallback)(void);
typedef void (*methodWithCallbackLongArg)(uint64_t);
typedef void (*methodWithCallbackIntArg)(int);
typedef void (*methodWithCallbackShortArg)(short);
typedef void (*methodWithCallbackByteArg)(char);
typedef void (*methodWithCallbackCharArg)(uint16_t);
typedef void (*methodWithCallbackBooleanArg)(bool);
typedef void (*methodWithCallbackFloatArg)(float);
typedef void (*methodWithCallbackDoubleArg)(double);
typedef void (*methodWithCallbackAllArgs)(uint64_t, int, short, char, uint16_t, bool, float, double);
typedef uint64_t (*methodWithCallbackLongReturn)(void);
typedef int (*methodWithCallbackIntReturn)(void);
typedef short (*methodWithCallbackShortReturn)(void);
typedef uint16_t (*methodWithCallbackCharReturn)(void);
typedef char (*methodWithCallbackByteReturn)(void);
typedef bool (*methodWithCallbackBooleanReturn)(void);
typedef float (*methodWithCallbackFloatReturn)(void);
typedef double (*methodWithCallbackDoubleReturn)(void);

// Function declarations
void call_methodWithCallback(methodWithCallback fnPtr);
void call_methodWithCallbackLongArg(methodWithCallbackLongArg fnPtr);
void call_methodWithCallbackIntArg(methodWithCallbackIntArg fnPtr);
void call_methodWithCallbackShortArg(methodWithCallbackShortArg fnPtr);
void call_methodWithCallbackByteArg(methodWithCallbackByteArg fnPtr);
void call_methodWithCallbackCharArg(methodWithCallbackCharArg fnPtr);
void call_methodWithCallbackBooleanArg(methodWithCallbackBooleanArg fnPtr);
void call_methodWithCallbackFloatArg(methodWithCallbackFloatArg fnPtr);
void call_methodWithCallbackDoubleArg(methodWithCallbackDoubleArg fnPtr);
void call_methodWithCallbackAllArgs(methodWithCallbackAllArgs fnPtr);
uint64_t call_methodWithCallbackLongReturn(methodWithCallbackLongReturn fnPtr);
int call_methodWithCallbackIntReturn(methodWithCallbackIntReturn fnPtr);
short call_methodWithCallbackShortReturn(methodWithCallbackShortReturn fnPtr);
uint16_t call_methodWithCallbackCharReturn(methodWithCallbackCharReturn fnPtr);
char call_methodWithCallbackByteReturn(methodWithCallbackByteReturn fnPtr);
bool call_methodWithCallbackBooleanReturn(methodWithCallbackBooleanReturn fnPtr);
float call_methodWithCallbackFloatReturn(methodWithCallbackFloatReturn fnPtr);
double call_methodWithCallbackDoubleReturn(methodWithCallbackDoubleReturn fnPtr);

// TestStruct stuff
TestStruct* returnTestStructPointer(void);
TestStruct returnTestStruct(void);
uint32_t passByValue(TestStruct testStruct);
uint32_t passByPointer(TestStruct* testStruct);

typedef TestStruct (*methodWithCallbackTestStructReturn)(void);
typedef TestStruct* (*methodWithCallbackTestStructPointerReturn)(void);
typedef void (*methodWithCallbackTestStructArg)(TestStruct);
typedef void (*methodWithCallbackTestStructPointerArg)(TestStruct*);

TestStruct call_methodWithCallbackTestStructReturn(methodWithCallbackTestStructReturn fnPtr);
TestStruct* call_methodWithCallbackTestStructPointerReturn(methodWithCallbackTestStructPointerReturn fnPtr);
void call_methodWithCallbackTestStructArg(methodWithCallbackTestStructArg fnPtr);
void call_methodWithCallbackTestStructPointerArg(methodWithCallbackTestStructPointerArg fnPtr);

// TestEnum stuff
int passTestEnum(TestEnum enumValue);
TestEnum returnTestEnum(void);
TestEnum passAndReturnTestEnum(TestEnum enumValue);

int passTestEnumPointer(TestEnum* enumValue);
TestEnum* returnTestEnumPointer(void);
TestEnum passAndReturnTestEnumPointer(TestEnum* enumValue);

typedef TestEnum (*methodWithCallbackTestEnumReturn)(void);
typedef void (*methodWithCallbackTestEnumArg)(TestEnum);

typedef TestEnum* (*methodWithCallbackTestEnumPointerReturn)(void);
typedef void (*methodWithCallbackTestEnumPointerArg)(TestEnum*);


TestEnum call_methodWithCallbackTestEnumReturn(methodWithCallbackTestEnumReturn fnPtr);
void call_methodWithCallbackTestEnumArg(methodWithCallbackTestEnumArg fnPtr);

TestEnum* call_methodWithCallbackTestEnumPointerReturn(methodWithCallbackTestEnumPointerReturn fnPtr);
void call_methodWithCallbackTestEnumPointerArg(methodWithCallbackTestEnumPointerArg fnPtr);

// Pointer stuff
int passIntPointer(int*);
int* returnIntPointer(int);

typedef int* (*methodWithCallbackIntPointerReturn)(void);
typedef int (*methodWithCallbackIntPointerArg)(int*);

int* call_methodWithCallbackIntPointerReturn(methodWithCallbackIntPointerReturn fnPtr, int val);
int call_methodWithCallbackIntPointerArg(methodWithCallbackIntPointerArg fnPtr);

float getFloatPtrFieldValue(SpecialStruct specialStruct);
int getFixedSizeArrayFieldValue(SpecialStruct specialStruct, int index);
int getIntPtrFieldValue(SpecialStruct specialStruct);

void setFloatPtrFieldValue(SpecialStruct specialStruct, float value);
void setFixedSizeArrayFieldValue(SpecialStruct* specialStruct, int index, int value);
void setIntPtrFieldValue(SpecialStruct specialStruct, int value);

// TestUnion

TestUnion* returnTestUnionPointer(void);
TestUnion returnTestUnion(void);

uint64_t getUnionUintTypeByValue(TestUnion testUnion);
void setUnionUintTypeByPointer(TestUnion* testUnion, uint64_t value);

double getUnionDoubleTypeByValue(TestUnion testUnion);
void setUnionDoubleTypeByPointer(TestUnion* testUnion, double value);

int* getUnionFixedSizeIntByPointer(TestUnion* testUnion);
int getUnionFixedSizeIntByValue(TestUnion testUnion, int index);
void setUnionFixedSizeIntByPointer(TestUnion* testUnion, int index, int value);

TestStruct getUnionStructTypeByValue(TestUnion testUnion);
void setUnionStructTypeByPointer(TestUnion* testUnion, TestStruct value);

// Callback Type Definitions
typedef TestUnion* (*methodWithCallbackTestUnionPointerReturn)(void);
typedef void (*methodWithCallbackTestUnionPointerArg)(TestUnion*);

// Functions to call callbacks
TestUnion* call_methodWithCallbackTestUnionPointerReturn(methodWithCallbackTestUnionPointerReturn fnPtr);
void call_methodWithCallbackTestUnionPointerArg(methodWithCallbackTestUnionPointerArg fnPtr);

#ifdef __cplusplus
}
#endif