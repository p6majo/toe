package com.p6majo.math.function;

import com.p6majo.math.complex.Complex;
import java.util.function.Function;

/**
 * Java function for the calculation of the cosine function for complex valued arguments
 * @author p6majo
 * @version 1.0
 */
public class FcnCos implements Function<Complex,Complex> {
    @Override
    public Complex apply(Complex complex) {
        return complex.cos();
    }
}