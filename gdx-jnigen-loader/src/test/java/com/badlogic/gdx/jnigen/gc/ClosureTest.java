package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackNoReturnNoArg;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

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
}
