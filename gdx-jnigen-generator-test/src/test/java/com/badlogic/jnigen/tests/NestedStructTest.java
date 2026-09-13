package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.jnigen.generated.structs.NestedNameCollision;
import com.badlogic.jnigen.generated.structs.NestedNameCollisionArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Anonymous nested structs with the same name under different parents (SDL_GamepadBinding shape). */
public class NestedStructTest extends BaseTest {

    @Test
    public void testSameNamedNestedStructsAreDistinct() {
        NestedNameCollision s = new NestedNameCollision();
        s.input().axis().axis(1);
        s.input().axis().axis_min(2);
        s.output().axis().axis(3);
        s.output().axis().axis_min(4);

        assertEquals(1, s.input().axis().axis());
        assertEquals(2, s.input().axis().axis_min());
        assertEquals(3, s.output().axis().axis());
        assertEquals(4, s.output().axis().axis_min());
        // union: button overlays axis.axis
        assertEquals(1, s.input().button());
        assertEquals(3, s.output().button());
    }

    @Test
    public void testClosuresInSameNamedNestedStructs() {
        NestedNameCollision s = new NestedNameCollision();
        ClosureObject<NestedNameCollision.input.axis.cb> inputCb = ClosureObject.fromClosure(v -> v + 1);
        ClosureObject<NestedNameCollision.output.axis.cb> outputCb = ClosureObject.fromClosure(v -> v * 2);
        s.input().axis().cb(inputCb);
        s.output().axis().cb(outputCb);

        // Each goes through its own native direct stub (NestedNameCollision_{input,output}_axis_cb_direct)
        assertEquals(6, s.input().axis().cb().getClosure().cb_call(5));
        assertEquals(10, s.output().axis().cb().getClosure().cb_call(5));

        inputCb.free();
        outputCb.free();
    }

    @Test
    public void testSameNamedNestedStructArrays() {
        NestedNameCollisionArray s = new NestedNameCollisionArray();
        s.input().axis().asStackElement(0).v(1);
        s.input().axis().asStackElement(1).v(2);
        s.output().axis().asStackElement(0).v(3);
        s.output().axis().asStackElement(1).v(4);

        assertEquals(1, s.input().axis().asStackElement(0).v());
        assertEquals(2, s.input().axis().asStackElement(1).v());
        assertEquals(3, s.output().axis().asStackElement(0).v());
        assertEquals(4, s.output().axis().asStackElement(1).v());
    }
}
