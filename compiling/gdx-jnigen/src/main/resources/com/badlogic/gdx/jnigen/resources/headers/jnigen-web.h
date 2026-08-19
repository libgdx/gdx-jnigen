/**
 * JNI compatibility shim for Emscripten/WebAssembly builds.
 *
 * Provides JNI-like type definitions so that code written for JNI
 * can compile under Emscripten without modification.
 */

#ifndef JNIGEN_WEB_H
#define JNIGEN_WEB_H

#include <emscripten.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>

/* JNI primitive type mappings */
typedef int8_t   jbyte;
typedef int16_t  jshort;
typedef int32_t  jint;
typedef int64_t  jlong;
typedef float    jfloat;
typedef double   jdouble;
typedef uint8_t  jboolean;
typedef uint16_t jchar;
typedef jint     jsize;

/* JNI reference types - opaque pointers on web */
typedef void*    jobject;
typedef void*    jclass;
typedef void*    jstring;
typedef void*    jarray;
typedef void*    jbyteArray;
typedef void*    jshortArray;
typedef void*    jintArray;
typedef void*    jlongArray;
typedef void*    jfloatArray;
typedef void*    jdoubleArray;
typedef void*    jbooleanArray;
typedef void*    jcharArray;
typedef void*    jobjectArray;
typedef void*    jthrowable;
typedef void*    jweak;
typedef void*    jmethodID;
typedef void*    jfieldID;

/* JNIEnv is not used in Emscripten builds */
typedef void*    JNIEnv;
typedef void*    JavaVM;

/* Export/calling convention macros */
#define JNIEXPORT EMSCRIPTEN_KEEPALIVE
#define JNICALL
#define JNIIMPORT

/* JNI boolean constants */
#define JNI_TRUE  1
#define JNI_FALSE 0

/* No-op macros for JNI env handling */
#define JNI_OnLoad(vm, reserved) __attribute__((unused)) static int _jni_onload_unused
#define JNI_OnUnload(vm, reserved) __attribute__((unused)) static int _jni_onunload_unused

#endif /* JNIGEN_WEB_H */
