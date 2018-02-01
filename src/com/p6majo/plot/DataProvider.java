package com.p6majo.plot;

import com.p6majo.math.utils.Utils;

public abstract class DataProvider extends Thread {

    protected PlotRange range=null ;
    protected Number[] data=null;


    public DataProvider(){

    }

    public void setPlotRange(PlotRange range){
        this.range = range;
        if (data!=null) {
            if (range.getSamplingPoints()!=data.length)
                Utils.errorMsg("The number of data points does not match the number of sampling points");
        }
    }

    public void setData(Number[] data){
        this.data = data;
        if (range!=null) {
            if (range.getSamplingPoints()!=data.length)
                Utils.errorMsg("The number of data points does not match the number of sampling points");
        }
    }

}
