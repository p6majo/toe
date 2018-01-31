package com.p6majo.plot.old;


import com.p6majo.info.aachen.Kon;
import com.p6majo.math.function.ComplexBesselJ;
import com.p6majo.math.function.ComplexFunction;
import com.p6majo.math.utils.Box;
import org.junit.Test;

public class ComplexPlotBesselJ {

    @Test
    public void generatePlot() {
        //..ComplexFunction bessel = new ComplexBesselJNswc(Complex.ONE.scale(10.));
        ComplexFunction bessel = new ComplexBesselJ(10);
        Box range = new Box(-58.,-14,10.,14);
        //range.rescale(5.);
       long start = System.currentTimeMillis();
        int width = 340*4;
        int height = 140*4;
        ComplexPlot plot = new ComplexPlot(bessel,range, ComplexPlot.PlotType.ARGMOD,width,height);
        plot.setMaxContourValue(1000.);
        plot.setMinContourValue(0.001);
        plot.setNumContourLines(7);
        plot.setMedValue(0);
        plot.setScale(PlotData.PlotScale.LOG);

        plot.plot();
        plot.save("tmp");

        System.out.println((System.currentTimeMillis()-start)+" ms.");
        int i= Kon.readInt();
    }
}