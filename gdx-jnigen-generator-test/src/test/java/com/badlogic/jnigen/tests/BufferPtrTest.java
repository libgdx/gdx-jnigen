package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtrManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BufferPtrTest extends BaseTest {

    @BeforeEach
    public void setup () {
        BufferPtrManager.reset();
    }

    @Test
    public void testAllocateBufferPtr() {
        Random random = new Random();
        random.setSeed(12345678);
        for (int i = 0; i < 10; i++) {
            long addr = random.nextLong();
            BufferPtr ptr = BufferPtrManager.get(addr);
            assertEquals(addr, ptr.getPointer());
        }
    }

    @Test
    public void testAllocateNullBufferPtr() {
          assertThrows(NullPointerException.class, BufferPtrManager.get(0)::getByte);
    }

    @Test
    public void testAllocateBufferPtrEquals() {
        long addr = CHandler.calloc(1, 1);
        BufferPtr buffPtr1 = BufferPtrManager.get(addr);
        BufferPtrManager.reset();
        BufferPtr buffPtr2 = BufferPtrManager.get(addr);
        assertNotEquals(buffPtr1, buffPtr2);
        buffPtr1.setBoolean(true);
        assertTrue(buffPtr2.getBoolean());
        CHandler.free(addr);
    }

    @Test
    public void testAllocateBufferPtrToLargeCapacity() {
        assertThrows(IllegalArgumentException.class, () -> BufferPtrManager.get(10, Integer.MAX_VALUE / 2 + 2));
    }

    @Test
    public void testUseAfterFree() {
        BufferPtr ptr = BufferPtrManager.get(10, 16);
        BufferPtrManager.insertPool(ptr);
        assertThrows(IllegalStateException.class, ptr::getPointer);
    }
}
