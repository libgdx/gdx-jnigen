package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.gc.GCHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;

import static org.junit.jupiter.api.Assertions.*;

public class GCTests extends BaseTest {


    @Test
    public void testPointingReleased() {
        assertEquals(0, GCHandler.nativeObjectCount());
        Pointing pointing = new Pointing(0, true, true);
        assertEquals(1, GCHandler.nativeObjectCount());
        WeakReference<Pointing> toCheckGC = new WeakReference<>(pointing);
        pointing = null;
        while (GCHandler.nativeObjectCount() != 0)
            emptyGC();

        assertNull(toCheckGC.get());
        assertEquals(0, GCHandler.nativeObjectCount());
    }

    @Test
    public void testPointingNoDoubleReleased() {
        assertEquals(0, GCHandler.nativeObjectCount());
        Pointing pointing = new Pointing(0, true, true);
        Pointing samePointer = new Pointing(pointing.getPointer(), false);
        samePointer.setParent(pointing);
        assertEquals(pointing.getPointer(), samePointer.getPointer());
        assertEquals(1, GCHandler.nativeObjectCount());
        WeakReference<Pointing> toCheckGC1 = new WeakReference<>(pointing);
        WeakReference<Pointing> toCheckGC2 = new WeakReference<>(samePointer);
        pointing = null;
        samePointer = null;
        while (GCHandler.nativeObjectCount() != 0)
            emptyGC();

        // The real test is, that there is no segfault
        assertNull(toCheckGC1.get());
        assertNull(toCheckGC2.get());
        assertEquals(0, GCHandler.nativeObjectCount());
    }

    @Test
    public void testPointingFreeEnqueuedPointer() {
        assertEquals(0, GCHandler.nativeObjectCount());
        Pointing pointing = new Pointing(1, true, true);
        Pointing manually = new Pointing(pointing.getPointer(), false);
        manually.setParent(pointing);
        assertThrows(IllegalStateException.class, manually::free);
    }
}
