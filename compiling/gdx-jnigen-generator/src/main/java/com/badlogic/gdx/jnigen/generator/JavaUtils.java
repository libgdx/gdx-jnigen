package com.badlogic.gdx.jnigen.generator;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.Expression;

public class JavaUtils {

    public static String cNameToJavaTypeName(String name) {
        return name.replace("struct ", "")
                .replace("union ", "")
                .replace("enum ", "")
                .replace("const ", "");
    }

    public static String capitalize(String toCapitalize) {
        return toCapitalize.substring(0, 1).toUpperCase() + toCapitalize.substring(1);
    }

    public static String javarizeName(String cName) {
        cName = capitalize(cName);
        int index;
        while ((index = cName.indexOf('_')) != -1) {
            cName = cName.substring(0, index) + capitalize(cName.substring(index + 1));
        }
        return cName;
    }

    public static Expression getOffsetAsExpression(int index, OffsetCalculator calculator) {
        return getSizeAsExpression((is32Bit, isWin) -> calculator.getOffset(index, is32Bit, isWin));
    }

    public static Expression getSizeAsExpression(SizeCalculator calculator) {
        int offset32BitWin = calculator.getOffset(true, true);
        int offset32BitNonWin = calculator.getOffset(true, false);
        int offset64BitWin = calculator.getOffset(false, true);
        int offset64BitNonWin = calculator.getOffset(false, false);

        if (offset32BitWin == offset32BitNonWin && offset32BitWin == offset64BitWin && offset32BitWin == offset64BitNonWin) {
            return StaticJavaParser.parseExpression(String.valueOf(offset32BitWin));
        }

        if (offset32BitWin == offset32BitNonWin && offset64BitWin == offset64BitNonWin) {
            return StaticJavaParser.parseExpression("CHandler.IS_32_BIT ? " + offset32BitWin + " : " + offset64BitWin);
        }

        if (offset32BitWin == offset64BitWin && offset32BitNonWin == offset64BitNonWin) {
            return StaticJavaParser.parseExpression("CHandler.IS_COMPILED_WIN ? " + offset32BitWin + " : " + offset32BitNonWin);
        }

        if (offset32BitWin == offset64BitWin && offset32BitWin == offset32BitNonWin)
            return StaticJavaParser.parseExpression("CHandler.IS_32_BIT || CHandler.IS_COMPILED_WIN ? " + offset32BitWin + " : " + offset64BitNonWin);

        return StaticJavaParser.parseExpression("CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? " +
                offset32BitWin + " : " + offset32BitNonWin + ") : (CHandler.IS_COMPILED_WIN ? " +
                offset64BitWin + " : " + offset64BitNonWin + ")");
    }

    @FunctionalInterface
    public interface OffsetCalculator {
        int getOffset(int index, boolean is32Bit, boolean isWin);
    }

    @FunctionalInterface
    public interface SizeCalculator {
        int getOffset(boolean is32Bit, boolean isWin);
    }
}
