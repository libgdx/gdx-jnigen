package com.badlogic.gdx.jnigen.util;

import com.badlogic.gdx.jnigen.pointer.Pointing;

@FunctionalInterface
public interface WrappingPointingSupplier<T extends Pointing> {

    T create(long pointer, boolean freeOnGC);
}
