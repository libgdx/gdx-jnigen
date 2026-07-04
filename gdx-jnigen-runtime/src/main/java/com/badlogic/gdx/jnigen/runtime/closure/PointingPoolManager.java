package com.badlogic.gdx.jnigen.runtime.closure;

import com.badlogic.gdx.jnigen.runtime.pointer.*;

import java.util.HashMap;

/**
 * A Manager that can be used for pooling Pointing objects, specifically for use with Closures. This Pool is also fixed-size.
 *
 * <p>When attached to a {@link JavaClosureObject} via {@link ClosureObject#setPoolManager(PointingPoolManager)},
 * the decoder derives one lightweight clone of this manager per invoking thread. The clones share this manager's
 * underlying {@link PointingPool}s (which are already thread-safe), but each clone owns its own per-invocation frame.
 * This keeps the {@link #poll(Class)} hot path on plain field access and makes concurrent callbacks from different
 * C threads safe without requiring callers to coordinate.
 *
 * <p>Usage contract:
 * <ul>
 *     <li>All {@link #addPool(PointingPool)} / {@link #addPool(PointerDereferenceSupplier, int)} calls must complete
 *         before the manager is handed to a closure via {@link ClosureObject#setPoolManager(PointingPoolManager)}.
 *         Do not mutate the pool configuration after a callback has fired — the shared {@code pools} map is not
 *         guarded by a lock.</li>
 *     <li>{@link ClosureObject#setPoolManager(PointingPoolManager)} should be called at most once per closure.
 *         Re-setting does not invalidate clones already materialised on other threads.</li>
 *     <li>Nested invocations on the same thread are not supported: the inner call's {@link #flush()} wipes the
 *         outer call's frame.</li>
 *     <li>Every thread that ever invokes a pooled closure retains one {@code Pointing[size]} array for the life
 *         of that thread's {@code ThreadLocalMap} entry.</li>
 * </ul>
 *
 * <p>To use this class, register {@link PointingPool}s for the relevant classes before attaching.
 */
public class PointingPoolManager {

    private final HashMap<Class<?>, PointingPool<?>> pools;
    private final int frameSize;
    private final Pointing[] frame;
    private int count;

    public PointingPoolManager (int size) {
        this.pools = new HashMap<>();
        this.frameSize = size;
        this.frame = new Pointing[size];
    }

    private PointingPoolManager (int size, HashMap<Class<?>, PointingPool<?>> pools) {
        this.pools = pools;
        this.frameSize = size;
        this.frame = new Pointing[size];
    }

    public PointingPoolManager newThreadLocalClone () {
        return new PointingPoolManager(frameSize, pools);
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
