package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.pointer.CSizedIntPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.DoublePointer;
import com.badlogic.gdx.jnigen.runtime.pointer.FloatPointer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicPointerTest extends BaseTest {

    @BeforeAll
    public static void setupCTypes() {
        CHandler.registerCType(new CTypeInfo("int32_t", 0, 4, true, false, false));
        CHandler.registerCType(new CTypeInfo("uint16_t", 0, 2, false, false, false));
        CHandler.registerCType(new CTypeInfo("uint32_t", 0, 4, false, false, false));
        CHandler.registerCType(new CTypeInfo("char", 0, 1, true, false, false));
    }

    @Test
    public void testPointerSizedGetSet() {
        CSizedIntPointer cSizedIntPointer = new CSizedIntPointer("int32_t", 4);
        cSizedIntPointer.setInt(10, 0);
        assertEquals(10, cSizedIntPointer.getInt(0));

        cSizedIntPointer.setInt(11, 1);
        assertEquals(11, cSizedIntPointer.getInt(1));

        cSizedIntPointer.setInt(12, 2);
        assertEquals(12, cSizedIntPointer.getInt(2));

        cSizedIntPointer.setInt(13, 3);
        assertEquals(13, cSizedIntPointer.getInt(3));
    }

    @Test
    public void testPointerSizedBoundCheck() {
        CSizedIntPointer cSizedCharPointer = new CSizedIntPointer("uint16_t", 1);
        assertThrows(IllegalArgumentException.class, () -> cSizedCharPointer.setInt(Character.MAX_VALUE + 1, 0));
        assertDoesNotThrow(() -> cSizedCharPointer.setInt(Character.MAX_VALUE, 0));
        assertThrows(IllegalArgumentException.class, () -> cSizedCharPointer.setInt(-1, 0));
        CSizedIntPointer cSizedIntPointer = new CSizedIntPointer("uint32_t", 1);
        assertThrows(IllegalArgumentException.class, () -> cSizedIntPointer.getInt(0));
        assertDoesNotThrow(() -> cSizedIntPointer.setInt(Integer.MAX_VALUE,0));
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
        CSizedIntPointer cSizedIntPointer = new CSizedIntPointer("int32_t", 4);
        assertDoesNotThrow(() -> cSizedIntPointer.getInt(3));
        assertThrows(IllegalArgumentException.class, () -> cSizedIntPointer.getInt(4));
    }

    @Test
    public void testPointerNullCheck() {
        CSizedIntPointer pointer = new CSizedIntPointer(0, false,"char");
        assertTrue(pointer.isNull());
        assertThrows(NullPointerException.class, pointer::getByte);
        assertNull(pointer.getString());
    }

    @Test
    public void testCTypeConformsTo() {
        CTypeInfo infoNonConst = new CTypeInfo("char", 0, 1, true, false, false);
        assertDoesNotThrow(() -> infoNonConst.assertConformsTo("const char"));
    }
}
