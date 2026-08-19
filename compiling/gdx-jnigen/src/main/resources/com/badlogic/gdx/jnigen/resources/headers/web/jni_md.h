/*
 * JNI platform-specific definitions for Emscripten/WebAssembly.
 */

#ifndef _JAVASOFT_JNI_MD_H_
#define _JAVASOFT_JNI_MD_H_

#include <emscripten.h>

#define JNIEXPORT EMSCRIPTEN_KEEPALIVE
#define JNIIMPORT
#define JNICALL

typedef int jint;
typedef long long jlong;
typedef signed char jbyte;

#endif /* !_JAVASOFT_JNI_MD_H_ */
