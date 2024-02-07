package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.pointer.CEnum;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.stmt.ReturnStmt;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

public class EnumType implements MappedType {

    private final String name;
    private final HashMap<String, Integer> constants = new HashMap<>();

    public EnumType(String name) {
        this.name = name;
    }

    public void registerConstant(String constantName, int index) {
        if (constants.containsKey(constantName))
            throw new IllegalArgumentException("Enum " + name + " already has constant with name: " + constantName);
        if (constants.containsValue(index))
            throw new IllegalArgumentException("Enum " + name + " already has constant with id: " + index);
        constants.put(constantName, index);
    }

    public void write(CompilationUnit cu) {
        EnumDeclaration declaration = cu.addEnum(name);
        declaration.addImplementedType(CEnum.class);
        constants.entrySet().stream()
                .sorted(Comparator.comparingInt(Entry::getValue))
                .forEach(stringIntegerEntry ->
                        declaration.addEnumConstant(stringIntegerEntry.getKey())
                                .addArgument(new IntegerLiteralExpr(String.valueOf(stringIntegerEntry.getValue()))));
        declaration.addField(int.class, "index", Keyword.PRIVATE, Keyword.FINAL);
        ConstructorDeclaration constructor = declaration.addConstructor().addParameter(int.class, "index");
        constructor.createBody().addStatement("this.index = index;");

        declaration.addMethod("getIndex", Keyword.PUBLIC).setType(int.class)
                .createBody().addStatement(new ReturnStmt("index"));
    }

    @Override
    public void importType(CompilationUnit cu) {
        cu.addImport(classFile());
    }

    @Override
    public String classFile() {
        return packageName() + "." + name;
    }

    @Override
    public String packageName() {
        return Manager.getInstance().getBasePackage() + ".enums";
    }

    @Override
    public String instantiationType() {
        throw new IllegalArgumentException("Not yet implemented");
    }

    @Override
    public String abstractType() {
        return name;
    }

    @Override
    public String primitiveType() {
        return int.class.getName(); // Should be able to hold it
    }
}
