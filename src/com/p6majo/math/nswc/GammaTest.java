package com.p6majo.math.nswc;

import com.p6majo.math.complex.Complex;

//
//###################################################################
//                   Class TemperatureApp
//###################################################################
//
//   
//  Creator               : Chris Anderson
//  (C) UCLA 1997
//
//###################################################################
//
public class GammaTest
{

public GammaTest() {

}

//
//#########################################################
//                MAIN
//#########################################################
//
public static void main(String args[])
{
    NswcMath calc = new NswcMath();
    System.out.println(calc.Gamma(Complex.ONE.scale(0.5)).toString());
    long start = System.currentTimeMillis();
    for (int i=0;i<100000;i++){
        Complex rand = new Complex(-10+Math.random()*20,-10+Math.random()*20);
        calc.Gamma(rand);
       // System.out.println(rand.toString()+"->"+calc.Gamma(rand).toString());
    }
    System.out.println("duration: "+(System.currentTimeMillis()-start));
   for (int r=0;r<30;r++) System.out.println(calc.Gamma(Complex.ONE.scale(r))+" "+calc.LogGamma(Complex.ONE.scale(r))+" "+calc.BesselJ(new Complex(1.,0.001),Complex.ONE.scale(r)));
}

}
