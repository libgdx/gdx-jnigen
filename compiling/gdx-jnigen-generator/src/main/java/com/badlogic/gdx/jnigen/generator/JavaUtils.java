package com.badlogic.gdx.jnigen.generator;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.*;

import java.util.*;
import java.util.stream.Collectors;

public class JavaUtils {

    public static String getGetter(String name) {
        String firstLetterUpperCase = name.substring(0, 1).toUpperCase() + name.substring(1);
        return "get" + firstLetterUpperCase;
    }

    public static String getSetter(String name) {
        String firstLetterUpperCase = name.substring(0, 1).toUpperCase() + name.substring(1);
        return "set" + firstLetterUpperCase;
    }

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
        return getSizeAsExpression((target) -> calculator.getOffset(index, target));
    }

    public static String findShortcutExpression(List<PossibleTarget> smallerGroup) {
        if (smallerGroup.size() == 1)
            return smallerGroup.get(0).javaCondition();

        if (smallerGroup.equals(Arrays.asList(PossibleTarget.UNIX_64, PossibleTarget.WIN_64)))
            return "CHandler.IS_64_BIT";

        if (smallerGroup.equals(Arrays.asList(PossibleTarget.WIN_64, PossibleTarget.UNIX_64)))
            return "CHandler.IS_64_BIT";
        return "(" + smallerGroup.stream().map(PossibleTarget::javaCondition).collect(Collectors.joining(" || ")) + ")";
    }

    public static Expression getSizeAsExpression(SizeCalculator calculator) {
        Map<Integer, List<PossibleTarget>> offsetGroups = Arrays.stream(PossibleTarget.values())
                .collect(Collectors.groupingBy(calculator::getOffset));

        if (offsetGroups.size() == 1)
            return new IntegerLiteralExpr(String.valueOf(calculator.getOffset(PossibleTarget.UNIX_64)));

        if (offsetGroups.size() == 2) {
            List<PossibleTarget> smallerGroup = offsetGroups.values().stream().min(Comparator.comparing(List::size)).get();
            List<PossibleTarget> bigger = offsetGroups.values().stream().max(Comparator.comparing(List::size)).get();
            int sizeSmaller = calculator.getOffset(smallerGroup.get(0));
            int sizeBigger = calculator.getOffset(bigger.get(0));
            String expr = findShortcutExpression(smallerGroup);
            return StaticJavaParser.parseExpression(expr + " ? " + sizeSmaller + " : " + sizeBigger);
        }

        List<Map.Entry<Integer, List<PossibleTarget>>> sortedGroups = offsetGroups.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().size()))
                .collect(Collectors.toList());

        int defaultOffset = sortedGroups.get(sortedGroups.size() - 1).getKey();
        Expression result = new IntegerLiteralExpr(String.valueOf(defaultOffset));

        for (int i = sortedGroups.size() - 2; i >= 0; i--) {
            Map.Entry<Integer, List<PossibleTarget>> currentGroup = sortedGroups.get(i);
            int currentOffset = currentGroup.getKey();
            List<PossibleTarget> currentTargets = currentGroup.getValue();

            String condition = currentTargets.stream()
                    .map(PossibleTarget::javaCondition)
                    .collect(Collectors.joining(" || "));

            Expression conditionExpr = StaticJavaParser.parseExpression("(" + condition + ")");

            Expression thenExpr = new IntegerLiteralExpr(String.valueOf(currentOffset));

            result = new ConditionalExpr(conditionExpr, thenExpr, result);
        }

        return result;
    }

    @FunctionalInterface
    public interface OffsetCalculator {
        int getOffset(int index, PossibleTarget target);
    }

    @FunctionalInterface
    public interface SizeCalculator {
        int getOffset(PossibleTarget target);
    }
}
