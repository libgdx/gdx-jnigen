package com.badlogic.gdx.jnigen.generator;

import com.badlogic.gdx.jnigen.commons.AndroidABI;
import com.badlogic.gdx.jnigen.commons.Architecture;
import com.badlogic.gdx.jnigen.commons.Architecture.Bitness;
import com.badlogic.gdx.jnigen.commons.Os;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum ParseTarget {
    LINUX_X86_64("x86_64-unknown-linux-gnu", PossibleTarget.UNIX_64, glibc("x86")),
    LINUX_X86("i386-unknown-linux-gnu", PossibleTarget.UNIX_X86_32, glibc("x86")),
    LINUX_AARCH64("aarch64-unknown-linux-gnu", PossibleTarget.UNIX_64, glibc("aarch64")),
    LINUX_ARM("arm-unknown-linux-gnueabihf", PossibleTarget.UNIX_32_NOT_X86, glibc("arm")),
    LINUX_RISCV64("riscv64-unknown-linux-gnu", PossibleTarget.UNIX_64, glibc("riscv")),
    LINUX_LOONGARCH64("loongarch64-unknown-linux-gnu", PossibleTarget.UNIX_64, glibc("loongarch")),
    WINDOWS_X86_64("x86_64-w64-mingw32", PossibleTarget.WIN_64, "any-windows-any"),
    WINDOWS_X86("i686-w64-mingw32", PossibleTarget.WIN_32, "any-windows-any"),
    WINDOWS_AARCH64("aarch64-w64-mingw32", PossibleTarget.WIN_64, "any-windows-any"),
    MACOS_AARCH64("aarch64-apple-macos", PossibleTarget.UNIX_64, "any-darwin-any"),
    MACOS_X86_64("x86_64-apple-macos", PossibleTarget.UNIX_64, "any-darwin-any");

    private final String triple;
    private final PossibleTarget possibleTarget;
    /** Sub directories of {@code <sysroot>/libc/include}, in search order. */
    private final String[] libcIncludeDirs;

    ParseTarget(String triple, PossibleTarget possibleTarget, String... libcIncludeDirs) {
        this.triple = triple;
        this.possibleTarget = possibleTarget;
        this.libcIncludeDirs = libcIncludeDirs;
    }

    private static String[] glibc(String arch) {
        return new String[] {arch + "-linux-gnu", "generic-glibc", arch + "-linux-any", "any-linux-any"};
    }

    public static ParseTarget of(Os os, Architecture architecture, Bitness bitness, AndroidABI androidABI) {
        boolean is64 = bitness == Bitness._64;
        boolean is32 = bitness == Bitness._32;

        if (!is32 && !is64)
            throw new IllegalArgumentException();

        switch (os) {
            case Android:
                switch (androidABI) {
                    case ABI_ARMEABI_V7A:
                        return LINUX_ARM;
                    case ABI_ARM64_V8A:
                        return LINUX_AARCH64;
                    case ABI_x86:
                        return LINUX_X86;
                    case ABI_x86_64:
                        return LINUX_X86_64;
                }
                return null;
            case Linux:
                switch (architecture) {
                    case x86:
                        return is64 ? LINUX_X86_64 : LINUX_X86;
                    case ARM:
                        return is64 ? LINUX_AARCH64 : LINUX_ARM;
                    case RISCV:
                        return is64 ? LINUX_RISCV64 : null;
                    case LOONGARCH:
                        return is64 ? LINUX_LOONGARCH64 : null;
                }
                return null;
            case Windows:
                switch (architecture) {
                    case x86:
                        return is64 ? WINDOWS_X86_64 : WINDOWS_X86;
                    case ARM:
                        return is64 ? WINDOWS_AARCH64 : null;
                    default:
                        return null;
                }
            case MacOsX:
            case IOS:
                switch (architecture) {
                    case x86:
                        return is64 ? MACOS_X86_64 : null;
                    case ARM:
                        return is64 ? MACOS_AARCH64 : null;
                    default:
                        return null;
                }
        }
        return null;
    }

    public static List<ParseTarget> parse(String names) {
        EnumSet<ParseTarget> targets = EnumSet.noneOf(ParseTarget.class);
        for (String name : names.split(","))
            targets.add(valueOf(name));
        return new ArrayList<>(targets);
    }

    public String getTriple() {
        return triple;
    }

    public PossibleTarget getPossibleTarget() {
        return possibleTarget;
    }

    public String[] clangArguments(Path sysroot) {
        List<String> args = new ArrayList<>();
        args.add("--target=" + triple);
        args.add("-nostdinc");
        args.add("-isystem");
        args.add(sysroot.resolve("include").toString());
        for (String dir : libcIncludeDirs) {
            args.add("-isystem");
            args.add(sysroot.resolve("libc").resolve("include").resolve(dir).toString());
        }
        return args.toArray(new String[0]);
    }
}
