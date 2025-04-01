package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.c.CXXException;
import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SBytePointer;
import com.badlogic.jnigen.generated.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicTest extends BaseTest {

    @Test
    public void testOutOfBoundToJava() {
        assertDoesNotThrow(() -> TestData.outOfBoundArg(Integer.MAX_VALUE));
        assertDoesNotThrow(() -> TestData.outOfBoundArg(1L << 32 - 1));
        assertDoesNotThrow(() -> TestData.outOfBoundArg(0));
        assertThrows(IllegalArgumentException.class, () -> TestData.outOfBoundArg(-1));
        assertThrows(IllegalArgumentException.class, () -> TestData.outOfBoundArg(Long.MAX_VALUE));
        assertThrows(IllegalArgumentException.class, () -> TestData.outOfBoundArg(1L << 32));
    }

    @Test
    public void testCXXException() {
        assertThrows(CXXException.class, TestData::throwOrdinaryException);
        assertThrows(CXXException.class, TestData::throwNumberException);

        try {
            TestData.throwOrdinaryException();
        } catch (CXXException e) {
            assertEquals("Normal error", e.getMessage());
        }

        try {
            TestData.throwNumberException();
        } catch (CXXException e) {
            assertEquals("An unknown error occurred", e.getMessage());
        }
    }

    @Test
    public void testCXXExceptionMessageSet() {
        ClosureObject<TestData.methodWithThrowingCallback> closureObject = ClosureObject.fromClosure(() -> {
            throw new IllegalArgumentException("TEST");
        });
        SBytePointer pointer = TestData.returnThrownCauseMessage(closureObject);
        assertTrue(pointer.getString().startsWith("java.lang.IllegalArgumentException: TEST"));

        pointer.free();
        closureObject.free();
    }

    @Test
    public void testCXXExceptionMessageSetIgnoreMessage() {
        ClosureObject<TestData.methodWithThrowingCallback> closureObject = ClosureObject.fromClosure(() -> {
            throw new IllegalArgumentException("TEST");
        });
        CHandler.setDisableCXXExceptionMessage(true);
        SBytePointer noMessage = TestData.returnThrownCauseMessage(closureObject);
        assertEquals("Java-Side exception", noMessage.getString());
        noMessage.free();

        CHandler.setDisableCXXExceptionMessage(false);
        SBytePointer withMessage = TestData.returnThrownCauseMessage(closureObject);
        assertTrue(withMessage.getString().startsWith("java.lang.IllegalArgumentException: TEST"));
        withMessage.free();

        closureObject.free();
    }
}
