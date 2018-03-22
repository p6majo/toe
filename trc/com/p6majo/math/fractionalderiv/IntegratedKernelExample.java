package com.p6majo.math.fractionalderiv;

import com.jgnuplot.*;
import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.FunctionWithParameters;
import com.p6majo.math.nswc.NswcMath;
import com.p6majo.math.utils.FileWriter;

/**
 * This class executes ALL demos in this directory, in functionality order.
 * 
 * @author Pander
 */
public final class IntegratedKernelExample {
    static double min = -10;
    static double max = 10;

    static Complex lambda = Complex.ONE.scale(0.8);
    static Complex alpha = Complex.ONE.scale(0.7);

    private IntegratedKernelExample() {

   }

   private static FunctionWithParameters<Complex,Complex> explx = new FunctionWithParameters<Complex,Complex>(){

       @Override
       public Complex apply(Complex complex) {
           return complex.times(params.get("lambda")).cos();
       }
   };

   public static void generateData(){
        StringBuilder dataBuilder = new StringBuilder();
        explx.addParam("lambda",lambda);
       NswcMath math = new NswcMath();

       Complex factor = math.Gamma(Complex.ONE.plus(alpha)).reciprocal();

        int resolution = 101;
        double delta = (max-min)/(resolution-1);
        Complex[][] field = new Complex[resolution][resolution];
        Complex[][] dfield = new Complex[resolution-1][resolution-1];


        for (int i=0;i<resolution;i++)
            for (int j=0;j<resolution;j++){
            Complex z = new Complex(min+i*delta,min+j*delta);
            DerivativeKernelComplex kernel = new DerivativeKernelComplex(explx,alpha,z);
            IntegratedSimpDerivativeKernelComplex intKernel = new IntegratedSimpDerivativeKernelComplex(kernel);
            intKernel.setAccuracy(1.e-2);
            //System.out.println(z.toString());
            Complex integral = intKernel.apply(z);
            field[i][j]=integral;
        }

        for (int i=0;i<resolution-1;i++)
            for (int j=0;j<resolution-1;j++){
                Complex derivative1 = field[i+1][j+1].minus(field[i][j]).divides(new Complex(delta,delta));
                Complex derivative2 = field[i+1][j].minus(field[i][j+1]).divides(new Complex(delta,-delta));
                if (derivative1.minus(derivative2).abs()/derivative1.abs()>10*delta) dfield[i][j]=Complex.NULL;
                else dfield[i][j]=derivative1.plus(derivative2).scale(0.5).times(factor);
                Complex z = new Complex(min+i*delta,min+j*delta);
                dataBuilder.append(z.re()+" "+z.im()+" "+dfield[i][j].re()+" "+dfield[i][j].im()+System.getProperty("line.separator"));

            }

       FileWriter.writeStringToFile("dkernel.dat",dataBuilder.toString());

   }

   public static void main(String[] args) {


     IntegratedKernelExample.generateData();
   }
}
