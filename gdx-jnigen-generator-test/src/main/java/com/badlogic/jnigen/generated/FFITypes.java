package com.badlogic.jnigen.generated;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import java.util.HashMap;

public class FFITypes {

    /*JNI
		#include <jnigen.h>
		#include <test_data.h>
*/
    /*JNI
		#if defined(_WIN32)
		#if ARCH_BITS == 32
		static_assert(sizeof(void*) == 4, "Expected size of void* on 32bit is 4");
		static_assert(sizeof(bool) == 1, "Type bool has unexpected size.");
		static_assert(sizeof(uint64_t) == 8, "Type uint64_t has unexpected size.");
		static_assert(sizeof(double) == 8, "Type double has unexpected size.");
		static_assert(sizeof(uint32_t) == 4, "Type uint32_t has unexpected size.");
		static_assert(sizeof(unsigned char) == 1, "Type unsigned char has unexpected size.");
		static_assert(sizeof(signed char) == 1, "Type signed char has unexpected size.");
		static_assert(sizeof(float) == 4, "Type float has unexpected size.");
		static_assert(sizeof(int) == 4, "Type int has unexpected size.");
		static_assert(sizeof(long) == 4, "Type long has unexpected size.");
		static_assert(sizeof(uint16_t) == 2, "Type uint16_t has unexpected size.");
		static_assert(sizeof(unsigned int) == 4, "Type unsigned int has unexpected size.");
		static_assert(sizeof(const char) == 1, "Type const char has unexpected size.");
		static_assert(sizeof(char) == 1, "Type char has unexpected size.");
		static_assert(sizeof(short) == 2, "Type short has unexpected size.");
		static_assert(sizeof(uint8_t) == 1, "Type uint8_t has unexpected size.");
		#elif ARCH_BITS == 64
		static_assert(sizeof(void*) == 8, "Expected size of void* on 64bit is 8");
		static_assert(sizeof(bool) == 1, "Type bool has unexpected size.");
		static_assert(sizeof(uint64_t) == 8, "Type uint64_t has unexpected size.");
		static_assert(sizeof(double) == 8, "Type double has unexpected size.");
		static_assert(sizeof(uint32_t) == 4, "Type uint32_t has unexpected size.");
		static_assert(sizeof(unsigned char) == 1, "Type unsigned char has unexpected size.");
		static_assert(sizeof(signed char) == 1, "Type signed char has unexpected size.");
		static_assert(sizeof(float) == 4, "Type float has unexpected size.");
		static_assert(sizeof(int) == 4, "Type int has unexpected size.");
		static_assert(sizeof(long) == 4, "Type long has unexpected size.");
		static_assert(sizeof(uint16_t) == 2, "Type uint16_t has unexpected size.");
		static_assert(sizeof(unsigned int) == 4, "Type unsigned int has unexpected size.");
		static_assert(sizeof(const char) == 1, "Type const char has unexpected size.");
		static_assert(sizeof(char) == 1, "Type char has unexpected size.");
		static_assert(sizeof(short) == 2, "Type short has unexpected size.");
		static_assert(sizeof(uint8_t) == 1, "Type uint8_t has unexpected size.");
		#else
		#error Unsupported OS
		#endif
		#else
		#if ARCH_BITS == 32
		static_assert(sizeof(void*) == 4, "Expected size of void* on 32bit is 4");
		static_assert(sizeof(bool) == 1, "Type bool has unexpected size.");
		static_assert(sizeof(uint64_t) == 8, "Type uint64_t has unexpected size.");
		static_assert(sizeof(double) == 8, "Type double has unexpected size.");
		static_assert(sizeof(uint32_t) == 4, "Type uint32_t has unexpected size.");
		static_assert(sizeof(unsigned char) == 1, "Type unsigned char has unexpected size.");
		static_assert(sizeof(signed char) == 1, "Type signed char has unexpected size.");
		static_assert(sizeof(float) == 4, "Type float has unexpected size.");
		static_assert(sizeof(int) == 4, "Type int has unexpected size.");
		static_assert(sizeof(long) == 4, "Type long has unexpected size.");
		static_assert(sizeof(uint16_t) == 2, "Type uint16_t has unexpected size.");
		static_assert(sizeof(unsigned int) == 4, "Type unsigned int has unexpected size.");
		static_assert(sizeof(const char) == 1, "Type const char has unexpected size.");
		static_assert(sizeof(char) == 1, "Type char has unexpected size.");
		static_assert(sizeof(short) == 2, "Type short has unexpected size.");
		static_assert(sizeof(uint8_t) == 1, "Type uint8_t has unexpected size.");
		#elif ARCH_BITS == 64
		static_assert(sizeof(void*) == 8, "Expected size of void* on 64bit is 8");
		static_assert(sizeof(bool) == 1, "Type bool has unexpected size.");
		static_assert(sizeof(uint64_t) == 8, "Type uint64_t has unexpected size.");
		static_assert(sizeof(double) == 8, "Type double has unexpected size.");
		static_assert(sizeof(uint32_t) == 4, "Type uint32_t has unexpected size.");
		static_assert(sizeof(unsigned char) == 1, "Type unsigned char has unexpected size.");
		static_assert(sizeof(signed char) == 1, "Type signed char has unexpected size.");
		static_assert(sizeof(float) == 4, "Type float has unexpected size.");
		static_assert(sizeof(int) == 4, "Type int has unexpected size.");
		static_assert(sizeof(long) == 8, "Type long has unexpected size.");
		static_assert(sizeof(uint16_t) == 2, "Type uint16_t has unexpected size.");
		static_assert(sizeof(unsigned int) == 4, "Type unsigned int has unexpected size.");
		static_assert(sizeof(const char) == 1, "Type const char has unexpected size.");
		static_assert(sizeof(char) == 1, "Type char has unexpected size.");
		static_assert(sizeof(short) == 2, "Type short has unexpected size.");
		static_assert(sizeof(uint8_t) == 1, "Type uint8_t has unexpected size.");
		#else
		#error Unsupported OS
		#endif
		#endif
		static_assert(IS_UNSIGNED_TYPE(bool), "Type bool is expected unsigned.");
		static_assert(IS_UNSIGNED_TYPE(uint64_t), "Type uint64_t is expected unsigned.");
		static_assert(IS_SIGNED_TYPE(double), "Type double is expected signed.");
		static_assert(IS_UNSIGNED_TYPE(uint32_t), "Type uint32_t is expected unsigned.");
		static_assert(IS_UNSIGNED_TYPE(unsigned char), "Type unsigned char is expected unsigned.");
		static_assert(IS_SIGNED_TYPE(signed char), "Type signed char is expected signed.");
		static_assert(IS_SIGNED_TYPE(float), "Type float is expected signed.");
		static_assert(IS_SIGNED_TYPE(int), "Type int is expected signed.");
		static_assert(IS_SIGNED_TYPE(long), "Type long is expected signed.");
		static_assert(IS_UNSIGNED_TYPE(uint16_t), "Type uint16_t is expected unsigned.");
		static_assert(IS_UNSIGNED_TYPE(unsigned int), "Type unsigned int is expected unsigned.");
		static_assert(IS_SIGNED_TYPE(short), "Type short is expected signed.");
		static_assert(IS_UNSIGNED_TYPE(uint8_t), "Type uint8_t is expected unsigned.");
*/
    public static void init() {
    }

    private final static HashMap<Integer, CTypeInfo> ffiIdMap = new HashMap<>();

    public static CTypeInfo getCTypeInfo(int id) {
        return ffiIdMap.get(id);
    }

    /*JNI
static native_type* getNativeType(int id) {
native_type* nativeType = (native_type*)malloc(sizeof(native_type));
switch(id) {
	case -2:
		nativeType->type = VOID_TYPE;
		return nativeType;
	case -1:
		nativeType->type = POINTER_TYPE;
		return nativeType;
	case 0:
		GET_NATIVE_TYPE(bool, nativeType);
		return nativeType;
	case 1:
		GET_NATIVE_TYPE(char, nativeType);
		return nativeType;
	case 2:
		GET_NATIVE_TYPE(const char, nativeType);
		return nativeType;
	case 3:
		GET_NATIVE_TYPE(double, nativeType);
		return nativeType;
	case 4:
		GET_NATIVE_TYPE(float, nativeType);
		return nativeType;
	case 5:
		GET_NATIVE_TYPE(int, nativeType);
		return nativeType;
	case 6:
		GET_NATIVE_TYPE(long, nativeType);
		return nativeType;
	case 7:
		GET_NATIVE_TYPE(short, nativeType);
		return nativeType;
	case 8:
		GET_NATIVE_TYPE(signed char, nativeType);
		return nativeType;
	case 9:
		GET_NATIVE_TYPE(uint16_t, nativeType);
		return nativeType;
	case 10:
		GET_NATIVE_TYPE(uint32_t, nativeType);
		return nativeType;
	case 11:
		GET_NATIVE_TYPE(uint64_t, nativeType);
		return nativeType;
	case 12:
		GET_NATIVE_TYPE(uint8_t, nativeType);
		return nativeType;
	case 13:
		GET_NATIVE_TYPE(unsigned char, nativeType);
		return nativeType;
	case 14:
		GET_NATIVE_TYPE(unsigned int, nativeType);
		return nativeType;
	case 15:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 2;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 2);
		nativeType->fields[0] = getNativeType(-1);
		nativeType->fields[1] = getNativeType(-1);
		return nativeType;
	case 16:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 2;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 2);
		nativeType->fields[0] = getNativeType(28);
		nativeType->fields[1] = getNativeType(5);
		return nativeType;
	case 17:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 3;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 3);
		nativeType->fields[0] = getNativeType(29);
		nativeType->fields[1] = getNativeType(29);
		nativeType->fields[2] = getNativeType(5);
		return nativeType;
	case 18:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 3;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 3);
		nativeType->fields[0] = getNativeType(5);
		nativeType->fields[1] = getNativeType(4);
		nativeType->fields[2] = getNativeType(5);
		return nativeType;
	case 19:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 5;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 5);
		nativeType->fields[0] = getNativeType(5);
		nativeType->fields[1] = getNativeType(5);
		nativeType->fields[2] = getNativeType(4);
		nativeType->fields[3] = getNativeType(5);
		nativeType->fields[4] = getNativeType(4);
		return nativeType;
	case 20:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 3;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 3);
		nativeType->fields[0] = getNativeType(5);
		nativeType->fields[1] = getNativeType(5);
		nativeType->fields[2] = getNativeType(4);
		return nativeType;
	case 21:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 3;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 3);
		nativeType->fields[0] = getNativeType(5);
		nativeType->fields[1] = getNativeType(4);
		nativeType->fields[2] = getNativeType(5);
		return nativeType;
	case 22:
		nativeType->type = UNION_TYPE;
		nativeType->field_count = 16;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 16);
		nativeType->fields[0] = getNativeType(11);
		nativeType->fields[1] = getNativeType(5);
		nativeType->fields[2] = getNativeType(7);
		nativeType->fields[3] = getNativeType(1);
		nativeType->fields[4] = getNativeType(9);
		nativeType->fields[5] = getNativeType(0);
		nativeType->fields[6] = getNativeType(4);
		nativeType->fields[7] = getNativeType(3);
		nativeType->fields[8] = getNativeType(-1);
		nativeType->fields[9] = getNativeType(-1);
		nativeType->fields[10] = getNativeType(24);
		nativeType->fields[11] = getNativeType(-1);
		nativeType->fields[12] = getNativeType(5);
		nativeType->fields[13] = getNativeType(-1);
		nativeType->fields[14] = getNativeType(-1);
		nativeType->fields[15] = getNativeType(26);
		return nativeType;
	case 23:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 7;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 7);
		nativeType->fields[0] = getNativeType(-1);
		nativeType->fields[1] = getNativeType(5);
		nativeType->fields[2] = getNativeType(5);
		nativeType->fields[3] = getNativeType(5);
		nativeType->fields[4] = getNativeType(5);
		nativeType->fields[5] = getNativeType(5);
		nativeType->fields[6] = getNativeType(-1);
		return nativeType;
	case 24:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 4;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 4);
		nativeType->fields[0] = getNativeType(11);
		nativeType->fields[1] = getNativeType(10);
		nativeType->fields[2] = getNativeType(9);
		nativeType->fields[3] = getNativeType(12);
		return nativeType;
	case 25:
		nativeType->type = UNION_TYPE;
		nativeType->field_count = 6;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 6);
		nativeType->fields[0] = getNativeType(11);
		nativeType->fields[1] = getNativeType(3);
		nativeType->fields[2] = getNativeType(5);
		nativeType->fields[3] = getNativeType(5);
		nativeType->fields[4] = getNativeType(5);
		nativeType->fields[5] = getNativeType(24);
		return nativeType;
	case 26:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 8;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 8);
		nativeType->fields[0] = getNativeType(11);
		nativeType->fields[1] = getNativeType(5);
		nativeType->fields[2] = getNativeType(7);
		nativeType->fields[3] = getNativeType(1);
		nativeType->fields[4] = getNativeType(9);
		nativeType->fields[5] = getNativeType(0);
		nativeType->fields[6] = getNativeType(4);
		nativeType->fields[7] = getNativeType(3);
		return nativeType;
	case 27:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 0;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 0);
		return nativeType;
	case 28:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 2;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 2);
		nativeType->fields[0] = getNativeType(5);
		nativeType->fields[1] = getNativeType(4);
		return nativeType;
	case 29:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 2;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 2);
		nativeType->fields[0] = getNativeType(5);
		nativeType->fields[1] = getNativeType(4);
		return nativeType;
	default:
		return NULL;
	}
}
*/
    private native static long getNativeType(int id);/*
    	return reinterpret_cast<jlong>(getNativeType(id));
    */

    static {
        ffiIdMap.put(-2, CHandler.constructCTypeFromNativeType(getNativeType(-2)));
        ffiIdMap.put(-1, CHandler.constructCTypeFromNativeType(getNativeType(-1)));
        ffiIdMap.put(0, CHandler.constructCTypeFromNativeType(getNativeType(0)));
        ffiIdMap.put(1, CHandler.constructCTypeFromNativeType(getNativeType(1)));
        ffiIdMap.put(2, CHandler.constructCTypeFromNativeType(getNativeType(2)));
        ffiIdMap.put(3, CHandler.constructCTypeFromNativeType(getNativeType(3)));
        ffiIdMap.put(4, CHandler.constructCTypeFromNativeType(getNativeType(4)));
        ffiIdMap.put(5, CHandler.constructCTypeFromNativeType(getNativeType(5)));
        ffiIdMap.put(6, CHandler.constructCTypeFromNativeType(getNativeType(6)));
        ffiIdMap.put(7, CHandler.constructCTypeFromNativeType(getNativeType(7)));
        ffiIdMap.put(8, CHandler.constructCTypeFromNativeType(getNativeType(8)));
        ffiIdMap.put(9, CHandler.constructCTypeFromNativeType(getNativeType(9)));
        ffiIdMap.put(10, CHandler.constructCTypeFromNativeType(getNativeType(10)));
        ffiIdMap.put(11, CHandler.constructCTypeFromNativeType(getNativeType(11)));
        ffiIdMap.put(12, CHandler.constructCTypeFromNativeType(getNativeType(12)));
        ffiIdMap.put(13, CHandler.constructCTypeFromNativeType(getNativeType(13)));
        ffiIdMap.put(14, CHandler.constructCTypeFromNativeType(getNativeType(14)));
        ffiIdMap.put(15, CHandler.constructStackElementCTypeFromNativeType(getNativeType(15)));
        ffiIdMap.put(16, CHandler.constructStackElementCTypeFromNativeType(getNativeType(16)));
        ffiIdMap.put(17, CHandler.constructStackElementCTypeFromNativeType(getNativeType(17)));
        ffiIdMap.put(18, CHandler.constructStackElementCTypeFromNativeType(getNativeType(18)));
        ffiIdMap.put(19, CHandler.constructStackElementCTypeFromNativeType(getNativeType(19)));
        ffiIdMap.put(20, CHandler.constructStackElementCTypeFromNativeType(getNativeType(20)));
        ffiIdMap.put(21, CHandler.constructStackElementCTypeFromNativeType(getNativeType(21)));
        ffiIdMap.put(22, CHandler.constructStackElementCTypeFromNativeType(getNativeType(22)));
        ffiIdMap.put(23, CHandler.constructStackElementCTypeFromNativeType(getNativeType(23)));
        ffiIdMap.put(24, CHandler.constructStackElementCTypeFromNativeType(getNativeType(24)));
        ffiIdMap.put(25, CHandler.constructStackElementCTypeFromNativeType(getNativeType(25)));
        ffiIdMap.put(26, CHandler.constructStackElementCTypeFromNativeType(getNativeType(26)));
        ffiIdMap.put(27, CHandler.constructStackElementCTypeFromNativeType(getNativeType(27)));
        ffiIdMap.put(28, CHandler.constructStackElementCTypeFromNativeType(getNativeType(28)));
        ffiIdMap.put(29, CHandler.constructStackElementCTypeFromNativeType(getNativeType(29)));
    }
}
