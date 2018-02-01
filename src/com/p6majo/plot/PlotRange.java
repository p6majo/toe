package com.p6majo.plot;

import java.util.ArrayList;
import java.util.List;

public class PlotRange {
    private List<Range> ranges;
    private long samplingPoints = 1;


    public PlotRange(){
        this.ranges = new ArrayList<Range>();
    }

    public void addRange(Range range){
        this.ranges.add(range);
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


    public int getNumberOfRanges(){
        return ranges.size();
    }

}
