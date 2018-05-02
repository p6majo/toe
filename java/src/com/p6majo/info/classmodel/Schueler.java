package com.p6majo.info.classmodel;

public class Schueler extends Mensch {
    private Klausur aktuelleKlausur;

    public Schueler(String name) {
        super(name);
    }

    public String toString() {
        return "Schueler - " + super.toString();
    }


    public void schreibeKlausur(String fach,Lehrer lehrer) {
       this.aktuelleKlausur = new Klausur(this, "Informatik",lehrer);
       this.aktuelleKlausur.setInhalt("Das ist der Inhalt meiner Klausur ....");
       System.out.println(this.toString()+": Uff, ich habe eine lange Klausur geschrieben und gebe sie jetzt meinem Lehrer");
        aktuelleKlausur.getLehrer().uebergebeKlausur(aktuelleKlausur);
    }


    public void empfangeKlausur(Klausur klausur) {
        this.aktuelleKlausur=klausur;
        System.out.println(this.toString()+": Ich habe eine "+this.aktuelleKlausur.getNote()+" bekommen!");
    }

}
