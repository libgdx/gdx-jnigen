package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.c.CEnum;

@FunctionalInterface
public interface IntToCEnumFunction<T extends CEnum> {

    T apply(int id);
}
