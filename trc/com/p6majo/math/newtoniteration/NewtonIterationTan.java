package com.p6majo.math.newtoniteration;


import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexFunction;
import com.p6majo.math.function.ComplexTan;
import com.p6majo.math.utils.Box;
import org.junit.Test;

import java.util.TreeSet;

public class NewtonIterationTan {

    @Test
    public void testNewtonIterationTan() throws Exception {


        Resolution res = new Resolution(8*688,8*288);
        Box box = new Box(-5.16,-2.16,5.16,2.16);

        ComplexFunction tan = new ComplexTan();

        //get roots for numerator
        TreeSet<Complex> roots = new TreeSet<Complex>();

        for (int i=-10;i<11;i++) roots.add(new Complex(Math.PI/2*i,0));

        NewtonIterator iterator = new NewtonIterator(tan,box,res,"tanApprox0.ppm");
        iterator.setRoots(roots);
        long start = System.currentTimeMillis();
        iterator.generateImageWithStream();
        System.out.println("Image generated with stream after "+(System.currentTimeMillis()-start)+" ms.");



    }

}
