package com.badlogic.gdx.jnigen.generator;

public enum PossibleTarget {
    WIN_32,
    WIN_64,
    UNIX_32_NOT_X86,
    UNIX_64,
    UNIX_X86_32;

    public String condition() {
        switch (this) {
            case WIN_32:
                return "defined(_WIN32) && ARCH_BITS == 32";
            case WIN_64:
                return "defined(_WIN32) && ARCH_BITS == 64";
            case UNIX_32_NOT_X86:
                return "!defined(_WIN32) && ARCH_BITS == 32 && !defined(__i386__)";
            case UNIX_64:
                return "!defined(_WIN32) && ARCH_BITS == 64";
            case UNIX_X86_32:
                return "!defined(_WIN32) && defined(__i386__)";
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
            case UNIX_32_NOT_X86:
                return "(CHandler.IS_32_BIT && CHandler.IS_COMPILED_UNIX && !CHandler.IS_COMPILED_UNIX_X86_32)";
            case UNIX_64:
                return "(CHandler.IS_64_BIT && CHandler.IS_COMPILED_UNIX)";
            case UNIX_X86_32:
                return "CHandler.IS_COMPILED_UNIX_X86_32";
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }

    public boolean is32Bit() {
        return this == WIN_32 || this == UNIX_32_NOT_X86 || this == UNIX_X86_32;
    }

    public boolean is64Bit() {
        return this == WIN_64 || this == UNIX_64;
    }

    public boolean isWin() {
        return this == WIN_32 || this == WIN_64;
    }

    public boolean isUnixX86_32() {
        return this == UNIX_X86_32;
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

