package com.p6majo.math.function;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.fraction.Fraction;
import com.p6majo.math.utils.Box;
import org.junit.Test;

public class ComplexMittagLefflerTest {

    @Test
    public void eval() {
        ComplexFunction mittagLeffler = new ComplexMittagLeffler(Complex.NULL,1e-4,100);

        for (int i=0;i<10;i++){
            Complex z=Complex.random(new Box(0.5));
            Complex ml = mittagLeffler.eval(z);
            System.out.println(z.toString()+": "+ml.toString()+" "+Complex.ONE.divides(Complex.ONE.minus(z)).toString()+" "+z.exp().toString());
        }

    }
}