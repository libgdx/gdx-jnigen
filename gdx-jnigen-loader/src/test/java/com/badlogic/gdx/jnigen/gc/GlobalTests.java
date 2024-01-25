package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackNoReturnByteArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackNoReturnIntArg;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalTests extends BaseTest {

    //@Test
    public void diffClassSameCifTest() {
        AtomicInteger i1 = new AtomicInteger(0);
        AtomicInteger i2 = new AtomicInteger(0);

        CallbackNoReturnIntArg cal1 = arg -> i1.set(arg);
        CallbackNoReturnIntArg cal2 = arg -> i2.set(arg);

        assertEquals(Global.getFFICifForClass(cal1.getClass()), Global.getFFICifForClass(cal1.getClass()));
        assertEquals(Global.getFFICifForClass(cal1.getClass()), Global.getFFICifForClass(cal2.getClass()));
    }
}
