package com.p6majo.info.klausuren;

import com.p6majo.info.aachen.Kon;

public class PrimeTest {
    public static void main(String[] args) {
        int n = Kon.readInt();
        int i=2;
        while (n%i!=0){
            i++;
        }
        if (i==n) System.out.println(n+" ist prim");
        else System.out.println(i+" ist ein Teiler von n");

        for (i=2;i<n;i++){
            if (n%i==0){
                System.out.println(i+" ist ein Teiler von n");
                System.exit(0);
            }
        }
        System.out.println(n+" ist prim");

        int test = (int) Math.pow(2.,31.);

        System.out.println("largest integer: "+test);
        test=test+1;
        System.out.println("largest integer: "+test);
    }
}
