/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_badlogic_gdx_jnigen_runtime_CHandler */

#ifndef _Included_com_badlogic_gdx_jnigen_runtime_CHandler
#define _Included_com_badlogic_gdx_jnigen_runtime_CHandler
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    is32Bit
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_is32Bit
  (JNIEnv *, jclass);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    isCompiledWin
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_isCompiledWin
  (JNIEnv *, jclass);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    isCharSigned
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_isCharSigned
  (JNIEnv *, jclass);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    init
 * Signature: (Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/Class;Ljava/lang/Class;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_init
  (JNIEnv *, jclass, jobject, jobject);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    testIllegalArgumentExceptionThrowable
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_testIllegalArgumentExceptionThrowable
  (JNIEnv *, jclass, jclass);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    testCXXExceptionThrowable
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_testCXXExceptionThrowable
  (JNIEnv *, jclass, jclass);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    setDisableCXXExceptionMessage
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_setDisableCXXExceptionMessage
  (JNIEnv *, jclass, jboolean);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    reExportSymbolsGlobally
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_reExportSymbolsGlobally
  (JNIEnv *, jclass, jstring);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    nativeCreateCif
 * Signature: (JLjava/nio/ByteBuffer;I)J
 */
JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_nativeCreateCif
  (JNIEnv *, jclass, jlong, jobject, jint);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    createClosureForObject
 * Signature: (JLcom/badlogic/gdx/jnigen/ffi/ClosureInfo;Ljava/nio/ByteBuffer;)J
 */
JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_createClosureForObject
  (JNIEnv *, jclass, jlong, jobject, jobject);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    getSizeFromFFIType
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_getSizeFromFFIType
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    freeClosure
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_freeClosure
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    malloc
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_malloc
  (JNIEnv *, jclass, jlong);

/*
* Class:     com_badlogic_gdx_jnigen_runtime_CHandler
* Method:    calloc
* Signature: (JJ)J
*/
JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_calloc
(JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    free
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_free
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    memcpy
 * Signature: (JJJ)V
 */
JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_memcpy
  (JNIEnv *, jclass, jlong, jlong, jlong);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    clone
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_clone
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    convertNativeTypeToFFIType
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_convertNativeTypeToFFIType
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    dispatchCCall
 * Signature: (JJJ)V
 */
JNIEXPORT void JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_dispatchCCall
  (JNIEnv *, jclass, jlong, jlong, jlong);

/*
 * Class:     com_badlogic_gdx_jnigen_runtime_CHandler
 * Method:    wrapPointer
 * Signature: (JI)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_com_badlogic_gdx_jnigen_runtime_CHandler_wrapPointer
  (JNIEnv *, jclass, jlong, jint);

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved);
JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved);

#ifdef __cplusplus
}
#endif
#endif
