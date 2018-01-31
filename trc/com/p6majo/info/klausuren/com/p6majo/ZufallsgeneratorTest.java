package com.p6majo.info.klausuren.com.p6majo;

public class ZufallsgeneratorTest {
    public static void main(String[] args) {
        int zaehler=0;
        int wuerfel = 0;
        for (int i=0;i<10000;i++){
            wuerfel = (int) Math.round(Math.random()*6+0.5);
            if (wuerfel==6) zaehler++;
        }
        System.out.println("Anzahl der Sechsen: "+zaehler);
    }
}
