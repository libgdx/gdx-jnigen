package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.closure.ClosureObject;

public class Closures {

    public interface CallbackNoReturnNoArg extends Closure {
        void toCall();
    }

    public interface CallbackNoReturnLongArg extends Closure {
        void toCall(long arg);
    }

    public static void methodWithCallback(ClosureObject<CallbackNoReturnNoArg> closure) {
        methodWithCallback(closure.getFnPtr());
    }

    private static native void methodWithCallback(long fnPtr);/*
        ((void (*)())fnPtr)();
    */

    public static native void methodWithCallbackLongArg(long fnPtr);/*
        long long arg = 5;
        ((void (*)(long long))fnPtr)(arg);
    */
}
