package com.p6majo.info.classmodel;

public class Klausur {
    private int note = -1;
    private String inhalt;
    private String fach;
    private Schueler schueler;

    public Klausur(Schueler schueler,String fach) {
        this.schueler = schueler;
        this.fach = fach;
    }

    public int getNote() {
        return this.note;
    }

    public void setNote(int note){this.note = note;}

    public String getInhalt() {
        return this.inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    public Schueler getSchueler() {
        return schueler;
    }
}
