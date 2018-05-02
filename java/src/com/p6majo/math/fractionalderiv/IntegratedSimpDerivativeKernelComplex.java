package com.p6majo.math.fractionalderiv;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.FunctionWithParameters;

public class IntegratedSimpDerivativeKernelComplex extends FunctionWithParameters<Complex,Complex> {

    private DerivativeKernelComplex kernel;
    private double EPS = 1.e-5;

    public IntegratedSimpDerivativeKernelComplex(DerivativeKernelComplex kernel){
       this.kernel =  kernel;
    }

    public void setAccuracy(double eps){
        this.EPS =eps;
    }

    @Override
    public Complex apply(Complex z) {
        kernel.setParam("z",z);
        if (z.equals(Complex.NULL)) return Complex.NULL;
        return QSimpComplex.qsimp(kernel,Complex.NULL,z.minus(z.scale(EPS)),EPS);
    }
}
