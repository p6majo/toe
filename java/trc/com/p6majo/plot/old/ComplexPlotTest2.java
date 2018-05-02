package com.p6majo.plot.old;

import com.p6majo.info.aachen.Kon;
import com.p6majo.math.function.ComplexFunction;
import com.p6majo.math.function.ComplexGamma;
import com.p6majo.math.utils.Box;
import org.junit.Test;

public class ComplexPlotTest2 {

    @Test
    public void generatePlot() {
        ComplexFunction gamma = new ComplexGamma();
        Box range = new Box(-15.,-1.25,3.,1.25);
        //range.rescale(5.);
        int width = 691;
        int height = 288;
        ComplexPlot plot = new ComplexPlot(gamma,range, ComplexPlot.PlotType.ARGMOD,width,height);
        plot.setMaxContourValue(1000.);
        plot.setMinContourValue(0.001);
        plot.setNumContourLines(7);
        plot.setMedValue(127);
        plot.setScale(PlotData.PlotScale.LOG);

        plot.plot();
        plot.save("tmp");

        //ColorContourPlot2 contourPlot = new ColorContourPlot2(width,height);
       // contourPlot.setCaptureMode(false);
       // contourPlot.setDataRange(0,25);
       // contourPlot.setData(plot.getData(),width,height);
        int i = Kon.readInt();
    }
}