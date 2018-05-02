package com.p6majo.math.newtoniteration;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexPolynomial;
import com.p6majo.math.utils.Box;
import com.p6majo.math.utils.Resolution;
import org.junit.Test;

import java.util.TreeSet;

public class NewtonIterationWithStreamTanExpansion {

    @Test
    public void testTanExpansion() throws Exception {


        Resolution res = new Resolution(4*688,4*288);
        Box box = new Box(-5.16,-2.16,5.16,2.16);


        int order =20;
        //taylor expansion of tan
        double[] coeffscale = new double[]{
                0,1.,
                0,1./3,
                0,2./15,
                0,17./315,
                0,62./2835,
                0,1382./155925,
                0,21844./6081075,
                0,929569./638512875,
                0,6404582./10854718875.,
                0,443861162./1856156927625.
        };

        Complex[] coeff = new Complex[order];
        for (int i=0;i<order;i++) coeff[i] = Complex.ONE.scale(coeffscale[i]);

        ComplexPolynomial tanApprox = new ComplexPolynomial(coeff);

        System.out.println("tan approx: "+tanApprox.toString());


        //get roots for numerator
        TreeSet<Complex> roots = new TreeSet<Complex>();
        roots = new NewtonIterator(tanApprox,box,res,"tmp.ppm").getRoots();

        NewtonIterator iterator = new NewtonIterator(tanApprox,box,res,"tanApprox4.ppm");
        iterator.setRoots(roots);
        long start = System.currentTimeMillis();

        iterator.setRoots(roots);
        start = System.currentTimeMillis();
        iterator.generateImageWithStream();
        System.out.println("Image generated with stream after "+(System.currentTimeMillis()-start)+" ms.");



    }

}
