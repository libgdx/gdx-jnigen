package com.badlogic.jnigen.generated;

import com.badlogic.gdx.jnigen.CHandler;
import java.util.HashMap;
import com.badlogic.gdx.jnigen.c.CTypeInfo;

public class FFITypes {

    /*JNI
		#include <jnigen.h>
		#include <test_data.h>
*/
    public static void init() {
    }

    private final static HashMap<Integer, CTypeInfo> ffiIdMap = new HashMap<>();

    public static CTypeInfo getCTypeInfo(int id) {
        return ffiIdMap.get(id);
    }

    /*JNI
static ffi_type* getFFIType(int id) {
switch(id) {
	case -2:
		return &ffi_type_void;
	case -1:
		return &ffi_type_pointer;
	case 0:
		return GET_FFI_TYPE(bool);
	case 1:
		return GET_FFI_TYPE(char);
	case 2:
		return GET_FFI_TYPE(const char);
	case 3:
		return GET_FFI_TYPE(double);
	case 4:
		return GET_FFI_TYPE(float);
	case 5:
		return GET_FFI_TYPE(int);
	case 6:
		return GET_FFI_TYPE(short);
	case 7:
		return GET_FFI_TYPE(uint16_t);
	case 8:
		return GET_FFI_TYPE(uint32_t);
	case 9:
		return GET_FFI_TYPE(uint64_t);
	case 10:
		return GET_FFI_TYPE(uint8_t);
	case 11:
	{
		ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
		type->type = FFI_TYPE_STRUCT;
		type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * 3);
		type->elements[0] = &ffi_type_pointer;
		type->elements[1] = &ffi_type_pointer;
		type->elements[2] = NULL;
		calculateAlignmentAndOffset(type, true);
		return type;
	}
	case 12:
	{
		ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
		type->type = FFI_TYPE_STRUCT;
		type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * 3);
		type->elements[0] = getFFIType(21);
		type->elements[1] = GET_FFI_TYPE(int);
		type->elements[2] = NULL;
		calculateAlignmentAndOffset(type, true);
		return type;
	}
	case 13:
	{
		ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
		type->type = FFI_TYPE_STRUCT;
		type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * 4);
		type->elements[0] = getFFIType(22);
		type->elements[1] = getFFIType(22);
		type->elements[2] = GET_FFI_TYPE(int);
		type->elements[3] = NULL;
		calculateAlignmentAndOffset(type, true);
		return type;
	}
	case 14:
	{
		ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
		type->type = FFI_TYPE_STRUCT;
		type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * 4);
		type->elements[0] = GET_FFI_TYPE(int);
		type->elements[1] = GET_FFI_TYPE(float);
		type->elements[2] = GET_FFI_TYPE(int);
		type->elements[3] = NULL;
		calculateAlignmentAndOffset(type, true);
		return type;
	}
	case 15:
	{
		ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
		type->type = FFI_TYPE_STRUCT;
		type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * 6);
		type->elements[0] = GET_FFI_TYPE(int);
		type->elements[1] = GET_FFI_TYPE(int);
		type->elements[2] = GET_FFI_TYPE(float);
		type->elements[3] = GET_FFI_TYPE(int);
		type->elements[4] = GET_FFI_TYPE(float);
		type->elements[5] = NULL;
		calculateAlignmentAndOffset(type, true);
		return type;
	}
	case 16:
	{
		ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
		type->type = FFI_TYPE_STRUCT;
		type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * 4);
		type->elements[0] = GET_FFI_TYPE(int);
		type->elements[1] = GET_FFI_TYPE(int);
		type->elements[2] = GET_FFI_TYPE(float);
		type->elements[3] = NULL;
		calculateAlignmentAndOffset(type, true);
		return type;
	}
	case 17:
	{
		ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
		type->type = FFI_TYPE_STRUCT;
		type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * 4);
		type->elements[0] = GET_FFI_TYPE(int);
		type->elements[1] = GET_FFI_TYPE(float);
		type->elements[2] = GET_FFI_TYPE(int);
		type->elements[3] = NULL;
		calculateAlignmentAndOffset(type, true);
		return type;
	}
	case 18:
	{
		ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
		type->type = FFI_TYPE_STRUCT;
		type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * 8);
		type->elements[0] = &ffi_type_pointer;
		type->elements[1] = GET_FFI_TYPE(int);
		type->elements[2] = GET_FFI_TYPE(int);
		type->elements[3] = GET_FFI_TYPE(int);
		type->elements[4] = GET_FFI_TYPE(int);
		type->elements[5] = GET_FFI_TYPE(int);
		type->elements[6] = &ffi_type_pointer;
		type->elements[7] = NULL;
		calculateAlignmentAndOffset(type, true);
		return type;
	}
	case 19:
	{
		ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
		type->type = FFI_TYPE_STRUCT;
		type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * 5);
		type->elements[0] = GET_FFI_TYPE(uint64_t);
		type->elements[1] = GET_FFI_TYPE(uint32_t);
		type->elements[2] = GET_FFI_TYPE(uint16_t);
		type->elements[3] = GET_FFI_TYPE(uint8_t);
		type->elements[4] = NULL;
		calculateAlignmentAndOffset(type, true);
		return type;
	}
	case 20:
	{
		ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
		type->type = FFI_TYPE_STRUCT;
		type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * 7);
		type->elements[0] = GET_FFI_TYPE(uint64_t);
		type->elements[1] = GET_FFI_TYPE(double);
		type->elements[2] = GET_FFI_TYPE(int);
		type->elements[3] = GET_FFI_TYPE(int);
		type->elements[4] = GET_FFI_TYPE(int);
		type->elements[5] = getFFIType(19);
		type->elements[6] = NULL;
		calculateAlignmentAndOffset(type, false);
		return type;
	}
	case 21:
	{
		ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
		type->type = FFI_TYPE_STRUCT;
		type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * 3);
		type->elements[0] = GET_FFI_TYPE(int);
		type->elements[1] = GET_FFI_TYPE(float);
		type->elements[2] = NULL;
		calculateAlignmentAndOffset(type, true);
		return type;
	}
	case 22:
	{
		ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
		type->type = FFI_TYPE_STRUCT;
		type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * 3);
		type->elements[0] = GET_FFI_TYPE(int);
		type->elements[1] = GET_FFI_TYPE(float);
		type->elements[2] = NULL;
		calculateAlignmentAndOffset(type, true);
		return type;
	}
	default:
		return NULL;
	}
}
*/
    private native static long getFFITypeNative(int id);/*
    	return reinterpret_cast<jlong>(getFFIType(id));
    */

    static {
        ffiIdMap.put(-2, CHandler.constructCTypeFromFFIType("void", getFFITypeNative(-2)));
        ffiIdMap.put(-1, CHandler.constructCTypeFromFFIType("void*", getFFITypeNative(-1)));
        ffiIdMap.put(0, CHandler.constructCTypeFromFFIType("bool", getFFITypeNative(0)));
        CHandler.registerCType(ffiIdMap.get(0));
        ffiIdMap.put(1, CHandler.constructCTypeFromFFIType("char", getFFITypeNative(1)));
        CHandler.registerCType(ffiIdMap.get(1));
        ffiIdMap.put(2, CHandler.constructCTypeFromFFIType("const char", getFFITypeNative(2)));
        CHandler.registerCType(ffiIdMap.get(2));
        ffiIdMap.put(3, CHandler.constructCTypeFromFFIType("double", getFFITypeNative(3)));
        CHandler.registerCType(ffiIdMap.get(3));
        ffiIdMap.put(4, CHandler.constructCTypeFromFFIType("float", getFFITypeNative(4)));
        CHandler.registerCType(ffiIdMap.get(4));
        ffiIdMap.put(5, CHandler.constructCTypeFromFFIType("int", getFFITypeNative(5)));
        CHandler.registerCType(ffiIdMap.get(5));
        ffiIdMap.put(6, CHandler.constructCTypeFromFFIType("short", getFFITypeNative(6)));
        CHandler.registerCType(ffiIdMap.get(6));
        ffiIdMap.put(7, CHandler.constructCTypeFromFFIType("uint16_t", getFFITypeNative(7)));
        CHandler.registerCType(ffiIdMap.get(7));
        ffiIdMap.put(8, CHandler.constructCTypeFromFFIType("uint32_t", getFFITypeNative(8)));
        CHandler.registerCType(ffiIdMap.get(8));
        ffiIdMap.put(9, CHandler.constructCTypeFromFFIType("uint64_t", getFFITypeNative(9)));
        CHandler.registerCType(ffiIdMap.get(9));
        ffiIdMap.put(10, CHandler.constructCTypeFromFFIType("uint8_t", getFFITypeNative(10)));
        CHandler.registerCType(ffiIdMap.get(10));
        ffiIdMap.put(11, CHandler.constructStackElementCTypeFromFFIType(null, getFFITypeNative(11)));
        ffiIdMap.put(12, CHandler.constructStackElementCTypeFromFFIType(null, getFFITypeNative(12)));
        ffiIdMap.put(13, CHandler.constructStackElementCTypeFromFFIType(null, getFFITypeNative(13)));
        ffiIdMap.put(14, CHandler.constructStackElementCTypeFromFFIType(null, getFFITypeNative(14)));
        ffiIdMap.put(15, CHandler.constructStackElementCTypeFromFFIType(null, getFFITypeNative(15)));
        ffiIdMap.put(16, CHandler.constructStackElementCTypeFromFFIType(null, getFFITypeNative(16)));
        ffiIdMap.put(17, CHandler.constructStackElementCTypeFromFFIType(null, getFFITypeNative(17)));
        ffiIdMap.put(18, CHandler.constructStackElementCTypeFromFFIType(null, getFFITypeNative(18)));
        ffiIdMap.put(19, CHandler.constructStackElementCTypeFromFFIType(null, getFFITypeNative(19)));
        ffiIdMap.put(20, CHandler.constructStackElementCTypeFromFFIType(null, getFFITypeNative(20)));
        ffiIdMap.put(21, CHandler.constructStackElementCTypeFromFFIType(null, getFFITypeNative(21)));
        ffiIdMap.put(22, CHandler.constructStackElementCTypeFromFFIType(null, getFFITypeNative(22)));
    }
}
