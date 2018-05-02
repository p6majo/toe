package com.p6majo.plot.old;

import com.p6majo.info.aachen.Kon;
import com.p6majo.math.function.ComplexElliptSn;
import com.p6majo.math.function.ComplexFunction;
import com.p6majo.math.utils.Box;
import org.junit.Test;

public class ComplexPlotTestElliptical {

    @Test
    public void generatePlot() {
            double k = 0.8;
            double l = Math.sqrt(1-k*k);
            ComplexFunction elliptical = new ComplexElliptSn(k,l);
            Box range = new Box(-8., -8, 8., 8.);
            //range.rescale(5.);
            int width = 1000;
            int height = 1000;
            ComplexPlot plot = new ComplexPlot(elliptical, range, ComplexPlot.PlotType.ARGMOD, width, height);
            plot.setMaxContourValue(2.);
            plot.setMinContourValue(0.);
            plot.setNumContourLines(11);
            plot.setMedValue(127);
            plot.setColorStretch(2.);
            plot.setScale(PlotData.PlotScale.LINEAR);

            plot.plot();
            plot.save("tmp");
        int i= Kon.readInt();
    }
}