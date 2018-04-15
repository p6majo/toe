package com.p6majo.info.klausuren;

public class FindMinimum {
    public static void main(String[] args) {
        double[] feld = new double[51];
        for (int i = 0; i < 51; i++) {
            feld[i] = Math.random() * 100;
        }
        int position = 0;
        double min = feld[0];

        for (int i = 1; i < 51; i++){
            if (min > feld[i]) {
                min = feld[i];
                position = i;
            }
        }
        System.out.println("Das Minimum " + min + " ist an der " + position + ".ten Stelle");
    }
}

