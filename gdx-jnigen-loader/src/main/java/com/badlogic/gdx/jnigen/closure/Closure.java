package com.badlogic.gdx.jnigen.closure;

import com.badlogic.gdx.jnigen.ffi.JavaTypeWrapper;

public interface Closure {

    long[] functionSignature();

    void invoke(JavaTypeWrapper[] parameter, JavaTypeWrapper returnType);

}
