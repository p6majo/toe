package com.p6majo.plot;

import com.p6majo.info.aachen.Kon;
import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.FcnExp;
import com.p6majo.math.function.FcnSin;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class PlotComplexExpTest {

    private PlotComplex cPlot ;
    private ComplexFunctionDataProvider cProvider;
    private int gridsize = 1401;

    @Before
    public void setUp() throws Exception {
        Function<Complex,Complex> exp = new FcnExp();

        this.cProvider = new ComplexFunctionDataProvider(exp);
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