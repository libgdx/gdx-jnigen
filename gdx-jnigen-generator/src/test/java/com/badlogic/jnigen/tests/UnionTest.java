package com.badlogic.jnigen.tests;

import com.badlogic.jnigen.generated.TestData;
import com.badlogic.jnigen.generated.structs.TestStruct;
import com.badlogic.jnigen.generated.structs.TestUnion;
import com.badlogic.jnigen.generated.structs.TestUnion.TestUnionPointer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UnionTest extends BaseTest {

    @Test
    public void testReturnUnions() {
        TestUnion testUnion = TestData.returnTestUnion();
        assertEquals(3.3, testUnion.doubleType());

        TestUnionPointer pointer = TestData.returnTestUnionPointer();
        assertEquals(50, pointer.asStackElement().uintType());
        assertEquals(50, pointer.asStackElement().fixedSizeInt().getInt());
        // Probably doesn't work on all endianness
        assertEquals(0, pointer.asStackElement().fixedSizeInt().getInt(1));
    }

    @Test
    public void testPassUnions() {
        TestUnion testUnion = new TestUnion();
        testUnion.uintType(25);
        assertEquals(25, TestData.getUnionUintTypeByValue(testUnion));

        testUnion.doubleType(5.5);
        assertEquals(5.5, TestData.getUnionDoubleTypeByValue(testUnion));

        testUnion.structType().field2(22);
        assertEquals(22, TestData.getUnionStructTypeByValue(testUnion).field2());

        for (int i = 0; i < 3; i++) {
            testUnion.fixedSizeInt().setInt(i, i);
            assertEquals(i, TestData.getUnionFixedSizeIntByValue(testUnion, i));
        }

        assertEquals(testUnion.fixedSizeInt().getPointer(), TestData.getUnionFixedSizeIntByPointer(testUnion.asPointer()).getPointer());
    }
}
