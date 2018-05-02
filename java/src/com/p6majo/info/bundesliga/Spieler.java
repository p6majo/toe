package com.p6majo.info.bundesliga;

public class Spieler {

    private String name;
    private int startnummer;

    public Spieler(String name, int startnummer){
        this.name = name;
        this.startnummer = startnummer;
    }

    public String getName() {
        return name;
    }

    public int getStartnummer() {
        return startnummer;
    }
}
