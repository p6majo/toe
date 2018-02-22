package com.p6majo.plot;

import java.awt.*;

public class PrimitiveLine extends Primitive {
    public final Point start;
    public final Point end;
    public final Color color;

    public PrimitiveLine(Point start, Point end, Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }

}
