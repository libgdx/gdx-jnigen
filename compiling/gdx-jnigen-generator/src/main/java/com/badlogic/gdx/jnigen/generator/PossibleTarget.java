package com.badlogic.gdx.jnigen.generator;

public enum PossibleTarget {
    WIN_32,
    WIN_64,
    UNIX_32_NOT_ANDROID_X86,
    UNIX_64,
    ANDROID_X86;

    public String condition() {
        switch (this) {
            case WIN_32:
                return "defined(_WIN32) && ARCH_BITS == 32";
            case WIN_64:
                return "defined(_WIN32) && ARCH_BITS == 64";
            case UNIX_32_NOT_ANDROID_X86:
                return "!defined(_WIN32) && ARCH_BITS == 32 && !(defined(__i386__) && defined(__ANDROID__))";
            case UNIX_64:
                return "!defined(_WIN32) && ARCH_BITS == 64";
            case ANDROID_X86:
                return "defined(__i386__) && defined(__ANDROID__)";
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }

    public String javaCondition() {
        switch (this) {
            case WIN_32:
                return "(CHandler.IS_32_BIT && CHandler.IS_COMPILED_WIN)";
            case WIN_64:
                return "(CHandler.IS_64_BIT && CHandler.IS_COMPILED_WIN)";
            case UNIX_32_NOT_ANDROID_X86:
                return "(CHandler.IS_32_BIT && CHandler.IS_COMPILED_UNIX && !CHandler.IS_COMPILED_ANDROID_X86)";
            case UNIX_64:
                return "(CHandler.IS_64_BIT && CHandler.IS_COMPILED_UNIX)";
            case ANDROID_X86:
                return "CHandler.IS_COMPILED_ANDROID_X86";
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }

    public boolean is32Bit() {
        return this == WIN_32 || this == UNIX_32_NOT_ANDROID_X86 || this == ANDROID_X86;
    }

    public boolean is64Bit() {
        return this == WIN_64 || this == UNIX_64;
    }

    public boolean isWin() {
        return this == WIN_32 || this == WIN_64;
    }

    public boolean isAndroidX86() {
        return this == ANDROID_X86;
    }

    public static String unsupportedPlatformCondition() {
        StringBuilder condition = new StringBuilder();
        condition.append("!(");

        PossibleTarget[] targets = values();
        for (int i = 0; i < targets.length; i++) {
            if (i > 0) {
                condition.append(" || ");
            }
            condition.append("(").append(targets[i].condition()).append(")");
        }

        condition.append(")");
        return condition.toString();
    }
}

