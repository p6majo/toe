package com.p6majo.math.fractionalderiv;

import com.p6majo.math.function.FunctionWithParameters;

public class DerivativeKernel extends FunctionWithParameters<Double,Double> {

    public FunctionWithParameters<Double,Double> f;

    public DerivativeKernel(FunctionWithParameters<Double,Double> f,double alpha,double x){
        this.f = f;
        this.addAll(f.getParams());
        this.addParam("alpha",alpha);
        this.addParam("x",x);
    }

    @Override
    public Double apply(Double aDouble) {
       return  f.apply(aDouble)/Math.pow(params.get("x")-aDouble,params.get("alpha"));
    }
}
