package com.badlogic.gdx.jnigen.generator.types;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;

import javax.lang.model.SourceVersion;

public class MacroType {

    private final String name;
    private final String value;
    private final String comment;

    public MacroType(String name, String value, String comment) {
        this.name = name;
        this.value = value;
        this.comment = comment;
    }

    public void write(CompilationUnit cu, ClassOrInterfaceDeclaration wrappingClass) {
        String processedValue = value;
        String processedName = name;
        // TODO: 21.06.24 We need to find more reliable ways, maybe a fancy regex?
        if (processedValue.startsWith("(") && processedValue.endsWith(")"))
            processedValue = processedValue.substring(1, processedValue.length() - 1);

        for (int i = 0; i < 3; i++) {
            if (processedValue.isEmpty())
                return;
            char indexLowerCase = processedValue.toLowerCase().charAt(processedValue.length() - 1);
            if (indexLowerCase == 'l' || indexLowerCase == 'u')
                processedValue = processedValue.substring(0, processedValue.length() - 1);
            else
                break;
        }

        if (SourceVersion.isKeyword(processedName))
            processedName = processedName + "_r";
        FieldDeclaration declaration = null;
        try {
            long l = Long.decode(processedValue);
            Class<?> lowestBound;
            if (l <= Byte.MAX_VALUE && l >= Byte.MIN_VALUE) {
                lowestBound = byte.class;
            } else if (l <= Short.MAX_VALUE && l >= Short.MIN_VALUE) {
                lowestBound = short.class;
            } else if (l <= Character.MAX_VALUE && l >= Character.MIN_VALUE) {
                lowestBound = char.class;
            } else if (l <= Integer.MAX_VALUE && l >= Integer.MIN_VALUE) {
                lowestBound = int.class;
            } else {
                processedValue += "L";
                lowestBound = long.class;
            }
            declaration = wrappingClass.addFieldWithInitializer(lowestBound, processedName, new IntegerLiteralExpr(processedValue), Modifier.Keyword.PUBLIC, Modifier.Keyword.STATIC, Modifier.Keyword.FINAL);
        }catch (NumberFormatException ignored){
            try {
                if (processedValue.endsWith("L") || processedValue.endsWith("l"))
                    processedValue = processedValue.substring(0, processedValue.length() - 1);
                Double.parseDouble(processedValue);
                boolean isFloat = processedValue.endsWith("f") || processedValue.endsWith("F");
                Class<?> lowestBound = isFloat ? float.class : double.class;
                declaration = wrappingClass.addFieldWithInitializer(lowestBound, processedName, new DoubleLiteralExpr(processedValue), Modifier.Keyword.PUBLIC, Modifier.Keyword.STATIC, Modifier.Keyword.FINAL);
            } catch (NumberFormatException ignored1){}
        }

        if (declaration != null && comment != null)
            declaration.setJavadocComment(comment);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getComment() {
        return comment;
    }
}
