package com.badlogic.gdx.jnigen.runtime.closure;

import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;

public final class ClosureDecoder<T extends Closure> {

    private final T toCallOn;
    private PointingPoolManager poolManager;

    public ClosureDecoder(T toCallOn) {
        this.toCallOn = toCallOn;
    }

    public void setPoolManager(PointingPoolManager poolManager) {
        this.poolManager = poolManager;
    }

    public void invoke(BufferPtr buf) {
        if (poolManager == null) {
            toCallOn.invoke(buf);
            return;
        }

        toCallOn.invokePooled(buf, poolManager);
        poolManager.flush();
    }
}
