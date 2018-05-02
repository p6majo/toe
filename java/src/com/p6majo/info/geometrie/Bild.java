package com.p6majo.info.geometrie;

public class Bild {
    private Figur[] figuren ;
    private int figurenZahl = 0;

    public Bild(){
        figuren = new Figur[100];
    }

    public void figurHinzufuegen(Figur figur){
        figuren[figurenZahl]=figur;
        figurenZahl++;
    }

    public String toString(){
        StringBuilder out = new StringBuilder();
        for (int i=0;i<figurenZahl;i++)
            out.append("Figur "+i+": "+figuren[i].toString()+" mit einer Flaeche von "+figuren[i].berechneFlaecheninhalt()+" und Farbe "+figuren[i].getFarbe());
        return out.toString();
    }
}
