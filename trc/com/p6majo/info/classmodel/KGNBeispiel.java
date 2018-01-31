package com.p6majo.info.classmodel;

public class KGNBeispiel {
    public static void main(String[] args) {
        Schule kgn = new Schule("Kopernikus-Gymnasium");

        Lehrer infoLehrer = new Lehrer("Herr Martin","Informatik",
                "Mathematik");
        kgn.addLehrer(infoLehrer);

        Schueler leo = new Schueler("Leo");
        kgn.addSchueler(leo);
        kgn.addSchueler(new Schueler("Hendrik S."));
        kgn.addSchueler(new Schueler("Hendrik V."));
        kgn.addSchueler(new Schueler("Anna"));
        kgn.addSchueler(new Schueler("Jette"));
        //usw.
        kgn.setHausmeister(new Hausmeister("Herr Sparacino"));
        System.out.println(kgn.toString());

        System.out.println("Klausurverfahren:");
        leo.schreibeKlausur("Informatik");
        leo.uebergebeKlausur(infoLehrer);
        Klausur klausur = infoLehrer.bewerteKlausur();
        leo.empfangeKlausur(klausur);
    }
}
