package com.p6majo.info.klausuren;

public class Sortieralgorithmus {
    public static void main(String[] args) {
        double[] data = new double[]{10.5,54.3,13.4,-15.2,37.6};
        double tmp=0;
        for (int i=0;i<data.length;i++){
            for (int j=i+1;j<data.length;j++){
                if (data[i]<data[j]) {
                    tmp = data[i];
                    data[i]=data[j];
                    data[j]=tmp;
                    //System.out.println(i+" "+j+" "+tmp+" "+data[0]+" "+data[1]+" "+data[2]+" "+data[3]+" "+data[4]);
                }
                System.out.println(i+" "+j+" "+tmp+" "+data[0]+" "+data[1]+" "+data[2]+" "+data[3]+" "+data[4]);
            }
        }
    }
}
