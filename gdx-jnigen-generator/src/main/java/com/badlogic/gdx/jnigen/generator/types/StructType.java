package com.badlogic.gdx.jnigen.generator.types;

import java.util.ArrayList;
import java.util.List;

public class StructType implements Emitable {

    private final String name;
    private final List<StructField> fields = new ArrayList<>();

    public StructType(String name) {
        this.name = name;
    }

    public void addField(String name, TypeKind kind) {
        fields.add(new StructField(name, kind));
    }

    @Override
    public void write() {
        throw new RuntimeException();
    }

    static class StructField {
        private String name;
        private TypeKind kind;

        public StructField(String name, TypeKind kind) {
            this.name = name;
            this.kind = kind;
        }
    }
}
