package com.p6majo.info.bundesliga;

public class Verein {

    private Spieler[] spieler;
    private String name;
    private int platzierung;

    public Verein(String name,int platzierung){
        this.name = name;
        this.spieler = new Spieler[25];
        this.platzierung =platzierung;
    }

    public void addSpieler(Spieler spieler,int position){
        this.spieler[position]=spieler;
    }

    public Spieler getSpieler(int position){
        return this.spieler[position];
    }

    public String getName() {
        return name;
    }


    public int getPlatzierung() {
        return platzierung;
    }

    public void setPlatzierung(int platzierung) {
        this.platzierung = platzierung;
    }

    public String toString(){
        return this.name;
    }
}
