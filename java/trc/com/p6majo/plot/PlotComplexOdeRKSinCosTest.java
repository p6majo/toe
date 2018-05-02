package com.p6majo.plot;

import com.p6majo.info.aachen.Kon;
import com.p6majo.math.complex.Complex;
import com.p6majo.math.complexode.ComplexDerivativeInf;
import com.p6majo.math.complexode.ComplexInitialConditions;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlotComplexOdeRKSinCosTest {

    private PlotComplex cPlot ;
    private ComplexDataProvider cProvider;
    private int gridsize = 1401;

    @Before
    public void setUp() throws Exception {


        ComplexDerivativeInf derivativeInf = new ComplexDerivativeInf() {
            @Override
            public void derivs(double x, Complex[] y, Complex[] dydx) {
                dydx[0]= y[1];
                dydx[1]= y[0].neg();
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
                dydx[0]= y[1].times(Complex.I);
                dydx[1]= y[0].neg().times(Complex.I);
            }

            @Override
            public void jacobian(double x, Complex[] y, Complex[] dfdx, Complex[][] dfdy) {
            }
        };

        ComplexInitialConditions ics = new ComplexInitialConditions(Complex.NULL,new Complex[]{Complex.NULL,Complex.ONE});

        long startIntegration = System.currentTimeMillis();
        //calculate the data with a DataProvider
        //choose 0 for sin
        //choose 1 for cos
        cProvider = new ComplexOdeRKDataProvider(derivativeInf,imagDerivativeInf,ics,0);

        this.cPlot = new PlotComplex(cProvider);

        this.cPlot.addRange(-3,3,gridsize);//xrange
        this.cPlot.addRange(-3,3,gridsize);//yrange
        this.cPlot.addRange(0,1);//zrange

        this.cPlot.plotOptions.setContourLines(21);
        this.cPlot.plotOptions.setContourRange(new Range(0.1,10));
        this.cPlot.plotOptions.setLogScaleZ(true);
        this.cPlot.plotOptions.setOutputOption(PlotOptions.OutputOption.DEFAULT);
        this.cPlot.plotOptions.setOutputFilename("test.ppm");

    }

    @Test
    public void testPlot(){

        long dataLength = this.cPlot.getDataSize();
        assertEquals(dataLength,gridsize*gridsize);

       // System.out.println(this.cPlot.getData().length+" ");




        this.cPlot.generateData();

        /*
        for (int d=0;d<this.cPlot.getData().length;d++) {
            System.out.print(cPlot.getData()[d].toString());
            if (d%71==0) System.out.println();
        }
        */

        this.cPlot.out();

        int i = Kon.readInt();
    }
}