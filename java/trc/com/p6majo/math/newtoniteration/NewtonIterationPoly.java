package com.p6majo.math.newtoniteration;


import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexPolynomial;
import com.p6majo.math.utils.Box;
import com.p6majo.math.utils.Resolution;
import org.junit.Test;

public class NewtonIterationPoly {

    @Test
    public void testNewtonIterationPoly() throws Exception {


        Resolution res = new Resolution(8*688,8*288);
        Box box = new Box(-2.58,-1.08,2.58,1.08);

        Complex[] coeff = new Complex[]{Complex.ONE.scale(-1),Complex.NULL,Complex.NULL,Complex.NULL,Complex.NULL,Complex.NULL,Complex.NULL,Complex.ONE};

        ComplexPolynomial pol = new ComplexPolynomial(coeff);

        NewtonIterator iterator = new NewtonIterator(pol,box,res,"newtonx7m1.ppm");
        long start = System.currentTimeMillis();
        iterator.generateImageWithStream();
        System.out.println("Image generated with stream after "+(System.currentTimeMillis()-start)+" ms.");



    }

}
