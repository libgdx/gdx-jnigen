package com.badlogic.gdx.jnigen;

import java.nio.Buffer;

class JniGenTestClass {

    public static native boolean testBoolean(boolean boolArg); /*
		return boolArg;
	*/

    public static native byte testByte(byte byteArg); /*
		return byteArg;
	*/

    public static native char testChar(char charArg); /*
		return charArg;
	*/

    public static native short testShort(short shortArg); /*
		return shortArg;
	*/

    public static native int testInt(int intArg); /*
		return intArg;
	*/

    public static native long testLong(long longArg); /*
		return longArg;
	*/

    public static native float testFloat(float floatArg); /*
		return floatArg;
	*/

    public static native double testDouble(double doubleArg); /*
		return doubleArg;
	*/

    public static native boolean test (boolean boolArg, byte byteArg, char charArg, short shortArg, int intArg, long longArg,
                                       float floatArg, double doubleArg, Buffer byteBuffer, boolean[] boolArray, char[] charArray, short[] shortArray,
                                       int[] intArray, long[] longArray, float[] floatArray, double[] doubleArray, double[][] multidim,
                                       String string, Class classy, Throwable thr, Object obj ); /*
			printf("boolean: %s\n", boolArg ? "true" : "false");
			printf("byte: %d\n", byteArg);
			printf("char: %c\n", charArg);
			printf("short: %d\n" , shortArg);
			printf("int: %d\n", intArg);
			printf("long: %l\n", longArg);
			printf("float: %f\n", floatArg);
			printf("double: %d\n", doubleArg);
			printf("byteBuffer: %d\n", byteBuffer [0]);
			printf("bool[0]: %s\n", boolArray [ 0]?"true" : "false");
			printf("char[0]: %c\n", charArray [0]);
			printf("short[0]: %d\n", shortArray [0]);
			printf("int[0]: %d\n", intArray [0]);
			printf("long[0]: %lld\n", longArray [0]);
			printf("float[0]: %f\n", floatArray [0]);
			printf("double[0]: %f\n", doubleArray [0]);
			printf("string: %s fuck this nuts\n", string);
			return true;
	*/

	public static class TestInner {
		public native static int testInner(int arg); /*
			return arg + 1;
		*/
	}

}
