package com.p6majo.math.function;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.utils.Box;
import org.junit.Test;

import java.util.function.Function;

public class DoubleFractionalExpTest {

    @Test
    public void eval() {
        Function<Double,Double> fracExp = new DoubleFractionalExp(1.00000001,1e-4,80);

        for (int i=0;i<10;i++){
            Complex z=Complex.random(new Box(0.5));
            Double expz = fracExp.apply(9.);
            System.out.println(z.re()+": "+expz+" "+Math.exp(9));
        }
    }
}