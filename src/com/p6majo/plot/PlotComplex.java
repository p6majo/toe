package com.p6majo.plot;


import com.p6majo.math.complex.Complex;
import com.p6majo.math.utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlotComplex extends Plot<Complex> {


    //black and transparent picture that contains the shape of the contours
    //it can be superimposed to the regular surface plot
    private Color[] contours = null;
    private double[][] edges  = null;

    private int width,height;
    private double zmax;

    private OutputChannel out ;

    /**
     * A function that calculates the color of a complex value indicating its phase information
     */
    Function<Point,Color> colorFunction = new Function<Point, Color>() {
        @Override
        public Color apply(Point p) {
            Complex z = (Complex) provider.getData(p.y*width+p.x);
            double phase = z.neg().phase();
            if (phase<0) phase+=Math.PI*2;
            return Color.getHSBColor((float) ((phase+Math.PI)/2./Math.PI),1f,Math.min(1f,(float) (z.abs()/zmax)));
        }
    };

    Function<Point,Color> colorFunctionWithContour = new Function<Point, Color>() {
        @Override
        public Color apply(Point p) {
            Complex z = (Complex) provider.getData(p.y*width+p.x);
            double phase = z.neg().phase();
            if (phase<0) phase+=Math.PI*2;
            double e = edges[p.y][p.x];
            Color bg =  Color.getHSBColor((float) ((phase+Math.PI)/2./Math.PI),1f,Math.min(1f,(float) (z.abs()/zmax)));
            Color fg = new Color(0f,0f,0f,(float) (1.-0.25*Math.min(4.,edges[p.y][p.x])));
            //perform overlay
            float alpha = 1f-fg.getAlpha()/255f;
            int newRed = (int) ((fg.getRed() * alpha) + (bg.getRed() * (1.0f - alpha)));
            int newGreen =(int) ( (fg.getGreen() * alpha) + (bg.getGreen() * (1.0f - alpha)));
            int newBlue = (int) ( (fg.getBlue() * alpha) + (bg.getBlue() * (1.0f - alpha)));

            return new Color(newRed,newGreen,newBlue);
        }
    };

    public PlotComplex(DataProvider<Complex> provider){
        //add supplier
        super.provider  = provider;
        super.provider.setPlotRange(this.plotRange);
    }

    @Override
    public void out() {
        switch(super.plotOptions.getOutputOption()){
            case DEFAULT:
                this.out = new OutputChannelDraw(this,this.plotOptions);
                break;
            case FILE:
                this.out = new OutputChannelFile(this,this.plotOptions);
            default:
                break;
        }

        this.width = super.plotRange.getRange(0).getSamples();
        this.height = super.plotRange.getRange(1).getSamples();
        this.zmax = super.plotRange.getRange(2).getEnd().doubleValue();
        this.generatePlot();
    }


    private void generatePlot(){
        switch(super.plotOptions.getTypeOption()){
            case DEFAULT:

                int clines = super.plotOptions.getContourLines();
                if (clines>0) generateContourLines(clines);

                final Function selFunc= clines==0?colorFunction:colorFunctionWithContour;

               // java.util.ArrayList<PrimitivePoint> pointList = new ArrayList<PrimitivePoint>();

                java.util.List<Point> pointList =IntStream.range(0,height).boxed()
                        .flatMap(y -> IntStream
                                .range(0, width)
                                .mapToObj(x->new Point(x,y))
                        ).collect(Collectors.toList());


                for (Point p:pointList)
                    out.addPrimitive(new PrimitivePoint(p,(Color) selFunc.apply(p)));

                out.finished();
                break;

            default:
                break;
        }
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
            double val = ((Complex) provider.getData(j*width+i)).abs();
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





}
