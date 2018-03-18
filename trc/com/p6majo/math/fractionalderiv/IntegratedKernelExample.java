package com.p6majo.math.fractionalderiv;

import com.jgnuplot.*;
import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.FunctionWithParameters;
import com.p6majo.math.utils.FileWriter;

/**
 * This class executes ALL demos in this directory, in functionality order.
 * 
 * @author Pander
 */
public final class IntegratedKernelExample {
    static double min = -20;
    static double max = 20;

    static Complex lambda = Complex.ONE.scale(0.3);
    static Complex alpha = Complex.ONE.scale(0.7);

    private IntegratedKernelExample() {

   }

   private static FunctionWithParameters<Complex,Complex> explx = new FunctionWithParameters<Complex,Complex>(){

       @Override
       public Complex apply(Complex complex) {
           return complex.times(params.get("lambda")).exp();
       }
   };

   public static void generateData(){
        StringBuilder dataBuilder = new StringBuilder();
        explx.addParam("lambda",lambda);

        int resolution = 100;
        double delta = (max-min)/(resolution-1);


        for (int i=0;i<resolution;i++)
            for (int j=0;j<resolution;j++){
            Complex z = new Complex(min+i*delta,min+j*delta);
            DerivativeKernelComplex kernel = new DerivativeKernelComplex(explx,alpha,z);
            IntegratedSimpDerivativeKernelComplex intKernel = new IntegratedSimpDerivativeKernelComplex(kernel);
            intKernel.setAccuracy(1.e-2);
            //System.out.println(z.toString());
            Complex integral = intKernel.apply(z);
            dataBuilder.append(z.re()+" "+z.im()+" "+integral.re()+" "+integral.im()+System.getProperty("line.separator"));
        }

       FileWriter.writeStringToFile("kernel.dat",dataBuilder.toString());

   }

   public static void main(String[] args) {
     IntegratedKernelExample.generateData();
   }
}
