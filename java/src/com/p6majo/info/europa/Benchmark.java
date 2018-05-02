package com.p6majo.info.europa;


public class Benchmark {
    private static double[] array ;

    private static void selectionSort(){
        System.out.println("Benchmarktest fuer den SelectionSort-Algorithmus:");
        long start = System.currentTimeMillis();

        for (int i=0;i<array.length;i++){
            int min = i;
            for (int j=i+1;j<array.length;j++){
                if (array[min]<array[j]) min=j;
            }
            if (min!=i) {
                tausche(min,i);
            }
        }

        System.out.println("Sortierung von "+array.length+" Zahlen durchgefuehrt in:");
        System.out.println((System.currentTimeMillis()-start)+" ms.");
    }

    private static void bubbleSort(){
        System.out.println("Benchmarktest fuer den BubbleSort-Algorithmus:");

        long start = System.currentTimeMillis();
        for(int i=array.length;i>1;i--){
            for (int j=0;j<i-1;j++){
                if (array[j]>array[j+1]) tausche(j,j+1);
            }
        }

        System.out.println("Sortierung von "+array.length+" Zahlen durchgefuehrt in:");
        System.out.println((System.currentTimeMillis()-start)+" ms.");

    }

    private static void insertionSort(){
        System.out.println("Benchmarktest fuer den InsertionSort-Algorithmus:");

        long start = System.currentTimeMillis();
        for (int i=1;i<array.length;i++){
            double insertValue = array[i];
            int j = i;
            while (j>0 && array[j-1]>insertValue){
                array[j]=array[j-1];
                j=j-1;
            }
            array[j]=insertValue;
        }

        System.out.println("Sortierung von "+array.length+" Zahlen durchgefuehrt in:");
        System.out.println((System.currentTimeMillis()-start)+" ms.");
    }


    private static void quickSort(){
        System.out.println("Benchmarktest fuer den QuickSort-Algorithmus:");

        long start = System.currentTimeMillis();

        quick(0,array.length-1);

        System.out.println("Sortierung von "+array.length+" Zahlen durchgefuehrt in:");
        System.out.println((System.currentTimeMillis()-start)+" ms.");
    }

    private static void quick(int links, int rechts){
        if (links<rechts){
            int teiler = teile(links,rechts);
            quick(links,teiler);
            quick(teiler+1,rechts);
        }
    }

    private static int teile(int links, int rechts){
        int i=links;
        int j=rechts-1;
        double pivot = array[rechts];

        do{
            while (array[i]<pivot && i<rechts-1) i++;
            while (array[j]>=pivot && j>links) j--;
            if (i<j) tausche(i,j);
        } while (i<j);

        if (array[i]>pivot) tausche(i,rechts);
        return i;
    }

    private static void tausche(int i,int j){
        double tmp = array[i];
        array[i]=array[j];
        array[j]=tmp;
    }

    public static void main(String[] args) {

        int n = 100000000;
        //Erzeuge n Zufallszahlen
        array = new double[n];
        for (int i=0;i<n;i++){
            array[i]=Math.random();
        }
        //Sortierung und Ausgabe der Werte
        //selectionSort();
        //bubbleSort();
        //insertionSort();
        quickSort();

        //Zum Test, dass der Sortieralgorithmus funktioniert
        //for (int i=0;i<n;i++) System.out.println(array[i]);
    }

}
