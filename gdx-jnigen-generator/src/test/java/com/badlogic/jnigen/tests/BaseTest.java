package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.ffi.FFITypes;
import com.badlogic.gdx.jnigen.gc.GCHandler;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

    @BeforeAll
    public static void setUp() {
        CHandler.init();
        FFITypes.init();
        new SharedLibraryLoader().load("test-natives");
        com.badlogic.jnigen.generated.FFITypes.init();
    }

    @BeforeEach
    public void emptyGC() {
        while (GCHandler.nativeObjectCount() != 0)
            System.gc();
    }
}
