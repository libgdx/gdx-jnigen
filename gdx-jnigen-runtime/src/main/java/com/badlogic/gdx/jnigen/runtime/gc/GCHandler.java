package com.badlogic.gdx.jnigen.runtime.gc;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.ReferenceQueue;

public class GCHandler {
    private static final boolean NO_GC_FREE = System.getProperty("com.badlogic.jnigen.gc_disabled", "false").equals("true");
    private static final boolean ENABLE_GC_LOG = System.getProperty("com.badlogic.jnigen.gc_log", "false").equals("true");
    protected static final ReferenceQueue<Pointing> REFERENCE_QUEUE = new ReferenceQueue<>();
    private static final ReferenceList referenceList = new ReferenceList();

    private static final Thread RELEASER = new Thread() {
        @SuppressWarnings("InfiniteLoopStatement")
        @Override
        public void run() {
            while (true) {
                try {
                    PointingPhantomReference releasedStructRef = (PointingPhantomReference)REFERENCE_QUEUE.remove();
                    referenceList.removeReference(releasedStructRef);

                    if (ENABLE_GC_LOG)
                        System.out.println("Freeing Pointer: " + releasedStructRef.getPointer());

                    CHandler.free(releasedStructRef.getPointer());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    static {
        RELEASER.setDaemon(true);
        RELEASER.setName("jnigen-releaser");
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
        if (NO_GC_FREE)
            return;
        if (ENABLE_GC_LOG)
            System.out.println("Enqueuing Pointer: " + pointing.getPointer() + " -> " + pointing.getClass().getSimpleName());

        PointingPhantomReference structPhantomReference = new PointingPhantomReference(pointing);
        referenceList.insertReference(structPhantomReference);
    }

    public static long nativeObjectCount() {
        return referenceList.getSize();
    }
}
