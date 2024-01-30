#include "definitions.h"

void call_methodWithCallback(methodWithCallback fnPtr) {
    fnPtr();
}

void call_methodWithCallbackLongArg(methodWithCallbackLongArg fnPtr) {
    uint64_t arg = 5;
    fnPtr(arg);
}

void call_methodWithCallbackIntArg(methodWithCallbackIntArg fnPtr) {
    uint32_t arg = 5;
    fnPtr(arg);
}

void call_methodWithCallbackShortArg(methodWithCallbackShortArg fnPtr) {
    uint16_t arg = 5;
    fnPtr(arg);
}

void call_methodWithCallbackByteArg(methodWithCallbackByteArg fnPtr) {
    uint8_t arg = 5;
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
    uint32_t arg2 = 2;
    uint16_t arg3 = 3;
    uint8_t arg4 = 4;
    uint16_t arg5 = 5;
    bool arg6 = true;
    float arg7 = 6.6;
    double arg8 = 7.7;
    fnPtr(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
}

uint64_t call_methodWithCallbackLongReturn(methodWithCallbackLongReturn fnPtr) {
    return fnPtr();
}

uint32_t call_methodWithCallbackIntReturn(methodWithCallbackIntReturn fnPtr) {
    return fnPtr();
}

uint16_t call_methodWithCallbackShortReturn(methodWithCallbackShortReturn fnPtr) {
    return fnPtr();
}

uint16_t call_methodWithCallbackCharReturn(methodWithCallbackCharReturn fnPtr) {
    return fnPtr();
}

uint8_t call_methodWithCallbackByteReturn(methodWithCallbackByteReturn fnPtr) {
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