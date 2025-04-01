package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.gdx.jnigen.runtime.pointer.FloatPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SIntPointer;
import com.badlogic.jnigen.generated.TestData;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestStructReturn;
import com.badlogic.jnigen.generated.structs.SpecialStruct;
import com.badlogic.jnigen.generated.structs.TestStruct;
import com.badlogic.jnigen.generated.structs.TestStruct.TestStructPointer;
import org.junit.jupiter.api.Test;

import static com.badlogic.jnigen.generated.TestData.*;
import static com.badlogic.jnigen.generated.TestData.call_methodWithCallbackTestStructPointerReturn;
import static org.junit.jupiter.api.Assertions.*;

public class StructMethodsTest extends BaseTest {

    @Test
    public void testCallbackStructReturn() {
        TestStruct struct = new TestStruct();
        struct.field3((char)77);
        ClosureObject<methodWithCallbackTestStructReturn> closureObject = ClosureObject.fromClosure(() -> struct);
        TestStruct ret = call_methodWithCallbackTestStructReturn(closureObject);
        assertNotEquals(struct.getPointer(), ret.getPointer());
        assertEquals(77, struct.field3());
        assertEquals(77, ret.field3());
        closureObject.free();
    }

    @Test
    public void testCallbackStructPointerReturn() {
        TestStructPointer structPointer = new TestStructPointer();
        structPointer.asStackElement().field3((char)77);
        ClosureObject<methodWithCallbackTestStructPointerReturn> closureObject = ClosureObject.fromClosure(() -> structPointer);
        TestStructPointer ret = call_methodWithCallbackTestStructPointerReturn(closureObject);
        assertEquals(structPointer.getPointer(), ret.getPointer());
        assertEquals(77, structPointer.asStackElement().field3());
        assertEquals(77, ret.asStackElement().field3());
        closureObject.free();
    }

    @Test
    public void pointerInStructTest() {
        SpecialStruct struct = new SpecialStruct();
        FloatPointer floatPointer = new FloatPointer();
        floatPointer.setFloat(3.3f);
        struct.floatPtrField(floatPointer);
        assertEquals(3.3f, TestData.getFloatPtrFieldValue(struct));
        TestData.setFloatPtrFieldValue(struct, 1.1f);
        assertEquals(1.1f, TestData.getFloatPtrFieldValue(struct));

        SIntPointer cSizedIntPointer = new SIntPointer();
        cSizedIntPointer.setInt(30);
        struct.intPtrField(cSizedIntPointer);
        assertEquals(30, TestData.getIntPtrFieldValue(struct));
        TestData.setIntPtrFieldValue(struct, 20);
        assertEquals(20, TestData.getIntPtrFieldValue(struct));
    }

    @Test
    public void fixedSizeArrayInStructTest() {
        SpecialStruct struct = new SpecialStruct();
        //noinspection EqualsWithItself
        assertEquals(struct.arrayField(), struct.arrayField());
        SIntPointer arrayField = struct.arrayField();
        for (int i = 0; i < 5; i++) {
            arrayField.setInt(i + 1, i);
        }

        assertThrows(IllegalArgumentException.class, () -> arrayField.setInt(3,5));
        for (int i = 0; i < 5; i++) {
            assertEquals(i + 1, TestData.getFixedSizeArrayFieldValue(struct, i));
        }

        for (int i = 0; i < 5; i++) {
            TestData.setFixedSizeArrayFieldValue(struct.asPointer(), i, i + 50);
        }

        for (int i = 0; i < 5; i++) {
            assertEquals(i + 50, arrayField.getInt(i));
        }
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
        assertEquals(7, TestData.passByValue(testStruct));
    }

    @Test
    public void testPassPointer() {
        TestStructPointer pointer = new TestStructPointer();
        TestStruct testStruct = new TestStruct();
        testStruct.field2(7);
        testStruct.field4((char)0);
        pointer.set(testStruct);
        assertEquals(7, TestData.passByPointer(pointer));

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

        TestStructPointer pointer = testStruct.asPointer();
        assertEquals(7, TestData.passByPointer(pointer));

        assertEquals(7, testStruct.field2());
        assertEquals(5, testStruct.field4());

        testStruct = pointer.get();
        assertEquals(5, testStruct.field4());
        assertEquals(7, testStruct.field2());
    }

    @Test
    public void returnStructTest() {
        TestStruct testStruct = TestData.returnTestStruct();
        assertEquals(1, testStruct.field1());
        assertEquals(2, testStruct.field2());
        assertEquals(3, testStruct.field3());
        assertEquals(4, testStruct.field4());
    }

    @Test
    public void returnStructTestParameterPass() {
        TestStruct testStruct = new TestStruct();
        TestData.returnTestStruct(testStruct);
        assertEquals(1, testStruct.field1());
        assertEquals(2, testStruct.field2());
        assertEquals(3, testStruct.field3());
        assertEquals(4, testStruct.field4());
    }

    @Test
    public void returnStructPointerTest() {
        TestStructPointer testStructPtr = TestData.returnTestStructPointer();
        TestStruct testStruct = testStructPtr.get();
        testStructPtr.free();

        assertEquals(1, testStruct.field1());
        assertEquals(2, testStruct.field2());
        assertEquals(3, testStruct.field3());
        assertEquals(4, testStruct.field4());
    }

    @Test
    public void structPointerAsStrutTest() {
        TestStructPointer testStructPtr = TestData.returnTestStructPointer();
        TestStruct testStruct = testStructPtr.asStackElement();

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
        testStruct.field1(0);
        testStruct.field2(0);
        testStruct.field3((char)0);
        testStruct.field4((char)0);

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
