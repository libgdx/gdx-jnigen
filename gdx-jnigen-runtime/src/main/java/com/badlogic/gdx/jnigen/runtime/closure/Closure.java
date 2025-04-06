package com.badlogic.gdx.jnigen.runtime.closure;

import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;

public interface Closure {

    CTypeInfo[] functionSignature();

    void invoke(BufferPtr buf);
}
