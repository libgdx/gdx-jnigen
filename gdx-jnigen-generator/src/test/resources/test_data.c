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

TestStruct* returnTestStructPointer() {
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

TestStruct returnTestStruct() {
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

TestEnum returnTestEnum() {
    return THIRD;
}

TestEnum passAndReturnTestEnum(TestEnum enumValue) {
    return enumValue;
}