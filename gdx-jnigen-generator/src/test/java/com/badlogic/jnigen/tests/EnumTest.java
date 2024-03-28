package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.gdx.jnigen.pointer.EnumPointer;
import com.badlogic.jnigen.generated.TestData;
import com.badlogic.jnigen.generated.enums.TestEnum;
import com.badlogic.jnigen.generated.enums.TestEnum.TestEnumPointer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumTest extends BaseTest {

    @Test
    public void testPassEnum() {
        assertEquals(1, TestData.passTestEnum(TestEnum.SECOND));
        assertEquals(0, TestData.passTestEnum(TestEnum.FIRST));
        assertEquals(0, TestData.passTestEnum(TestEnum.THIRD));
    }

    @Test
    public void testPassEnumPointer() {
        TestEnumPointer testEnumEnumPointer = new TestEnumPointer();
        testEnumEnumPointer.setEnumValue(TestEnum.SECOND);
        assertEquals(1, TestData.passTestEnumPointer(testEnumEnumPointer));
        testEnumEnumPointer.setEnumValue(TestEnum.FIRST);
        assertEquals(0, TestData.passTestEnumPointer(testEnumEnumPointer));
        testEnumEnumPointer.setEnumValue(TestEnum.THIRD);
        assertEquals(0, TestData.passTestEnumPointer(testEnumEnumPointer));

    }

    @Test
    public void testReturnEnum() {
        assertEquals(TestEnum.THIRD, TestData.returnTestEnum());
    }

    @Test
    public void testReturnEnumPointer() {
        EnumPointer<TestEnum> pointer = TestData.returnTestEnumPointer();
        assertEquals(TestEnum.THIRD, pointer.getEnumValue());
        pointer.free();
    }

    @Test
    public void testPassAndReturnEnum() {
        assertEquals(TestEnum.FIRST, TestData.passAndReturnTestEnum(TestEnum.FIRST));
        assertEquals(TestEnum.SECOND, TestData.passAndReturnTestEnum(TestEnum.SECOND));
        assertEquals(TestEnum.THIRD, TestData.passAndReturnTestEnum(TestEnum.THIRD));
    }

    @Test
    public void testPassAndReturnEnumPointer() {
        TestEnumPointer testEnumEnumPointer = new TestEnumPointer();
        testEnumEnumPointer.setEnumValue(TestEnum.FIRST);
        assertEquals(TestEnum.FIRST, TestData.passAndReturnTestEnumPointer(testEnumEnumPointer));
        testEnumEnumPointer.setEnumValue(TestEnum.SECOND);
        assertEquals(TestEnum.SECOND, TestData.passAndReturnTestEnumPointer(testEnumEnumPointer));
        testEnumEnumPointer.setEnumValue(TestEnum.THIRD);
        assertEquals(TestEnum.THIRD, TestData.passAndReturnTestEnumPointer(testEnumEnumPointer));
    }

    @Test
    public void testReturnEnumClosure() {
        AtomicReference<TestEnum> toReturn = new AtomicReference<>();
        ClosureObject<TestData.methodWithCallbackTestEnumReturn> closure = ClosureObject.fromClosure(toReturn::get);
        toReturn.set(TestEnum.FIRST);
        assertEquals(toReturn.get(), TestData.call_methodWithCallbackTestEnumReturn(closure));
        toReturn.set(TestEnum.SECOND);
        assertEquals(toReturn.get(), TestData.call_methodWithCallbackTestEnumReturn(closure));
        toReturn.set(TestEnum.THIRD);
        assertEquals(toReturn.get(), TestData.call_methodWithCallbackTestEnumReturn(closure));
        closure.free();
    }

    @Test
    public void testReturnEnumPointerClosure() {
        TestEnumPointer toReturn = new TestEnumPointer();
        ClosureObject<TestData.methodWithCallbackTestEnumPointerReturn> closure = ClosureObject.fromClosure(() -> toReturn);
        assertEquals(toReturn.getPointer(), TestData.call_methodWithCallbackTestEnumPointerReturn(closure).getPointer());
        closure.free();
    }

    @Test
    public void testArgEnumClosure() {
        AtomicReference<TestEnum> returned = new AtomicReference<>();
        ClosureObject<TestData.methodWithCallbackTestEnumArg> closure = ClosureObject.fromClosure(returned::set);
        TestData.call_methodWithCallbackTestEnumArg(closure);
        assertEquals(TestEnum.SECOND, returned.get());
        closure.free();
    }

    @Test
    public void testArgEnumPointerClosure() {
        AtomicReference<TestEnum> returned = new AtomicReference<>();
        ClosureObject<TestData.methodWithCallbackTestEnumPointerArg> closure = ClosureObject.fromClosure(newValue -> returned.set(newValue.getEnumValue()));
        TestData.call_methodWithCallbackTestEnumPointerArg(closure);
        assertEquals(TestEnum.SECOND, returned.get());
        closure.free();
    }
}
