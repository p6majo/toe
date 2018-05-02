package com.p6majo.math.fractionalderiv;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.FcnCos;
import com.p6majo.math.function.FcnExp;
import com.p6majo.math.utils.Box;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.*;

public class QSimpComplexTest {

    Function<Complex,Complex> sqr = new Function<Complex,Complex>(){
        @Override
        public Complex apply(Complex z) {
            return z.times(z).times(z);
        }
    };

    @Test
    /**
     * Test numerical integration
     */
    public void qsimp() {

        Box box = new Box(10);
    //of a complex function z^3
        for (int i=0;i<10;i++) {
            Complex a = Complex.random(box);
            Complex b = Complex.random(box);
            Complex integral = QSimpComplex.qsimp(sqr, a, b, 1.e-3);
            System.out.println(integral.toString());

            Complex theory = b.times(b).times(b).times(b).minus(a.times(a).times(a).times(a)).scale(1. / 4);
            assertEquals(theory.minus(integral).abs(), 0., 1e-10);
        }

    //of a complex function exp(z)
        for (int i=0;i<10;i++){
            Complex a=Complex.random(box);
            Complex b = Complex.random(box);
            Complex integral = QSimpComplex.qsimp(new FcnExp(),a,b,1.e-9);

            Complex theory = b.exp().minus(a.exp());
            assertEquals(theory.minus(integral).abs(),0.,1.e-6);
        }

    //of a complex function cos(z)
        for (int i=0;i<10;i++){
            Complex a=Complex.random(box);
            Complex b = Complex.random(box);
            Complex integral = QSimpComplex.qsimp(new FcnCos(),a,b,1.e-9);

            Complex theory = b.sin().minus(a.sin());
            assertEquals(theory.minus(integral).abs(),0.,1.e-6);
        }
    }
}