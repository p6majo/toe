package com.p6majo.math.mandelbrot;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexPolynomial;
import com.p6majo.math.utils.Box;
import com.p6majo.math.utils.Resolution;
import org.junit.Test;

public class MandelbrotTest {

    @Test
    public void tesMandelbrot() throws Exception {


        Resolution res = new Resolution(8*688,8*288);
        Box box = new Box(-1.1*2.58,-1.1*1.08,1.1*2.58,1.1*1.08);

        Complex[] coeff = new Complex[]{Complex.NULL,Complex.NULL,Complex.ONE};

        ComplexPolynomial pol = new ComplexPolynomial(coeff);

        MandelIterator iterator = new MandelIterator(pol,box,res,"mandelx3.ppm");
        long start = System.currentTimeMillis();
        iterator.generateImage();
        System.out.println("Image generated with stream after "+(System.currentTimeMillis()-start)+" ms.");



    }

}
