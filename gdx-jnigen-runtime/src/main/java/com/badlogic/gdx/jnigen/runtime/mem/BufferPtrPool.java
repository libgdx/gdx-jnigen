package com.badlogic.gdx.jnigen.runtime.mem;

/**
 * A non-thread-safe array-stack pool for {@link BufferPtr}. Held in a {@code ThreadLocal} on
 * {@link BufferPtrManager}, so each thread has its own private instance and no synchronisation is
 * needed on the hot path.
 */
public class BufferPtrPool {

    private final BufferPtr[] pool;
    private final int capacity;
    private int size;

    public BufferPtrPool(int capacity) {
        this.capacity = capacity;
        this.pool = new BufferPtr[capacity];
    }

    public BufferPtr poll() {
        if (size == 0)
            return null;
        BufferPtr obj = pool[--size];
        pool[size] = null;
        return obj;
    }

    public BufferPtr pollOrCreate() {
        if (size == 0)
            return new BufferPtr();
        BufferPtr obj = pool[--size];
        pool[size] = null;
        return obj;
    }

    public boolean offer(BufferPtr obj) {
        if (size >= capacity)
            return false;
        pool[size++] = obj;
        return true;
    }

    public void clear() {
        for (int i = 0; i < size; i++)
            pool[i] = null;
        size = 0;
    }
}
