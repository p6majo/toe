package com.p6majo.math.fractionalderiv;

import com.jgnuplot.*;
import com.p6majo.math.function.FunctionWithParameters;
import com.p6majo.math.utils.FileWriter;

/**
 * This class executes ALL demos in this directory, in functionality order.
 * 
 * @author Pander
 */
public final class MyDem {
    static double xmin = 0;
    static double xmax = 1;

    static double lambda = 0.5;

    private MyDem() {

   }

   private static FunctionWithParameters<Double,Double> explx = new FunctionWithParameters<Double, Double>() {

        /**
        * Applies this function to the given argument.
        *
        * @param aDouble the function argument
        * @return the function result
        */
       @Override
       public Double apply(Double aDouble) {
           return Math.exp(params.get("lambda")*aDouble);
       }
   };

   public static void generateData(){
        StringBuilder dataBuilder = new StringBuilder();
        explx.addParam("lambda",lambda);


       DerivativeKernel kernel = new DerivativeKernel(explx,0.5,1.);
       IntegratedSimpDerivativeKernel intKernel4 = new IntegratedSimpDerivativeKernel(kernel);
       IntegratedRombDerivativeKernel intKernel5 = new IntegratedRombDerivativeKernel(kernel);
       intKernel5.setAccuracy(1.e-6);

       double dx=(xmax-xmin)/100;

        for (int i=0;i<101;i++){
            double x = xmin+i*dx;
            dataBuilder.append(x+" "+kernel.apply(x)+" "+intKernel4.apply(x)+" "+intKernel5.apply(x)+System.getProperty("line.separator"));
        }

       FileWriter.writeStringToFile("kernel.dat",dataBuilder.toString());

   }


    public static void gnuplot() {
        generateData();


        Plot.setGnuplotExecutable("gnuplot");
        Plot.setPlotDirectory("/tmp");

        Plot aPlot = new Plot();
        aPlot.setKey("left box");
        aPlot.setAutoscale();
        aPlot.setRanges("["+xmin+":"+xmax+"]");
        aPlot.pushGraph(new Graph("kernel.dat", "1:3", Axes.NOT_SPECIFIED,
                "integrated Kernel simp 1e-4 ", Style.POINTS));
        aPlot.pushGraph(new Graph("kernel.dat", "1:4", Axes.NOT_SPECIFIED,
                "integrated Kernel romb 1e-6 ", Style.LINES));
         aPlot.setOutput(Terminal.POSTSCRIPT, "test/out/myDem.eps");
        try {
            aPlot.plot();
        }
        catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }


   public static void main(String[] args) {
     MyDem.gnuplot();
   }
}
