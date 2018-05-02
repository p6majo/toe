package com.p6majo.math.fractionalderiv;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.FunctionWithParameters;

public class DerivativeKernelComplex extends FunctionWithParameters<Complex,Complex> {

    public FunctionWithParameters<Complex,Complex> f;

    public DerivativeKernelComplex(FunctionWithParameters<Complex,Complex> f, Complex alpha, Complex z){
        this.f = f;
        this.addAll(f.getParams());
        this.addParam("alpha",alpha);
        this.addParam("z",z);
    }

    @Override
    public Complex apply(Complex u) {
       return  f.apply(u).times(params.get("z").minus(u).log().times(params.get("alpha").neg()).exp());
    }
}
