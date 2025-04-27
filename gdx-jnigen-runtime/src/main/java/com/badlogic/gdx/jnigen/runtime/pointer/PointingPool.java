package com.badlogic.gdx.jnigen.runtime.pointer;

import com.badlogic.gdx.jnigen.runtime.util.SynchronizedPool;

/**
 * A syncronized pool to pool Pointing objects for Closures
 */
public class PointingPool<T extends Pointing> extends SynchronizedPool<T> {

    private final PointerDereferenceSupplier<T> supplier;

    public PointingPool(int capacity, PointerDereferenceSupplier<T> supplier) {
        super(capacity);
        this.supplier = supplier;
    }

    @Override
    public T pollOrCreate() {
        T obj = poll();
        if (obj == null)
            obj = supplier.create(0, false);

        return obj;
    }

    @Override
    public void reset(T obj) {
        obj.setPointer(0);
    }
}
