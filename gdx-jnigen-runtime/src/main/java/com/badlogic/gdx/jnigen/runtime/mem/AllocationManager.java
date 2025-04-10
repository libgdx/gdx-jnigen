package com.badlogic.gdx.jnigen.runtime.mem;

import com.badlogic.gdx.jnigen.runtime.CHandler;

public class AllocationManager {

    private static final ThreadLocal<ArenaAllocator> currentAllocator = new ThreadLocal<>();

    public static void setCurrentAllocator (ArenaAllocator allocator) {
        if (currentAllocator.get() != null)
            throw new IllegalStateException("Allocator already set");
        currentAllocator.set(allocator);
    }

    public static void removeCurrentAllocator () {
        if (currentAllocator.get() == null)
            throw new IllegalStateException("Allocator not set");
        currentAllocator.remove();
    }

    public static BufferPtr allocate (int size, boolean free) {
        ArenaAllocator allocator = currentAllocator.get();
        if (allocator == null)
            return BufferPtrAllocator.get(CHandler.calloc(1, size), size, free ? MemoryManagementStrategy.GC : MemoryManagementStrategy.UNMANAGED);

        return allocator.allocate(size);
    }

    public static boolean hasAllocator () {
        return currentAllocator.get() != null;
    }

    public static BufferPtr wrap (long address) {
        return wrap(address, -1);
    }

    public static BufferPtr wrap (long address, boolean free) {
        return wrap(address, -1, free);
    }

    public static BufferPtr wrap (long address, int size) {
        return wrap(address, size, false);
    }

    public static BufferPtr wrap (long address, int size, boolean free) {
        ArenaAllocator allocator = currentAllocator.get();
        MemoryManagementStrategy strategy = allocator != null ? MemoryManagementStrategy.ARENA : free ? MemoryManagementStrategy.GC : MemoryManagementStrategy.UNMANAGED;
        BufferPtr bufferPtr = BufferPtrAllocator.get(address, size, strategy);
        if (allocator == null)
            return bufferPtr;

        allocator.submit(bufferPtr, free);
        return bufferPtr;
    }
}
