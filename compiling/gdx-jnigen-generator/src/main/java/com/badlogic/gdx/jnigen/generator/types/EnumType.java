package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.ClassNameConstants;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.PrimitiveType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

public class EnumType implements MappedType {

    private static final int MAX_VALUE_ARRAY_SIZE = 16;

    private final TypeDefinition definition;
    private final String javaName;
    private final HashMap<Integer, EnumConstant> constants = new HashMap<>();
    private int highestConstantID = Integer.MIN_VALUE;
    private int lowestConstantID = Integer.MAX_VALUE;
    private String comment;

    public EnumType(TypeDefinition definition, String javaName) {
        this.javaName = javaName;
        this.definition = definition;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void registerConstant(EnumConstant constant) {
        if (constants.values().stream().anyMatch(e -> e.getName().equals(constant.getName())))
            throw new IllegalArgumentException("Enum " + javaName + " already has constant with name: " + constant.getName());
        if (constants.containsKey(constant.getId())) {
            // This is valid... Why shouldn't it.... Urgh, this breaks my whole enum assumption
            constants.computeIfPresent(constant.getId(), (integer, old_const) -> new EnumConstant(integer, old_const.getName() + "_" + constant.getName(), constant.getComment()));
            return;
        }

        constants.put(constant.getId(), constant);

        if (constant.getId() > highestConstantID)
            highestConstantID = constant.getId();
        if (constant.getId() < lowestConstantID)
            lowestConstantID = constant.getId();
    }

    public void write(CompilationUnit cu) {
        cu.addImport(ClassNameConstants.ENUMPOINTER_CLASS);
        cu.addImport(ClassNameConstants.CENUM_CLASS);
        EnumDeclaration declaration = cu.addEnum(javaName);
        declaration.addImplementedType("CEnum");

        if (comment != null)
            declaration.setJavadocComment(comment);

        constants.values().stream()
                .sorted(Comparator.comparingInt(EnumConstant::getId))
                .forEach(constant -> {
                    EnumConstantDeclaration dec = declaration.addEnumConstant(constant.getName())
                            .addArgument(new IntegerLiteralExpr(String.valueOf(constant.getId())));
                    if (constant.getComment() != null)
                        dec.setJavadocComment(constant.getComment());
                });

        declaration.addField(int.class, "index", Keyword.PRIVATE, Keyword.FINAL);
        ConstructorDeclaration constructor = declaration.addConstructor().addParameter(int.class, "index");
        constructor.createBody().addStatement("this.index = index;");

        declaration.addMethod("getIndex", Keyword.PUBLIC).setType(int.class)
                .createBody().addStatement(new ReturnStmt("index"));

        MethodDeclaration getByIndex = declaration.addMethod("getByIndex", Keyword.PUBLIC, Keyword.STATIC);
        getByIndex.addParameter(int.class, "index");
        getByIndex.setType(javaName);
        if (highestConstantID - lowestConstantID <= MAX_VALUE_ARRAY_SIZE) {
            getByIndex.createBody().addStatement("return _values[index];");
            NodeList<Expression> expressions = new NodeList<>();
            for (int i = lowestConstantID; i <= highestConstantID; i++) {
                EnumConstant constant = constants.get(i);
                expressions.add(constant == null ? new NullLiteralExpr() : new NameExpr(constant.getName()));
            }
            ArrayInitializerExpr initializerExpr = new ArrayInitializerExpr(expressions);
            declaration.addFieldWithInitializer(javaName + "[]", "_values", initializerExpr, Keyword.PRIVATE, Keyword.FINAL, Keyword.STATIC);
        } else {
            cu.addImport(HashMap.class);
            ObjectCreationExpr createHashMap = new ObjectCreationExpr();
            createHashMap.setType(HashMap.class);
            declaration.addFieldWithInitializer("HashMap<Integer, " + javaName + ">", "_values", createHashMap, Keyword.PRIVATE, Keyword.FINAL, Keyword.STATIC);
            BlockStmt staticInit = declaration.addStaticInitializer();
            staticInit.addStatement("for (" + javaName + " _val : values()) _values.put(_val.index, _val);");
            getByIndex.createBody().addStatement("return _values.get(index);");
        }

        String pointerName = javaName + "Pointer";

        // Pointer
        ClassOrInterfaceDeclaration pointerClass = new ClassOrInterfaceDeclaration(new NodeList<>(Modifier.publicModifier(), Modifier.staticModifier(), Modifier.finalModifier()), false, pointerName);

        declaration.addMember(pointerClass);
        pointerClass.addExtendedType("EnumPointer<" + javaName + ">");
        ConstructorDeclaration pointerConstructor = pointerClass.addConstructor(Keyword.PUBLIC);
        pointerConstructor.addParameter(new Parameter(com.github.javaparser.ast.type.PrimitiveType.longType(), "pointer"));
        pointerConstructor.addParameter(new Parameter(PrimitiveType.booleanType(), "freeOnGC"));
        BlockStmt body = new BlockStmt();
        body.addStatement("super(pointer, freeOnGC);");
        pointerConstructor.setBody(body);

        pointerClass.addConstructor(Keyword.PUBLIC).getBody().addStatement("this(1, true, true);");

        ConstructorDeclaration defaultConstructorPointer = pointerClass.addConstructor(Keyword.PUBLIC);
        defaultConstructorPointer.addParameter(int.class, "count");
        defaultConstructorPointer.addParameter(boolean.class, "freeOnGC");
        defaultConstructorPointer.addParameter(boolean.class, "guard");
        defaultConstructorPointer.createBody().addStatement("super(count, freeOnGC, guard);");

        pointerClass.addMethod("guardCount", Keyword.PUBLIC).setType(javaName + "." + pointerName)
                .addParameter(long.class, "count")
                .createBody()
                .addStatement("super.guardCount(count);")
                .addStatement("return this;");

        pointerClass.addMethod("getEnum", Keyword.PROTECTED).setType(javaName)
                .addParameter(int.class, "index")
                .createBody().addStatement("return getByIndex(index);");

    }

    @Override
    public void importType(CompilationUnit cu) {
        cu.addImport(classFile());
    }

    @Override
    public String classFile() {
        return packageName() + "." + javaName;
    }

    @Override
    public String packageName() {
        return Manager.getInstance().getBasePackage() + ".enums";
    }

    @Override
    public String abstractType() {
        return javaName;
    }

    @Override
    public String primitiveType() {
        return int.class.getName(); // Should be able to hold it
    }

    @Override
    public Expression fromC(Expression cRetrieved) {
        CastExpr intCast = new CastExpr();
        intCast.setType(int.class);
        intCast.setExpression(cRetrieved);
        MethodCallExpr callByIndex = new MethodCallExpr("getByIndex", intCast);
        callByIndex.setScope(new NameExpr(instantiationType()));
        return callByIndex;
    }

    @Override
    public Expression toC(Expression cSend) {
        MethodCallExpr methodCallExpr = new MethodCallExpr("getIndex");
        methodCallExpr.setScope(cSend);
        return methodCallExpr;
    }

    @Override
    public int typeID() {
        return Manager.getInstance().getCTypeID("int");
    }
}
