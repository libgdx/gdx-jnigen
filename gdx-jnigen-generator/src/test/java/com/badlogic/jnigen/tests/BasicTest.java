package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.c.CXXException;
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
}
