package com.p6majo.plot;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.utils.Box;
import com.p6majo.math.utils.Utils;
import com.princeton.Draw;
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

    //black and transparent picture that contains the shape of the contours
    //it can be superimposed to the regular surface plot
    private Color[] contours = null;
    private double[][] edges  = null;

    /**
     * A function that calculates the color of a complex value indicating its phase information
     */
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

                int clines = super.plotOptions.getContourLines();

                if (clines>0) generateContourLines(clines);


                long points = IntStream.range(0,height)
                        .boxed()
                        //.parallel()//parallel plotting leads to disturbances
                        .flatMap(y -> IntStream
                                .range(0, width)
                                .mapToObj(x->new Point(x,y,colorFunction.apply(this.plotData.getData(y*width+x))))
                        ).count();
                System.out.println(points + "  data points generated.");



                break;

            default:
                break;
        }




        frame.show();

    }



    private void generateContourLines(int n){
        this.contours = new Color[width*height];
        Range contourRange ;
        if (super.plotOptions.getContourRange()==null) contourRange = super.plotRange.getRange(2);
        else contourRange = super.plotOptions.getContourRange();
        double zmax = contourRange.getEnd().doubleValue();
        double zmin = contourRange.getStart().doubleValue();

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
        boolean[][] zebra = new boolean[height][width]; //careful matrix indices are opposite to x,y coordinates

        for (int i=0;i<width;i++)
            for (int j=0;j<height;j++){
            int level = 0;
            double val = plotData.getData(j*width+i).abs();
            for (int k=0;k<n;k++) {
                if (levels[k] >= val) {
                    level = k-1;
                    break;
                }
            }
            if (level%2==0) zebra[j][i]=false;
            else zebra[j][i]=true;
        }

        /*
        //output zebra
        for (int j=0;j<height;j++){
            for (int i=0;i<width;i++) System.out.print((zebra[j][i]?1:0)+" ");
            System.out.println();
        }
        */


        //apply edge detection
        double[][] vedges = calcVerticalEdges(zebra);
        double[][] hedges = calcHorizontalEdges(zebra);

        edges = new double[height][width];

        for (int i=0;i<width;i++)
            for (int j=0;j<height;j++){
                double vedge = vedges[j][i];
                double hedge = hedges[j][i];
                edges[j][i] = Math.sqrt(vedge*vedge+hedge*hedge);
                //the largest value for straight edges is 4 and for diagonal edges is 4.5
                //to get nice antialiasing curves the transparent values are adjusted according to their
                //edge strength 0 (transparent) 4 (opaque)
                //this.contours[j*width+i]=new Color(0f,0f,0f,(float) (1.-0.25*Math.min(4.,edges[j][i])));
        }

        /*
        //output edges
        for (int j=0;j<height;j++){
            for (int i=0;i<width;i++)
                System.out.printf("%.1f ",edges[j][i]);
            System.out.println();
        }
        */

    }

    private double[][] calcVerticalEdges(boolean[][] zebra){
        //vertical edges
        double[][] tmp = new double[height][width];
        for (int i=1;i<width-1;i++)
            for(int j=1;j<height-1;j++)
                tmp[j][i]=(zebra[j][i-1]?1:0)-(zebra[j][(i+1)]?1:0);

        double[][] vedges = new double[height][width];
        for (int i=1;i<width-1;i++)
            for(int j=1;j<height-1;j++)
                vedges[j][i]=tmp[(j-1)][i]+2.*tmp[j][i]+tmp[(j+1)][i];
       // System.out.println("Vertical edges calculated");

        return vedges;

    }

    private double[][] calcHorizontalEdges(boolean[][] zebra){
        double[][] tmp = new double[height][width];
        for (int i=1;i<width-1;i++)
            for(int j=1;j<height-1;j++)
                tmp[j][i]=(zebra[(j-1)][i]?1:0)-(zebra[(j+1)][i]?1:0);

        double[][] hEdges = new double[height][width];
        for (int i=1;i<width-1;i++)
            for(int j=1;j<height-1;j++)
                hEdges[j][i]=tmp[j][i-1]+2.*tmp[j][i]+tmp[j][i+1];
       // System.out.println("Horizontal edges calculated");
        return hEdges;
    }


    /*************************/
    /** Output objects *******/
    /*************************/

    public class Point{


        public Point(int x,int y,Color color) {
            if (plotOptions.getContourLines()>0){

                //forground
                Color edge = new Color(0f,0f,0f,(float) (1.-0.25*Math.min(4.,edges[y][x])));
                //perform overlay

                float alpha = 1f-edge.getAlpha()/255f;
                int newRed = (int) ((edge.getRed() * alpha) + (color.getRed() * (1.0f - alpha)));
                int newGreen =(int) ( (edge.getGreen() * alpha) + (color.getGreen() * (1.0f - alpha)));
                int newBlue = (int) ( (edge.getBlue() * alpha) + (color.getBlue() * (1.0f - alpha)));

                frame.setPenColor(new Color(newRed,newGreen,newBlue));
            }
            else frame.setPenColor(color);
            frame.point(x,y);
        }
    }




}
