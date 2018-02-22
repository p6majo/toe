package com.p6majo.plot;

import com.p6majo.info.aachen.Kon;
import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.FcnSin;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class PlotSinTest {

    private PlotXY plot ;
    private DoubleFunctionDataProvider provider;
    private int gridsize = 1201;

    @Before
    public void setUp() throws Exception {
        Function<Double,Double> sin = new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return Math.sin(aDouble);
            }
        };

        this.provider = new DoubleFunctionDataProvider(sin);
        this.plot = new PlotXY(provider);

        this.plot.addRange(-3*Math.PI,3*Math.PI,gridsize);//xrange
        this.plot.addRange(-1,1);//yrange

        this.plot.plotOptions.setLogScaleZ(false);
        this.plot.plotOptions.setOutputOption(PlotOptions.OutputOption.DEFAULT);
    }

    @Test
    public void testPlot(){

        long dataLength = this.plot.getDataSize();
        assertEquals(dataLength,gridsize);

       // System.out.println(this.cPlot.getData().length+" ");




        this.plot.generateData();
        ArrayList<Double> data = this.plot.provider.getPlotData().getData();

        System.out.println(data.stream().map(x->x.toString()).collect(Collectors.joining("\n")));

        this.plot.out();

        int i = Kon.readInt();
    }
}