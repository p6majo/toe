package com.p6majo.math.function;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.fraction.Fraction;
import com.p6majo.math.utils.Box;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComplexFractionalExpTest {

    @Test
    public void eval() {
        ComplexFunction fracExp = new ComplexFractionalExp(new Complex(0.999999,0),1e-4,80);

        for (int i=0;i<10;i++){
            Complex z=Complex.random(new Box(0.5));
            Complex expz = fracExp.eval(z);
            System.out.println(z.toString()+": "+expz.toString()+" "+z.exp().toString());
        }
    }
}