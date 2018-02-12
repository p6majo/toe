package com.p6majo.plot;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.complexode.ComplexDerivativeInf;
import com.p6majo.math.complexode.ComplexInitialConditions;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ComplexOdeDataProviderTest {


    @Test
    public void start() {
        int xdim = 1001;
        int ydim = 1001;

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

        //TODO it might be possible to modify the stepper routine and call odeint with a flag, depending on a real or imaginary path of integration
        //right now, a second derivative information is provided for the integration along an imaginary path
        ComplexDerivativeInf imagDerivativeInf = new ComplexDerivativeInf() {
            @Override
            public void derivs(double x, Complex[] y, Complex[] dydx) {
                dydx[0]= y[0].times(Complex.I);
            }

            @Override
            public void jacobian(double x, Complex[] y, Complex[] dfdx, Complex[][] dfdy) {
            }
        };

        ComplexInitialConditions ics = new ComplexInitialConditions(Complex.NULL,new Complex[]{Complex.ONE});

        long startIntegration = System.currentTimeMillis();
        //calculate the data with a DataProvider
        ComplexOdeBSDataProvider provider = new ComplexOdeBSDataProvider(derivativeInf,imagDerivativeInf,ics);
        provider.setPlotRange(plotRange);
        provider.start();
        long endIntegration = System.currentTimeMillis()-startIntegration;

        //compare with implemented function
        double xmin = -Math.PI;
        double ymin = -Math.PI;
        double dx = 2.*Math.PI/(xdim-1);
        double dy = dx;
        Complex tmpData[] = new Complex[xdim*ydim];
        long startInternal = System.currentTimeMillis();
        for (int y=0;y<ydim;y++){
            for (int x=0;x<xdim;x++){
                tmpData[y*xdim+x]=new Complex(xmin+dx*x,ymin+dy*y).exp();
            }
        };
        long endInternal = System.currentTimeMillis()-startInternal;


        System.out.println("time requirement for integration: "+endIntegration+" ms.");
        System.out.println("time requirement for interal functions: "+endInternal+" ms.");

        for (int y=0;y<ydim;y++){
            for (int x=0;x<xdim;x++){
                assertEquals(tmpData[y*xdim+x].minus(provider.getData(y*xdim+x)).abs(),0.,1.E-3);
            }
        };

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