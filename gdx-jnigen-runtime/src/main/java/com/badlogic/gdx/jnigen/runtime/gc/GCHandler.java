package com.badlogic.gdx.jnigen.runtime.gc;

import com.badlogic.gdx.jnigen.runtime.mem.BufferPtrAllocator;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.ReferenceQueue;

public class GCHandler {
    private static final boolean NO_GC_FREE = System.getProperty("com.badlogic.jnigen.gc.gc_disabled", "false").equals("true");
    private static final boolean ENABLE_GC_LOG = System.getProperty("com.badlogic.jnigen.gc.gc_log", "false").equals("true");
    protected static final ReferenceQueue<Pointing> REFERENCE_QUEUE = new ReferenceQueue<>();
    private static final ReferenceList referenceList = new ReferenceList();

    private static final Thread RELEASER = new Thread() {
        @SuppressWarnings("InfiniteLoopStatement")
        @Override
        public void run() {
            while (true) {
                try {
                    PointingPhantomReference pointingRef = (PointingPhantomReference)REFERENCE_QUEUE.remove();
                    referenceList.removeReference(pointingRef);

                    if (ENABLE_GC_LOG)
                        System.out.println("Freeing Pointer: " + pointingRef.getBufferPtr().getPointer());

                    if (pointingRef.isFreeOnGC())
                        pointingRef.getBufferPtr().free();
                    BufferPtrAllocator.insertPool(pointingRef.getBufferPtr());
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

    public static void enqueuePointer(Pointing pointing, boolean freeOnGC) {
        if (NO_GC_FREE)
            return;
        if (ENABLE_GC_LOG)
            System.out.println("Enqueuing Pointer: " + pointing.getPointer() + " of class " + pointing.getClass() + " freeOnGC: " + freeOnGC);
        if (pointing.isNull())
            return;

        // TODO: The memory pressure gets to much on many object allocations. Revisit this switch, once Arena is implemented
        if (!freeOnGC)
            return;

        PointingPhantomReference structPhantomReference = new PointingPhantomReference(pointing, freeOnGC);
        referenceList.insertReference(structPhantomReference);
    }

    public static long nativeObjectCount() {
        return referenceList.getSize();
    }
}
