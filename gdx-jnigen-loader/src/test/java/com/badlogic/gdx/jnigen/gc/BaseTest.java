package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.Global;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

    @BeforeAll
    public static void setUp() {
        Global.init();
    }

    @BeforeEach
    public void emptyGCBefore() {
        while (GCHandler.nativeObjectCount() != 0)
            System.gc();
    }

    @AfterEach
    public void emptyGCAfter() {
        while (GCHandler.nativeObjectCount() != 0)
            System.gc();
    }
}
