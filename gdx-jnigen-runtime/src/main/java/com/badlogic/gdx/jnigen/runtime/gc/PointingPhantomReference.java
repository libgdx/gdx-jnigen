package com.badlogic.gdx.jnigen.runtime.gc;

import com.badlogic.gdx.jnigen.runtime.gc.ReferenceList.ReferenceListNode;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;

import java.lang.ref.PhantomReference;

public final class PointingPhantomReference extends PhantomReference<Pointing> {

    private final BufferPtr bufferPtr;
    private final boolean freeOnGC;
    private ReferenceListNode node;
    private int position;


    public PointingPhantomReference(Pointing referent, boolean freeOnGC) {
        super(referent, GCHandler.REFERENCE_QUEUE);
        this.bufferPtr = referent.getBufPtr();
        this.freeOnGC = freeOnGC;
    }

    public void setNode(ReferenceListNode node) {
        this.node = node;
    }

    public ReferenceListNode getNode() {
        return node;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public BufferPtr getBufferPtr() {
        return bufferPtr;
    }

    public boolean isFreeOnGC() {
        return freeOnGC;
    }
}
