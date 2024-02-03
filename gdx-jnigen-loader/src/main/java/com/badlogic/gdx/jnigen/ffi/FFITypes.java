package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.CHandler;

public class FFITypes {


    /*JNI
        #include "definitions.h"
    */

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
    			return reinterpret_cast<jlong>(GET_FFI_TYPE(float));
    		case 5:
    			return reinterpret_cast<jlong>(GET_FFI_TYPE(double));
    		case 6:
    			return reinterpret_cast<jlong>(GET_FFI_TYPE(int));
    		default:
    			return -1;
    	}
    */

    public static void init(){}

    static {
        CHandler.registerCTypeFFIType("uint64_t", getFFIType(0));
        CHandler.registerCTypeFFIType("uint32_t", getFFIType(1));
        CHandler.registerCTypeFFIType("uint16_t", getFFIType(2));
        CHandler.registerCTypeFFIType("uint8_t", getFFIType(3));
        CHandler.registerCTypeFFIType("float", getFFIType(4));
        CHandler.registerCTypeFFIType("double", getFFIType(5));
        CHandler.registerCTypeFFIType("int", getFFIType(6));
    }
}
