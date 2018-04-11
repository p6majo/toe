package com.p6majo.info.geometrie;

public class Kreis extends Figur {

    private double radius;

    public Kreis( String farbe,double radius) {
        super("Kreis", farbe);
        this.radius = radius;
    }

    @Override
    public double berechneFlaecheninhalt() {
        return Math.PI*radius*radius;
    }
}
