package com.p6majo.info.geometrie;

public abstract class Figur {
    private String farbe;
    private String name;
    public abstract double berechneFlaecheninhalt();

    public Figur(String name,String farbe){
        this.name = name;
        this.farbe = farbe;
    }


    public String getFarbe(){
        return this.farbe;
    }
    public String toString(){
        return this.name;
    }
}
