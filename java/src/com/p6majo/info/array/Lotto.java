package com.p6majo.info.array;

public class Lotto {
    public static int[] lottoZahlen(){
        int [] zahlen = new int[7];
        boolean[] gezogen = new boolean[50];

        int zaehler = 0;
        while (zaehler<7){
            int zahl = (int) Math.round(Math.random()*49+0.5);
            if (gezogen[zahl]==false){
                zahlen[zaehler]=zahl;
                zaehler++;
                gezogen[zahl]=true;
            }
        }

        return zahlen;
    }
}
