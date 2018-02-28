package com.p6majo.plot;

import com.p6majo.info.aachen.Kon;
import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.FcnSin;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class PlotComplexX2 {

    private PlotComplex cPlot ;
    private ComplexFunctionDataProvider cProvider;
    private int gridsize = 1401;

    @Before
    public void setUp() throws Exception {
        Function<Complex,Complex> x2 = new Function<Complex,Complex>(){
            @Override
            public Complex apply(Complex complex) {
                return complex.times(complex.times(complex));
            }
        };

        this.cProvider = new ComplexFunctionDataProvider(x2);
        this.cPlot = new PlotComplex(cProvider);

        this.cPlot.addRange(-3,3,gridsize);//xrange
        this.cPlot.addRange(-3,3,gridsize);//yrange
        this.cPlot.addRange(0,1);//zrange

        this.cPlot.plotOptions.setContourLines(21);
        this.cPlot.plotOptions.setContourRange(new Range(0.1,10));
        this.cPlot.plotOptions.setLogScaleZ(true);
        this.cPlot.plotOptions.setOutputOption(PlotOptions.OutputOption.FILE);
        this.cPlot.plotOptions.setOutputFilename("x2.ppm");

    }

    @Test
    public void testPlot(){

        long dataLength = this.cPlot.getDataSize();
        assertEquals(dataLength,gridsize*gridsize);


        this.cPlot.generateData();

        this.cPlot.out();

        int i = Kon.readInt();
    }
}