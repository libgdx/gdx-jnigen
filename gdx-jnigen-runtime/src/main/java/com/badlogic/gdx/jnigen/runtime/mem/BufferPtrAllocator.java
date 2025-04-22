package com.badlogic.gdx.jnigen.runtime.mem;

import com.badlogic.gdx.jnigen.runtime.CHandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class BufferPtrAllocator {

    // A ByteBuffer can address 2^31 bits. Furthermore, we want redundancy around the edges of the sliced byte buffer, to allow for always at least 1GB size
    // So for the whole 64bit spectrum, we have 34 bits to split up.
    // This is just an arbitrary split up, performance tests need to show, how to best split it up.
    private static final int L1_BITS = 12;
    private static final int L2_BITS = 11;
    private static final int L3_BITS = 11;
    private static final int PAGE_OFFSET_BITS = 30;

    private static final int L1_SIZE = 1 << L1_BITS;
    private static final int L2_SIZE = 1 << L2_BITS;
    private static final int L3_SIZE = 1 << L3_BITS;
    private static final int PAGE_SIZE = 1 << PAGE_OFFSET_BITS;

    private static final int L1_MASK = (1 << L1_BITS) - 1;
    private static final int L2_MASK = (1 << L2_BITS) - 1;
    private static final int L3_MASK = (1 << L3_BITS) - 1;
    private static final long PAGE_OFFSET_MASK = (1L << PAGE_OFFSET_BITS) - 1;
    private static final long ADDRESS_MASK = ~PAGE_OFFSET_MASK;

    private static final ByteBuffer[][][] BUFFER_CACHE = new ByteBuffer[L1_SIZE][][];


    private static final boolean NO_CACHE = System.getProperty("com.badlogic.jnigen.allocator.no_cache", "true").equals("true");
    private static final int MAX_CACHED_CAP_SIZE = Integer.parseInt(System.getProperty("com.badlogic.jnigen.allocator.max_cached_capacity", "32"));
    private static final int LRU_CACHE_CAPACITY = Integer.parseInt(System.getProperty("com.badlogic.jnigen.allocator.lru_cache_capacity", "256"));

    private static final LRUCache CACHE_UNMANAGED_NO_CAP = new LRUCache(LRU_CACHE_CAPACITY);
    private static final LRUCache[] CACHE_UNMANAGED_CAP = new LRUCache[MAX_CACHED_CAP_SIZE];

    static {
        for (int i = 0; i < MAX_CACHED_CAP_SIZE; i++) {
            CACHE_UNMANAGED_CAP[i] = new LRUCache(LRU_CACHE_CAPACITY);
        }
    }

    private static ByteBuffer getBuffer(long basePtr) {

        // Address format: [L1 (12 bits) | L2 (11 bits) | L3 (11 bits) | offset (30 bits)]
        int l1_index = (int)(basePtr >> (PAGE_OFFSET_BITS + L3_BITS + L2_BITS)) & L1_MASK;
        int l2_index = (int)(basePtr >> (PAGE_OFFSET_BITS + L3_BITS)) & L2_MASK;
        int l3_index = (int)(basePtr >> (PAGE_OFFSET_BITS)) & L3_MASK;

        ByteBuffer[][] L1 = BUFFER_CACHE[l1_index];
        if (L1 == null) {
            L1 = new ByteBuffer[L2_SIZE][];
            BUFFER_CACHE[l1_index] = L1;
        }

        ByteBuffer[] L2 = L1[l2_index];
        if (L2 == null) {
            L2 = new ByteBuffer[L3_SIZE];
            L1[l2_index] = L2;
        }

        ByteBuffer L3 = L2[l3_index];
        if (L3 == null) {
            L3 = CHandler.wrapPointer(basePtr, Integer.MAX_VALUE).order(ByteOrder.nativeOrder());
            L2[l3_index] = L3;
        }

        return L3;
    }

    public static BufferPtr get(long pointer) {
        return get(pointer, -1);
    }

    public static BufferPtr get(long pointer, int capacity) {
        if (pointer == 0)
            return null;
        if (capacity > PAGE_SIZE)
            throw new IllegalArgumentException("capacity > PAGE_SIZE (" + capacity + " > " + PAGE_SIZE + ")");

        int offset = (int)(pointer & PAGE_OFFSET_MASK);
        long basePtr = pointer & ADDRESS_MASK;

        if (NO_CACHE || capacity >= MAX_CACHED_CAP_SIZE)
            return new BufferPtr(getBuffer(basePtr), pointer, offset, capacity);

        LRUCache cache;

        if (capacity == -1) {
            cache = CACHE_UNMANAGED_NO_CAP;
        } else {
            cache = CACHE_UNMANAGED_CAP[capacity];
        }

        synchronized (cache) {
            BufferPtr ptr = cache.get(pointer);
            if (ptr != null)
                return ptr;

            ptr = new BufferPtr(getBuffer(basePtr), pointer, offset, capacity);
            cache.put(pointer, ptr);
            return ptr;
        }
    }

    public static void reset() {
        Arrays.fill(BUFFER_CACHE, null);
        for (int i = 0; i < MAX_CACHED_CAP_SIZE; i++) {
            CACHE_UNMANAGED_CAP[i].clear();
        }
        CACHE_UNMANAGED_NO_CAP.clear();
    }
}