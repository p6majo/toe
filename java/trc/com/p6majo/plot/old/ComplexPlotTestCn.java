package com.p6majo.plot.old;


import com.p6majo.info.aachen.Kon;
import com.p6majo.math.function.ComplexElliptCn;
import com.p6majo.math.function.ComplexFunction;
import com.p6majo.math.utils.Box;
import org.junit.Test;

public class ComplexPlotTestCn {

    @Test
    public void generatePlot() {
        for (int r=101;r<199;r++) {
            double k = 0.01*(r-100);
            double l = Math.sqrt(1-k*k);
            ComplexFunction cn = new ComplexElliptCn(k,l);
            Box range = new Box(-8., -8, 8., 8.);
            //range.rescale(5.);
            int width = 200;
            int height = 200;
            ComplexPlot plot = new ComplexPlot(cn, range, ComplexPlot.PlotType.ARGMOD, width, height);
            plot.setMaxContourValue(100.);
            plot.setMinContourValue(0.01);
            plot.setNumContourLines(5);
            plot.setMedValue(127);
            plot.setScale(PlotData.PlotScale.LOG);

            plot.plot();
            plot.save("tmp"+r);
        }
        int i= Kon.readInt();
    }
}