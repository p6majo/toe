package com.p6majo.info;

import com.p6majo.math.utils.Utils;

public class Klausur2_20180410 {
    public static void main(String[] args) {
        for (int i=0;i<5;i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println("Hallo Welt!");
            }
        }

        int summe = 0;
        for (int i=0;i<3;i++){
            for (int j=0;j<5;j=j+2){
                summe =summe +j;
            }
        }
        System.out.println("summe: "+summe);


        for (int i=1;i<5;i++){
            for (int j=i+1;j<5;j++){
                System.out.println("Ergebnis: "+(i*j));
            }
        }

        double[] feld = new double[100];
        for (int i=0;i<100;i++){
            feld[i] = Math.random()*10;
        }
        double max = feld[0];
        for (int i=1;i<100;i++){
            if (feld[i]>max) max = feld[i];
        }
        System.out.println("Die groesste Zufallszahl ist :"+max);


        double[] data = new double[]{10.5,54.3,13.4,-15.2,37.6};

        System.out.println(Utils.doubleArray2String(data, ",", "[]"));

        for (int i=0;i<data.length;i++){
            for (int j=i+1;j<data.length;j++){
                double tmp = data[i];
                if (data[i]<data[j]) {
                    data[i]=data[j];
                    data[j]=tmp;
                }
                System.out.println(i+"   "+j+"   "+tmp+"   "+Utils.doubleArray2String(data,"   ",""));
            }
        }

        System.out.println(Utils.doubleArray2String(data, ",", "[]"));

    }
}
