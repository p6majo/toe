package com.p6majo.info.europa;

/**
 * Laenderklasse
 *
 * die Attribute eines Landes sind dessen Name, Hauptstadt, Flaeche, Einwohnerzahl,
 * die hoechste Erhebung und der Name der hoechsten Erhebung
 *
 * @author jmartin
 * @version 1.0
 */
public class Land {

    private String name;
    private String hauptstadt;
    private int flaeche;
    private int einwohner;
    private String topName;
    private int top;


    public Land(String name,String hauptstadt, int flaeche, int einwohner, String topName, int top ){
        this.name =name;
        this.hauptstadt=hauptstadt;
        this.flaeche=flaeche;
        this.einwohner=einwohner;
        this.topName =topName;
        this.top = top;
    }

    public String getName() {
        return name;
    }

    public String getHauptstadt() {
        return hauptstadt;
    }

    public int getFlaeche() {
        return flaeche;
    }

    public int getEinwohner() {
        return einwohner;
    }

    public String getTopName() {
        return topName;
    }

    public int getTop() {
        return top;
    }

    public String toString(){
        return this.name+" - Hauptstadt: "+this.hauptstadt+" - Flaeche: "+flaeche+"qkm - Einwohner: "+einwohner+" - hoechster Punkt: "+topName+" mit "+top+"m.";
    }
}
