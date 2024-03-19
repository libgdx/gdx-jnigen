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
		return GET_FFI_TYPE(double);
	case 3:
		return GET_FFI_TYPE(float);
	case 4:
		return GET_FFI_TYPE(int);
	case 5:
		return GET_FFI_TYPE(short);
	case 6:
		return GET_FFI_TYPE(uint16_t);
	case 7:
		return GET_FFI_TYPE(uint32_t);
	case 8:
		return GET_FFI_TYPE(uint64_t);
	case 9:
		return GET_FFI_TYPE(uint8_t);
	case 10:
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
	case 11:
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
	case 12:
	{
		ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
		type->type = FFI_TYPE_STRUCT;
		type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * 7);
		type->elements[0] = GET_FFI_TYPE(uint64_t);
		type->elements[1] = GET_FFI_TYPE(double);
		type->elements[2] = GET_FFI_TYPE(int);
		type->elements[3] = GET_FFI_TYPE(int);
		type->elements[4] = GET_FFI_TYPE(int);
		type->elements[5] = getFFIType(11);
		type->elements[6] = NULL;
		calculateAlignmentAndOffset(type, false);
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
        ffiIdMap.put(2, CHandler.constructCTypeFromFFIType("double", getFFITypeNative(2)));
        CHandler.registerCType(ffiIdMap.get(2));
        ffiIdMap.put(3, CHandler.constructCTypeFromFFIType("float", getFFITypeNative(3)));
        CHandler.registerCType(ffiIdMap.get(3));
        ffiIdMap.put(4, CHandler.constructCTypeFromFFIType("int", getFFITypeNative(4)));
        CHandler.registerCType(ffiIdMap.get(4));
        ffiIdMap.put(5, CHandler.constructCTypeFromFFIType("short", getFFITypeNative(5)));
        CHandler.registerCType(ffiIdMap.get(5));
        ffiIdMap.put(6, CHandler.constructCTypeFromFFIType("uint16_t", getFFITypeNative(6)));
        CHandler.registerCType(ffiIdMap.get(6));
        ffiIdMap.put(7, CHandler.constructCTypeFromFFIType("uint32_t", getFFITypeNative(7)));
        CHandler.registerCType(ffiIdMap.get(7));
        ffiIdMap.put(8, CHandler.constructCTypeFromFFIType("uint64_t", getFFITypeNative(8)));
        CHandler.registerCType(ffiIdMap.get(8));
        ffiIdMap.put(9, CHandler.constructCTypeFromFFIType("uint8_t", getFFITypeNative(9)));
        CHandler.registerCType(ffiIdMap.get(9));
        ffiIdMap.put(10, CHandler.constructStackElementCTypeFromFFIType(null, getFFITypeNative(10), true));
        ffiIdMap.put(11, CHandler.constructStackElementCTypeFromFFIType(null, getFFITypeNative(11), true));
        ffiIdMap.put(12, CHandler.constructStackElementCTypeFromFFIType(null, getFFITypeNative(12), false));
    }
}
