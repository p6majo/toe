package com.p6majo.math.function;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.fraction.Fraction;
import com.p6majo.math.utils.Box;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComplexFractionalExpTest {

    @Test
    public void eval() {
        ComplexFunction fracExp = new ComplexFractionalExp(new Fraction(2,3),1e-4,100);

        for (int i=0;i<10;i++){
            Complex z=Complex.random(new Box(1));
            Complex expz = fracExp.eval(z);
            System.out.println(z.toString()+": "+expz.toString()+" "+z.exp().toString());
        }
    }
}