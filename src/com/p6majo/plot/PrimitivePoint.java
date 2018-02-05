package com.p6majo.plot;

import java.awt.*;

public class PrimitivePoint extends Primitive {
    public final Point point;
    public final Color color;

    public PrimitivePoint(Point point,Color color) {
        this.point = point;
        this.color = color;
    }

}
