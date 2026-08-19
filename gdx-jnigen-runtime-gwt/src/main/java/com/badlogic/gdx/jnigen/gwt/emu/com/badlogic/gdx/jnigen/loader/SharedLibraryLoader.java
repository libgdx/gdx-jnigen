package com.badlogic.gdx.jnigen.loader;

/**
 * GWT emulation of SharedLibraryLoader.
 * On web, native libraries are loaded as WASM modules by the host page, not by Java code.
 */
public class SharedLibraryLoader {

    public SharedLibraryLoader() {
    }

    public SharedLibraryLoader(String nativesJar) {
    }

    public void load(String libraryName) {
        // No-op on web - WASM module is loaded by the host page / JS bootstrap
    }

    static public synchronized void setLoaded(String libraryName) {
    }

    static public synchronized boolean isLoaded(String libraryName) {
        return true;
    }
}
