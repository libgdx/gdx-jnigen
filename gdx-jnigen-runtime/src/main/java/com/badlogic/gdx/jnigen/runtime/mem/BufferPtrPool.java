package com.badlogic.gdx.jnigen.runtime.mem;

import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

public class BufferPtrPool {

    private final ReentrantLock lock = new ReentrantLock();
    private final BufferPtr[] pool;
    private final int capacity;

    private int size;

    public BufferPtrPool(int capacity) {
        this.capacity = capacity;
        this.pool = new BufferPtr[capacity];
        this.size = 0;
    }

    public BufferPtr poll() {
        lock.lock();
        try {
            if (size == 0)
                return null;
            return pool[--size];
        } finally {
            lock.unlock();
        }
    }

    public boolean offer(BufferPtr buffer) {
        lock.lock();
        try {
            if (size >= capacity)
                return false;

            buffer.reset(null, 0, 0, 0, null);
            pool[size++] = buffer;
            return true;
        } finally {
            lock.unlock();
        }
    }

    public int offerAll(Collection<BufferPtr> buffers) {
        lock.lock();
        try {
            int count = 0;
            int remaining = capacity - size;

            for (BufferPtr buffer : buffers) {
                if (count >= remaining)
                    break;

                buffer.reset(null, 0, 0, 0, null);
                pool[size++] = buffer;
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
