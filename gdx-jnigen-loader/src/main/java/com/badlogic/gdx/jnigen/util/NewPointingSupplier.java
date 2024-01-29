package com.badlogic.gdx.jnigen.util;

import com.badlogic.gdx.jnigen.pointer.Pointing;

@FunctionalInterface
public interface NewPointingSupplier<T extends Pointing> {

    T create();
}
