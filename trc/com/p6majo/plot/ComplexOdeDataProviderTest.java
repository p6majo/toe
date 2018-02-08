package com.p6majo.plot;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.complexode.ComplexDerivativeInf;
import com.p6majo.math.complexode.ComplexInitialConditions;
import com.p6majo.math.function.FcnCos;
import org.junit.Test;

public class ComplexOdeDataProviderTest {


    @Test
    public void start() {
        int xdim = 11;
        int ydim = 11;
        final Complex data[] = new Complex[xdim*ydim];

        PlotRange plotRange = new PlotRange();
        plotRange.addRange(-Math.PI,Math.PI,xdim);
        plotRange.addRange(-Math.PI,Math.PI,ydim);


        //try to analytically continue the exponential function from its ode into the complex plane

        //setup the ode and the ics
        ComplexDerivativeInf derivativeInf = new ComplexDerivativeInf() {
            @Override
            public void derivs(double x, Complex[] y, Complex[] dydx) {
                dydx[0]= y[0];
            }

            @Override
            public void jacobian(double x, Complex[] y, Complex[] dfdx, Complex[][] dfdy) {
            }
        };
        ComplexInitialConditions ics = new ComplexInitialConditions(Complex.NULL,new Complex[]{Complex.ONE});

        //calculate the data with a DataProvider
        ComplexOdeDataProvider provider = new ComplexOdeDataProvider(derivativeInf,ics);
        provider.setPlotRange(plotRange);
        provider.setData(data);
        provider.start();







        /*

        for (int y=0;y<ydim;y++){
            for (int x=0;x<xdim;x++){
                System.out.print(data[y*xdim+x].toString()+" ");
            }
            System.out.println("\n");
        };
        */

        /*
        System.out.println(IntStream.range(0,ydim)
                .boxed()
                .flatMap(y->IntStream.range(0,xdim)
                        .mapToObj(x->outData[y*xdim+x].toString())
                ).collect(Collectors.joining(" "))
        );
        */

    }
}