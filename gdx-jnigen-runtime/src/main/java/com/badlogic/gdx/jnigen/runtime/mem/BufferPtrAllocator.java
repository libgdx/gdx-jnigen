package com.badlogic.gdx.jnigen.runtime.mem;

import com.badlogic.gdx.jnigen.runtime.CHandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BufferPtrAllocator {

    // A ByteBuffer can address 2^31 bits. So for the whole 64bit spectrum, we have 33 bits to split up.
    // This is just an arbitrary split up, performance tests need to show, how to best split it up.
    private static final int L1_BITS = 11;
    private static final int L2_BITS = 11;
    private static final int L3_BITS = 11;

    private static final int L1_SIZE = 1 << L1_BITS;
    private static final int L2_SIZE = 1 << L2_BITS;
    private static final int L3_SIZE = 1 << L3_BITS;

    private static final long PAGE_OFFSET_MASK = 0x7FFFFFFFL;

    private static final ByteBuffer[][][] BUFFER_CACHE = new ByteBuffer[L1_SIZE][][];

    private static ByteBuffer getBuffer(long pointer) {
        long basePtr = pointer & ~PAGE_OFFSET_MASK;

        // Address format: [L1 (11 bits) | L2 (11 bits) | L3 (11 bits) | offset (31 bits)]
        int l1_index = (int)(pointer >> 53) & 0x7FF;
        int l2_index = (int)(pointer >> 42) & 0x7FF;
        int l3_index = (int)(pointer >> 31) & 0x7FF;

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

        int offset = (int)(pointer & PAGE_OFFSET_MASK);

        ByteBuffer base = getBuffer(pointer);

        ByteBuffer start = base.position(offset).slice().order(ByteOrder.nativeOrder());
        ByteBuffer next = getBuffer(pointer + Integer.MAX_VALUE);

        return new BufferPtr(start, next, pointer, capacity, freeOnGC);
    }
}