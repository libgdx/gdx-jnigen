package com.badlogic.gdx.jnigen.pointer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CType {
    String value();
}
