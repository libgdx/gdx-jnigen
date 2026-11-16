package com.badlogic.gdx.jnigen.generator;

import org.bytedeco.llvm.clang.CXClientData;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXCursorVisitor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.bytedeco.llvm.global.clang.CXChildVisit_Continue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * JavaCPP hands out at most 10 callback slots per FunctionPointer class. An 11th live
 * {@link CXCursorVisitor} silently gets a NULL native function pointer, and the next
 * clang_visitChildren jumps to 0x0 (SIGSEGV). These tests lock in the loud failure
 * ({@link ClangUtils#checkVisitorAllocated}) and that the generator never leaks visitor slots,
 * even when a parse fails half-way through a nested visit.
 */
public class CursorVisitorSlotTest {

    /** Mirrors JavaCPP's {@code @Allocator(max)} default. */
    private static final int JAVACPP_CALLBACK_SLOTS = 10;

    private static CXCursorVisitor newVisitor() {
        return new CXCursorVisitor() {
            @Override
            public int call(CXCursor current, CXCursor parent, CXClientData clientData) {
                return CXChildVisit_Continue;
            }
        };
    }

    @Test
    void allocatedVisitorPasses() {
        CXCursorVisitor visitor = newVisitor();
        try {
            assertDoesNotThrow(() -> ClangUtils.checkVisitorAllocated(visitor));
        } finally {
            visitor.close();
        }
    }

    @Test
    void unallocatedVisitorIsDetected() {
        CXCursorVisitor[] hoarded = new CXCursorVisitor[JAVACPP_CALLBACK_SLOTS];
        CXCursorVisitor eleventh = null;
        CXCursorVisitor afterRelease = null;
        try {
            for (int i = 0; i < hoarded.length; i++)
                hoarded[i] = newVisitor();
            for (CXCursorVisitor visitor : hoarded)
                ClangUtils.checkVisitorAllocated(visitor);

            eleventh = newVisitor();
            CXCursorVisitor unallocated = eleventh;
            assertThrows(IllegalStateException.class, () -> ClangUtils.checkVisitorAllocated(unallocated));

            // Closing a visitor frees its slot for the next allocation.
            hoarded[0].close();
            hoarded[0] = null;
            afterRelease = newVisitor();
            CXCursorVisitor reallocated = afterRelease;
            assertDoesNotThrow(() -> ClangUtils.checkVisitorAllocated(reallocated));
        } finally {
            for (CXCursorVisitor visitor : hoarded)
                if (visitor != null)
                    visitor.close();
            if (eleventh != null)
                eleventh.close();
            if (afterRelease != null)
                afterRelease.close();
        }
    }

    /**
     * A struct field with a variadic function-pointer type makes the generator throw from inside the
     * StackElementParser visitor callback, which Generator.parse reports as "Failed to parse function"
     * and carries on. Before try/finally closing, every such parse leaked the translation-unit visitor
     * and the struct visitor; more runs than there are slots would then hand out a dead visitor.
     */
    @Test
    void visitorsAreReleasedOnParseFailure() throws IOException {
        assumeTrue(clangAvailable(), "clang not on PATH; Generator.parse needs it for include paths");

        Path header = Files.createTempFile("visitor-slot", ".h");
        Files.write(header, ("typedef struct Holder { int (*fn)(int, ...); } Holder;\n"
                + "void use(Holder h);\n").getBytes());
        try {
            for (int run = 0; run < JAVACPP_CALLBACK_SLOTS + 2; run++) {
                Manager.init(ParseTarget.LINUX_X86_64, header.toString(), "com.example");
                Generator.parse(header.toString(), new String[0], ParseTarget.LINUX_X86_64.name());
            }
        } finally {
            Files.deleteIfExists(header);
        }

        CXCursorVisitor visitor = newVisitor();
        try {
            assertDoesNotThrow(() -> ClangUtils.checkVisitorAllocated(visitor));
        } finally {
            visitor.close();
        }
    }

    private static boolean clangAvailable() {
        try {
            return new ProcessBuilder("clang", "--version").redirectErrorStream(true).start().waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}
