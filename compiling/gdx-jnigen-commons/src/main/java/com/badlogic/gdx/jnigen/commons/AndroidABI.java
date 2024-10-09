package com.badlogic.gdx.jnigen.commons;

public enum AndroidABI {

    ABI_ARMEABI_V7A("armeabi-v7a"),
    ABI_x86("x86"),
    ABI_x86_64("x86_64"),
    ABI_ARM64_V8A("arm64-v8a");

    private final String abiString;

    AndroidABI (String abiString) {
        this.abiString = abiString;
    }

    public String getAbiString () {
        return abiString;
    }
}

