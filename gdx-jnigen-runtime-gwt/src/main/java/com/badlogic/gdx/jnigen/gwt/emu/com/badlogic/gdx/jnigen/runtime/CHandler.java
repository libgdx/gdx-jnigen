package com.badlogic.gdx.jnigen.runtime;

import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.c.CXXException;
import com.badlogic.gdx.jnigen.runtime.closure.Closure;
import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.gdx.jnigen.runtime.closure.ClosureDecoder;
import com.badlogic.gdx.jnigen.runtime.util.DowncallCClosureObjectSupplier;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * GWT emulation of CHandler. Delegates to Emscripten WASM Module via JSNI.
 * <p>
 * WASM is 32-bit, so all pointer values are int-width.
 * Web is single-threaded, so synchronized blocks are no-ops.
 */
public class CHandler {

    public static final int POINTER_SIZE = 4;
    public static final boolean IS_32_BIT = true;
    public static final boolean IS_64_BIT = false;
    public static final boolean IS_COMPILED_UNIX = false;
    public static final boolean IS_COMPILED_WIN = false;
    public static final boolean IS_COMPILED_ANDROID_X86 = false;
    public static final boolean IS_CHAR_SIGNED = true;
    public static final int LONG_SIZE = 4;

    private static final HashMap<Long, ClosureObject<?>> fnPtrClosureMap = new HashMap<>();

    public static void init() {
        // No-op on web - WASM module must be loaded by the host page
    }

    public static String getExceptionString(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static <T extends Closure> void dispatchCallback(ClosureDecoder<T> toCallOn, long parameter) {
        // Web closures dispatch differently via JS function pointers
        throw new UnsupportedOperationException("dispatchCallback not supported on web - use JS function pointers");
    }

    public static CTypeInfo constructCTypeFromNativeType(long nativeType) {
        throw new UnsupportedOperationException("constructCTypeFromNativeType not available on web");
    }

    public static long getFFICifForSignature(CTypeInfo[] signature) {
        // No libffi on web - signatures are known at generation time
        return 0;
    }

    public static <T extends Closure> ClosureObject<T> createClosureForObject(T object) {
        throw new UnsupportedOperationException("createClosureForObject not yet implemented for web");
    }

    @SuppressWarnings("unchecked")
    public static <T extends Closure> ClosureObject<T> getClosureObject(long fnPtr, DowncallCClosureObjectSupplier<T> closureFallback) {
        ClosureObject<T> closureObject = (ClosureObject<T>) fnPtrClosureMap.get(fnPtr);
        if (closureObject == null) {
            if (closureFallback == null)
                throw new RuntimeException("No Closure object found for " + fnPtr);
            closureObject = closureFallback.get(fnPtr);
            fnPtrClosureMap.put(fnPtr, closureObject);
        }
        return closureObject;
    }

    public static void deregisterFunctionPointer(long fnPtr) {
        fnPtrClosureMap.remove(fnPtr);
    }

    public static void clearRegisteredFunctionPointer() {
        fnPtrClosureMap.clear();
    }

    public static native void setDisableCXXExceptionMessage(boolean disable) /*-{
        // No-op on web
    }-*/;

    public static native boolean reExportSymbolsGlobally(String libPath) /*-{
        return false;
    }-*/;

    public static native void dispatchCCall(long fnPtr, long cif, long parameter) /*-{
        // Direct function dispatch - the generator emits specific calls instead
    }-*/;

    public static native long convertNativeTypeToFFIType(long nativeType) /*-{
        return 0;
    }-*/;

    public static native long createClosureForObject(long cifArg, Object object, long closureRet) /*-{
        return 0;
    }-*/;

    public static native ByteBuffer wrapPointer(long pointer, int size) /*-{
        return null;
    }-*/;

    public static native int getSizeFromFFIType(long type) /*-{
        return 0;
    }-*/;

    public static native void freeClosure(long closurePtr) /*-{
        if ($wnd.Module && $wnd.Module.removeFunction) {
            $wnd.Module.removeFunction(closurePtr);
        }
    }-*/;

    public static native long malloc(long size) /*-{
        if ($wnd.Module && $wnd.Module._malloc) {
            return $wnd.Module._malloc(size);
        }
        return 0;
    }-*/;

    public static native long calloc(long count, long size) /*-{
        if ($wnd.Module && $wnd.Module._calloc) {
            return $wnd.Module._calloc(count, size);
        }
        var total = count * size;
        var ptr = $wnd.Module._malloc(total);
        if (ptr) {
            $wnd.Module.HEAPU8.fill(0, ptr, ptr + total);
        }
        return ptr;
    }-*/;

    public static native void free(long pointer) /*-{
        if ($wnd.Module && $wnd.Module._free) {
            $wnd.Module._free(pointer);
        }
    }-*/;

    public static native void memcpy(long dst, long src, long size) /*-{
        if ($wnd.Module && $wnd.Module.HEAPU8) {
            $wnd.Module.HEAPU8.copyWithin(dst, src, src + size);
        }
    }-*/;

    public static native long clone(long src, long size) /*-{
        if ($wnd.Module) {
            var dst = $wnd.Module._malloc(size);
            if (dst) {
                $wnd.Module.HEAPU8.copyWithin(dst, src, src + size);
            }
            return dst;
        }
        return 0;
    }-*/;
}
