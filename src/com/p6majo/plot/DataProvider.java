package com.p6majo.plot;

import com.p6majo.math.utils.Utils;

import java.util.ArrayList;
import java.util.function.Supplier;

public abstract class DataProvider<D> extends Thread {

    //the plotData is initialized when the dataProvider is assoiciated with a Plot
    protected PlotData<D> plotData = null;
    protected PlotRange plotRange = null;
    final Supplier<D> supplier;


    /**
     * implementations of this abstract class have to be constructed with a supplier for the generic field D
     * e.g. for D Double
     * super(()->new Double(0.));
     * or for D Complex
     * supper(Complex::new);
     *
     * @param supplier
     */
    public DataProvider(Supplier<D> supplier){
        this.supplier = supplier;
        this.plotData = new PlotData<D>(supplier);

    }

    public void setPlotRange(PlotRange range){
        this.plotRange = range;
        this.plotRange.setDataProvider(this);

        //determine the amount of data points that are required for the given sampling
        int samplePoints = 1;
        if (this.plotRange.getNumberOfRanges()>0){
            for (int r = 0;r<this.plotRange.getNumberOfRanges();r++) {
                Integer samples = this.plotRange.getRange(r).getSamples();
                if (samples!=null)
                    if (samples>0)
                        samplePoints *=samples;
            }
        }

        this.plotData.setDataSize(samplePoints);
    }


    public PlotData<D> getPlotData(){
        return this.plotData;
    }

    public D getData(int x){
        return this.plotData.getData(x);
    }

    public void setData(int x,D data){
        this.plotData.setData(x,data);
    }

}
