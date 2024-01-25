package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.pointer.Struct;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GCTests extends BaseTest {

    @Test
    public void testStructReleased() {
        assertEquals(0, GCHandler.nativeObjectCount());
        Struct struct = new TestStruct();
        assertEquals(1, GCHandler.nativeObjectCount());
        WeakReference<Struct> toCheckGC = new WeakReference<>(struct);
        struct = null;
        while (GCHandler.nativeObjectCount() != 0)
            System.gc();

        assertNull(toCheckGC.get());
        assertEquals(0, GCHandler.nativeObjectCount());
    }
}
