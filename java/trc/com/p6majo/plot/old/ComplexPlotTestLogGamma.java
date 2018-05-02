package com.p6majo.plot.old;

import com.p6majo.info.aachen.Kon;
import com.p6majo.math.function.ComplexFunction;
import com.p6majo.math.function.ComplexLogGamma;
import com.p6majo.math.utils.Box;
import org.junit.Test;

public class ComplexPlotTestLogGamma {

    @Test
    public void generatePlot() {
        ComplexFunction logGamma = new ComplexLogGamma();
        Box range = new Box(-10.,-10,10.,10);
        //range.rescale(5.);
        int width = 5000;
        int height = 5000;
        ComplexPlot plot = new ComplexPlot(logGamma,range, ComplexPlot.PlotType.ARGMOD,width,height);
        plot.setMaxContourValue(16.);
        plot.setMinContourValue(0.00);
        plot.setNumContourLines(17);
        plot.setMedValue(0);
        plot.setScale(PlotData.PlotScale.LINEAR);

        plot.plot();
        plot.save("tmp");

        //ColorContourPlot2 contourPlot = new ColorContourPlot2(width,height);
       // contourPlot.setCaptureMode(false);
       // contourPlot.setDataRange(0,25);
       // contourPlot.setData(plot.getData(),width,height);
        int i= Kon.readInt();
    }
}