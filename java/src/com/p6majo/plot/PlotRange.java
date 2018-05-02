package com.p6majo.plot;

import javax.xml.ws.Provider;
import java.util.ArrayList;
import java.util.List;

public class PlotRange<D>{
    private List<Range> ranges;
    private long samplingPoints = 1;
    private DataProvider<D> provider;


    public PlotRange(){
        this.ranges = new ArrayList<Range>();
    }

    public void addRange(Range range){
        this.addRange(range.getStart(),range.getEnd(),range.getSamples());
        if (range.getSamples()!=null) this.provider.getPlotData().extendDataSet(range.getSamples());
    }

    public void addRange(Number start, Number end, Integer samples){
        this.ranges.add(new Range(start,end,samples));
        if (samples!=null) samplingPoints*=samples;
    }

    /**
     * return the total number of sampling points
     * @return
     */
    public long getSamplingPoints(){
        return this.samplingPoints;
    }

    public Range getRange(int i){
        return this.ranges.get(i);
    }

    public void setDataProvider(DataProvider<D> provider){
        this.provider = provider;
    }

    public int getNumberOfRanges(){
        return ranges.size();
    }

}
