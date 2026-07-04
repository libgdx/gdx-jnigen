package com.badlogic.gdx.jnigen.runtime.closure;

import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;

public final class ClosureDecoder<T extends Closure> {

    private final T toCallOn;
    private PointingPoolManager poolManager;
    private final ThreadLocal<PointingPoolManager> localManager = new ThreadLocal<>();

    public ClosureDecoder(T toCallOn) {
        this.toCallOn = toCallOn;
    }

    public void setPoolManager(PointingPoolManager poolManager) {
        this.poolManager = poolManager;
    }

    public void invoke(BufferPtr buf) {
        PointingPoolManager template = poolManager;
        if (template == null) {
            toCallOn.invoke(buf);
            return;
        }

        PointingPoolManager mgr = localManager.get();
        if (mgr == null) {
            mgr = template.newThreadLocalClone();
            localManager.set(mgr);
        }
        toCallOn.invokePooled(buf, mgr);
        mgr.flush();
    }
}
