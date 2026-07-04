package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtrManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
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

    @Test
    public void testSetBytesWritesNoTerminator() {
        long addr = CHandler.calloc(1, 4);
        BufferPtr ptr = BufferPtrManager.get(addr, 4);
        for (int i = 0; i < 4; i++)
            ptr.setByte(i, (byte)0x7F);
        ptr.setBytes(new byte[]{1, 2});
        assertEquals((byte)1, ptr.getByte(0));
        assertEquals((byte)2, ptr.getByte(1));
        assertEquals((byte)0x7F, ptr.getByte(2));
        assertEquals((byte)0x7F, ptr.getByte(3));
        CHandler.free(addr);
    }

    @Test
    public void testSetBytesAtIndex() {
        long addr = CHandler.calloc(1, 4);
        BufferPtr ptr = BufferPtrManager.get(addr, 4);
        ptr.setBytes(2, new byte[]{5, 6});
        assertEquals((byte)0, ptr.getByte(0));
        assertEquals((byte)0, ptr.getByte(1));
        assertEquals((byte)5, ptr.getByte(2));
        assertEquals((byte)6, ptr.getByte(3));
        CHandler.free(addr);
    }

    @Test
    public void testSetBytesOutOfBounds() {
        long addr = CHandler.calloc(1, 2);
        BufferPtr ptr = BufferPtrManager.get(addr, 2);
        assertThrows(IndexOutOfBoundsException.class, () -> ptr.setBytes(new byte[3]));
        assertThrows(IndexOutOfBoundsException.class, () -> ptr.setBytes(1, new byte[2]));
        CHandler.free(addr);
    }

    @Test
    public void testGetStringUnterminatedThrows() {
        long addr = CHandler.calloc(1, 16);
        BufferPtr wide = BufferPtrManager.get(addr, 16);
        for (int i = 0; i < 8; i++)
            wide.setByte(i, (byte)'A'); // NUL only at index 8, past the bounded view
        BufferPtr bounded = BufferPtrManager.get(addr, 4);
        assertThrows(IndexOutOfBoundsException.class, bounded::getString);
        CHandler.free(addr);
    }

    @Test
    public void testGetStringTerminatorAtLastByte() {
        long addr = CHandler.calloc(1, 4);
        BufferPtr ptr = BufferPtrManager.get(addr, 4);
        ptr.setString("abc"); // 3 bytes + NUL fills the buffer exactly
        assertEquals("abc", ptr.getString());
        CHandler.free(addr);
    }

    @Test
    public void testGetStringUseAfterFree() {
        BufferPtr ptr = BufferPtrManager.get(10, 16);
        BufferPtrManager.insertPool(ptr);
        assertThrows(IllegalStateException.class, ptr::getString);
    }

    @Test
    public void testGetStringNullPointer() {
        assertThrows(NullPointerException.class, BufferPtrManager.get(0)::getString);
    }

    @Test
    public void testGetStringExactLength() {
        long addr = CHandler.calloc(1, 4);
        BufferPtr ptr = BufferPtrManager.get(addr, 4);
        ptr.setBytes(new byte[]{'a', 'b', 0, 'c'});
        assertEquals("ab\0c", ptr.getString(4));
        assertEquals("ab", ptr.getString(2));
        assertEquals("", ptr.getString(0));
        CHandler.free(addr);
    }

    @Test
    public void testGetStringExactLengthCharset() {
        long addr = CHandler.calloc(1, 2);
        BufferPtr ptr = BufferPtrManager.get(addr, 2);
        ptr.setBytes(new byte[]{'h', (byte)0xE9});
        assertEquals("h\u00e9", ptr.getString(2, StandardCharsets.ISO_8859_1));
        CHandler.free(addr);
    }

    @Test
    public void testGetStringExactLengthOutOfBounds() {
        long addr = CHandler.calloc(1, 2);
        BufferPtr ptr = BufferPtrManager.get(addr, 2);
        assertThrows(IndexOutOfBoundsException.class, () -> ptr.getString(3));
        assertThrows(IndexOutOfBoundsException.class, () -> ptr.getString(-1));
        CHandler.free(addr);
    }

    @Test
    public void testSetStringWritesTerminator() {
        long addr = CHandler.calloc(1, 4);
        BufferPtr ptr = BufferPtrManager.get(addr, 4);
        for (int i = 0; i < 4; i++)
            ptr.setByte(i, (byte)0x7F);
        ptr.setString("ab");
        assertEquals((byte)'a', ptr.getByte(0));
        assertEquals((byte)'b', ptr.getByte(1));
        assertEquals((byte)0, ptr.getByte(2));
        assertEquals((byte)0x7F, ptr.getByte(3));
        CHandler.free(addr);
    }
}
