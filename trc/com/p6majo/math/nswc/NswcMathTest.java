package com.tests.p6majo.mathematics.nswc;


import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexBesselJ;
import com.p6majo.math.function.ComplexElliptSn;
import com.p6majo.math.function.ComplexFunction;
import com.p6majo.math.nswc.NswcMath;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class NswcMathTest {

    @Test
    public void besselJ() {
        //compare to different libraries for besselJ with integer order
        //fortran nswc
        //java from numerical recipes
        int order = (int) (Math.random()*10.);

        //time requirement
        NswcMath math = new NswcMath();
        ComplexFunction besselJ = new ComplexBesselJ(order);
        int n = 100;
        Complex[] testnumbers = new Complex[n];
        for (int i = 0; i < n; i++) testnumbers[i] = Complex.random(10.);

        long start = System.currentTimeMillis();
        for (int i = 0; i < n ; i++) {
            System.out.println( testnumbers[i].toString() + " " + math.BesselJ(Complex.ONE.scale(order), testnumbers[i]).toString());
        }
        System.out.println("in " + (System.currentTimeMillis() - start) + " ms.");
        start = System.currentTimeMillis();
        for (int i = 0; i < n ; i++) {
           System.out.println( testnumbers[i].toString() + " " + besselJ.eval(testnumbers[i]).toString());
        }
        System.out.println("in " + (System.currentTimeMillis() - start) + " ms.");

        //difference
        for (int i=0;i<n;i++) {
            Complex nswcValue = math.BesselJ(Complex.ONE.scale(order), testnumbers[i]);
            Complex mathValue = besselJ.eval(testnumbers[i]);
            Complex relError = nswcValue.minus(mathValue).divides(mathValue);
            assertEquals(relError.abs(),0.,0.0001);
        }
    }

    @Test
    public void elliptical(){
        ComplexFunction sn = new ComplexElliptSn(0.6,0.8);
        for (int i=0;i<10;i++){
            Complex z = Complex.random(10.);
            System.out.println(z.toString()+sn.eval(z).toString());
        }
    }
}