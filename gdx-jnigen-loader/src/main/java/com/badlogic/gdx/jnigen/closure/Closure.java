package com.badlogic.gdx.jnigen.closure;

import com.badlogic.gdx.jnigen.c.CTypeInfo;
import com.badlogic.gdx.jnigen.ffi.JavaTypeWrapper;

public interface Closure {

    CTypeInfo[] functionSignature();

    void invoke(JavaTypeWrapper[] parameter, JavaTypeWrapper returnType);

}
