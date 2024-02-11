package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.pointer.CSizedIntPointer;
import com.badlogic.jnigen.generated.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PointerTest extends BaseTest {

    @Test
    public void testPassPointer() {
        CSizedIntPointer pointer = new CSizedIntPointer("int", 1);
        for (int i = -10; i < 10; i++) {
            pointer.setInt(i, 0);
            assertEquals(i, TestData.passIntPointer(pointer));
        }
    }

    @Test
    public void testReturnPointer() {
        for (int i = -10; i < 10; i++) {
            CSizedIntPointer pointer = TestData.returnIntPointer(i);
            assertEquals(i, pointer.getInt(0));
            pointer.free();
        }
    }
}
