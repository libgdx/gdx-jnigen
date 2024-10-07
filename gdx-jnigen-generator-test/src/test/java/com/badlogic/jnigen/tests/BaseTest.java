package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.gc.GCHandler;
import com.badlogic.gdx.jnigen.loader.SharedLibraryLoader;
import com.badlogic.jnigen.generated.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.LinkedList;
import java.util.List;

public class BaseTest {

    @BeforeAll
    public static void setUp() {
        new SharedLibraryLoader().load("test-natives");
        TestData.initialize();
    }

    @SuppressWarnings("all")
    @BeforeEach
    @AfterEach
    public void emptyGC() {
        long time = System.currentTimeMillis();
        while (GCHandler.nativeObjectCount() != 0) {
            if (System.currentTimeMillis() - time > 5000)
                throw new RuntimeException("GC timed out");

            System.gc();
            System.runFinalization();

            if (System.currentTimeMillis() - time > 1000) {
                try {
                    final List<long[]> memhog = new LinkedList<>();
                    while(true) {
                        memhog.add(new long[102400]);
                    }
                } catch(OutOfMemoryError ignored) {}
            }
        }
    }
}
