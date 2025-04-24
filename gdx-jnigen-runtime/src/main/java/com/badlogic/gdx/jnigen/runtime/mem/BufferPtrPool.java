package com.badlogic.gdx.jnigen.runtime.mem;

import com.badlogic.gdx.jnigen.runtime.util.SynchronizedPool;

public class BufferPtrPool extends SynchronizedPool<BufferPtr> {

    public BufferPtrPool(int capacity) {
        super(capacity);
    }

    @Override
    public void reset(BufferPtr obj) {
        obj.reset(null, 0, 0, 0);
    }
}
