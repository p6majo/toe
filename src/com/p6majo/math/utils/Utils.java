package com.p6majo.math.utils;

import org.apfloat.Apint;

import java.util.ArrayList;

public class Utils {
    public static void errorMsg(String text){
        System.err.println(text);
        System.exit(0);
    }


    public static Apint ggT(Apint a, Apint b){

        if (b.compareTo(a)>0) {
            Apint tmp = a;
            a = b;
            b= tmp;
        }

        Apint ggt = b;

        while (a.mod(b).compareTo(Apint.ZERO)!=0){
            ggt = a.mod(b);
            a=b;
            b=ggt;
        }

        return ggt;
    }

}
