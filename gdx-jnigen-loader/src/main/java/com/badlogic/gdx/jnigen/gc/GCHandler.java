package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.pointer.Pointing;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.ReferenceQueue;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class GCHandler {
    protected static final ReferenceQueue<Pointing> REFERENCE_QUEUE = new ReferenceQueue<>();
    private static final Set<PointingPhantomReference> referenceHolder = Collections.synchronizedSet(new HashSet<>());
    private static final Map<Long, AtomicInteger> countMap = Collections.synchronizedMap(new HashMap<>());

    private static final Thread RELEASER = new Thread() {
        @Override
        public void run() {
            while (true) {
                try {
                    PointingPhantomReference releasedStructRef = (PointingPhantomReference)REFERENCE_QUEUE.remove();
                    if (!referenceHolder.remove(releasedStructRef)) {
                        System.err.println("Reference holder did not contained released StructRef.");
                    }
                    AtomicInteger counter = countMap.get(releasedStructRef.getPointer());
                    int count = counter.decrementAndGet();
                    if (count <= 0)
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
        countMap.computeIfAbsent(pointing.getPointer(), peer -> new AtomicInteger(0)).incrementAndGet();
        referenceHolder.add(structPhantomReference);
    }

    public static int nativeObjectCount() {
        return referenceHolder.size();
    }

}
