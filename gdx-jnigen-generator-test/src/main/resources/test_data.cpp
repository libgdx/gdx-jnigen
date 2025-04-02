#include <stdexcept>
#include <jnigen.h>
#include "test_data.h"

GlobalArg g_lastArg;

void random(char, unsigned char, signed char, char*, unsigned char*, signed char*) {}

GlobalArg getGlobalArgState(void) {
    return g_lastArg;
}

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

void commentedMethod(void) {
    // This method does actually no great stuff MUAHAHAHA
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
        return strdup(e.what());
    }
    return NULL;
}

char* returnString(void) {
    return strdup("HALLO 123");
}

bool validateString(char* str) {
    return strcmp(str, "TEST STRING") == 0;
}

void ensureParsed(SpecialEnum, AnonymousStructNoField, AnonymousStructField, AnonymousStructFieldArray, struct AnonymousClosure, AnonymousStructNoFieldEnd, AnonymousStructNoFieldConsecutive, AnonymousStructNoFieldNested, struct forwardDeclStruct*) {}
void weirdPointer(FILE *_file) {}
void constArrayParameter(const TestStruct structs[]) {}

#ifdef _WIN32
#include <windows.h>

DWORD WINAPI thread_wrapper(LPVOID param) {
    auto callback = reinterpret_cast<void* (*)(void*)>(param);
    callback(nullptr);
    return 0;
}

void call_callback_in_thread(void* (*thread_callback)(void*)) {
    HANDLE thread = CreateThread(NULL, 0, thread_wrapper, reinterpret_cast<LPVOID>(thread_callback), 0, NULL);
    WaitForSingleObject(thread, INFINITE);
    CloseHandle(thread);
}
#else
#include <pthread.h>

void call_callback_in_thread(void* (*thread_callback)(void*)) {
    pthread_t thread;
    pthread_create(&thread, NULL, thread_callback, NULL);
    pthread_join(thread, NULL);
}
#endif

static void voidCallback(void) {
    g_lastArg.longVal = 1;
}

static void longArgCallback(uint64_t arg) {
    g_lastArg.longVal = arg;
}

static void intArgCallback(int arg) {
    g_lastArg.intVal = arg;
}

static void shortArgCallback(short arg) {
    g_lastArg.shortVal = arg;
}

static void byteArgCallback(char arg) {
    g_lastArg.byteVal = arg;
}

static void charArgCallback(uint16_t arg) {
    g_lastArg.charVal = arg;
}

static void booleanArgCallback(bool arg) {
    g_lastArg.boolVal = arg;
}

static void floatArgCallback(float arg) {
    g_lastArg.floatVal = arg;
}

static void doubleArgCallback(double arg) {
    g_lastArg.doubleVal = arg;
}

static void allArgsCallback(uint64_t arg1, int arg2, short arg3, char arg4,
                          uint16_t arg5, bool arg6, float arg7, double arg8) {
    g_lastArg.allArgs.arg1 = arg1;
    g_lastArg.allArgs.arg2 = arg2;
    g_lastArg.allArgs.arg3 = arg3;
    g_lastArg.allArgs.arg4 = arg4;
    g_lastArg.allArgs.arg5 = arg5;
    g_lastArg.allArgs.arg6 = arg6;
    g_lastArg.allArgs.arg7 = arg7;
    g_lastArg.allArgs.arg8 = arg8;
}

static uint64_t longReturnCallback(void) {
    return 123456789ULL;
}

static int intReturnCallback(void) {
    return 42;
}

static short shortReturnCallback(void) {
    return 100;
}

static uint16_t charReturnCallback(void) {
    return 65;
}

static char byteReturnCallback(void) {
    return 'X';
}

static bool booleanReturnCallback(void) {
    return true;
}

static float floatReturnCallback(void) {
    return 3.14159f;
}

static double doubleReturnCallback(void) {
    return 2.71828;
}

static void intPtrPtrArgCallback(int** arg) {
    g_lastArg.intPtrPtr = arg;
}

static int** intPtrPtrRetCallback(void) {
    int* value = (int*)malloc(sizeof(int));
    *value = 42;
    int** ptr_to_ptr = (int**)malloc(sizeof(int*));
    *ptr_to_ptr = value;
    return ptr_to_ptr;
}

static TestStruct testStructReturnCallback(void) {
    TestStruct result = {
        .field1 = 0x1234567890ABCDEF,
        .field2 = 0x12345678,
        .field3 = 0x1234,
        .field4 = 0x12
    };
    return result;
}

static TestStruct* testStructPointerReturnCallback(void) {
    TestStruct* result = (TestStruct*) malloc(sizeof(TestStruct));
    result->field1 = 0x1234567890ABCDEF;
    result->field2 = 0x12345678;
    result->field3 = 0x1234;
    result->field4 = 0x12;
    return result;
}

static void testStructArgCallback(TestStruct arg) {
    g_lastArg.structVal = arg;
}

static void testStructPointerArgCallback(TestStruct* arg) {
    g_lastArg.structPtr = arg;
}

static TestEnum testEnumReturnCallback(void) {
    return THIRD;
}

static void testEnumArgCallback(TestEnum arg) {
    g_lastArg.enumVal = arg;
}

static TestEnum* testEnumPointerReturnCallback(void) {
    TestEnum* value = (TestEnum*)malloc(sizeof(TestEnum));
    *value = SECOND;
    return value;
}

static void testEnumPointerArgCallback(TestEnum* arg) {
    g_lastArg.enumPtr = arg;
}

static int* intPointerReturnCallback(void) {
    int* value = (int*)malloc(sizeof(int));
    *value = 42;
    return value;
}

static int intPointerArgCallback(int* arg) {
    return *arg;
}

static TestUnion* testUnionPointerReturnCallback(void) {
    TestUnion* value = (TestUnion*)malloc(sizeof(TestUnion));
    value->uintType = 0x1234567890ABCDEF;
    return value;
}

static void testUnionPointerArgCallback(TestUnion* arg) {
    g_lastArg.unionPtr = arg;
}

static void testCallThrowingCallback(methodWithThrowingCallback fnPtr) {
    fnPtr();
}

methodWithCallback getVoidCallback(void) {
    return voidCallback;
}

methodWithCallbackLongArg getLongArgCallback(void) {
    return longArgCallback;
}

methodWithCallbackIntArg getIntArgCallback(void) {
    return intArgCallback;
}

methodWithCallbackShortArg getShortArgCallback(void) {
    return shortArgCallback;
}

methodWithCallbackByteArg getByteArgCallback(void) {
    return byteArgCallback;
}

methodWithCallbackCharArg getCharArgCallback(void) {
    return charArgCallback;
}

methodWithCallbackBooleanArg getBooleanArgCallback(void) {
    return booleanArgCallback;
}

methodWithCallbackFloatArg getFloatArgCallback(void) {
    return floatArgCallback;
}

methodWithCallbackDoubleArg getDoubleArgCallback(void) {
    return doubleArgCallback;
}

methodWithCallbackAllArgs getAllArgsCallback(void) {
    return allArgsCallback;
}

methodWithCallbackLongReturn getLongReturnCallback(void) {
    return longReturnCallback;
}

methodWithCallbackIntReturn getIntReturnCallback(void) {
    return intReturnCallback;
}

methodWithCallbackShortReturn getShortReturnCallback(void) {
    return shortReturnCallback;
}

methodWithCallbackCharReturn getCharReturnCallback(void) {
    return charReturnCallback;
}

methodWithCallbackByteReturn getByteReturnCallback(void) {
    return byteReturnCallback;
}

methodWithCallbackBooleanReturn getBooleanReturnCallback(void) {
    return booleanReturnCallback;
}

methodWithCallbackFloatReturn getFloatReturnCallback(void) {
    return floatReturnCallback;
}

methodWithCallbackDoubleReturn getDoubleReturnCallback(void) {
    return doubleReturnCallback;
}

methodWithIntPtrPtrArg getIntPtrPtrArgCallback(void) {
    return intPtrPtrArgCallback;
}

methodWithIntPtrPtrRet getIntPtrPtrRetCallback(void) {
    return intPtrPtrRetCallback;
}

methodWithCallbackTestStructReturn getTestStructReturnCallback(void) {
    return testStructReturnCallback;
}

methodWithCallbackTestStructPointerReturn getTestStructPointerReturnCallback(void) {
    return testStructPointerReturnCallback;
}

methodWithCallbackTestStructArg getTestStructArgCallback(void) {
    return testStructArgCallback;
}

methodWithCallbackTestStructPointerArg getTestStructPointerArgCallback(void) {
    return testStructPointerArgCallback;
}

methodWithCallbackTestEnumReturn getTestEnumReturnCallback(void) {
    return testEnumReturnCallback;
}

methodWithCallbackTestEnumArg getTestEnumArgCallback(void) {
    return testEnumArgCallback;
}

methodWithCallbackTestEnumPointerReturn getTestEnumPointerReturnCallback(void) {
    return testEnumPointerReturnCallback;
}

methodWithCallbackTestEnumPointerArg getTestEnumPointerArgCallback(void) {
    return testEnumPointerArgCallback;
}

methodWithCallbackIntPointerReturn getIntPointerReturnCallback(void) {
    return intPointerReturnCallback;
}

methodWithCallbackIntPointerArg getIntPointerArgCallback(void) {
    return intPointerArgCallback;
}

methodWithCallbackTestUnionPointerReturn getTestUnionPointerReturnCallback(void) {
    return testUnionPointerReturnCallback;
}

methodWithCallbackTestUnionPointerArg getTestUnionPointerArgCallback(void) {
    return testUnionPointerArgCallback;
}

methodWithCallbackCallThrowingCallback getCallThrowingCallbackCallback(void) {
    return testCallThrowingCallback;
}