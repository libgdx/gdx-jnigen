package com.badlogic.gdx.jnigen.teavm;

import org.teavm.jso.JSBody;

/**
 * TeaVM bridge to Emscripten WASM Module.
 * <p>
 * All methods are static with {@code @JSBody} annotations that call into the
 * Emscripten Module object. Pointer types are narrowed to {@code int} since
 * WASM has a 32-bit address space.
 */
public final class CHandlerWeb {

    private CHandlerWeb() {
    }

    @JSBody(params = {"size"}, script = "return Module._malloc(size);")
    public static native int malloc(int size);

    @JSBody(params = {"count", "size"}, script = "return Module._calloc(count, size);")
    public static native int calloc(int count, int size);

    @JSBody(params = {"ptr"}, script = "Module._free(ptr);")
    public static native void free(int ptr);

    @JSBody(params = {"dst", "src", "size"}, script = "Module.HEAPU8.copyWithin(dst, src, src + size);")
    public static native void memcpy(int dst, int src, int size);

    @JSBody(params = {"src", "size"}, script = ""
            + "var dst = Module._malloc(size);"
            + "Module.HEAPU8.copyWithin(dst, src, src + size);"
            + "return dst;")
    public static native int clone(int src, int size);

    @JSBody(params = {"fnPtr"}, script = "Module.removeFunction(fnPtr);")
    public static native void freeClosure(int fnPtr);

    @JSBody(params = {"jsCallback", "signature"}, script = "return Module.addFunction(jsCallback, signature);")
    public static native int addFunction(Object jsCallback, String signature);

    @JSBody(params = {"fnPtr"}, script = "Module.removeFunction(fnPtr);")
    public static native void removeFunction(int fnPtr);

    @JSBody(params = {}, script = "return 4;")
    public static native int getPointerSize();

    @JSBody(params = {}, script = "return true;")
    public static native boolean is32Bit();
}
