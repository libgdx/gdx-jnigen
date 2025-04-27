package com.badlogic.gdx.jnigen.runtime.pointer;

/**
 * A simple functional interface to create Pointing's for a long pointer
 */
public interface PointerDereferenceSupplier<S extends Pointing> {

    S create(long pointer, boolean freeOnGC);
}
