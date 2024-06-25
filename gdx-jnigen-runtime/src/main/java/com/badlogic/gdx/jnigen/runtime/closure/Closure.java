package com.badlogic.gdx.jnigen.runtime.closure;

import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper;

public interface Closure {

    CTypeInfo[] functionSignature();

    void invoke(JavaTypeWrapper[] parameter, JavaTypeWrapper returnType);

}
