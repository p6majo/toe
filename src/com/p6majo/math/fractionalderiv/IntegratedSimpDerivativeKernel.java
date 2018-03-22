package com.p6majo.math.fractionalderiv;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.FunctionWithParameters;

public class IntegratedSimpDerivativeKernel extends FunctionWithParameters<Double,Double> {

    private DerivativeKernel kernel;
    private double EPS = 1.e-5;

    public IntegratedSimpDerivativeKernel(DerivativeKernel kernel){
       this.kernel =  kernel;
    }

    public void setAccuracy(double eps){
        this.EPS =eps;
    }

    @Override
    public Double apply(Double aDouble) {
        kernel.setParam("x",aDouble);
        if (aDouble==0.) return 0.;
        return  QSimp.qsimp(kernel,0.,aDouble-EPS,EPS);
    }
}
