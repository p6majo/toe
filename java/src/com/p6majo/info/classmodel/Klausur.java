package com.p6majo.info.classmodel;

public class Klausur {
    private int note = -1;
    private String inhalt;
    private String fach;
    private Schueler schueler;
    private Lehrer lehrer;

    public Klausur(Schueler schueler,String fach,Lehrer lehrer) {
        this.schueler = schueler;
        this.fach = fach;
        this.lehrer = lehrer;
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

    public Lehrer getLehrer() {
        return lehrer;
    }
}
