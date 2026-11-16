package com.badlogic.gdx.jnigen.runtime.closure;

import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;

public final class ClosureDecoder<T extends Closure> {

    private final T toCallOn;
    private volatile ThreadLocal<PointingPoolManager> localManager;

    public ClosureDecoder(T toCallOn) {
        this.toCallOn = toCallOn;
    }

    public void setPoolManager(final PointingPoolManager poolManager) {
        this.localManager = new ThreadLocal<PointingPoolManager>() {
            @Override
            protected PointingPoolManager initialValue() {
                return poolManager.newClone();
            }
        };
    }

    public void invoke(BufferPtr buf) {
        ThreadLocal<PointingPoolManager> local = localManager;
        if (local == null) {
            toCallOn.invoke(buf);
            return;
        }
        PointingPoolManager mgr = local.get();
        toCallOn.invokePooled(buf, mgr);
        mgr.flush();
    }
}
