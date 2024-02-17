package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.gc.GCHandler;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

    @BeforeAll
    public static void setUp() {
        CHandler.init();
        new SharedLibraryLoader().load("test-natives");
        com.badlogic.jnigen.generated.FFITypes.init();
    }

    @BeforeEach
    @AfterEach
    public void emptyGC() {
        long time = System.currentTimeMillis();
        while (GCHandler.nativeObjectCount() != 0) {
            if (System.currentTimeMillis() - time > 1000)
                throw new RuntimeException("GC timed out");
            System.gc();
        }
    }
}
