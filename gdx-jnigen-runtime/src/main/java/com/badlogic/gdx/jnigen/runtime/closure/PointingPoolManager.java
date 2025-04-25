package com.badlogic.gdx.jnigen.runtime.closure;

import com.badlogic.gdx.jnigen.runtime.pointer.*;

import java.util.HashMap;

public class PointingPoolManager {

    private final HashMap<Class<?>, PointingPool<?>> pools = new HashMap<>();
    private final Pointing[] frame;
    private int count;

    public PointingPoolManager (int size) {
        this.frame = new Pointing[size];
    }

    @SuppressWarnings("unchecked")
    public <T extends Pointing> T poll(Class<T> clazz) {
        if (count >= frame.length)
            throw new IllegalStateException("Pooled objects exceed max size of " + frame.length);
        PointingPool<T> pool = (PointingPool<T>)pools.get(clazz);
        if (pool == null)
            throw new IllegalArgumentException("No PointingPool found for " + clazz);
        T obj = pool.pollOrCreate();
        frame[count++] = obj;
        return obj;
    }

    public <T extends Pointing> T getPointing(Class<T> clazz, long ptr) {
        if (PointerPointer.class.isAssignableFrom(clazz))
            throw new IllegalArgumentException("Call getPointerPointer to retrieve a PointerPointer");
        T pointing = poll(clazz);
        pointing.setPointer(ptr);
        return pointing;
    }

    public <S extends Pointing, T extends PointerPointer<S>> T getPointerPointer(Class<T> clazz, long ptr, PointerDereferenceSupplier<S> supplier) {
        T pointing = poll(clazz);
        pointing.setPointer(ptr);
        pointing.setPointerSupplier(supplier);
        return pointing;
    }

    public <T extends Pointing> void addPool(PointerDereferenceSupplier<T> supplier, int capacity) {
        PointingPool<T> pool = new PointingPool<>(capacity, supplier);
        addPool(pool);
    }

    public <T extends Pointing> void addPool(PointingPool<T> pool) {
        T obj = pool.pollOrCreate();
        pools.put(obj.getClass(), pool);
        pool.offer(obj);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void flush() {
        for (int i = 0; i < count; i++) {
            Pointing obj = frame[i];
            if (obj instanceof StackElement)
                obj.free();
            PointingPool pool = pools.get(obj.getClass());
            pool.offer(obj);
            frame[i] = null;
        }
        count = 0;
    }
}
