package com.badlogic.gdx.jnigen.runtime.c;

public interface CEnum {

    /**
     * Returns the associated number of the enum constant
     */
    int getIndex();

    /**
     * Returns the underlying size of the backing C type in bytes
     */
    int getSize();
}
