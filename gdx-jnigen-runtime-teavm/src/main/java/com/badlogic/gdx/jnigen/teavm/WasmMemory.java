package com.badlogic.gdx.jnigen.teavm;

import org.teavm.jso.JSBody;

/**
 * Utilities for accessing WASM linear memory via Emscripten's typed array views.
 */
public final class WasmMemory {

    private WasmMemory() {
    }

    @JSBody(params = {"ptr"}, script = "return Module.HEAP8[ptr];")
    public static native byte getByte(int ptr);

    @JSBody(params = {"ptr", "value"}, script = "Module.HEAP8[ptr] = value;")
    public static native void setByte(int ptr, byte value);

    @JSBody(params = {"ptr"}, script = "return Module.HEAP16[ptr >> 1];")
    public static native short getShort(int ptr);

    @JSBody(params = {"ptr", "value"}, script = "Module.HEAP16[ptr >> 1] = value;")
    public static native void setShort(int ptr, short value);

    @JSBody(params = {"ptr"}, script = "return Module.HEAP32[ptr >> 2];")
    public static native int getInt(int ptr);

    @JSBody(params = {"ptr", "value"}, script = "Module.HEAP32[ptr >> 2] = value;")
    public static native void setInt(int ptr, int value);

    @JSBody(params = {"ptr"}, script = "return Module.HEAPF32[ptr >> 2];")
    public static native float getFloat(int ptr);

    @JSBody(params = {"ptr", "value"}, script = "Module.HEAPF32[ptr >> 2] = value;")
    public static native void setFloat(int ptr, float value);

    @JSBody(params = {"ptr"}, script = "return Module.HEAPF64[ptr >> 3];")
    public static native double getDouble(int ptr);

    @JSBody(params = {"ptr", "value"}, script = "Module.HEAPF64[ptr >> 3] = value;")
    public static native void setDouble(int ptr, double value);

    @JSBody(params = {"ptr"}, script = "return Module.HEAPU8[ptr];")
    public static native int getUnsignedByte(int ptr);

    @JSBody(params = {"ptr"}, script = "return Module.HEAPU16[ptr >> 1];")
    public static native int getUnsignedShort(int ptr);

    @JSBody(params = {"ptr"}, script = "return Module.HEAPU32[ptr >> 2];")
    public static native int getUnsignedInt(int ptr);

    /**
     * Read a null-terminated UTF-8 string from WASM memory.
     * @param ptr WASM pointer to the string, or 0 for null
     * @return Java String, or null if ptr is 0
     */
    @JSBody(params = {"ptr"}, script = "return ptr ? $rt_str(Module.UTF8ToString(ptr)) : null;")
    public static native String readString(int ptr);

    /**
     * Read a float array from WASM memory.
     * @param ptr WASM pointer to float data
     * @param count number of float elements to read
     * @return Java float array
     */
    @JSBody(params = {"ptr", "count"}, script =
            "var result = $rt_createFloatArray(count);"
          + "result.data.set(Module.HEAPF32.subarray(ptr >> 2, (ptr >> 2) + count));"
          + "return result;")
    public static native float[] readFloatArray(int ptr, int count);
}
