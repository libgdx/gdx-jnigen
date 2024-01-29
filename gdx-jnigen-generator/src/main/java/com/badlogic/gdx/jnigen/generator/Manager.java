package com.badlogic.gdx.jnigen.generator;

import com.badlogic.gdx.jnigen.generator.types.Emitable;
import com.badlogic.gdx.jnigen.generator.types.StructType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Manager {

    private static final Manager instance;

    static {
        instance = new Manager();
    }

    private List<StructType> structs = new LinkedList<>();



    public static Manager getInstance() {
        return instance;
    }


}
