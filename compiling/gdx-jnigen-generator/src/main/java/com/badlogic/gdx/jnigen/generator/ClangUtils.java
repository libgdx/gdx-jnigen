package com.badlogic.gdx.jnigen.generator;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.llvm.clang.CXClientData;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXCursorVisitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static org.bytedeco.llvm.global.clang.clang_visitChildren;

public class ClangUtils {

    public static void checkVisitorAllocated(CXCursorVisitor visitor) {
        boolean allocated = false;
        if (visitor != null && !visitor.isNull()) {
            Pointer function = new PointerPointer<>(visitor).get(0);
            allocated = function != null && !function.isNull();
        }
        if (!allocated)
            throw new IllegalStateException("CXCursorVisitor got no JavaCPP callback slot (at most 10 per class). "
                    + "Either a visitor was not closed (missing try/finally) or visits are nested too deep.");
    }

    @FunctionalInterface
    public interface CursorCallback {
        int call(CXCursor current, CXCursor parent);
    }

    public static int visitChildren(CXCursor cursor, CursorCallback callback) {

        try (CXCursorVisitor visitor = new CXCursorVisitor() {
            @Override
            public int call(CXCursor current, CXCursor parent, CXClientData clientData) {
                return callback.call(current, parent);
            }
        }) {
            checkVisitorAllocated(visitor);
            return clang_visitChildren(cursor, visitor, null);
        }
    }
}
