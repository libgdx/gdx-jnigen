package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.pointer.StructPointer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StructMethodsTest extends BaseTest {


    @Test
    public void testReadField() {
        TestStruct testStruct = new TestStruct();
        testStruct.field1(1);
        testStruct.field2(2);
        testStruct.field3((short)3);
        testStruct.field4((byte)4);
        assertEquals(1, Global.getStructField(testStruct.getPointer(), testStruct.getFFIType(), 0));
        assertEquals(2, Global.getStructField(testStruct.getPointer(), testStruct.getFFIType(), 1));
        assertEquals(3, Global.getStructField(testStruct.getPointer(), testStruct.getFFIType(), 2));
        assertEquals(4, Global.getStructField(testStruct.getPointer(), testStruct.getFFIType(), 3));
    }

    @Test
    public void testWriteField() {
        TestStruct testStruct = new TestStruct();
        Global.setStructField(testStruct.getPointer(), testStruct.getFFIType(), 0, 1);
        Global.setStructField(testStruct.getPointer(), testStruct.getFFIType(), 1, 2);
        Global.setStructField(testStruct.getPointer(), testStruct.getFFIType(), 2, 3);
        Global.setStructField(testStruct.getPointer(), testStruct.getFFIType(), 3, 4);

        assertEquals(1, testStruct.field1());
        assertEquals(2, testStruct.field2());
        assertEquals(3, testStruct.field3());
        assertEquals(4, testStruct.field4());
    }

    @Test
    public void testWriteOutOfBound() {
        TestStruct testStruct = new TestStruct();
        assertDoesNotThrow(() -> testStruct.field4((short)(0xFF)));
        assertThrows(IllegalArgumentException.class, () -> testStruct.field4((short)(0xFF + 1)));

        assertDoesNotThrow(() -> testStruct.field3(0xFFFF));
        assertThrows(IllegalArgumentException.class, () -> testStruct.field3(0xFFFF + 1));

        assertDoesNotThrow(() -> testStruct.field2(0xFFFFFFFFL));
        assertThrows(IllegalArgumentException.class, () -> testStruct.field2(0xFFFFFFFFL + 1));

        assertDoesNotThrow(() -> testStruct.field1(0xFFFFFFFFFFFFFFFFL));
    }

    @Test
    public void testPassByValue() {
        TestStruct testStruct = new TestStruct();
        testStruct.field2(7);
        assertEquals(7, TestStruct.passByValueTest(testStruct));
    }

    @Test
    public void testPassPointer() {
        StructPointer<TestStruct> pointer = new TestStruct.Pointer();
        TestStruct testStruct = new TestStruct();
        testStruct.field2(7);
        pointer.set(testStruct);
        assertEquals(7, TestStruct.passPointerTest(pointer));

        assertEquals(7, testStruct.field2());
        assertEquals(0, testStruct.field4());

        testStruct = pointer.get();
        assertEquals(5, testStruct.field4());
        assertEquals(7, testStruct.field2());
    }

    @Test
    public void testPassPointerAsPointer() {
        TestStruct testStruct = new TestStruct();
        testStruct.field2(7);

        StructPointer<TestStruct> pointer = testStruct.asPointer();
        assertEquals(7, TestStruct.passPointerTest(pointer));

        assertEquals(7, testStruct.field2());
        assertEquals(5, testStruct.field4());

        testStruct = pointer.get();
        assertEquals(5, testStruct.field4());
        assertEquals(7, testStruct.field2());
    }

    @Test
    public void returnStructTest() {
        TestStruct testStruct = TestStruct.returnStructTest();
        assertEquals(1, testStruct.field1());
        assertEquals(2, testStruct.field2());
        assertEquals(3, testStruct.field3());
        assertEquals(4, testStruct.field4());
    }

    @Test
    public void returnStructPointerTest() {
        StructPointer<TestStruct> testStructPtr = TestStruct.returnStructPointerTest();
        TestStruct testStruct = testStructPtr.get();
        testStructPtr.free();

        assertEquals(1, testStruct.field1());
        assertEquals(2, testStruct.field2());
        assertEquals(3, testStruct.field3());
        assertEquals(4, testStruct.field4());
    }

    @Test
    public void structPointerAsStrutTest() {
        StructPointer<TestStruct> testStructPtr = TestStruct.returnStructPointerTest();
        TestStruct testStruct = testStructPtr.asStruct();

        assertEquals(1, testStruct.field1());
        assertEquals(2, testStruct.field2());
        assertEquals(3, testStruct.field3());
        assertEquals(4, testStruct.field4());

        testStruct.field1(7);
        assertEquals(7, testStruct.field1());
        testStruct = testStructPtr.get();
        assertEquals(7, testStruct.field1());
        testStructPtr.free();
    }

    @Test
    public void testGetSetStruct() {

        TestStruct testStruct = new TestStruct();

        assertEquals(16, testStruct.getSize());// Depends on padding, so kinda shit test

        assertEquals(0, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(0, testStruct.field3());
        assertEquals(0, testStruct.field4());

        testStruct.field1(5);
        assertEquals(5, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(0, testStruct.field3());
        assertEquals(0, testStruct.field4());

        testStruct.field1(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(0, testStruct.field3());
        assertEquals(0, testStruct.field4());

        testStruct.field1(0);
        assertEquals(0, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(0, testStruct.field3());
        assertEquals(0, testStruct.field4());


        testStruct.field2(5);
        assertEquals(0, testStruct.field1());
        assertEquals(5, testStruct.field2());
        assertEquals(0, testStruct.field3());
        assertEquals(0, testStruct.field4());

        testStruct.field2(Integer.MAX_VALUE);
        assertEquals(0, testStruct.field1());
        assertEquals(Integer.MAX_VALUE, testStruct.field2());
        assertEquals(0, testStruct.field3());
        assertEquals(0, testStruct.field4());

        testStruct.field2(0);
        assertEquals(0, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(0, testStruct.field3());
        assertEquals(0, testStruct.field4());


        testStruct.field3((short)5);
        assertEquals(0, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(5, testStruct.field3());
        assertEquals(0, testStruct.field4());

        testStruct.field3(Short.MAX_VALUE);
        assertEquals(0, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(Short.MAX_VALUE, testStruct.field3());
        assertEquals(0, testStruct.field4());

        testStruct.field3((short)0);
        assertEquals(0, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(0, testStruct.field3());
        assertEquals(0, testStruct.field4());


        testStruct.field4((byte)5);
        assertEquals(0, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(0, testStruct.field3());
        assertEquals(5, testStruct.field4());

        testStruct.field4(Byte.MAX_VALUE);
        assertEquals(0, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(0, testStruct.field3());
        assertEquals(Byte.MAX_VALUE, testStruct.field4());

        testStruct.field4((byte)0);
        assertEquals(0, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(0, testStruct.field3());
        assertEquals(0, testStruct.field4());
    }
}
