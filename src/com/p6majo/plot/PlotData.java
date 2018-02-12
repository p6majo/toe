package com.p6majo.plot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

/**
 * This class stores data points for each sample points in an ArrayList
 * The ArrayList is filled empty points, such that each point of the data can be addressed directly
 * via the corresponding getData,setData methods
 * @param <D>
 */
public class PlotData<D> {
    private ArrayList<D> data;
    private Supplier<D> supplier;

    public PlotData(Supplier<D> supplier){
        //generate array of zero length
        this.supplier = supplier;
        this.data = new ArrayList<D>();
        this.data.add(supplier.get());
    }

    /**
     * if a new range is added to the plot, the size of data is extended correspondingly
     * the new range with non-zero sampling data acounts for a new dimension
     * @param sampleSize
     */
    public void extendDataSet(int sampleSize){
        int size =  this.data.size()*sampleSize;
        for (int i=this.data.size();i<size;i++) this.data.add(supplier.get());
    }

    public void setDataSize(int size){
        for (int i=this.data.size();i<size;i++) this.data.add(supplier.get());
    }

    public ArrayList<D> getData(){
        return this.data;
    }


    public D getData(int i){
        return this.data.get(i);
    }

    public void setData(int i,D data){
        this.data.set(i,data);
    }
}
