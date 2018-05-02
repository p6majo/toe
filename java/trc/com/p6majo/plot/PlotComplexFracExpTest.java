package com.p6majo.plot;

import com.p6majo.info.aachen.Kon;
import com.p6majo.math.complex.Complex;
import com.p6majo.math.fraction.Fraction;
import com.p6majo.math.function.ComplexFractionalExp;
import com.p6majo.math.function.FcnSin;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class PlotComplexFracExpTest {

    private PlotComplex cPlot ;
    private ComplexFunctionDataProvider cProvider;
    private int gridsize = 1400;

    @Before
    public void setUp() throws Exception {
        Function<Complex,Complex> fracExp = new Function<Complex,Complex>(){
            ComplexFractionalExp fracExp = new ComplexFractionalExp(new Complex(1./4,0.0),1.e-3,100);
            @Override
            public Complex apply(Complex complex) {
                return fracExp.eval(complex);
            }
        };

        this.cProvider = new ComplexFunctionDataProvider(fracExp);
        this.cPlot = new PlotComplex(cProvider);

        this.cPlot.addRange(-6,2,2*gridsize);//xrange
        this.cPlot.addRange(-2,2,gridsize);//yrange
        this.cPlot.addRange(0,1);//zrange

        this.cPlot.plotOptions.setContourLines(41);
        this.cPlot.plotOptions.setContourRange(new Range(0.01,100));
        this.cPlot.plotOptions.setLogScaleZ(true);
        this.cPlot.plotOptions.setOutputOption(PlotOptions.OutputOption.FILE);
        this.cPlot.plotOptions.setOutputFilename("exp0.25.ppm");

    }

    @Test
    public void testPlot(){

        long dataLength = this.cPlot.getDataSize();
        assertEquals(dataLength,2*gridsize*gridsize);

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