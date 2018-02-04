package com.p6majo.plot;

public class PlotData<D extends Number> {
    private Number[] data;

    public PlotData(){
        this.data = new Number[1];
    }

    /**
     * if a new range is added to the plot, the size of data is extended correspondingly
     * the new range with non-zero sampling data acounts for a new dimension
     * @param sampleSize
     */
    public void extendDataSet(int sampleSize){
        Number[] newData = new Number[this.data.length*sampleSize];
        for (int n=0;n<this.data.length;n++) newData[n] = data[n];
        this.data = newData;
    }

    public Number[] getData(){
        return this.data;
    }

    public void setData(Number[] data){
        this.data = data;
    }

    public D getData(int i){
        return (D) this.data[i];
    }

    public void setData(int i,Number data){
        this.data[i]=data;
    }
}
