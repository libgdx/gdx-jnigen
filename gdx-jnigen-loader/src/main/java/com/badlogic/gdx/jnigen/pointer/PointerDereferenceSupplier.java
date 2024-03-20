package com.badlogic.gdx.jnigen.pointer;

@FunctionalInterface
public interface PointerDereferenceSupplier<S extends Pointing> {

    S create(long pointer, boolean freeOnGC);
}
