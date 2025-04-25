package com.badlogic.gdx.jnigen.runtime.util;

import com.badlogic.gdx.jnigen.runtime.closure.CClosureObject;
import com.badlogic.gdx.jnigen.runtime.closure.Closure;

public interface DowncallCClosureObjectSupplier<T extends Closure> {

    CClosureObject<T> get(long fnPtr);
}
