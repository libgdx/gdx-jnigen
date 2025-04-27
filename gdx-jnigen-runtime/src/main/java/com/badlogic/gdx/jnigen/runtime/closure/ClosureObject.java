package com.badlogic.gdx.jnigen.runtime.closure;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

/**
 * A ClosureObject manages a function pointer. The relation of a function pointer to ClosureObject is 1:1.
 * A ClosureObject can either wrap a java allocated interface or a C function pointer.
 * A ClosureObject has a manual lifecycle. If the closure is java allocated, it needs to be freed manually with {@link ClosureObject#free()}.
 */
public abstract class ClosureObject<T extends Closure> extends VoidPointer {

    private final T closure;

    /**
     * Creates a new Closure for the java object. The ClosureObject needs to be freed manually with {@link ClosureObject#free()}.
     */
    public static <T extends Closure> ClosureObject<T> fromClosure(T object) {
        return CHandler.createClosureForObject(object);
    }

    public ClosureObject(T closure, long fnPtr) {
        super(fnPtr, false);
        this.closure = closure;
    }

    @Override
    public abstract void free();

    /**
     * Sets a Pool to avoid allocations on upcalls (aka C -> java).
     * The Manager needs to be fully initialized with the relevant inner pools. For C closures this is a no-op
     * @see PointingPoolManager for more information on cautions
     */
    public abstract void setPoolManager(PointingPoolManager manager);

    /**
     * The java object that is wrapped. This can either be implemented on C or in java.
     * Invoking a C closure will initiate a downcall into C code.
     */
    public T getClosure() {
        return closure;
    }
}
