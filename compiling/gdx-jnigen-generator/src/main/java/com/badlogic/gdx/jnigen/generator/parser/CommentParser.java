package com.badlogic.gdx.jnigen.generator.parser;

import com.badlogic.gdx.jnigen.generator.Manager;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXString;

import java.util.stream.Collectors;

import static org.bytedeco.llvm.global.clang.*;

public class CommentParser {

    private static final String SEE_TAG = "@see";
    private final CXCursor cursor;

    public CommentParser (CXCursor cursor) {
        this.cursor = cursor;
    }

    public boolean isPresent() {
        return clang_Cursor_getRawCommentText(cursor).data() != null;
    }

    public String normalizeSee(String comment) {
        // This method assumes, that the targeted struct/function is already seen
        int see = comment.indexOf(SEE_TAG);
        if (see < 0)
            return comment;

        String ref = comment.substring(see + SEE_TAG.length()).trim();
        String base = comment.substring(0, see + SEE_TAG.length());

        if (ref.contains("."))
            ref = ref.replace(".", "#");

        if (Manager.getInstance().hasCTypeMapping(ref))
            return base + " " + ref;

        if (!Manager.getInstance().hasFunctionWithName(ref))
            return base + " " + ref;

        ref = Manager.getInstance().getGlobalType().abstractType() + "#" + ref;

        return base + " " + ref;
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
                    .filter(s -> !s.isEmpty())
                    .map(s -> s.startsWith("*") ? s.substring(1) : s)
                    .collect(Collectors.joining("\n"));
        }

        comment = comment.lines()
                .map(this::normalizeSee)
                .collect(Collectors.joining("\n"));

        return comment;
    }
}
