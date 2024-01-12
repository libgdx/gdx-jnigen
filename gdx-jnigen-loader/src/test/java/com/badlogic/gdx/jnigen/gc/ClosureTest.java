package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackNoReturnLongArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackNoReturnNoArg;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClosureTest {


    @Test
    public void testCallbackNoValue() {
        AtomicBoolean changed = new AtomicBoolean(false);
        ClosureObject<CallbackNoReturnNoArg> closureObject = ClosureObject.fromClosure(() -> changed.set(true));
        Closures.methodWithCallback(closureObject);
        assertTrue(changed.get());
        closureObject.free();
    }

    @Test
    public void testCallbackLongArg() {
        AtomicLong changed = new AtomicLong(0);
        ClosureObject<CallbackNoReturnLongArg> closureObject = ClosureObject.fromClosure((arg) -> changed.set(arg));
        Closures.methodWithCallbackLongArg(closureObject.getFnPtr());
        assertEquals(5, changed.get());
        closureObject.free();
    }
}
