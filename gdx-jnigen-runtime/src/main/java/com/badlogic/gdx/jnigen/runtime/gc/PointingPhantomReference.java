package com.badlogic.gdx.jnigen.runtime.gc;

import com.badlogic.gdx.jnigen.runtime.gc.ReferenceList.ReferenceListNode;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;

import java.lang.ref.PhantomReference;

public final class PointingPhantomReference extends PhantomReference<Pointing> {

    private final long pointer;
    private ReferenceListNode node;
    private int position;


    public PointingPhantomReference(Pointing referent) {
        super(referent, GCHandler.REFERENCE_QUEUE);
        this.pointer = referent.getPointer();
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

    public long getPointer() {
        return pointer;
    }
}
