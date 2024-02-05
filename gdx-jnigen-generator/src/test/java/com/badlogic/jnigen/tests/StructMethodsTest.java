package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.pointer.StructPointer;
import com.badlogic.jnigen.generated.Global;
import com.badlogic.jnigen.generated.structs.TestStruct;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StructMethodsTest extends BaseTest {


    @Test
    public void testReadField() {
        TestStruct testStruct = new TestStruct();
        testStruct.field1(1);
        testStruct.field2(2);
        testStruct.field3((char)3);
        testStruct.field4((char)4);
        assertEquals(1, CHandler.getStructField(testStruct.getPointer(), testStruct.getFFIType(), 0));
        assertEquals(2, CHandler.getStructField(testStruct.getPointer(), testStruct.getFFIType(), 1));
        assertEquals(3, CHandler.getStructField(testStruct.getPointer(), testStruct.getFFIType(), 2));
        assertEquals(4, CHandler.getStructField(testStruct.getPointer(), testStruct.getFFIType(), 3));
    }

    @Test
    public void testWriteField() {
        TestStruct testStruct = new TestStruct();
        CHandler.setStructField(testStruct.getPointer(), testStruct.getFFIType(), 0, 1);
        CHandler.setStructField(testStruct.getPointer(), testStruct.getFFIType(), 1, 2);
        CHandler.setStructField(testStruct.getPointer(), testStruct.getFFIType(), 2, 3);
        CHandler.setStructField(testStruct.getPointer(), testStruct.getFFIType(), 3, 4);

        assertEquals(1, testStruct.field1());
        assertEquals(2, testStruct.field2());
        assertEquals(3, testStruct.field3());
        assertEquals(4, testStruct.field4());
    }

    @Test
    public void testWriteOutOfBound() {
        TestStruct testStruct = new TestStruct();
        assertDoesNotThrow(() -> testStruct.field4((char)0xFF));
        assertThrows(IllegalArgumentException.class, () -> testStruct.field4((char)(0xFF + 1)));

        assertDoesNotThrow(() -> testStruct.field3(Character.MAX_VALUE));

        assertDoesNotThrow(() -> testStruct.field2(0xFFFFFFFFL));
        assertThrows(IllegalArgumentException.class, () -> testStruct.field2(0xFFFFFFFFL + 1));

        assertDoesNotThrow(() -> testStruct.field1(0xFFFFFFFFFFFFFFFFL));
    }

    @Test
    public void testPassByValue() {
        TestStruct testStruct = new TestStruct();
        testStruct.field2(7);
        assertEquals(7, Global.passByValue(testStruct));
    }

    @Test
    public void testPassPointer() {
        StructPointer<TestStruct> pointer = new TestStruct.Pointer();
        TestStruct testStruct = new TestStruct();
        testStruct.field2(7);
        pointer.set(testStruct);
        assertEquals(7, Global.passByPointer(pointer));

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
        assertEquals(7, Global.passByPointer(pointer));

        assertEquals(7, testStruct.field2());
        assertEquals(5, testStruct.field4());

        testStruct = pointer.get();
        assertEquals(5, testStruct.field4());
        assertEquals(7, testStruct.field2());
    }

    @Test
    public void returnStructTest() {
        TestStruct testStruct = Global.returnTestStruct();
        assertEquals(1, testStruct.field1());
        assertEquals(2, testStruct.field2());
        assertEquals(3, testStruct.field3());
        assertEquals(4, testStruct.field4());
    }

    @Test
    public void returnStructPointerTest() {
        StructPointer<TestStruct> testStructPtr = Global.returnTestStructPointer();
        TestStruct testStruct = testStructPtr.get();
        testStructPtr.free();

        assertEquals(1, testStruct.field1());
        assertEquals(2, testStruct.field2());
        assertEquals(3, testStruct.field3());
        assertEquals(4, testStruct.field4());
    }

    @Test
    public void structPointerAsStrutTest() {
        StructPointer<TestStruct> testStructPtr = Global.returnTestStructPointer();
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


        testStruct.field3((char)5);
        assertEquals(0, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(5, testStruct.field3());
        assertEquals(0, testStruct.field4());

        testStruct.field3(Character.MAX_VALUE);
        assertEquals(0, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(Character.MAX_VALUE, testStruct.field3());
        assertEquals(0, testStruct.field4());

        testStruct.field3((char)0);
        assertEquals(0, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(0, testStruct.field3());
        assertEquals(0, testStruct.field4());


        testStruct.field4((char)5);
        assertEquals(0, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(0, testStruct.field3());
        assertEquals(5, testStruct.field4());

        testStruct.field4((char)0xFF);
        assertEquals(0, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(0, testStruct.field3());
        assertEquals(0xFF, testStruct.field4());

        testStruct.field4((char)0);
        assertEquals(0, testStruct.field1());
        assertEquals(0, testStruct.field2());
        assertEquals(0, testStruct.field3());
        assertEquals(0, testStruct.field4());
    }
}
