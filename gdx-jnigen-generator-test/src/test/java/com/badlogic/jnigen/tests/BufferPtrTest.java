package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtrAllocator;
import com.badlogic.gdx.jnigen.runtime.mem.MemoryManagementStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BufferPtrTest extends BaseTest {

    @BeforeEach
    public void setup () {
        BufferPtrAllocator.reset();
    }

    @Test
    public void testAllocateBufferPtr() {
        Random random = new Random();
        random.setSeed(12345678);
        for (int i = 0; i < 10; i++) {
            long addr = random.nextLong();
            BufferPtr ptr = BufferPtrAllocator.get(addr, MemoryManagementStrategy.UNMANAGED);
            assertEquals(addr, ptr.getPointer());
        }
    }

    @Test
    public void testAllocateNullBufferPtr() {
        assertNull(BufferPtrAllocator.get(0, MemoryManagementStrategy.UNMANAGED));
    }

    @Test
    public void testAllocateBufferPtrEquals() {
        long addr = CHandler.calloc(1, 1);
        BufferPtr buffPtr1 = BufferPtrAllocator.get(addr, MemoryManagementStrategy.UNMANAGED);
        BufferPtrAllocator.reset();
        BufferPtr buffPtr2 = BufferPtrAllocator.get(addr, MemoryManagementStrategy.UNMANAGED);
        assertNotEquals(buffPtr1, buffPtr2);
        buffPtr1.setBoolean(true);
        assertTrue(buffPtr2.getBoolean());
        CHandler.free(addr);
    }

    @Test
    public void testAllocateBufferPtrToLargeCapacity() {
        assertThrows(IllegalArgumentException.class, () -> BufferPtrAllocator.get(10, Integer.MAX_VALUE / 2 + 2, MemoryManagementStrategy.UNMANAGED));
    }

    @Test
    public void testUseAfterFree() {
        BufferPtr ptr = BufferPtrAllocator.get(10, 16, MemoryManagementStrategy.UNMANAGED);
        BufferPtrAllocator.insertPool(ptr);
        assertThrows(IllegalStateException.class, () -> ptr.getPointer());
    }
}
