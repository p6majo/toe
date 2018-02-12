package com.p6majo.plot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * base class for plots
 * @author p6majo
 * @version 1.0
 * @param <D> type of data (i.e. Double, Complex)
 */
public abstract class Plot<D> {

    final protected PlotRange<D> plotRange;
    final public PlotOptions plotOptions;
    protected List<Primitive> primitiveList;
    protected DataProvider provider;
    protected OutputChannel out;


    /**
     * there are two possible scenarios of initializing a plot
     * I) the plot ranges are set before the dataProvider is associated
     * II) the dataProvider is associated before the ranges are set
     *
     * The plotRange has consequences on the number of data points are provided by the data provider
     * Therefore, a change in plot Range feeds back to the data provider
     */
    public Plot(){
        this.plotRange = new PlotRange();
        this.plotOptions  = new PlotOptions();
    }


    /**
     * add the range of number for one axis
     * first range (with sample size) ( x-axis)
     * second range (with sample size) ( y-axis)
     * third range (z-axis or second x-axis in case of 2d plots)
     * fourth range (second y-axis in case of 2d plots))
     *
     * @param start
     * @param end
     */
    public void addRange(Number start, Number end){
        addRange(start,end,null);
    }

    /**
     * add the range of number for one dimension
     * for independent variables the number of sample points should be given
     * it is advisable to use 101 or 21 or 31 to account for an additional sample point
     * at the beginning of the interval
     *
     * @param start
     * @param end
     * @param sampleSize
     */
    public void addRange(Number start, Number end, Integer sampleSize){
        //when the provider is assoiciated already, the change in plotRange feeds back to the data provider
        this.plotRange.addRange(new Range(start,end,sampleSize));
    }

    public void addDataProvider(DataProvider provider){
        this.provider = provider;
        //if plot ranges are defined already, the required data is constructed when the plotRange is set in the provider
        this.provider.setPlotRange(this.plotRange);
    }


    public void setPlotOption(PlotOptions.TypeOption type){
        this.plotOptions.setTypeOption(type);
    }

    public void setStyleOption(PlotOptions.StyleOption styleOption){
        this.plotOptions.setStyleOption(styleOption);
    }

    public void setOutputOption(PlotOptions.OutputOption outputOption){
        this.plotOptions.setOutputOption(outputOption);
    }


    /**
     * this method utilizes the dataProvider to generate one data point for each sampling point
     */
    public void generateData(){
         provider.start();
    }

    public abstract void out();

    public int getDataSize(){
        return this.provider.getPlotData().getData().size();
    }



}
