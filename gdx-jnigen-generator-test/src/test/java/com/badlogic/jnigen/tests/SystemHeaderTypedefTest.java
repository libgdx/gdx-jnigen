package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.jnigen.generated.TestData;
import com.badlogic.jnigen.generated.TestData.public_callback;
import com.badlogic.jnigen.generated.enums.public_color;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verifies that a typedef which resolves into a system-header type is bound under the outer public
 * alias, not the internal (__-prefixed) name it nests through. The types live in the simulated system
 * header {@code sysheaders/test_system_types.h} (included via {@code -isystem}). The fact that this
 * class references {@code public_callback} / {@code public_color} at all is the naming assertion - if
 * the alias-stopping failed, the generator would have emitted {@code __internal_callback} /
 * {@code __internal_color} and this file would not compile.
 */
public class SystemHeaderTypedefTest extends BaseTest {

    // public_callback -> __internal_callback -> void(*)(int value). The closure interface is named
    // after the outer alias, and the argument keeps the name "value" recovered from the inner typedef.
    @Test
    public void testSystemFunctionPointerTypedefAlias() {
        AtomicInteger got = new AtomicInteger();
        ClosureObject<public_callback> cb = ClosureObject.fromClosure(value -> got.set(value));
        TestData.invokePublicCallback(cb, 99);
        assertEquals(99, got.get());
        cb.free();
    }

    // public_color -> enum __internal_color. The enum is named after the alias; nextColor cycles
    // RED(1) -> GREEN(2) in C.
    @Test
    public void testSystemEnumTypedefAlias() {
        assertEquals(public_color.COLOR_GREEN, TestData.nextColor(public_color.COLOR_RED));
    }
}
