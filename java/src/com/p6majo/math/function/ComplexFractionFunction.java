package com.p6majo.math.function;


import com.p6majo.math.complex.Complex;

/**
 * special type of complex function
 * @author jmartin
 *
 */
public class ComplexFractionFunction extends ComplexFunction{


    ComplexFunction numerator;
    ComplexFunction denominator;

    public ComplexFractionFunction(ComplexFunction numerator,ComplexFunction denominator){
        super();
        this.numerator=numerator;
        this.denominator=denominator;
    }

    @Override
    public Complex eval(Complex z) {
        return numerator.eval(z).divides(denominator.eval(z));
    }

    @Override
    public ComplexFunction derivative() {
        return null;
    }

    @Override
    public Complex evalDerivative(Complex z) {
        return numerator.evalDerivative(z).times(denominator.eval(z))
                .minus(denominator.evalDerivative(z).times(numerator.eval(z)))
                .divides(denominator.eval(z).times(denominator.eval(z)));
    }

}

