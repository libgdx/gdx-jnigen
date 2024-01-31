package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.Global;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

    @BeforeAll
    public static void setUp() {
        Global.init();
        Global.registerCTypeSize("uint8_t", 1);
        Global.registerCTypeSize("uint16_t", 2);
        Global.registerCTypeSize("uint32_t", 4);
        Global.registerCTypeSize("uint64_t", 8);
        Global.registerCTypeSize("float", 4);
        Global.registerCTypeSize("double", 8);
    }

    @BeforeEach
    @AfterEach
    public void emptyGC() {
        while (GCHandler.nativeObjectCount() != 0)
            System.gc();
    }
}
