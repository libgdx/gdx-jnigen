package com.badlogic.gdx.jnigen.runtime.ffi;

import com.badlogic.gdx.jnigen.runtime.closure.Closure;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;

public final class ClosureDecoder<T extends Closure> {

    private final T toCallOn;

    public ClosureDecoder(T toCallOn) {
        this.toCallOn = toCallOn;
    }

    public void invoke(BufferPtr buf) {
        toCallOn.invoke(buf);
    }
}
