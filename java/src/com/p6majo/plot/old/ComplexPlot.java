package com.p6majo.plot.old;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexFunction;
import com.p6majo.math.utils.Box;
import com.p6majo.math.utils.Utils;
import com.princeton.Draw;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class ComplexPlot{
    private Draw frame;
    private Box range;
    private ComplexFunction function;
    private PlotData data;
    private Complex[] cData;
    private Double maxMod;
    private Double minMod;



    public static enum PlotType {ARGMOD};

    private int width=300;
    private int height = 300;

    //manually entered by the user
    private Double maxContourValue = null;
    private Double minContourValue = null;
    private int medValue = 127;
    private Integer numContourLines = 10;
    private PlotData.PlotScale scale = PlotData.PlotScale.LINEAR;
    private double colorStretch = 1.;


    public ComplexPlot(ComplexFunction function, Box range,PlotType type,int width, int height){
        this.function = function;
        this.range = range;
        this.width = width;
        this.height = height;
        this.cData = new Complex[width*height];

        //calculate the complex values for the grid
        this.generateData();
        this.data = new PlotData(width,height,this.minMod,this.maxMod);


        frame = new Draw();
        frame.setCanvasSize(width,height);
        frame.setXscale(0,width);
        frame.setYscale(0,height);
        //frame.setXscale(range.lowleftx,range.uprightx);
        //frame.setYscale(range.lowlefty,range.uprighty);
        frame.enableDoubleBuffering();

    }

    private Complex getZ(int x,int y){
        double re = range.lowleftx+(range.uprightx-range.lowleftx)/width*x;
        double im = range.lowlefty+(range.uprighty-range.lowlefty)/height*y;
        return new Complex(re,im);
    }

    /**
     * The color is purely red at a phase of 0
     * the color is purely green at a phase of 120
     * the color is purely blue at a phase of 240
     * in between there are linear transitions
     * @param z
     * @return
     */
    private Color getColor(Complex z){
        double phase = z.arg();
        double mod = z.abs();
        if (phase<0) phase+=2.*Math.PI;
        int transition = (int) Math.floor(phase*6./2./Math.PI);

        double modStretched = 127*Math.log(mod)/Math.log(this.maxMod)*this.colorStretch;
        int base = (int) Math.min(Math.max(-255,modStretched),255);
        //int base = (int) (-128+(mod/maxMod)*256);
        int red=0;
        int green=0;
        int blue=0;

        double fraction = phase*6./2./Math.PI-transition;

        switch (transition){
            case 0:
                red = 255+base ;
                green = (int) (255*fraction+base);
                blue = base;
                break;
            case 1:
                red = base+255 -(int) (255*fraction);
                green = 255+base;
                blue = base;
                break;
            case 2:
                green = 255+base;
                blue = base+(int)(255*fraction);
                red = base;
                break;
            case 3:
                green = (int) (255-255*fraction)+base;
                blue = 255+base;
                red = base;
                break;
            case 4:
                blue = 255+base;
                red =base+(int) (255*fraction);
                green = base;
                break;
            case 5:
                blue = (int)(base+255-255*fraction);
                red = base+255;
                green = base;
                break;
        }
        red = Math.max(0,Math.min(red,255));
        green = Math.max(0,Math.min(green,255));
        blue = Math.max(0,Math.min(blue,255));

        return new Color(red,green,blue);
    }

    public void setColorStretch(double colorStretch) {
        this.colorStretch = colorStretch;
    }

    public void setMaxContourValue(Double maxContourValue) {
        this.maxContourValue = maxContourValue;
    }

    public void setMinContourValue(Double minContourValue) {
        this.minContourValue = minContourValue;
    }


    public void setNumContourLines(Integer numContourLines) {
        this.numContourLines = numContourLines;
    }

    public void setScale(PlotData.PlotScale scale) {
        this.scale = scale;
    }

    public void setMedValue(int medValue) {
        this.medValue = medValue;
    }


    public void generatePlot(){
        for (int y=0;y<height;y++)
            for (int x=0;x<width;x++){
                Complex w = cData[y*width+x];
                Color color = getColor(w);
                data.setData(x,y,w.abs());
                data.setColor(x,y,color);
            }
        data.generateContourLines(this.numContourLines,this.scale,this.minContourValue,this.maxContourValue);
    }


    private void generateData(){
        System.out.println("start generation");
        for (int y=0;y<height;y++)
            for(int x=0;x<width;x++){
                Complex z = getZ(x,y);
                Complex w = function.eval(z);
                cData[y*width+x] = w;
                double abs = w.abs();
                if (this.maxMod == null) this.maxMod = abs; else this.maxMod = Math.max(this.maxMod,abs);
                if (this.minMod == null) this.minMod = abs; else this.minMod = Math.min(this.minMod,abs);
            }
            System.out.println("Data generated: maximal value: "+this.maxMod+" minimal value: "+this.minMod);
    }




    public void plot(){
        this.generatePlot();

        double[] edges = data.getEdges();
        frame.setPenColor(Color.BLACK);
        for (int y=0;y<height;y++)
            for(int x=0;x<width;x++){
                double val = Math.abs(edges[y*width+x]);
                int grey = (int) (255-32*val);
                if (grey<200) frame.setPenColor(Color.BLACK);
                else frame.setPenColor(data.getColor(x,y));
                frame.point(x,y);
            }
        frame.show();
    }

    public void save(String filename){
        StringBuilder out= new StringBuilder();
        //construct file header
        out.append("P3\n");
        out.append(this.width+" "+this.height+"\n");
        out.append("256\n");

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(filename+".ppm", "UTF-8");
            Color[] cols = data.getColors();
            double[] edges = data.getEdges();

            for (int y=0;y<height;y++){
                for (int x=0;x<width;x++){
                    double val = Math.abs(edges[y*width+x]);
                    int grey = (int) (255-32*val);
                    if (grey<200) out.append("0 0 0 ");
                    else {
                        Color col = cols[y * width + x];
                        out.append(col.getRed() + " " + col.getGreen() + " " + col.getBlue() + " ");
                    }
                }
                out.append("\n");
            }
            writer.write(out.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Utils.errorMsg(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Utils.errorMsg(e.getMessage());
        }
        System.out.println("File saved");
    }
}
