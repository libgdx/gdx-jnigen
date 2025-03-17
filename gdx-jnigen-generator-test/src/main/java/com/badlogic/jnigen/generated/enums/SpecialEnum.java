package com.badlogic.jnigen.generated.enums;

import com.badlogic.gdx.jnigen.runtime.pointer.EnumPointer;
import com.badlogic.gdx.jnigen.runtime.c.CEnum;
import java.util.HashMap;

public enum SpecialEnum implements CEnum {

    LOWER(0), HIGH(160);

    private final int index;

    SpecialEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static SpecialEnum getByIndex(int index) {
        return _values.get(index);
    }

    private final static HashMap<Integer, SpecialEnum> _values = new HashMap();

    static {
        for (SpecialEnum _val : values()) _values.put(_val.index, _val);
    }

    public static final class SpecialEnumPointer extends EnumPointer<SpecialEnum> {

        public SpecialEnumPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public SpecialEnumPointer() {
            this(1, true, true);
        }

        public SpecialEnumPointer(int count, boolean freeOnGC, boolean guard) {
            super(count, freeOnGC, guard);
        }

        public SpecialEnum.SpecialEnumPointer guardCount(long count) {
            super.guardCount(count);
            return this;
        }

        protected SpecialEnum getEnum(int index) {
            return getByIndex(index);
        }
    }
}
