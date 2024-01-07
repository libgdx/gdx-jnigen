package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.Struct;
import com.badlogic.gdx.jnigen.pointer.Pointing;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.ReferenceQueue;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GCHandler {
    protected static final ReferenceQueue<Pointing> REFERENCE_QUEUE = new ReferenceQueue<>();
    protected static final Set<PointingPhantomReference> referenceHolder = Collections.synchronizedSet(new HashSet<PointingPhantomReference>());

    private static final Thread RELEASER = new Thread() {
        @Override
        public void run() {
            while (true) {
                try {
                    PointingPhantomReference releasedStructRef = (PointingPhantomReference)REFERENCE_QUEUE.remove();
                    if (!referenceHolder.remove(releasedStructRef)) {
                        System.err.println("Reference holder did not contained released StructRef.");
                    }
                    Global.free(releasedStructRef.getPointer());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    static {
        RELEASER.setDaemon(true);
        RELEASER.setName("jnigen release thread");
        RELEASER.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace(); // Better logging :/
                System.exit(1);
            }
        });
        RELEASER.start();
    }

    public static void enqueuePointer(Pointing pointing) {
        PointingPhantomReference structPhantomReference = new PointingPhantomReference(pointing);
        referenceHolder.add(structPhantomReference);
    }

}
