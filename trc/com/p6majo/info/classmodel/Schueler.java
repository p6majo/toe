package com.p6majo.info.classmodel;

public class Schueler extends Mensch {
    private Klausur aktuelleKlausur;

    public Schueler(String name) {
        super(name);
    }

    public String toString() {
        return "Schueler: " + super.toString();
    }


    public void schreibeKlausur(String fach) {
       this.aktuelleKlausur = new Klausur(this, "Informatik");
       this.aktuelleKlausur.setInhalt("Das ist der Inhalt meiner Klausur ....");
    }

    public void uebergebeKlausur(Lehrer lehrer) {
        lehrer.setAktuelleKlausur(aktuelleKlausur);
    }

    public void empfangeKlausur(Klausur klausur) {
        this.aktuelleKlausur=klausur;
        System.out.println("Ich habe eine "+this.aktuelleKlausur.getNote()+" bekommen!");
    }

}
