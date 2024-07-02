package com.badlogic.gdx.jnigen.runtime.pointer;

@FunctionalInterface
public interface PointerDereferenceSupplier<S extends Pointing> {

    S create(long pointer, boolean freeOnGC);
}
