package com.badlogic.gdx.jnigen.runtime.mem;

import com.badlogic.gdx.jnigen.runtime.CHandler;

public class AllocationManager {

    public static BufferPtr allocate (int size) {
        return BufferPtrAllocator.get(CHandler.calloc(1, size), size);
    }

    public static BufferPtr wrap (long address) {
        return BufferPtrAllocator.get(address, -1);
    }

    public static BufferPtr wrap (long address, int size) {
        return BufferPtrAllocator.get(address, size);
    }
}
