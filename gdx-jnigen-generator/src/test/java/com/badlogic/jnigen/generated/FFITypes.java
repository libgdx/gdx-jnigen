package com.badlogic.jnigen.generated;

import com.badlogic.gdx.jnigen.CHandler;
import java.util.HashMap;

public class FFITypes {

    /*JNI
		#include <jnigen.h>
		#include <test_data.h>
*/
    public static void init() {
    }

    final static HashMap<Integer, Long> ffiIdMap = new HashMap<>();

    public static long getFFIType(int id) {
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
		return GET_FFI_TYPE(uint64_t);
	case 1:
		return GET_FFI_TYPE(uint32_t);
	case 2:
		return GET_FFI_TYPE(uint16_t);
	case 3:
		return GET_FFI_TYPE(uint8_t);
	case 4:
		return GET_FFI_TYPE(float);
	case 5:
		return GET_FFI_TYPE(int);
	case 6:
		return GET_FFI_TYPE(short);
	case 7:
		return GET_FFI_TYPE(char);
	case 8:
		return GET_FFI_TYPE(bool);
	case 9:
		return GET_FFI_TYPE(double);
	case 10:
	{
		ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));
		type->type = FFI_TYPE_STRUCT;
		type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * 5);
		type->elements[0] = GET_FFI_TYPE(uint64_t);
		type->elements[1] = GET_FFI_TYPE(uint32_t);
		type->elements[2] = GET_FFI_TYPE(uint16_t);
		type->elements[3] = GET_FFI_TYPE(uint8_t);
		type->elements[4] = NULL;
		return type;
	}
	case 11:
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
        ffiIdMap.put(-2, getFFITypeNative(-2));
        ffiIdMap.put(-1, getFFITypeNative(-1));
        CHandler.registerCTypeFFIType("void*", ffiIdMap.get(-1));
        ffiIdMap.put(0, getFFITypeNative(0));
        CHandler.registerCTypeFFIType("uint64_t", ffiIdMap.get(0));
        ffiIdMap.put(1, getFFITypeNative(1));
        CHandler.registerCTypeFFIType("uint32_t", ffiIdMap.get(1));
        ffiIdMap.put(2, getFFITypeNative(2));
        CHandler.registerCTypeFFIType("uint16_t", ffiIdMap.get(2));
        ffiIdMap.put(3, getFFITypeNative(3));
        CHandler.registerCTypeFFIType("uint8_t", ffiIdMap.get(3));
        ffiIdMap.put(4, getFFITypeNative(4));
        CHandler.registerCTypeFFIType("float", ffiIdMap.get(4));
        ffiIdMap.put(5, getFFITypeNative(5));
        CHandler.registerCTypeFFIType("int", ffiIdMap.get(5));
        ffiIdMap.put(6, getFFITypeNative(6));
        CHandler.registerCTypeFFIType("short", ffiIdMap.get(6));
        ffiIdMap.put(7, getFFITypeNative(7));
        CHandler.registerCTypeFFIType("char", ffiIdMap.get(7));
        ffiIdMap.put(8, getFFITypeNative(8));
        CHandler.registerCTypeFFIType("bool", ffiIdMap.get(8));
        ffiIdMap.put(9, getFFITypeNative(9));
        CHandler.registerCTypeFFIType("double", ffiIdMap.get(9));
        ffiIdMap.put(10, getFFITypeNative(10));
        CHandler.registerFFITypeCType(ffiIdMap.get(10), CHandler.getCTypeInfo("void*"));
        ffiIdMap.put(11, getFFITypeNative(11));
        CHandler.registerFFITypeCType(ffiIdMap.get(11), CHandler.getCTypeInfo("void*"));
    }
}
