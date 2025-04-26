package com.badlogic.gdx.jnigen.runtime.mem;

import com.badlogic.gdx.jnigen.runtime.CHandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collection;

public class BufferPtrManager {

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
    private static final ByteBuffer NULL_BUFFER = CHandler.wrapPointer(0, Integer.MAX_VALUE);

    private static final boolean NO_POOLING = System.getProperty("com.badlogic.jnigen.allocator.no_pooling", "false").equals("true");
    private static final int POOL_SIZE = Integer.parseInt(System.getProperty("com.badlogic.jnigen.allocator.pool_size", "256"));

    private static final BufferPtrPool BUFFER_PTR_POOL = new BufferPtrPool(POOL_SIZE);

    private static ByteBuffer getBuffer(long basePtr) {
        if (basePtr == 0)
            return NULL_BUFFER;

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
            return new BufferPtr(NULL_BUFFER, 0, 0, 0);
        if (capacity > PAGE_SIZE)
            throw new IllegalArgumentException("capacity > PAGE_SIZE (" + capacity + " > " + PAGE_SIZE + ")");

        int offset = (int)(pointer & PAGE_OFFSET_MASK);
        long basePtr = pointer & ADDRESS_MASK;

        ByteBuffer buffer = getBuffer(basePtr);
        if (NO_POOLING)
            return new BufferPtr(buffer, pointer, offset, capacity);

        BufferPtr bufferPtr = BUFFER_PTR_POOL.pollOrCreate();
        bufferPtr.reset(buffer, pointer, offset, capacity);
        return bufferPtr;
    }

    public static void setBufferPtrPointer(BufferPtr bufferPtr, long newPointer) {
        setBufferPtrPointer(bufferPtr, newPointer, -1);
    }

    public static void setBufferPtrPointer(BufferPtr bufferPtr, long newPointer, int capacity) {
        int offset = (int)(newPointer & PAGE_OFFSET_MASK);
        long basePtr = newPointer & ADDRESS_MASK;

        ByteBuffer buffer = getBuffer(basePtr);

        bufferPtr.reset(buffer, newPointer, offset, capacity);
    }

    public static void insertPool(BufferPtr bufferPtr) {
        if (NO_POOLING)
            return;

        BUFFER_PTR_POOL.offer(bufferPtr);
    }

    public static void insertPool(Collection<BufferPtr> bufferPtr) {
        if (NO_POOLING)
            return;

        BUFFER_PTR_POOL.offerAll(bufferPtr);
    }

    public static void reset() {
        Arrays.fill(BUFFER_CACHE, null);
        BUFFER_PTR_POOL.clear();
    }

    public static boolean isPoolingEnabled() {
        return !NO_POOLING;
    }

    public static int getMaxPoolSize() {
        return POOL_SIZE;
    }
}