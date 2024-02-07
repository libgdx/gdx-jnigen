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
}
