package com.badlogic.gdx.jnigen.runtime.c;

/**
 * Java exception wrapper for a C++ exception
 */
public final class CXXException extends RuntimeException {

    public CXXException (String message) {
        super(message);
    }
}
