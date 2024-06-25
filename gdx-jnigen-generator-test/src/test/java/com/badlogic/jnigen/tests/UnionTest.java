package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.jnigen.generated.TestData;
import com.badlogic.jnigen.generated.structs.TestStruct;
import com.badlogic.jnigen.generated.structs.TestUnion;
import com.badlogic.jnigen.generated.structs.TestUnion.TestUnionPointer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

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

    @Test
    public void testSetPassUnions() {
        TestUnion testUnion = new TestUnion();

        TestData.setUnionDoubleTypeByPointer(testUnion.asPointer(), 4.4);
        assertEquals(4.4, testUnion.doubleType());

        TestData.setUnionUintTypeByPointer(testUnion.asPointer(), 18);
        assertEquals(18, testUnion.uintType());

        for (int i = 0; i < 3; i++) {
            TestData.setUnionFixedSizeIntByPointer(testUnion.asPointer(), i, i);
            assertEquals(i, testUnion.fixedSizeInt().getInt(i));
        }

        TestStruct testStruct = new TestStruct();
        testStruct.field2(24);
        TestData.setUnionStructTypeByPointer(testUnion.asPointer(), testStruct);
        assertEquals(24, testUnion.structType().field2());
    }

    @Test
    public void testUnionClosureReturn() {
        TestUnion testUnion = new TestUnion();
        ClosureObject<TestData.methodWithCallbackTestUnionPointerReturn> closureObject = ClosureObject.fromClosure(testUnion::asPointer);
        TestUnionPointer pointer = TestData.call_methodWithCallbackTestUnionPointerReturn(closureObject);
        assertEquals(pointer.getPointer(), testUnion.getPointer());

        closureObject.free();
    }

    @Test
    public void testUnionClosureArgument() {
        AtomicReference<TestUnion> ref = new AtomicReference<>();
        ClosureObject<TestData.methodWithCallbackTestUnionPointerArg> closureObject = ClosureObject.fromClosure(arg0 -> ref.set(arg0.get()));
        TestData.call_methodWithCallbackTestUnionPointerArg(closureObject);
        assertEquals(50, ref.get().uintType());

        closureObject.free();
    }
}
