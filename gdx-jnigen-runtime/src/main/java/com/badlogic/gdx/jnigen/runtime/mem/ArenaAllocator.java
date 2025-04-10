package com.badlogic.gdx.jnigen.runtime.mem;

import com.badlogic.gdx.jnigen.runtime.CHandler;

import java.util.ArrayList;

public class ArenaAllocator implements AutoCloseable {

    private final long baseAddress;
    private final int capacity;
    private int offset;
    private final ArrayList<BufferPtr> allocatedBuffers;
    private final ArrayList<BufferPtr> toFreeBuffers;

    public ArenaAllocator(int capacity) {
        this.baseAddress = CHandler.calloc(1, capacity);
        this.capacity = capacity;
        this.offset = 0;

        // We assume 8 byte allocation min
        allocatedBuffers = new ArrayList<>(capacity / 8);
        toFreeBuffers = new ArrayList<>(capacity / 8);

        AllocationManager.setCurrentAllocator(this);
    }

    public synchronized BufferPtr allocate(int size) {
        if (offset + size > capacity)
            throw new IllegalArgumentException("Allocated buffer exceeds remaining space");
        // TODO: Do some alignment?
        BufferPtr bufferPtr = BufferPtrAllocator.get(baseAddress + offset, size, MemoryManagementStrategy.ARENA);
        offset += size;

        submit(bufferPtr, true);

        return bufferPtr;
    }

    public synchronized void submit(BufferPtr bufferPtr, boolean free) {
        if (bufferPtr == null)
            return;
        if (free)
            toFreeBuffers.add(bufferPtr);
        else if(BufferPtrAllocator.isPoolingEnabled() && getHandledBuffersCount() > BufferPtrAllocator.getMaxPoolSize())
            allocatedBuffers.add(bufferPtr);
    }

    public int getRemainingCapacity() {
        return capacity - offset;
    }

    public int getAllocatedBufferCount() {
        return allocatedBuffers.size();
    }

    public int getToFreeBuffersCount() {
        return toFreeBuffers.size();
    }

    public int getHandledBuffersCount() {
        return toFreeBuffers.size() + allocatedBuffers.size();
    }

    @Override
    public void close() {
        AllocationManager.removeCurrentAllocator();
        CHandler.free(baseAddress);
        BufferPtrAllocator.insertPool(allocatedBuffers);
        for (BufferPtr bufferPtr : toFreeBuffers) {
            bufferPtr.free();
        }
        BufferPtrAllocator.insertPool(toFreeBuffers);
        allocatedBuffers.clear();
    }
}
