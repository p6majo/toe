package com.p6majo.info.geometrie;

public class Rechteck extends Figur {

    private double laenge;
    private double breite;

    public Rechteck(String farbe,double laenge, double breite) {
        super("Rechteck", farbe);
    }

    public Rechteck(String name,String farbe,double laenge, double breite){
        super(name,farbe);
    }

    @Override
    public double berechneFlaecheninhalt() {
        return laenge*breite;
    }
}
