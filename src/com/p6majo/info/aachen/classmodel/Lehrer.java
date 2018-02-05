package com.p6majo.info.aachen.classmodel;

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
        return "Lehrer - " + super.toString();
    }

    public void bewerteKlausur() {
        this.aktuelleKlausur.setNote((int) Math.round(Math.random()*6));
        System.out.println(toString()+": Fertig, ich habe eine Klausur bewertet und übergebe sie jetzt an "+this.aktuelleKlausur.getSchueler().toString());
        this.aktuelleKlausur.getSchueler().empfangeKlausur(this.aktuelleKlausur);
    }

    public void uebergebeKlausur(Klausur klausur) {
        this.aktuelleKlausur = klausur;
        System.out.println(this.toString()+": Ohje, ich habe eine Klausur bekommen und bewerte sie jetzt");
        this.bewerteKlausur();
    }

}
