package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.pointer.DoublePointer;
import com.badlogic.gdx.jnigen.runtime.pointer.FloatPointer;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.BytePointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SBytePointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SIntPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SLongPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SWordPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.UIntPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.ULongPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.UWordPointer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicPointerTest extends BaseTest {

    @Test
    public void testPointerSizedGetSet() {
        SIntPointer sIntPtr = new SIntPointer(4);
        sIntPtr.setInt(10, 0);
        assertEquals(10, sIntPtr.getInt(0));

        sIntPtr.setInt(11, 1);
        assertEquals(11, sIntPtr.getInt(1));

        sIntPtr.setInt(12, 2);
        assertEquals(12, sIntPtr.getInt(2));

        sIntPtr.setInt(13, 3);
        assertEquals(13, sIntPtr.getInt(3));
    }

    @Test
    public void testPointerSizedBoundCheck() {
        UIntPointer uIntPointer = new UIntPointer(1);
        assertThrows(IllegalArgumentException.class, () -> uIntPointer.setUInt(0xFFFFFFFFL + 1L, 0));
        assertDoesNotThrow(() -> uIntPointer.setUInt(0xFFFFFFFFL, 0));
    }

    @Test
    public void testPointerFloatGetSet() {
        FloatPointer floatPointer = new FloatPointer(2);
        floatPointer.setFloat(1.1f, 0);
        floatPointer.setFloat(1.2f, 1);
        assertEquals(1.1f, floatPointer.getFloat(0));
        assertEquals(1.2f, floatPointer.getFloat(1));
    }

    @Test
    public void testPointerDoubleGetSet() {
        DoublePointer doublePointer = new DoublePointer(2);
        doublePointer.setDouble(1.1, 0);
        doublePointer.setDouble(1.2, 1);
        assertEquals(1.1, doublePointer.getDouble(0));
        assertEquals(1.2, doublePointer.getDouble(1));
    }

    @Test
    public void testPointerBoundCheck() {
        UIntPointer uIntPointer = new UIntPointer(4);
        assertDoesNotThrow(() -> uIntPointer.getUInt(3));
        assertThrows(IndexOutOfBoundsException.class, () -> uIntPointer.getUInt(4));
    }

    @Test
    public void testPointerNullCheck() {
        BytePointer pointer = new BytePointer(0L, false);
        assertTrue(pointer.isNull());
        assertThrows(NullPointerException.class, pointer::getByte);
    }

    // The range guards below used to be inverted (throwing on every in-range value), so in-range
    // round trips are the regression check.

    @Test
    public void testLongPointerAcceptsInRangeValues() {
        SLongPointer sLong = new SLongPointer(2);
        sLong.setLong(5, 0);
        sLong.setLong(-5, 1);
        assertEquals(5, sLong.getLong(0));
        assertEquals(-5, sLong.getLong(1));

        ULongPointer uLong = new ULongPointer(1);
        uLong.setLong(0xFFFFFFFFL, 0);
        assertEquals(0xFFFFFFFFL, uLong.getLong(0));
        if (CHandler.LONG_SIZE == 4) {
            assertThrows(IllegalArgumentException.class, () -> uLong.setLong(-1, 0));
            assertThrows(IllegalArgumentException.class, () -> uLong.setLong(0xFFFFFFFFL + 1, 0));
        } else {
            // 8-byte unsigned: a negative java long stands for the upper half of the range.
            uLong.setLong(-1, 0);
            assertEquals(-1, uLong.getLong(0));
        }
    }

    @Test
    public void testBytePointerAcceptsAnyByteAndRejectsWideChars() {
        BytePointer pointer = new BytePointer(2);
        pointer.setByte((byte)5, 0);
        pointer.setByte((byte)-1, 1);
        assertEquals(5, pointer.getByte(0));
        assertEquals(-1, pointer.getByte(1));
        assertThrows(IllegalArgumentException.class, () -> pointer.setByte((char)300, 0));
    }

    // Word pointers stride by the pointer size, so on every platform (Windows x64 included) they
    // index size_t / ssize_t arrays correctly.

    @Test
    public void testWordPointerStridesByPointerSize() {
        UWordPointer uWord = new UWordPointer(3);
        assertEquals(3 * CHandler.POINTER_SIZE, uWord.getCapacity());
        uWord.setLong(1, 0);
        uWord.setLong(2, 1);
        uWord.setLong(3, 2);
        BufferPtr raw = uWord.getBufPtr();
        assertEquals(2, raw.getNativeUWord(CHandler.POINTER_SIZE));
        assertEquals(3, raw.getNativeUWord(2 * CHandler.POINTER_SIZE));

        SWordPointer sWord = new SWordPointer(2);
        assertEquals(2 * CHandler.POINTER_SIZE, sWord.getCapacity());
        sWord.setLong(-7, 1);
        assertEquals(-7, sWord.getBufPtr().getNativeWord(CHandler.POINTER_SIZE));
    }

    @Test
    public void testSignedWordPointerRoundTripsNegativeValues() {
        SWordPointer sWord = new SWordPointer(2);
        sWord.setLong(-1, 0);
        sWord.setLong(Integer.MIN_VALUE, 1);
        assertEquals(-1, sWord.getLong(0));
        assertEquals(Integer.MIN_VALUE, sWord.getLong(1));
    }

    @Test
    public void testUnsignedWordPointerRoundTripsFullRange() {
        UWordPointer uWord = new UWordPointer(1);
        uWord.setLong(0xFFFFFFFFL, 0);
        assertEquals(0xFFFFFFFFL, uWord.getLong(0));
        if (CHandler.IS_32_BIT) {
            assertThrows(IllegalArgumentException.class, () -> uWord.setLong(-1, 0));
            assertThrows(IllegalArgumentException.class, () -> uWord.setLong(0xFFFFFFFFL + 1, 0));
        } else {
            uWord.setLong(0xFFFFFFFFL + 1, 0);
            assertEquals(0xFFFFFFFFL + 1, uWord.getLong(0));
            // 8-byte unsigned: a negative java long stands for the upper half of the range.
            uWord.setLong(-1, 0);
            assertEquals(-1, uWord.getLong(0));
        }
    }

    @Test
    public void testSignedWordPointerBoundCheck() {
        SWordPointer sWord = new SWordPointer(1);
        if (CHandler.IS_32_BIT) {
            assertThrows(IllegalArgumentException.class, () -> sWord.setLong(Integer.MAX_VALUE + 1L, 0));
            assertThrows(IllegalArgumentException.class, () -> sWord.setLong(Integer.MIN_VALUE - 1L, 0));
        } else {
            sWord.setLong(Long.MIN_VALUE, 0);
            assertEquals(Long.MIN_VALUE, sWord.getLong(0));
        }
    }

    @Test
    public void testBufferPtrWordAccessorsUseThePointerWidth() {
        // The owner must stay in a live local: it frees its native block on GC, and
        // BufferPtr holds no reference back to it.
        UWordPointer owner = new UWordPointer(2);
        BufferPtr buf = owner.getBufPtr();
        buf.setNativeWord(-1);
        assertEquals(-1, buf.getNativeWord());
        // The same bytes read unsigned: all ones over exactly POINTER_SIZE bytes.
        assertEquals(CHandler.IS_32_BIT ? 0xFFFFFFFFL : -1L, buf.getNativeUWord());
        buf.setNativeUWord(CHandler.POINTER_SIZE, 42);
        assertEquals(42, buf.getNativeUWord(CHandler.POINTER_SIZE));
        assertEquals(42, buf.getNativeWord(CHandler.POINTER_SIZE));
        assertThrows(IndexOutOfBoundsException.class, () -> buf.getNativeWord(2 * CHandler.POINTER_SIZE));
        // Touch the owner last so it stays reachable for the whole method.
        assertEquals(2 * CHandler.POINTER_SIZE, owner.getCapacity());
    }
}
