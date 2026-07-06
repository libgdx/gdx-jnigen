package com.badlogic.gdx.jnigen.runtime.pointer;

/**
 * A non-thread-safe array-stack pool of {@link Pointing} wrappers. Each
 * {@link com.badlogic.gdx.jnigen.runtime.closure.PointingPoolManager} clone owns its own private
 * instances, and a clone is only ever touched by a single thread (the decoder hands each invoking
 * thread its own clone), so the {@link #pollOrCreate()} / {@link #offer(Pointing)} hot path needs no
 * synchronisation.
 */
public class PointingPool<T extends Pointing> {

    private final PointerDereferenceSupplier<T> supplier;
    private final int capacity;
    private final T[] slots;
    private int size;

    @SuppressWarnings("unchecked")
    public PointingPool(int capacity, PointerDereferenceSupplier<T> supplier) {
        this.capacity = capacity;
        this.supplier = supplier;
        this.slots = (T[]) new Pointing[capacity];
    }

    public T pollOrCreate() {
        if (size == 0)
            return supplier.create(0, false);
        T obj = slots[--size];
        slots[size] = null;
        return obj;
    }

    public boolean offer(T obj) {
        if (size >= capacity)
            return false;
        slots[size++] = obj;
        return true;
    }

    public PointingPool<T> newEmpty() {
        return new PointingPool<T>(capacity, supplier);
    }
}
