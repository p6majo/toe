package com.p6majo.math.complex;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComplexTest {

    @Test
    public void times() {
        Complex factor = Complex.random(10.);
        Complex factor2 = Complex.random(10.);

        Complex product = factor.times(factor2);
        System.out.println(factor.toString()+"*"+factor2.toString()+"="+product.toString());

        assertEquals(product.minus(new Complex(factor.re()*factor2.re()-factor.im()*factor2.im(),
                factor.re()*factor2.im()+factor2.re()*factor.im())).abs(),0.,Complex.EPS_FLOAT);
    }

    @Test
    public void plus() {
        Complex summand = Complex.random(10.);
        Complex summand2 = Complex.random(10.);

        Complex sum = summand.plus(summand2);
        System.out.println(summand.toString()+"+"+summand2.toString()+"="+sum.toString());

        assertEquals(sum.minus(new Complex(summand.re()+summand2.re(),
                summand.im()+summand2.im())).abs(),0.,Complex.EPS_FLOAT);
    }

    @Test
    public void minus() {
        Complex minuend = Complex.random(10.);
        Complex subtrahend = Complex.random(10.);

        Complex difference = minuend.minus(subtrahend);
        System.out.println(minuend.toString()+"-"+subtrahend.toString()+"="+difference.toString());

        assertEquals(difference.minus(new Complex(minuend.re()-subtrahend.re(),
                minuend.im()-subtrahend.im())).abs(),0.,Complex.EPS_FLOAT);
    }

    @Test
    public void divide() {
        Complex dividend = Complex.random(10.);
        Complex divisor = Complex.random(10.);

        Complex quotient = dividend.divides(divisor);
        System.out.println(dividend.toString()+"/"+divisor.toString()+"="+quotient.toString());

        assertEquals(quotient.minus(dividend.times(divisor.conjugate()).scale(1./divisor.abs()/divisor.abs())).abs(),0.,Complex.EPS_FLOAT);
    }
}