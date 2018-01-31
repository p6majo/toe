package com.p6majo.math.newtoniteration;



import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexFractionFunction;
import com.p6majo.math.function.ComplexPolynomial;
import com.p6majo.math.utils.Box;
import org.junit.Test;

import java.util.TreeSet;

public class NewtonIterationTanFractionExpansion {

    @Test
    public void testNewtonIterationTanFractionExpansion() throws Exception {


        Resolution res = new Resolution(16*688,16*288);
        Box box = new Box(-5.16,-2.16,5.16,2.16);


        int order =10;
        //approximated sin
        Complex[] coeff = new Complex[2*order];
        double sign=-1.;
        long fac = 1;
        for (int i=0;i<order;i++) {
            sign *=-1;
            if (i>0) fac*=(2*i);
            fac*=(2*i+1);
            coeff[2 * i] = Complex.NULL;
            coeff[2 * i + 1] = Complex.ONE.scale(sign/fac);
        }

        ComplexPolynomial num = new ComplexPolynomial(coeff);

        System.out.println("sin approx: "+num.toString());


        //approximated cos
        Complex[] coeff2 = new Complex[2*order];
        sign=-1.;
        fac = 1;
        for (int i=0;i<order;i++) {
            sign *=-1;
            if (i>0){
                fac*=(2*i-1);
                fac*=(2*i);
            }
            coeff2[2 * i] = Complex.ONE.scale(sign/fac);
            coeff2[2 * i + 1] = Complex.NULL;
        }
        ComplexPolynomial den = new ComplexPolynomial(coeff2);
        System.out.println("cos approx: "+den.toString());

        ComplexFractionFunction fraction = new ComplexFractionFunction(num,den);

        //get roots for numerator
        TreeSet<Complex> roots = new TreeSet<Complex>();
        roots = new NewtonIterator(num,box,res,"tmp.ppm").getRoots();

        long start = System.currentTimeMillis();
        NewtonIterator iterator = new NewtonIterator(fraction,box,res,"tanApprox2.ppm");
        iterator.setRoots(roots);
        iterator.shuffleColors();

        start = System.currentTimeMillis();
        iterator.generateImageWithStream();
        System.out.println("Image generated with stream after "+(System.currentTimeMillis()-start)+" ms.");



    }

}
