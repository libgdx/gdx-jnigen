package com.badlogic.gdx.jnigen.runtime.closure;

import com.badlogic.gdx.jnigen.runtime.pointer.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A fixed-size manager for pooling {@link Pointing} objects across closure upcalls, so a callback
 * that is invoked repeatedly does not allocate a fresh wrapper per invocation.
 *
 * <p>The instance you configure is a <em>template</em>. When attached to a {@link JavaClosureObject}
 * via {@link ClosureObject#setPoolManager(PointingPoolManager)}, the decoder lazily derives one
 * {@link #newClone() clone} per invoking thread. Each clone owns its own per-invocation frame
 * <em>and</em> its own private {@link PointingPool}s, so {@link #poll(Class)} and {@link #flush()}
 * run entirely on plain field access — no locks, no atomics — and concurrent callbacks from
 * different C threads never share pool state.
 *
 * <p>Usage contract:
 * <ul>
 *     <li>Register all pools via {@link #addPool(PointingPool)} / {@link #addPool(PointerDereferenceSupplier, int)}
 *         before handing the manager to a closure. The template's pool set is read when each thread's
 *         clone is first materialised; adding pools afterwards is unsupported.</li>
 *     <li>{@link ClosureObject#setPoolManager(PointingPoolManager)} should be called at most once per closure.</li>
 *     <li>Nested invocations on the same thread are not supported: a thread reuses its single clone,
 *         so the inner call's {@link #flush()} wipes the outer call's frame.</li>
 *     <li>Every thread that invokes a pooled closure retains one clone (a {@code Pointing[size]} frame
 *         plus its private pools) for the life of that thread's {@code ThreadLocalMap} entry — i.e.
 *         until the thread dies. A bounded thread population therefore bounds the retained memory.</li>
 * </ul>
 */
public class PointingPoolManager {

    private final HashMap<Class<?>, PointingPool<?>> pools;
    private final int frameSize;
    private final Pointing[] frame;
    private final PointingPool<?>[] framePools;
    private int count;

    public PointingPoolManager (int size) {
        this.pools = new HashMap<>();
        this.frameSize = size;
        this.frame = new Pointing[size];
        this.framePools = new PointingPool<?>[size];
    }

    private PointingPoolManager (int size, HashMap<Class<?>, PointingPool<?>> pools) {
        this.frameSize = size;
        this.frame = new Pointing[size];
        this.framePools = new PointingPool<?>[size];
        this.pools = new HashMap<>(pools.size());
        for (Map.Entry<Class<?>, PointingPool<?>> entry : pools.entrySet())
            this.pools.put(entry.getKey(), entry.getValue().newEmpty());
    }

    public PointingPoolManager newClone() {
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
        framePools[count] = pool;
        frame[count] = obj;
        count++;
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
        PointingPool<T> pool = new PointingPool<T>(capacity, supplier);
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
            PointingPool pool = framePools[i];
            pool.offer(obj);
            frame[i] = null;
            framePools[i] = null;
        }
        count = 0;
    }
}
