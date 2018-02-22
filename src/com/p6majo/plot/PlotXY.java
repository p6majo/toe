package com.p6majo.plot;


import com.p6majo.math.complex.Complex;
import com.p6majo.math.utils.Utils;

import java.awt.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlotXY extends Plot<Double> {


    //black and transparent picture that contains the shape of the contours
    //it can be superimposed to the regular surface plot
    private Color[] contours = null;
    private double[][] edges  = null;

    private int width,height;
    private int maxHeight = 1201;
    private double zmax;

    private Range xRange;
    private Range yRange;

    private OutputChannel out ;



    public PlotXY(DataProvider<Double> provider){
        //add supplier
        super.provider  = provider;
        super.provider.setPlotRange(this.plotRange);
    }

    @Override
    public void out() {
        //the height is calculated to preserve the aspect ratio of the axes as long as possible
        //unless the value of maxHeight is exceeded
        yRange = super.plotRange.getRange(1);
        xRange = super.plotRange.getRange(0);
        this.width = super.plotRange.getRange(0).getSamples();
        if (yRange.getSamples()==null) {
            this.height = (int) Math.min(maxHeight, this.width * (yRange.getEnd().doubleValue() - yRange.getStart().doubleValue()) / (xRange.getEnd().doubleValue() - xRange.getStart().doubleValue()));
            super.plotRange.getRange(1).setSamples(this.height);
        }

        switch(super.plotOptions.getOutputOption()){
            case DEFAULT:
                this.out = new OutputChannelDraw(this,this.plotOptions);
            default:
                break;
        }



        this.generatePlot();
    }



    private void generatePlot(){
        switch(super.plotOptions.getTypeOption()){
            case DEFAULT:

                generateAxes();
                generateGrid();

                double ymin = yRange.getStart().doubleValue();
                double ymax = yRange.getEnd().doubleValue();

                Point[] points = new Point[width];
                for (int x=0;x<width;x++){
                    double y = provider.plotData.getData().get(x);
                    points[x]=new Point(x,(int) (height-1-(y-ymin)/(ymax-ymin)*height));
                }

                for (int i=1;i<width;i++) out.addPrimitive(new PrimitiveLine(points[i-1],points[i],Color.BLUE));
                out.finished();
                break;

            default:
                break;
        }
    }

    private void generateAxes() {

        out.addPrimitive(new PrimitiveLine(new Point(0, 0), new Point(width , 0), Color.BLACK));
        out.addPrimitive(new PrimitiveLine(new Point(0, 0), new Point(0, height), Color.BLACK));
        out.addPrimitive(new PrimitiveLine(new Point(0, height ), new Point(width , height ), Color.BLACK));
        out.addPrimitive(new PrimitiveLine(new Point(width , 0), new Point(width , height), Color.BLACK));

    }


    private void generateGrid() {

        //ZxZ grid
        double ymin = yRange.getStart().doubleValue();
        double ymax = yRange.getEnd().doubleValue();
        for (int y = (int) Math.ceil(ymin); y < Math.ceil(ymax); y++) {
            int ypos = (int) Math.round((y - ymin) / (ymax - ymin) * height);
            out.addPrimitive(new PrimitiveLine(new Point(0, ypos), new Point(width, ypos), Color.LIGHT_GRAY));
        }
        double xmin = xRange.getStart().doubleValue();
        double xmax = xRange.getEnd().doubleValue();
        for (int x = (int) Math.ceil(xmin); x < Math.ceil(xmax); x++) {
            int xpos = (int) Math.round((x - xmin) / (xmax - xmin) * width);
            out.addPrimitive(new PrimitiveLine(new Point( xpos,0), new Point( xpos,height), Color.LIGHT_GRAY));
        }

    }




}
