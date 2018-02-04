package com.p6majo.plot;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.utils.Box;
import com.p6majo.math.utils.Utils;
import com.princeton.Draw;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.util.function.Function;
import java.util.stream.IntStream;


public class PlotComplex extends Plot<Complex> {

    //frame for default output
    private Draw frame;
    private Box range;
    private int width=300;
    private int height = 300;
    private double zmax = 1;

    //

    Function<Complex,Color> colorFunction = new Function<Complex, Color>() {
        @Override
        public Color apply(Complex z) {
            double phase = z.neg().phase();
            return Color.getHSBColor((float) ((phase+Math.PI)/2./Math.PI),1f,Math.min(1f,(float) (z.abs()/zmax)));
        }
    };


    public PlotComplex(DataProvider<Complex> provider){
        super.provider  = provider;
    }

    @Override
    public void out() {
        switch(super.plotOptions.getOutputOption()){
            case DEFAULT:
            default:
                this.width = super.plotRange.getRange(0).getSamples();
                this.height = super.plotRange.getRange(1).getSamples();
                this.zmax = super.plotRange.getRange(2).getEnd().doubleValue();

                this.generatePlot();
                break;
        }
    }


    private void generatePlot(){
        frame = new Draw();
        frame.setCanvasSize(width,height);
        frame.setXscale(0,width);
        frame.setYscale(0,height);
        frame.enableDoubleBuffering();


        switch(super.plotOptions.getTypeOption()){
            case DEFAULT:

                long points = IntStream.range(0,height)
                        .boxed()
                        //.parallel()//parallel plotting leads to disturbances
                        .flatMap(y -> IntStream
                                .range(0, width)
                                .mapToObj(x->new Point(frame,x,y,colorFunction.apply(this.plotData.getData(y*width+x))))
                        ).count();
                System.out.println(points + "  data points generated.");

                int clines = super.plotOptions.getContourLines();
                if (clines>0){
                    generateContourLines(clines);
                }

                break;

            default:
                break;
        }




        frame.show();

    }



    private void generateContourLines(int n){
        double zmax = super.plotRange.getRange(2).getEnd().doubleValue();
        double zmin = super.plotRange.getRange(2).getStart().doubleValue();

        //initialize contour level values
        double[] levels = new double[n];
        if (!super.plotOptions.isLogScaleZ()) {
            double delta = (zmax-zmin)/(n-1);
            for (int l=0;l<n;l++) levels[l]=zmin+l*delta;
        }
        else {
            if (super.plotOptions.isLogScaleZ() && zmin>0){
                double delta = Math.pow(zmax/zmin,1./(n-1));
                for (int l=0;l<n;l++) levels[l]=zmin*Math.pow(delta,l);
            }
            else Utils.errorMsg("lower limit on z must be larger than zero for log scale presentation of data, instead it is "+zmin);
        }

        //create data array of alternating 1 and 0 depending on which level it belongs to



    }

    /*************************/
    /** Output objects *******/
    /*************************/

    public class Point{

        public Point(Draw frame,int x,int y,Color color) {
            frame.setPenColor(color);
            frame.point(x,y);
        }
    }




}
