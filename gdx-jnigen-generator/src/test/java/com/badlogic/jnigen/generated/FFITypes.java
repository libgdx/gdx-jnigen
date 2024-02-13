package com.badlogic.jnigen.generated;

import com.badlogic.gdx.jnigen.CHandler;

public class FFITypes {

    /*JNI
		#include <jnigen.h>
		#include <test_data.h>
*/
    public static void init() {
    }

    /*JNI
static ffi_type* getFFIType(int id) {
switch(id) {
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
    public native static long getFFIType(int id);/*
    	return reinterpret_cast<jlong>(getFFIType(id));
    */

    static {
        CHandler.registerCTypeFFIType("uint64_t", getFFIType(0));
        CHandler.registerCTypeFFIType("uint32_t", getFFIType(1));
        CHandler.registerCTypeFFIType("uint16_t", getFFIType(2));
        CHandler.registerCTypeFFIType("uint8_t", getFFIType(3));
        CHandler.registerCTypeFFIType("float", getFFIType(4));
        CHandler.registerCTypeFFIType("int", getFFIType(5));
        CHandler.registerCTypeFFIType("short", getFFIType(6));
        CHandler.registerCTypeFFIType("char", getFFIType(7));
        CHandler.registerCTypeFFIType("bool", getFFIType(8));
        CHandler.registerCTypeFFIType("double", getFFIType(9));
    }
}
