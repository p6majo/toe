package com.p6majo.plot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface OutputChannel {

    List<PrimitivePoint> primitives = new ArrayList<PrimitivePoint>();

    default void addPrimitive(PrimitivePoint primitive){
        primitives.add(primitive);
    }

    default void addPrimitives(Collection<PrimitivePoint>primitives){
        primitives.addAll(primitives);
    }

    /**
     * finish the creation, no further changes are performed to the output channel
     */
    public abstract void finished();

    /**
     * close output
     */
    public abstract void close();

}
