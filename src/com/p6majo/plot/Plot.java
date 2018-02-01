package com.p6majo.plot;

import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType;

import java.util.List;

/**
 * base class for plots
 * @author p6majo
 * @version 1.0
 * @param <D> type of data (i.e. Double, Complex)
 */
public class Plot<D extends Number> {

    final private PlotData<D> plotData;
    final private PlotRange plotRange;
    private List<PlotOption> plotOptionList;
    private List<JvmType.Primitive> primitiveList;
    private DataProvider provider;
    private OutputChannel out;

    public Plot(){
        this.plotRange = new PlotRange();
        this.plotData = new PlotData<D>();
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
        this.plotRange.addRange(new Range(start,end,sampleSize));
        if (sampleSize!=null) this.plotData.extendDataSet(sampleSize);
    }

    public void addDataProvider(DataProvider provider){
        this.provider = provider;
    }


    /**
     * this method utilizes the dataProvider to generate one data point for each sampling point
     */
    public void generateData(){
        provider.setPlotRange(plotRange);
        provider.setData(plotData.getData());
        provider.start();
    }


}
