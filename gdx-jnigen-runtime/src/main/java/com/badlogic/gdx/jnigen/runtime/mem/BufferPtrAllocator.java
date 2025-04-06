package com.badlogic.gdx.jnigen.runtime.mem;

import com.badlogic.gdx.jnigen.runtime.CHandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class BufferPtrAllocator {

    // A ByteBuffer can address 2^31 bits. Furthermore, we want reduncancy around the edges of the sliced byte buffer, to allow for always at least 1GB size
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

    private static final ByteBuffer[][][] BUFFER_CACHE = new ByteBuffer[L1_SIZE][][];

    private static ByteBuffer getBuffer(long pointer) {
        long basePtr = pointer & ~PAGE_OFFSET_MASK;

        // Address format: [L1 (12 bits) | L2 (11 bits) | L3 (11 bits) | offset (30 bits)]
        int l1_index = (int)(pointer >> (PAGE_OFFSET_BITS + L3_BITS + L2_BITS)) & L1_MASK;
        int l2_index = (int)(pointer >> (PAGE_OFFSET_BITS + L3_BITS)) & L2_MASK;
        int l3_index = (int)(pointer >> (PAGE_OFFSET_BITS)) & L3_MASK;

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
            L3 = CHandler.wrapPointer(basePtr, Integer.MAX_VALUE);
            L2[l3_index] = L3;
        }

        return L3;
    }

    public static BufferPtr get(long pointer) {
        return get(pointer, -1, false);
    }

    public static BufferPtr get(long pointer, int capacity, boolean freeOnGC) {
        if (pointer == 0)
            return null;
        if (capacity > PAGE_SIZE)
            throw new IllegalArgumentException("capacity > PAGE_SIZE (" + capacity + " > " + PAGE_SIZE + ")");

        int offset = (int)(pointer & PAGE_OFFSET_MASK);

        ByteBuffer base = getBuffer(pointer);

        // TODO: This creates uneccessary garbage to be thread safe
        //  As BufferPtr is a abstraction anyway, we might not need to do that and would get by, by just manually calculating offsets later
        ByteBuffer start = base.duplicate().position(offset).slice().order(ByteOrder.nativeOrder());

        return new BufferPtr(start, pointer, capacity, freeOnGC);
    }

    public static void reset() {
        Arrays.fill(BUFFER_CACHE, null);
    }
}