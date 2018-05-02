package com.p6majo.plot.old;


import com.p6majo.info.aachen.Kon;
import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexBesselJNuExtension;
import com.p6majo.math.function.ComplexFunction;
import com.p6majo.math.utils.Box;
import org.junit.Test;

public class ComplexPlotBesselJNuExtension {

    @Test
    public void generatePlot() {
        ComplexFunction bessel = new ComplexBesselJNuExtension(new Complex(5,1));
        Box range = new Box(-10.,-14,10.,14);
        //range.rescale(5.);
        int width = 1000;
        int height = 1000;
        long start = System.currentTimeMillis();
        ComplexPlot plot = new ComplexPlot(bessel,range, ComplexPlot.PlotType.ARGMOD,width,height);
        plot.setMaxContourValue(1000.);
        plot.setMinContourValue(0.001);
        plot.setNumContourLines(7);
        plot.setMedValue(0);
        plot.setScale(PlotData.PlotScale.LOG);

        plot.plot();
        plot.save("tmp");
        System.out.println((System.currentTimeMillis()-start)+" ms.");

        //ColorContourPlot2 contourPlot = new ColorContourPlot2(width,height);
       // contourPlot.setCaptureMode(false);
       // contourPlot.setDataRange(0,25);
       // contourPlot.setData(plot.getData(),width,height);
        int i= Kon.readInt();
    }
}