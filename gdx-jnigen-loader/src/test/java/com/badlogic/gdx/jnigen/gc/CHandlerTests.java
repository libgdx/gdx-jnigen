package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.util.Utils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CHandlerTests extends BaseTest {

    //@Test
    /*public void diffClassSameCifTest() {
        AtomicInteger i1 = new AtomicInteger(0);
        AtomicInteger i2 = new AtomicInteger(0);

        CallbackNoReturnIntArg cal1 = arg -> i1.set(arg);
        CallbackNoReturnIntArg cal2 = arg -> i2.set(arg);

        assertEquals(CHandler.getFFICifForClass(cal1.getClass()), CHandler.getFFICifForClass(cal1.getClass()));
        assertEquals(CHandler.getFFICifForClass(cal1.getClass()), CHandler.getFFICifForClass(cal2.getClass()));
    }*/

    @Test
    public void testBoundCheck() {
        assertTrue(Utils.checkBoundsForNumber(0xFF, 1, false));
        assertTrue(Utils.checkBoundsForNumber(0xFFFF, 2, false));
        assertTrue(Utils.checkBoundsForNumber(0xFFFFFFFFL, 4, false));

        assertTrue(Utils.checkBoundsForNumber(Byte.MAX_VALUE, 1, true));
        assertTrue(Utils.checkBoundsForNumber(Short.MAX_VALUE, 2, true));
        assertTrue(Utils.checkBoundsForNumber(Integer.MAX_VALUE, 4, true));

        assertTrue(Utils.checkBoundsForNumber(Byte.MIN_VALUE, 1, true));
        assertTrue(Utils.checkBoundsForNumber(Short.MIN_VALUE, 2, true));
        assertTrue(Utils.checkBoundsForNumber(Integer.MIN_VALUE, 4, true));


        assertFalse(Utils.checkBoundsForNumber(0xFF + 1, 1, false));
        assertFalse(Utils.checkBoundsForNumber(0xFFFF + 1, 2, false));
        assertFalse(Utils.checkBoundsForNumber(0xFFFFFFFFL + 1, 4, false));

        assertFalse(Utils.checkBoundsForNumber(Byte.MAX_VALUE + 1, 1, true));
        assertFalse(Utils.checkBoundsForNumber(Short.MAX_VALUE + 1, 2, true));
        assertFalse(Utils.checkBoundsForNumber(Integer.MAX_VALUE + 1L, 4, true));

        assertFalse(Utils.checkBoundsForNumber(Byte.MIN_VALUE - 1, 1, true));
        assertFalse(Utils.checkBoundsForNumber(Short.MIN_VALUE - 1, 2, true));
        assertFalse(Utils.checkBoundsForNumber(Integer.MIN_VALUE - 1L, 4, true));


    }
}
