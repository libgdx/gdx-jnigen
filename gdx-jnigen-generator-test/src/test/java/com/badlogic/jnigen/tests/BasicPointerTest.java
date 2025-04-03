package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.pointer.DoublePointer;
import com.badlogic.gdx.jnigen.runtime.pointer.FloatPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.BytePointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SBytePointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SIntPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.UIntPointer;
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
        assertNull(pointer.getString());
    }
}
