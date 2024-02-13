#include <stdint.h>
#include <stdlib.h>
#include <stdbool.h>

#define RANDOM_MACRO 5
#ifdef __cplusplus
extern "C" {
#endif

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
TestStruct* returnTestStructPointer();
TestStruct returnTestStruct();
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
TestEnum returnTestEnum();
TestEnum passAndReturnTestEnum(TestEnum enumValue);

typedef TestEnum (*methodWithCallbackTestEnumReturn)(void);
typedef void (*methodWithCallbackTestEnumArg)(TestEnum);

TestEnum call_methodWithCallbackTestEnumReturn(methodWithCallbackTestEnumReturn fnPtr);
void call_methodWithCallbackTestEnumArg(methodWithCallbackTestEnumArg fnPtr);

// Pointer stuff
int passIntPointer(int*);
int* returnIntPointer(int);

float getFloatPtrFieldValue(SpecialStruct specialStruct);
int getFixedSizeArrayFieldValue(SpecialStruct specialStruct, int index);
int getIntPtrFieldValue(SpecialStruct specialStruct);

void setFloatPtrFieldValue(SpecialStruct specialStruct, float value);
void setFixedSizeArrayFieldValue(SpecialStruct* specialStruct, int index, int value);
void setIntPtrFieldValue(SpecialStruct specialStruct, int value);

#ifdef __cplusplus
}
#endif