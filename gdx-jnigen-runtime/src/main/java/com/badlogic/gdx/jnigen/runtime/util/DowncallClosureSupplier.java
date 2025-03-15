package com.badlogic.gdx.jnigen.runtime.util;

import com.badlogic.gdx.jnigen.runtime.closure.Closure;

public interface DowncallClosureSupplier<T extends Closure> {

    T get(long fnPtr);
}
