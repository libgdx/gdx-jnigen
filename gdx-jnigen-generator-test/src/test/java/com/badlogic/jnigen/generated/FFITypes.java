package com.badlogic.jnigen.generated;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import java.util.HashMap;

public class FFITypes {

    /*JNI
		#include <jnigen.h>
		#include <test_data.h>
*/
    public static void init() {
    }

    private final static HashMap<Integer, CTypeInfo> ffiIdMap = new HashMap<>();

    public static com.badlogic.gdx.jnigen.runtime.c.CTypeInfo getCTypeInfo(int id) {
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
		GET_NATIVE_TYPE(short, nativeType);
		return nativeType;
	case 7:
		GET_NATIVE_TYPE(uint16_t, nativeType);
		return nativeType;
	case 8:
		GET_NATIVE_TYPE(uint32_t, nativeType);
		return nativeType;
	case 9:
		GET_NATIVE_TYPE(uint64_t, nativeType);
		return nativeType;
	case 10:
		GET_NATIVE_TYPE(uint8_t, nativeType);
		return nativeType;
	case 11:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 2;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 2);
		nativeType->fields[0] = getNativeType(-1);
		nativeType->fields[1] = getNativeType(-1);
		return nativeType;
	case 12:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 2;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 2);
		nativeType->fields[0] = getNativeType(24);
		nativeType->fields[1] = getNativeType(5);
		return nativeType;
	case 13:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 3;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 3);
		nativeType->fields[0] = getNativeType(25);
		nativeType->fields[1] = getNativeType(25);
		nativeType->fields[2] = getNativeType(5);
		return nativeType;
	case 14:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 3;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 3);
		nativeType->fields[0] = getNativeType(5);
		nativeType->fields[1] = getNativeType(4);
		nativeType->fields[2] = getNativeType(5);
		return nativeType;
	case 15:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 5;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 5);
		nativeType->fields[0] = getNativeType(5);
		nativeType->fields[1] = getNativeType(5);
		nativeType->fields[2] = getNativeType(4);
		nativeType->fields[3] = getNativeType(5);
		nativeType->fields[4] = getNativeType(4);
		return nativeType;
	case 16:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 3;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 3);
		nativeType->fields[0] = getNativeType(5);
		nativeType->fields[1] = getNativeType(5);
		nativeType->fields[2] = getNativeType(4);
		return nativeType;
	case 17:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 3;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 3);
		nativeType->fields[0] = getNativeType(5);
		nativeType->fields[1] = getNativeType(4);
		nativeType->fields[2] = getNativeType(5);
		return nativeType;
	case 18:
		nativeType->type = UNION_TYPE;
		nativeType->field_count = 16;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 16);
		nativeType->fields[0] = getNativeType(9);
		nativeType->fields[1] = getNativeType(5);
		nativeType->fields[2] = getNativeType(6);
		nativeType->fields[3] = getNativeType(1);
		nativeType->fields[4] = getNativeType(7);
		nativeType->fields[5] = getNativeType(0);
		nativeType->fields[6] = getNativeType(4);
		nativeType->fields[7] = getNativeType(3);
		nativeType->fields[8] = getNativeType(-1);
		nativeType->fields[9] = getNativeType(-1);
		nativeType->fields[10] = getNativeType(20);
		nativeType->fields[11] = getNativeType(-1);
		nativeType->fields[12] = getNativeType(5);
		nativeType->fields[13] = getNativeType(-1);
		nativeType->fields[14] = getNativeType(-1);
		nativeType->fields[15] = getNativeType(22);
		return nativeType;
	case 19:
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
	case 20:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 4;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 4);
		nativeType->fields[0] = getNativeType(9);
		nativeType->fields[1] = getNativeType(8);
		nativeType->fields[2] = getNativeType(7);
		nativeType->fields[3] = getNativeType(10);
		return nativeType;
	case 21:
		nativeType->type = UNION_TYPE;
		nativeType->field_count = 6;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 6);
		nativeType->fields[0] = getNativeType(9);
		nativeType->fields[1] = getNativeType(3);
		nativeType->fields[2] = getNativeType(5);
		nativeType->fields[3] = getNativeType(5);
		nativeType->fields[4] = getNativeType(5);
		nativeType->fields[5] = getNativeType(20);
		return nativeType;
	case 22:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 8;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 8);
		nativeType->fields[0] = getNativeType(9);
		nativeType->fields[1] = getNativeType(5);
		nativeType->fields[2] = getNativeType(6);
		nativeType->fields[3] = getNativeType(1);
		nativeType->fields[4] = getNativeType(7);
		nativeType->fields[5] = getNativeType(0);
		nativeType->fields[6] = getNativeType(4);
		nativeType->fields[7] = getNativeType(3);
		return nativeType;
	case 23:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 0;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 0);
		return nativeType;
	case 24:
		nativeType->type = STRUCT_TYPE;
		nativeType->field_count = 2;
		nativeType->fields = (native_type**)malloc(sizeof(native_type*) * 2);
		nativeType->fields[0] = getNativeType(5);
		nativeType->fields[1] = getNativeType(4);
		return nativeType;
	case 25:
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
        ffiIdMap.put(-2, CHandler.constructCTypeFromNativeType("void", getNativeType(-2)));
        ffiIdMap.put(-1, CHandler.constructCTypeFromNativeType("void*", getNativeType(-1)));
        ffiIdMap.put(0, CHandler.constructCTypeFromNativeType("bool", getNativeType(0)));
        CHandler.registerCType(ffiIdMap.get(0));
        ffiIdMap.put(1, CHandler.constructCTypeFromNativeType("char", getNativeType(1)));
        CHandler.registerCType(ffiIdMap.get(1));
        ffiIdMap.put(2, CHandler.constructCTypeFromNativeType("const char", getNativeType(2)));
        CHandler.registerCType(ffiIdMap.get(2));
        ffiIdMap.put(3, CHandler.constructCTypeFromNativeType("double", getNativeType(3)));
        CHandler.registerCType(ffiIdMap.get(3));
        ffiIdMap.put(4, CHandler.constructCTypeFromNativeType("float", getNativeType(4)));
        CHandler.registerCType(ffiIdMap.get(4));
        ffiIdMap.put(5, CHandler.constructCTypeFromNativeType("int", getNativeType(5)));
        CHandler.registerCType(ffiIdMap.get(5));
        ffiIdMap.put(6, CHandler.constructCTypeFromNativeType("short", getNativeType(6)));
        CHandler.registerCType(ffiIdMap.get(6));
        ffiIdMap.put(7, CHandler.constructCTypeFromNativeType("uint16_t", getNativeType(7)));
        CHandler.registerCType(ffiIdMap.get(7));
        ffiIdMap.put(8, CHandler.constructCTypeFromNativeType("uint32_t", getNativeType(8)));
        CHandler.registerCType(ffiIdMap.get(8));
        ffiIdMap.put(9, CHandler.constructCTypeFromNativeType("uint64_t", getNativeType(9)));
        CHandler.registerCType(ffiIdMap.get(9));
        ffiIdMap.put(10, CHandler.constructCTypeFromNativeType("uint8_t", getNativeType(10)));
        CHandler.registerCType(ffiIdMap.get(10));
        ffiIdMap.put(11, CHandler.constructStackElementCTypeFromNativeType(null, getNativeType(11)));
        ffiIdMap.put(12, CHandler.constructStackElementCTypeFromNativeType(null, getNativeType(12)));
        ffiIdMap.put(13, CHandler.constructStackElementCTypeFromNativeType(null, getNativeType(13)));
        ffiIdMap.put(14, CHandler.constructStackElementCTypeFromNativeType(null, getNativeType(14)));
        ffiIdMap.put(15, CHandler.constructStackElementCTypeFromNativeType(null, getNativeType(15)));
        ffiIdMap.put(16, CHandler.constructStackElementCTypeFromNativeType(null, getNativeType(16)));
        ffiIdMap.put(17, CHandler.constructStackElementCTypeFromNativeType(null, getNativeType(17)));
        ffiIdMap.put(18, CHandler.constructStackElementCTypeFromNativeType(null, getNativeType(18)));
        ffiIdMap.put(19, CHandler.constructStackElementCTypeFromNativeType(null, getNativeType(19)));
        ffiIdMap.put(20, CHandler.constructStackElementCTypeFromNativeType(null, getNativeType(20)));
        ffiIdMap.put(21, CHandler.constructStackElementCTypeFromNativeType(null, getNativeType(21)));
        ffiIdMap.put(22, CHandler.constructStackElementCTypeFromNativeType(null, getNativeType(22)));
        ffiIdMap.put(23, CHandler.constructStackElementCTypeFromNativeType(null, getNativeType(23)));
        ffiIdMap.put(24, CHandler.constructStackElementCTypeFromNativeType(null, getNativeType(24)));
        ffiIdMap.put(25, CHandler.constructStackElementCTypeFromNativeType(null, getNativeType(25)));
    }
}
