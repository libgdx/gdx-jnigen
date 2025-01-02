package com.badlogic.gdx.jnigen.generator.parser;

import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXString;

import java.util.stream.Collectors;

import static org.bytedeco.llvm.global.clang.*;

public class CommentParser {

    private final CXCursor cursor;

    public CommentParser (CXCursor cursor) {
        this.cursor = cursor;
    }

    public boolean isPresent() {
        return clang_Cursor_getRawCommentText(cursor).data() != null;
    }

    public String parse () {
        CXString string = clang_Cursor_getRawCommentText(cursor);
        if (string.data() == null)
            return null;

        String comment = string.getString();
        comment = comment.replace("///", "");
        comment = comment.replace("//!", "");
        if (comment.startsWith("<"))
            comment = comment.substring(1);
        if (comment.startsWith("/**") || comment.startsWith("/*!")) {
            comment = comment.replace("/**", "");
            comment = comment.replace("/*!", "");
            comment = comment.replace("*/", "");
            comment = comment.lines()
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .map(s -> s.startsWith("*") ? s.substring(1) : s)
                    .collect(Collectors.joining("\n"));
        }

        return comment;
    }
}
