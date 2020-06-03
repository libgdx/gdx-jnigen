package com.badlogic.gdx.jnigen;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.utils.SharedLibraryLoader;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JniGenTest {

    @BeforeClass
    public static void setUp() throws Exception {
        // generate C/C++ code
        new NativeCodeGenerator().generate(
                "src/test/java",
                System.getProperty("java.class.path"),
                "build/generated/jni",
                new String[] { "**/*.java" },
                null
        );

        // generate build scripts
        BuildConfig buildConfig = new BuildConfig("test", "../../tmp/gdx-jnigen", "../../build/libs", "build/generated/jni");
        
        BuildTarget target;
        if (SharedLibraryLoader.isWindows)
        	target = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Windows, SharedLibraryLoader.is64Bit);
        else if (SharedLibraryLoader.isLinux)
        	target = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Linux, SharedLibraryLoader.is64Bit, SharedLibraryLoader.isARM);
        else if (SharedLibraryLoader.isMac)
        	target = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.MacOsX, SharedLibraryLoader.is64Bit, SharedLibraryLoader.isARM);
        else
        	throw new RuntimeException("Unsupported OS to run tests.");

        new AntScriptGenerator().generate(buildConfig, target);

        if (SharedLibraryLoader.isMac) {
            boolean macAntExecutionStatus = BuildExecutor.executeAnt("build/generated/jni/build-macosx64.xml", "-v");
            if (!macAntExecutionStatus) {
                throw new RuntimeException("Failure to execute mac ant.");
            }
        }
        boolean antExecutionStatus = BuildExecutor.executeAnt("build/generated/jni/build.xml", "-v", "pack-natives");


        // compile and pack natives

        if (!antExecutionStatus) {
            throw new RuntimeException("Failure to execute ant.");
        }

        // load the test-natives.jar and from it the shared library, then execute the test.
        new SharedLibraryLoader("build/libs/test-natives.jar").load("test");
    }

    @Test
    public void testBoolean() {
        assertTrue(JniGenTestClass.testBoolean(true));
        assertFalse(JniGenTestClass.testBoolean(false));
    }

    @Test
    public void testByte() {
        assertEquals((byte) 0, JniGenTestClass.testByte((byte) 0));
        assertEquals((byte) 1, JniGenTestClass.testByte((byte) 1));
    }

    @Test
    public void testChar() {
        assertEquals('A', JniGenTestClass.testChar('A'));
        assertEquals('B', JniGenTestClass.testChar('B'));
    }

    @Test
    public void testShort() {
        assertEquals((short) 0, JniGenTestClass.testShort((short) 0));
        assertEquals((short) 1, JniGenTestClass.testShort((short) 1));
    }

    @Test
    public void testInt() {
        assertEquals(0, JniGenTestClass.testInt(0));
        assertEquals(1, JniGenTestClass.testInt(1));
    }

    @Test
    public void testLong() {
        assertEquals(0L, JniGenTestClass.testLong(0L));
        assertEquals(1L, JniGenTestClass.testLong(1L));
    }

    @Test
    public void testFloat() {
        assertEquals(0.0f, JniGenTestClass.testFloat(0.0f), 0.001);
        assertEquals(1.0f, JniGenTestClass.testFloat(1.0f), 0.001);
    }

    @Test
    public void testDouble() {
        assertEquals(0.0, JniGenTestClass.testDouble(0.0), 0.001);
        assertEquals(1.0, JniGenTestClass.testDouble(1.0), 0.001);
    }

    @Test
    public void testAll() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(1);
        buffer.put(0, (byte)8);

        assertTrue(JniGenTestClass.test(
                true, (byte)1, (char)2, (short)3, 4, 5, 6, 7,
                buffer, new boolean[] { false }, new char[] { 9 },
                new short[] { 10 }, new int[] { 11 }, new long[] { 12 },
                new float[] { 13 }, new double[] { 14 },
                null, "Hurray", JniGenTestClass.class, new RuntimeException(), new JniGenTestClass()));
    }

}

