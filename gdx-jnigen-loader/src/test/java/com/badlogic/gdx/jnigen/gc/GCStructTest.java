package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.Struct;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GCStructTest {

    @Test
    public void testStructReleased() throws InterruptedException {
        assertEquals(0, GCHandler.referenceHolder.size());
        Struct struct = new TestStruct();
        assertEquals(1, GCHandler.referenceHolder.size());
        WeakReference<Struct> toCheckGC = new WeakReference<>(struct);
        struct = null;
        while (toCheckGC.get() != null)
            System.gc();
        Thread.sleep(100); // SHit, but works for the moment
        assertEquals(0, GCHandler.referenceHolder.size());
    }
}
