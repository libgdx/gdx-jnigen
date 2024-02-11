package com.badlogic.jnigen.generated;

import com.badlogic.gdx.jnigen.CHandler;

public class FFITypes {

    /*JNI
		#include <jnigen.h>
*/
    public static void init() {
    }

    /*JNI
ffi_type* getFFIType(int id) {
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
		return GET_FFI_TYPE(int);
	case 5:
		return GET_FFI_TYPE(short);
	case 6:
		return GET_FFI_TYPE(char);
	case 7:
		return GET_FFI_TYPE(bool);
	case 8:
		return GET_FFI_TYPE(float);
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
        CHandler.registerCTypeFFIType("int", getFFIType(4));
        CHandler.registerCTypeFFIType("short", getFFIType(5));
        CHandler.registerCTypeFFIType("char", getFFIType(6));
        CHandler.registerCTypeFFIType("bool", getFFIType(7));
        CHandler.registerCTypeFFIType("float", getFFIType(8));
        CHandler.registerCTypeFFIType("double", getFFIType(9));
    }
}
