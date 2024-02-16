package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.c.CEnum;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

public class EnumType implements MappedType {

    private final TypeDefinition definition;
    private final String javaName;
    private final HashMap<Integer, String> constants = new HashMap<>();
    private int highestConstantID = 0;

    public EnumType(TypeDefinition definition) {
        this.definition = definition;
        this.javaName = definition.getTypeName().replace("enum ", "");
    }

    public void registerConstant(String constantName, int index) {
        if (constants.containsValue(constantName))
            throw new IllegalArgumentException("Enum " + javaName + " already has constant with name: " + constantName);
        if (constants.containsKey(index)) {
            // This is valid... Why shouldn't it.... Urgh, this breaks my whole enum assumption
            constants.computeIfPresent(index, (integer, s) -> s + "_" + constantName);
            return;
        }

        constants.put(index, constantName);
        if (index > highestConstantID)
            highestConstantID = index;
    }

    public void write(CompilationUnit cu) {
        EnumDeclaration declaration = cu.addEnum(javaName);
        declaration.addImplementedType(CEnum.class);
        constants.entrySet().stream()
                .sorted(Comparator.comparingInt(Entry::getKey))
                .forEach(stringIntegerEntry ->
                        declaration.addEnumConstant(stringIntegerEntry.getValue())
                                .addArgument(new IntegerLiteralExpr(String.valueOf(stringIntegerEntry.getKey()))));
        declaration.addField(int.class, "index", Keyword.PRIVATE, Keyword.FINAL);
        ConstructorDeclaration constructor = declaration.addConstructor().addParameter(int.class, "index");
        constructor.createBody().addStatement("this.index = index;");

        declaration.addMethod("getIndex", Keyword.PUBLIC).setType(int.class)
                .createBody().addStatement(new ReturnStmt("index"));

        MethodDeclaration getByIndex = declaration.addMethod("getByIndex", Keyword.PUBLIC, Keyword.STATIC);
        getByIndex.addParameter(int.class, "index");
        getByIndex.setType(javaName);
        if (highestConstantID <= 16) {
            getByIndex.createBody().addStatement("return _values[index];");
            NodeList<Expression> expressions = new NodeList<>();
            for (int i = 0; i <= highestConstantID; i++) {
                String name = constants.get(i);
                expressions.add(name == null ? new NullLiteralExpr() : new NameExpr(name));
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
    public MappedType asPointer() {
        throw new IllegalArgumentException("To implement");
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
