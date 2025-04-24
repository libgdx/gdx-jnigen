package com.badlogic.gdx.jnigen.runtime.closure;

import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.gdx.jnigen.runtime.pointer.PointingPool;

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
        T pointing = poll(clazz);
        pointing.setPointer(ptr);
        return pointing;
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
            PointingPool pool = pools.get(obj.getClass());
            pool.offer(obj);
            frame[i] = null;
        }
        count = 0;
    }
}
