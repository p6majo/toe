package com.p6majo.info;

import com.p6majo.info.classmodel.Hausmeister;
import com.p6majo.info.classmodel.Lehrer;
import com.p6majo.info.classmodel.Schueler;
import com.p6majo.info.classmodel.Schule;

public class KGNBeispiel {
    public static void main(String[] args) {
        Schule kgn = new Schule("Kopernikus-Gymnasium");

        Lehrer infoLehrer = new Lehrer("Herr Martin","Informatik","Mathematik");
        kgn.addLehrer(infoLehrer);
        kgn.addSchueler(new Schueler("Hendrik S."));
        kgn.addSchueler(new Schueler("Hendrik V."));
        kgn.addSchueler(new Schueler("Anna"));
        kgn.addSchueler(new Schueler("Jette"));
        //usw.
        kgn.setHausmeister(new Hausmeister("Herr Sparacino"));
        System.out.println(kgn.toString());

        System.out.println("Klausurtag:");

        for (int i=0;i<kgn.getSchuelerschaft().size();i++) {
            kgn.getSchuelerschaft().get(i).schreibeKlausur("Informatik", infoLehrer);
        }

    }
}
