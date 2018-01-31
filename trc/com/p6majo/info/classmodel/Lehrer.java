package com.p6majo.info.classmodel;

public class Lehrer extends Mensch {
    private String fach1;
    private String fach2;
    private Klausur aktuelleKlausur;

    public Lehrer(String name, String fach1, String fach2) {
        super(name);
        this.fach1 = fach1;
        this.fach2 = fach2;
    }

    public String toString() {
        return "Lehrer: " + super.toString();
    }

    public Klausur bewerteKlausur() {
        this.aktuelleKlausur.setNote((int) Math.round(Math.random()*6));
        return this.aktuelleKlausur;
    }

    public void setAktuelleKlausur(Klausur klausur) {
        this.aktuelleKlausur = klausur;
    }

    public void rueckgabeKlausur(){
        this.aktuelleKlausur.getSchueler().empfangeKlausur(this.aktuelleKlausur);
    }
}
