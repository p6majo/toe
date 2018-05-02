package com.p6majo.info.array;

import com.p6majo.info.aachen.Kon;

public class Aufgaben
{
    public static void main(String[] args){

        System.out.println("Aufgabe 0:");
        Hund[] hunde = new Hund[10];
        for (int i=0;i<hunde.length;i++){
            hunde[i] = new Hund();
        }


        for (int i=0;i<hunde.length;i++){
            hunde[i].bellen();
        }

        System.out.println("Aufgabe 1:");
        int[] zahlen = new int[10];
        for (int i=0;i<zahlen.length;i++){
            zahlen[i]=i*i;
        }
        for (int i=zahlen.length-1;i>-1;i--){
            System.out.println(zahlen[i]);
        }

        System.out.println("Aufgabe 2:");

        hunde = Generator.getHunde();
        zahlen = Generator.getZufallszahlen();

        for (int i=0;i<hunde.length;i++){
            hunde[i].bellen();
        }

        System.out.println(hunde.length+" Hunde haben gebellt.");

        for (int i=zahlen.length-1;i>-1;i--){
            System.out.println(zahlen[i]);
        }

        System.out.println("Aufgabe 3:");

        zahlen = Lotto.lottoZahlen();
        for (int i=zahlen.length-1;i>-1;i--){
            System.out.println(zahlen[i]);
        }

        System.out.println("10 Runden NoRiskNoMoney:");
        int runde = 0;
        while (runde<10) {
            System.out.println("Runde " + (runde + 1));

            NoRiskNoMoney game = new NoRiskNoMoney();
            while(!game.isSpielFertig()){
                System.out.println("Nochmal wuerfeln [j/n]");
                String antwort =  Kon.readString();
                if (antwort.compareTo("j")==0) game.wuerfeln();
                else break;
            }

            System.out.println("Du hast folgenden Gewinn: "+game.getGewinn());
            runde++;
        }
    }



}
