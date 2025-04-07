package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.commons.HostDetection;
import com.badlogic.gdx.jnigen.commons.Os;
import com.badlogic.gdx.jnigen.loader.SharedLibraryLoader;
import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.gc.GCHandler;
import com.badlogic.jnigen.generated.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessHandle;

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
        CHandler.clearRegisteredFunctionPointer();
        String javaHome = System.getProperty("java.home");
        if (javaHome == null)
            throw new RuntimeException("System property 'java.home' is not set, can't force GC run");

        String jcmdPath = javaHome + File.separator + "bin" + File.separator + "jcmd";
        if (HostDetection.os == Os.Windows)
            jcmdPath += ".exe";

        ProcessBuilder jcmdGCRunner = new ProcessBuilder(jcmdPath, String.valueOf(ProcessHandle.current().pid()), "GC.run");

        long start = System.currentTimeMillis();
        while (GCHandler.nativeObjectCount() != 0) {
            if (System.currentTimeMillis() - start > 5000)
                throw new RuntimeException("GC timed out");

            try {
                jcmdGCRunner.start().waitFor();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
