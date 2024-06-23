#include <stdexcept>
#include <jnigen.h>
#include "test_data.h"

void call_methodWithCallback(methodWithCallback fnPtr) {
    fnPtr();
}

void call_methodWithCallbackLongArg(methodWithCallbackLongArg fnPtr) {
    uint64_t arg = 5;
    fnPtr(arg);
}

void call_methodWithCallbackIntArg(methodWithCallbackIntArg fnPtr) {
    int arg = 5;
    fnPtr(arg);
}

void call_methodWithCallbackShortArg(methodWithCallbackShortArg fnPtr) {
    short arg = 5;
    fnPtr(arg);
}

void call_methodWithCallbackByteArg(methodWithCallbackByteArg fnPtr) {
    char arg = 5;
    fnPtr(arg);
}

void call_methodWithCallbackCharArg(methodWithCallbackCharArg fnPtr) {
    uint16_t arg = 5;
    fnPtr(arg);
}

void call_methodWithCallbackBooleanArg(methodWithCallbackBooleanArg fnPtr) {
    bool arg = true;
    fnPtr(arg);
}

void call_methodWithCallbackFloatArg(methodWithCallbackFloatArg fnPtr) {
    float arg = 5.5;
    fnPtr(arg);
}

void call_methodWithCallbackDoubleArg(methodWithCallbackDoubleArg fnPtr) {
    double arg = 5.5;
    fnPtr(arg);
}

void call_methodWithCallbackAllArgs(methodWithCallbackAllArgs fnPtr) {
    uint64_t arg1 = 1;
    int arg2 = 2;
    short arg3 = 3;
    char arg4 = 4;
    uint16_t arg5 = 5;
    bool arg6 = true;
    float arg7 = 6.6;
    double arg8 = 7.7;
    fnPtr(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
}

uint64_t call_methodWithCallbackLongReturn(methodWithCallbackLongReturn fnPtr) {
    return fnPtr();
}

int call_methodWithCallbackIntReturn(methodWithCallbackIntReturn fnPtr) {
    return fnPtr();
}

short call_methodWithCallbackShortReturn(methodWithCallbackShortReturn fnPtr) {
    return fnPtr();
}

uint16_t call_methodWithCallbackCharReturn(methodWithCallbackCharReturn fnPtr) {
    return fnPtr();
}

char call_methodWithCallbackByteReturn(methodWithCallbackByteReturn fnPtr) {
    return fnPtr();
}

bool call_methodWithCallbackBooleanReturn(methodWithCallbackBooleanReturn fnPtr) {
    return fnPtr() == 1;
}

float call_methodWithCallbackFloatReturn(methodWithCallbackFloatReturn fnPtr) {
    return fnPtr();
}

double call_methodWithCallbackDoubleReturn(methodWithCallbackDoubleReturn fnPtr) {
    return fnPtr();
}

TestStruct call_methodWithCallbackTestStructReturn(methodWithCallbackTestStructReturn fnPtr) {
    return fnPtr();
}

TestStruct* call_methodWithCallbackTestStructPointerReturn(methodWithCallbackTestStructPointerReturn fnPtr) {
    return fnPtr();
}

void call_methodWithThrowingCallback(methodWithThrowingCallback fnPtr) {
    fnPtr();
}

void call_methodWithCallbackTestStructArg(methodWithCallbackTestStructArg fnPtr) {
   TestStruct arg = {
       .field1 = 1,
       .field2 = 2,
       .field3 = 3,
       .field4 = 4
   };
   fnPtr(arg);
}

void call_methodWithCallbackTestStructPointerArg(methodWithCallbackTestStructPointerArg fnPtr) {
    TestStruct str = {
        .field1 = 1,
        .field2 = 2,
        .field3 = 3,
        .field4 = 4
    };
    TestStruct* arg = (TestStruct*) malloc(sizeof(TestStruct));
    *arg = str;
    fnPtr(arg);
}

TestStruct* returnTestStructPointer(void) {
    TestStruct* ptr = (TestStruct*)malloc(sizeof(TestStruct));
    TestStruct str = {
        .field1 = 1,
        .field2 = 2,
        .field3 = 3,
        .field4 = 4
    };

    *ptr = str;
    return ptr;
}

TestStruct returnTestStruct(void) {
    TestStruct str = {
        .field1 = 1,
        .field2 = 2,
        .field3 = 3,
        .field4 = 4
    };
    return str;
}

uint32_t passByValue(TestStruct testStruct) {
    testStruct.field4 = 0;
    return testStruct.field2;
}

uint32_t passByPointer(TestStruct* testStruct) {
    testStruct->field4 = 5;
    return testStruct->field2;
}

int passTestEnum(TestEnum enumValue) {
    return enumValue == SECOND;
}

TestEnum returnTestEnum(void) {
    return THIRD;
}

TestEnum passAndReturnTestEnum(TestEnum enumValue) {
    return enumValue;
}

int passTestEnumPointer(TestEnum* enumValue) {
    return *enumValue == SECOND;
}

TestEnum* returnTestEnumPointer(void) {
    TestEnum* ptr = (TestEnum*)malloc(sizeof(TestEnum));
    *ptr = THIRD;
    return ptr;
}

TestEnum passAndReturnTestEnumPointer(TestEnum* enumValue) {
    return *enumValue;
}

TestEnum call_methodWithCallbackTestEnumReturn(methodWithCallbackTestEnumReturn fnPtr) {
    return fnPtr();
}

void call_methodWithCallbackTestEnumArg(methodWithCallbackTestEnumArg fnPtr) {
    fnPtr(SECOND);
}

TestEnum* call_methodWithCallbackTestEnumPointerReturn(methodWithCallbackTestEnumPointerReturn fnPtr) {
    return fnPtr();
}

void call_methodWithCallbackTestEnumPointerArg(methodWithCallbackTestEnumPointerArg fnPtr) {
    TestEnum test = SECOND;
    fnPtr(&test);
}


// Pointer stuff
int passIntPointer(int* ptr) {
    return *ptr;
}

int* returnIntPointer(int i) {
    int* ret = (int*)malloc(sizeof(int));
    *ret = i;
    return ret;
}

int* call_methodWithCallbackIntPointerReturn(methodWithCallbackIntPointerReturn fnPtr, int val) {
    int* ret = fnPtr();
    *ret = val;
    return ret;
}

int call_methodWithCallbackIntPointerArg(methodWithCallbackIntPointerArg fnPtr) {
    int* arg = (int*) malloc(sizeof(int));
    *arg = 15;
    fnPtr(arg);
    return *arg; // We don't free, since we will free it on java side
}

void setFloatPtrFieldValue(SpecialStruct specialStruct, float value) {
    *(specialStruct.floatPtrField) = value;
}

void setFixedSizeArrayFieldValue(SpecialStruct* specialStruct, int index, int value) {
    specialStruct->arrayField[index] = value;
}

void setIntPtrFieldValue(SpecialStruct specialStruct, int value) {
    *(specialStruct.intPtrField) = value;
}

float getFloatPtrFieldValue(SpecialStruct specialStruct) {
    return *(specialStruct.floatPtrField);
}

int getFixedSizeArrayFieldValue(SpecialStruct specialStruct, int index) {
    return specialStruct.arrayField[index];
}

int getIntPtrFieldValue(SpecialStruct specialStruct) {
    return *(specialStruct.intPtrField);
}

// TestUnion
TestUnion* returnTestUnionPointer(void) {
    TestUnion* ptr = (TestUnion*)malloc(sizeof(TestUnion));
    ptr->uintType = 50;
    return ptr;
}

TestUnion returnTestUnion(void) {
    TestUnion value;
    value.doubleType = 3.3;
    return value;
}

uint64_t passByValueUnion(TestUnion testUnion) {
    return testUnion.uintType;
}

uint64_t passByPointerUnion(TestUnion* testUnion) {
    return testUnion->uintType;
}

uint64_t getUnionUintTypeByValue(TestUnion testUnion) {
    return testUnion.uintType;
}

void setUnionUintTypeByPointer(TestUnion* testUnion, uint64_t value) {
    testUnion->uintType = value;
}

double getUnionDoubleTypeByValue(TestUnion testUnion) {
    return testUnion.doubleType;
}

void setUnionDoubleTypeByPointer(TestUnion* testUnion, double value) {
    testUnion->doubleType = value;
}

int* getUnionFixedSizeIntByPointer(TestUnion* testUnion) {
    return testUnion->fixedSizeInt;
}

int getUnionFixedSizeIntByValue(TestUnion testUnion, int index) {
    return testUnion.fixedSizeInt[index];
}

void setUnionFixedSizeIntByPointer(TestUnion* testUnion, int index, int value) {
    testUnion->fixedSizeInt[index] = value;
}

TestStruct getUnionStructTypeByValue(TestUnion testUnion) {
    return testUnion.structType;
}

void setUnionStructTypeByPointer(TestUnion* testUnion, TestStruct value) {
    testUnion->structType = value;
}

TestUnion* call_methodWithCallbackTestUnionPointerReturn(methodWithCallbackTestUnionPointerReturn fnPtr) {
    return fnPtr();
}

void call_methodWithCallbackTestUnionPointerArg(methodWithCallbackTestUnionPointerArg fnPtr) {
    TestUnion* testUnion = returnTestUnionPointer();
    fnPtr(testUnion);
    free(testUnion);
}

// These tests are very cheap, but I'm just to lazy... To bad!
void** voidPointerPointer(void** test) {
    return test;
}

TestEnum** enumPointerPointer(TestEnum** test) {
    TestEnum* ptr = (TestEnum*)malloc(sizeof(TestEnum));
    *ptr = SECOND;
    *test = ptr;
    return test;
}

TestStruct** structPointerPointer(TestStruct** test) {
    TestStruct* ptr = (TestStruct*)malloc(sizeof(TestStruct));
    TestStruct str = {
        .field1 = 1,
        .field2 = 2,
        .field3 = 3,
        .field4 = 4
    };

    *ptr = str;
    *test = ptr;
    return test;
}

int** intPointerPointer(int** test) {
    int* ptr = (int*)malloc(sizeof(int));
    *ptr = 5;
    *test = ptr;
    return test;
}

float** floatPointerPointer(float** test) {
    float* ptr = (float*)malloc(sizeof(float));
    *ptr = 5.5;
    *test = ptr;
    return test;
}

void***** pointerPointerManyyy(void***** test) {
    void**** ptr4 = (void****)malloc(sizeof(void***));
    void*** ptr3 = (void***)malloc(sizeof(void**));
    void** ptr2 = (void**)malloc(sizeof(void*));
    void* ptr1 = (void*)5;

    *ptr2 = ptr1;
    *ptr3 = ptr2;
    *ptr4 = ptr3;
    *test = ptr4;

    return test;
}

void call_methodWithIntPtrPtrArg(methodWithIntPtrPtrArg fnPtr) {
    int i = 5;
    int* i1 = &i;
    int** i2 = &i1;
    fnPtr(i2);
}

int** call_methodWithIntPtrPtrRet(methodWithIntPtrPtrRet fnPtr) {
    return fnPtr();
}

void outOfBoundArg(uint32_t par) {
    // do nothing
}

void throwOrdinaryException() {
    throw std::runtime_error("Normal error");
}

void throwNumberException() {
    throw -2;
}

const char* returnThrownCauseMessage(methodWithThrowingCallback fnPtr) {
    try {
        fnPtr();
    } catch (const JavaExceptionMarker& e) {
        return e.what();
    }
    return NULL;
}

char* returnString(void) {
    return strdup("HALLO 123");
}

bool validateString(char* str) {
    return strcmp(str, "TEST STRING") == 0;
}

void ensureAnonymousStructParsed(AnonymousStructNoField, AnonymousStructField, AnonymousStructFieldArray, struct AnonymousClosure, AnonymousStructNoFieldEnd, AnonymousStructNoFieldConsecutive, AnonymousStructNoFieldNested) {}
void weirdPointer(FILE *_file) {}
void constArrayParameter(const TestStruct structs[]) {}


