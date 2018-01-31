package com.p6majo.plot.old;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexBesselJNswc;
import com.p6majo.math.function.ComplexFunction;
import com.p6majo.math.utils.Box;
import org.junit.Test;

public class ComplexPlotBesselJNswc {

    @Test
    public void generatePlot() {

       for (int r=1947;r<2000;r++) {
            ComplexFunction bessel = new ComplexBesselJNswc(new Complex(0.015001*(r-1000),0));
            Box range = new Box(-15., -15, 15., 15);
            //range.rescale(5.);
            int width = 1000;
            int height = 1000;
            ComplexPlot plot = new ComplexPlot(bessel, range, ComplexPlot.PlotType.ARGMOD, width, height);
            plot.setMaxContourValue(1000.);
            plot.setMinContourValue(0.001);
            plot.setNumContourLines(7);
            plot.setMedValue(0);
            plot.setScale(PlotData.PlotScale.LOG);

            plot.plot();
            plot.save("tmp"+r+"");
        }
    }
}