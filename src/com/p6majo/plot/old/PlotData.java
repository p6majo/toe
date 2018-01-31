package com.p6majo.plot.old;

import com.p6majo.math.utils.Utils;

import java.awt.*;

public class PlotData {

    private double[] data;
    private Color[] colors;
    //the data is stored alternatingly as 1 and 0 depending to which contour level the data point belongs to
    private double[] contourZebra;
    private double[] verticalEdges;
    private double[] horizontalEdges;
    private double[] edges;
    private int width;
    private int height;
    private Double max=null;
    private Double min=null;
    private Double positiveMin = null;

    public static enum PlotScale {LINEAR,LOG};

    public PlotData(int width,int height,double min,double max){
        this.width = width;
        this.height = height;
        this.data = new double[width*height];
        this.contourZebra = new double[width*height];
        this.verticalEdges = new double[width*height];
        this.horizontalEdges = new double[width*height];
        this.edges = new double[width*height];
        this.colors = new Color[width*height];
        this.min = min;
        this.max = max;
    }

    public void setData(int i,int j,double value){
        if (i>-1 && i<width){
            if (j>-1 && j<height){
                this.data[j*width+i]=value;
                if (max==null) max=value;
                else max = Math.max(max,value);
                if (min==null) min=value;
                else min = Math.min(min,value);
                if (value>0){
                    if (positiveMin==null) positiveMin = value;
                    else positiveMin = Math.min(positiveMin,value);
                }
            }
            else Utils.errorMsg("j index out of range: "+j);
        }
        else Utils.errorMsg("i index out of range: "+i);
    }

    public void setColor(int i,int j ,Color color){
        if (i>-1 && i<width){
            if (j>-1 && j<height){
                this.colors[j*width+i]=color;
            }
            else Utils.errorMsg("j index out of range: "+j);
        }
        else Utils.errorMsg("i index out of range: "+i);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getMaxValue(){
        return this.max;
    }

    public double getMinValue(){
        return this.min;
    }

    public double[] getData(){
        return this.data;
    }

    public Color getColor(int x,int y){
        return this.colors[y*width+x];
    }

    public Color[] getColors(){
        return this.colors;
    }

    public double getData(int x,int y){
        return this.data[y*width+x];
    }


    public void generateContourLines(Integer nlines, PlotScale scale,Double cmin, Double cmax){
        double[] contValues = new double[nlines];
        if (cmin==null) cmin = this.min;
        if (cmax==null) cmax = this.max;

        switch(scale){
            case LINEAR:
                double delta = (cmax-cmin)/(nlines-1);
                for (int i=0;i<nlines;i++) contValues[i] = cmin+delta*i;
                break;
            case LOG:
                if (cmin<0) Utils.errorMsg("Sorry, negative values do not allow for logarithmic scales");
                if (cmin==0.) cmin = positiveMin;
                delta = Math.pow(cmax/cmin,1./(nlines-1));
                for (int i=0;i<nlines;i++) contValues[i]=cmin*Math.pow(delta,i);
                break;
        }

        for (int i=0;i<nlines;i++) System.out.println(i+": "+contValues[i]);

        for (int i=0;i<width;i++){
            for (int j=0;j<height;j++){
                int level = 0;
                double val = data[j*width+i];
                for (int k=0;k<nlines;k++) {
                    if (contValues[k] >= val) {
                        level = k-1;
                       // System.out.println(val+"<"+contValues[k]);
                        break;
                    }
                }
               // System.out.println(i+","+j+": "+level);
                if (level%2==0) this.contourZebra[j*width+i]=0.;
                else this.contourZebra[j*width+i]=1.;
            }
        }

        this.calcVerticalEdges();
        this.calcHorizontalEdges();
        this.calcEdges();
    }


    private void calcVerticalEdges(){
        double[] tmp = new double[height*width];
        for (int i=1;i<width-1;i++)
            for(int j=1;j<height-1;j++)
                tmp[j*width+i]=this.contourZebra[j*width+(i-1)]-this.contourZebra[j*width+(i+1)];

        for (int i=1;i<width-1;i++)
            for(int j=1;j<height-1;j++)
                this.verticalEdges[j*width+i]=tmp[(j-1)*width+i]+2.*tmp[j*width+i]+tmp[(j+1)*width+i];
        System.out.println("Vertical edges calculated");
    }

    private void calcHorizontalEdges(){
        double[] tmp = new double[height*width];
        for (int i=1;i<width-1;i++)
            for(int j=1;j<height-1;j++)
                tmp[j*width+i]=this.contourZebra[(j-1)*width+i]-this.contourZebra[(j+1)*width+i];

        for (int i=1;i<width-1;i++)
            for(int j=1;j<height-1;j++)
                this.horizontalEdges[j*width+i]=tmp[j*width+i-1]+2.*tmp[j*width+i]+tmp[j*width+i+1];
        System.out.println("Horizontal edges calculated");
    }

    public void calcEdges(){
        this.calcVerticalEdges();
        this.calcHorizontalEdges();
        for (int i=0;i<height*width;i++) this.edges[i]=Math.sqrt(this.horizontalEdges[i]*this.horizontalEdges[i]+this.verticalEdges[i]*this.verticalEdges[i]);
        System.out.println("Edges calculated");
    }

    public double[] getEdges(){
        return this.edges;
    }
}
