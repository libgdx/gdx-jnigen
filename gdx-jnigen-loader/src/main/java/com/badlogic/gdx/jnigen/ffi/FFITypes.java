package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.CHandler;

public class FFITypes {


    /*JNI
        #include <ffi.h>
        #include <jnigen.h>
    */

    public static void init(){}

    native private static long getFFIType(int id);/*
    	switch(id) {
    		case 0:
    			return reinterpret_cast<jlong>(GET_FFI_TYPE(uint64_t));
    		case 1:
    			return reinterpret_cast<jlong>(GET_FFI_TYPE(uint32_t));
    		case 2:
    			return reinterpret_cast<jlong>(GET_FFI_TYPE(uint16_t));
    		case 3:
    			return reinterpret_cast<jlong>(GET_FFI_TYPE(uint8_t));
    		case 4:
    			return reinterpret_cast<jlong>(GET_FFI_TYPE(int));
    		case 5:
    			return reinterpret_cast<jlong>(GET_FFI_TYPE(short));
    		case 6:
    			return reinterpret_cast<jlong>(GET_FFI_TYPE(char));
    		case 7:
    			return reinterpret_cast<jlong>(GET_FFI_TYPE(bool));
    		case 8:
    			return reinterpret_cast<jlong>(GET_FFI_TYPE(float));
    		case 9:
    			return reinterpret_cast<jlong>(GET_FFI_TYPE(double));
            case 10:
    			return reinterpret_cast<jlong>(&ffi_type_pointer);
    		default:
    			return -1;
    	}
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
        CHandler.registerCTypeFFIType("void*", getFFIType(10));
    }
}
