package com.badlogic.gdx.jnigen.runtime.util;

import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SynchronizedPool<T> {

    private final ReentrantLock lock = new ReentrantLock();
    private final T[] pool;
    private final int capacity;

    private int size;

    @SuppressWarnings("unchecked")
    public SynchronizedPool(int capacity) {
        this.capacity = capacity;
        this.pool = (T[])new Object[capacity];
        this.size = 0;
    }

    public T poll() {
        lock.lock();
        try {
            if (size == 0)
                return null;
            return pool[--size];
        } finally {
            lock.unlock();
        }
    }
    
    public abstract void reset(T obj);
    public abstract T pollOrCreate();

    public boolean offer(T obj) {
        lock.lock();
        try {
            if (size >= capacity)
                return false;

            reset(obj);
            pool[size++] = obj;
            return true;
        } finally {
            lock.unlock();
        }
    }

    public int offerAll(Collection<T> objs) {
        lock.lock();
        try {
            int count = 0;
            int remaining = capacity - size;

            for (T obj : objs) {
                if (count >= remaining)
                    break;

                reset(obj);
                pool[size++] = obj;
                count++;
            }

            return count;
        } finally {
            lock.unlock();
        }
    }

    public void clear() {
        lock.lock();
        try {
            size = 0;
        } finally {
            lock.unlock();
        }
    }
}
