package com.p6majo.math.newtoniteration;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexPolynomial;
import org.junit.Test;

import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

public class RootFinderTest {
    @Test
    public void findRoots() throws Exception {
        //check polynomial without multiple roots
        Complex[] coeff = new Complex[]{Complex.ONE.neg(),Complex.NULL,Complex.NULL,Complex.ONE};
        ComplexPolynomial pol = new ComplexPolynomial(coeff);

        RootFinder rootFinder = new RootFinder();

        TreeSet<Complex> roots = rootFinder.findRoots(pol);
        System.out.println(pol.toString());
        System.out.println("roots: "+roots.toString());
        assertEquals(roots.size(),pol.getDegree());

        //check polynomial multiple roots
        coeff = new Complex[]{Complex.ONE,Complex.ONE.scale(2),Complex.ONE};
        pol = new ComplexPolynomial(coeff);


        roots = rootFinder.findRoots(pol);
        System.out.println(pol.toString());
        System.out.println("roots: "+roots.toString());
        assertEquals(roots.size(),pol.getDegree()-1);

        //check polynomial multiple roots
        coeff = new Complex[]{Complex.NULL,Complex.NULL,Complex.NULL,Complex.ONE};
        pol = new ComplexPolynomial(coeff);


        roots = rootFinder.findRoots(pol);
        System.out.println(pol.toString());
        System.out.println("roots: "+roots.toString());
        assertEquals(roots.size(),pol.getDegree()-2);
    }

}