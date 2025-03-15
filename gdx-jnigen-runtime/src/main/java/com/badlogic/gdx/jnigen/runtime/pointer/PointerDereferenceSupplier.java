package com.badlogic.gdx.jnigen.runtime.pointer;

public interface PointerDereferenceSupplier<S extends Pointing> {

    S create(long pointer, boolean freeOnGC);
}
