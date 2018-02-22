package com.p6majo.plot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface OutputChannel {

    List<PrimitivePoint> primitivePoints = new ArrayList<PrimitivePoint>();
    List<PrimitiveLine> primitiveLines = new ArrayList<PrimitiveLine>();

    default void addPrimitive(Primitive primitive){
        if (primitive instanceof PrimitivePoint)
            primitivePoints.add((PrimitivePoint) primitive);
        else if (primitive instanceof PrimitiveLine)
            primitiveLines.add((PrimitiveLine) primitive);

    }

    default void addPrimitive(PrimitivePoint primitive){
            primitivePoints.add((PrimitivePoint) primitive);
    }

    default void addPrimitive(PrimitiveLine primitive){
            primitiveLines.add((PrimitiveLine) primitive);
    }

    default void addPrimitives(Collection<PrimitivePoint>primitives){
        primitivePoints.addAll(primitives);
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
