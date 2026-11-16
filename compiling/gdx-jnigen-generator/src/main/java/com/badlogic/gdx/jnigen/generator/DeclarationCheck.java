package com.badlogic.gdx.jnigen.generator;

import com.badlogic.gdx.jnigen.generator.types.ClosureType;
import com.badlogic.gdx.jnigen.generator.types.EnumConstant;
import com.badlogic.gdx.jnigen.generator.types.EnumType;
import com.badlogic.gdx.jnigen.generator.types.FunctionSignature;
import com.badlogic.gdx.jnigen.generator.types.MacroType;
import com.badlogic.gdx.jnigen.generator.types.NamedType;
import com.badlogic.gdx.jnigen.generator.types.NativeFunction;
import com.badlogic.gdx.jnigen.generator.types.StackElementType;
import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class DeclarationCheck {

    public static void checkSame(Manager own, Manager other) {
        ParseTarget ownTarget = own.getTarget();
        ParseTarget otherTarget = other.getTarget();

        checkSameNames("Type", own.getCTypeNames(), other.getCTypeNames(), ownTarget, otherTarget);

        // The stack elements are sorted by name and may repeat a name (nested types of different parents), so
        // the two lists are compared position by position once their names are known to match.
        List<StackElementType> ownStackElements = own.getStackElements();
        List<StackElementType> otherStackElements = other.getStackElements();
        checkSameNames("Struct or union",
                ownStackElements.stream().map(StackElementType::abstractType).collect(Collectors.toList()),
                otherStackElements.stream().map(StackElementType::abstractType).collect(Collectors.toList()),
                ownTarget, otherTarget);
        for (int i = 0; i < ownStackElements.size(); i++)
            checkSameStackElement(ownStackElements.get(i), otherStackElements.get(i), ownTarget, otherTarget);

        Map<String, EnumType> ownEnums = own.getEnums();
        Map<String, EnumType> otherEnums = other.getEnums();
        checkSameNames("Enum", ownEnums.keySet(), otherEnums.keySet(), ownTarget, otherTarget);
        ownEnums.forEach((name, ownEnum) -> checkSameEnum(ownEnum, otherEnums.get(name), ownTarget, otherTarget));

        List<NativeFunction> ownFunctions = own.getGlobalType().getFunctions();
        List<NativeFunction> otherFunctions = other.getGlobalType().getFunctions();
        checkSameNames("Function",
                ownFunctions.stream().map(function -> function.getSignature().getName()).collect(Collectors.toList()),
                otherFunctions.stream().map(function -> function.getSignature().getName()).collect(Collectors.toList()),
                ownTarget, otherTarget);
        for (int i = 0; i < ownFunctions.size(); i++)
            checkSameSignature("Function", ownFunctions.get(i).getSignature(), otherFunctions.get(i).getSignature(), ownTarget, otherTarget);

        Map<String, ClosureType> ownClosures = own.getGlobalType().getClosures().stream().collect(Collectors.toMap(ClosureType::getName, closure -> closure));
        Map<String, ClosureType> otherClosures = other.getGlobalType().getClosures().stream().collect(Collectors.toMap(ClosureType::getName, closure -> closure));
        checkSameNames("Closure", ownClosures.keySet(), otherClosures.keySet(), ownTarget, otherTarget);
        ownClosures.forEach((name, ownClosure) -> checkSameSignature("Closure", ownClosure.getSignature(), otherClosures.get(name).getSignature(), ownTarget, otherTarget));

        Map<String, MacroType> ownMacros = own.getMacros();
        Map<String, MacroType> otherMacros = other.getMacros();
        checkSameNames("Macro", ownMacros.keySet(), otherMacros.keySet(), ownTarget, otherTarget);
        ownMacros.forEach((name, ownMacro) -> {
            MacroType seen = otherMacros.get(name);
            if (!ownMacro.getValue().equals(seen.getValue()))
                throw new IllegalStateException("Macro " + name + " is " + ownMacro.getValue() + " when parsing for " + ownTarget
                        + " but " + seen.getValue() + " when parsing for " + otherTarget);
        });
    }

    private static void checkSameNames(String what, Collection<String> own, Collection<String> other, ParseTarget ownTarget, ParseTarget otherTarget) {
        List<String> onlyOwn = new ArrayList<>(own);
        List<String> onlyOther = new ArrayList<>(other);
        for (String name : own)
            onlyOther.remove(name);
        for (String name : other)
            onlyOwn.remove(name);
        if (!onlyOwn.isEmpty())
            throw new IllegalStateException(what + " " + onlyOwn.get(0) + " was only seen when parsing for " + ownTarget + ", not for " + otherTarget);
        if (!onlyOther.isEmpty())
            throw new IllegalStateException(what + " " + onlyOther.get(0) + " was only seen when parsing for " + otherTarget + ", not for " + ownTarget);
    }

    private static void checkSameStackElement(StackElementType own, StackElementType seen, ParseTarget ownTarget, ParseTarget otherTarget) {
        String what = (own.isStruct() ? "Struct " : "Union ") + own.abstractType();
        if (own.isStruct() != seen.isStruct())
            throw new IllegalStateException(what + " is a " + (seen.isStruct() ? "struct" : "union") + " when parsing for " + otherTarget);
        if (own.isOpaque() != seen.isOpaque())
            throw new IllegalStateException(what + " is " + (own.isOpaque() ? "opaque" : "not opaque") + " when parsing for " + ownTarget
                    + " but " + (seen.isOpaque() ? "opaque" : "not opaque") + " when parsing for " + otherTarget);
        List<NamedType> ownFields = own.getFields();
        List<NamedType> seenFields = seen.getFields();
        if (ownFields.size() != seenFields.size())
            throw new IllegalStateException(what + " has " + ownFields.size() + " field(s) when parsing for " + ownTarget
                    + " but " + seenFields.size() + " when parsing for " + otherTarget);
        for (int i = 0; i < ownFields.size(); i++)
            checkSameNamedType(what + " field", ownFields.get(i), seenFields.get(i), ownTarget, otherTarget);
    }

    private static void checkSameEnum(EnumType own, EnumType seen, ParseTarget ownTarget, ParseTarget otherTarget) {
        String what = "Enum " + own.abstractType();
        List<EnumConstant> ownConstants = own.getConstants();
        List<EnumConstant> seenConstants = seen.getConstants();
        if (ownConstants.size() != seenConstants.size())
            throw new IllegalStateException(what + " has " + ownConstants.size() + " constant(s) when parsing for " + ownTarget
                    + " but " + seenConstants.size() + " when parsing for " + otherTarget);
        for (int i = 0; i < ownConstants.size(); i++) {
            EnumConstant ownConstant = ownConstants.get(i);
            EnumConstant seenConstant = seenConstants.get(i);
            if (ownConstant.getId() != seenConstant.getId() || !ownConstant.getName().equals(seenConstant.getName()))
                throw new IllegalStateException(what + " has constant " + ownConstant.getName() + " = " + ownConstant.getId() + " when parsing for " + ownTarget
                        + " but " + seenConstant.getName() + " = " + seenConstant.getId() + " when parsing for " + otherTarget);
        }
    }

    private static void checkSameSignature(String what, FunctionSignature own, FunctionSignature seen, ParseTarget ownTarget, ParseTarget otherTarget) {
        what += " " + own.getName();
        if (!sameType(own.getReturnType(), seen.getReturnType()))
            throw differentType(what + " returns", own.getReturnType(), seen.getReturnType(), ownTarget, otherTarget);
        NamedType[] ownArguments = own.getArguments();
        NamedType[] seenArguments = seen.getArguments();
        if (ownArguments.length != seenArguments.length)
            throw new IllegalStateException(what + " has " + ownArguments.length + " parameter(s) when parsing for " + ownTarget
                    + " but " + seenArguments.length + " when parsing for " + otherTarget);
        for (int i = 0; i < ownArguments.length; i++)
            checkSameNamedType(what + " parameter", ownArguments[i], seenArguments[i], ownTarget, otherTarget);
    }

    private static void checkSameNamedType(String what, NamedType own, NamedType seen, ParseTarget ownTarget, ParseTarget otherTarget) {
        if (!own.getName().equals(seen.getName()))
            throw new IllegalStateException(what + " " + own.getName() + " when parsing for " + ownTarget + " is called " + seen.getName() + " when parsing for " + otherTarget);
        if (!sameType(own.getDefinition(), seen.getDefinition()))
            throw differentType(what + " " + own.getName() + " is", own.getDefinition(), seen.getDefinition(), ownTarget, otherTarget);
    }

    private static boolean sameType(TypeDefinition own, TypeDefinition seen) {
        if (own == null || seen == null)
            return own == seen;
        return own.getTypeName().equals(seen.getTypeName())
                && own.getCount() == seen.getCount()
                && own.isAnonymous() == seen.isAnonymous()
                && sameType(own.getNestedDefinition(), seen.getNestedDefinition());
    }

    private static IllegalStateException differentType(String what, TypeDefinition own, TypeDefinition seen, ParseTarget ownTarget, ParseTarget otherTarget) {
        return new IllegalStateException(what + " " + spelling(own) + " when parsing for " + ownTarget + " but " + spelling(seen) + " when parsing for " + otherTarget);
    }

    private static String spelling(TypeDefinition definition) {
        String spelling = definition.getTypeName();
        if (definition.getCount() != 1)
            spelling += "[" + definition.getCount() + "]";
        return definition.getNestedDefinition() == null ? spelling : spelling + " -> " + spelling(definition.getNestedDefinition());
    }
}
