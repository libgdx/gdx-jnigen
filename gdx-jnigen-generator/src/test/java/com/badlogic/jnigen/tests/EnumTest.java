package com.badlogic.jnigen.tests;

import com.badlogic.jnigen.generated.TestData;
import com.badlogic.jnigen.generated.enums.TestEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumTest extends BaseTest {

    @Test
    public void testPassEnum() {
        assertEquals(1, TestData.passTestEnum(TestEnum.SECOND));
        assertEquals(0, TestData.passTestEnum(TestEnum.FIRST));
        assertEquals(0, TestData.passTestEnum(TestEnum.THIRD));
    }

    @Test
    public void testReturnEnum() {
        assertEquals(TestEnum.THIRD, TestData.returnTestEnum());
    }

    @Test
    public void testPassAndReturnEnum() {
        assertEquals(TestEnum.FIRST, TestData.passAndReturnTestEnum(TestEnum.FIRST));
        assertEquals(TestEnum.SECOND, TestData.passAndReturnTestEnum(TestEnum.SECOND));
        assertEquals(TestEnum.THIRD, TestData.passAndReturnTestEnum(TestEnum.THIRD));
    }
}
