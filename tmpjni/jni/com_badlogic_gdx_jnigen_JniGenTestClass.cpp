#include <com_badlogic_gdx_jnigen_JniGenTestClass.h>
#include <com_badlogic_gdx_jnigen_JniGenTestClass_TestInner.h>
JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_JniGenTestClass_testBoolean(JNIEnv* env, jclass clazz, jboolean boolArg) {


//@line:7
asdfasdf
		return boolArg;


}

JNIEXPORT jbyte JNICALL Java_com_badlogic_gdx_jnigen_JniGenTestClass_testByte(JNIEnv* env, jclass clazz, jbyte byteArg) {


//@line:11

		return byteArg;


}

JNIEXPORT jchar JNICALL Java_com_badlogic_gdx_jnigen_JniGenTestClass_testChar(JNIEnv* env, jclass clazz, jchar charArg) {


//@line:15

		return charArg;


}

JNIEXPORT jshort JNICALL Java_com_badlogic_gdx_jnigen_JniGenTestClass_testShort(JNIEnv* env, jclass clazz, jshort shortArg) {


//@line:19

		return shortArg;


}

JNIEXPORT jint JNICALL Java_com_badlogic_gdx_jnigen_JniGenTestClass_testInt(JNIEnv* env, jclass clazz, jint intArg) {


//@line:23

		return intArg;


}

JNIEXPORT jlong JNICALL Java_com_badlogic_gdx_jnigen_JniGenTestClass_testLong(JNIEnv* env, jclass clazz, jlong longArg) {


//@line:27

		return longArg;


}

JNIEXPORT jfloat JNICALL Java_com_badlogic_gdx_jnigen_JniGenTestClass_testFloat(JNIEnv* env, jclass clazz, jfloat floatArg) {


//@line:31

		return floatArg;


}

JNIEXPORT jdouble JNICALL Java_com_badlogic_gdx_jnigen_JniGenTestClass_testDouble(JNIEnv* env, jclass clazz, jdouble doubleArg) {


//@line:35

		return doubleArg;


}

static inline jboolean wrapped_Java_com_badlogic_gdx_jnigen_JniGenTestClass_test
(JNIEnv* env, jclass clazz, jboolean boolArg, jbyte byteArg, jchar charArg, jshort shortArg, jint intArg, jlong longArg, jfloat floatArg, jdouble doubleArg, jobject obj_byteBuffer, jbooleanArray obj_boolArray, jcharArray obj_charArray, jshortArray obj_shortArray, jintArray obj_intArray, jlongArray obj_longArray, jfloatArray obj_floatArray, jdoubleArray obj_doubleArray, jobjectArray multidim, jstring obj_string, jclass classy, jthrowable thr, jobject obj, unsigned char* byteBuffer, char* string, bool* boolArray, unsigned short* charArray, short* shortArray, int* intArray, long long* longArray, float* floatArray, double* doubleArray) {

//@line:42

			printf("boolean: %s\n", boolArg ? "true" : "false");
			printf("byte: %d\n", byteArg);
			printf("char: %c\n", charArg);
			printf("short: %d\n" , shortArg);
			printf("int: %d\n", intArg);
			printf("long: %l\n", longArg);
			printf("float: %f\n", floatArg);
			printf("double: %d\n", doubleArg);
			printf("byteBuffer: %d\n", byteBuffer [0]);
			printf("bool[0]: %s\n", boolArray [ 0]?"true" : "false");
			printf("char[0]: %c\n", charArray [0]);
			printf("short[0]: %d\n", shortArray [0]);
			printf("int[0]: %d\n", intArray [0]);
			printf("long[0]: %lld\n", longArray [0]);
			printf("float[0]: %f\n", floatArray [0]);
			printf("double[0]: %f\n", doubleArray [0]);
			printf("string: %s fuck this nuts\n", string);
			return true;

}

JNIEXPORT jboolean JNICALL Java_com_badlogic_gdx_jnigen_JniGenTestClass_test(JNIEnv* env, jclass clazz, jboolean boolArg, jbyte byteArg, jchar charArg, jshort shortArg, jint intArg, jlong longArg, jfloat floatArg, jdouble doubleArg, jobject obj_byteBuffer, jbooleanArray obj_boolArray, jcharArray obj_charArray, jshortArray obj_shortArray, jintArray obj_intArray, jlongArray obj_longArray, jfloatArray obj_floatArray, jdoubleArray obj_doubleArray, jobjectArray multidim, jstring obj_string, jclass classy, jthrowable thr, jobject obj) {
	unsigned char* byteBuffer = (unsigned char*)(obj_byteBuffer?env->GetDirectBufferAddress(obj_byteBuffer):0);
	char* string = (char*)env->GetStringUTFChars(obj_string, 0);
	bool* boolArray = (bool*)env->GetPrimitiveArrayCritical(obj_boolArray, 0);
	unsigned short* charArray = (unsigned short*)env->GetPrimitiveArrayCritical(obj_charArray, 0);
	short* shortArray = (short*)env->GetPrimitiveArrayCritical(obj_shortArray, 0);
	int* intArray = (int*)env->GetPrimitiveArrayCritical(obj_intArray, 0);
	long long* longArray = (long long*)env->GetPrimitiveArrayCritical(obj_longArray, 0);
	float* floatArray = (float*)env->GetPrimitiveArrayCritical(obj_floatArray, 0);
	double* doubleArray = (double*)env->GetPrimitiveArrayCritical(obj_doubleArray, 0);

	jboolean JNI_returnValue = wrapped_Java_com_badlogic_gdx_jnigen_JniGenTestClass_test(env, clazz, boolArg, byteArg, charArg, shortArg, intArg, longArg, floatArg, doubleArg, obj_byteBuffer, obj_boolArray, obj_charArray, obj_shortArray, obj_intArray, obj_longArray, obj_floatArray, obj_doubleArray, multidim, obj_string, classy, thr, obj, byteBuffer, string, boolArray, charArray, shortArray, intArray, longArray, floatArray, doubleArray);

	env->ReleasePrimitiveArrayCritical(obj_boolArray, boolArray, 0);
	env->ReleasePrimitiveArrayCritical(obj_charArray, charArray, 0);
	env->ReleasePrimitiveArrayCritical(obj_shortArray, shortArray, 0);
	env->ReleasePrimitiveArrayCritical(obj_intArray, intArray, 0);
	env->ReleasePrimitiveArrayCritical(obj_longArray, longArray, 0);
	env->ReleasePrimitiveArrayCritical(obj_floatArray, floatArray, 0);
	env->ReleasePrimitiveArrayCritical(obj_doubleArray, doubleArray, 0);
	env->ReleaseStringUTFChars(obj_string, string);

	return JNI_returnValue;
}

JNIEXPORT jint JNICALL Java_com_badlogic_gdx_jnigen_JniGenTestClass_00024TestInner_testInner(JNIEnv* env, jclass clazz, jint arg) {


//@line:64

			return arg + 1;


}

