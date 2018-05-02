package com.p6majo.plot;

/**
 * The range is used ot indicate the extension of the axis or the data in the plot
 * @author p6majo
 * @version 1.0
 */
public class Range {
    private Number start;
    private Number end;
    private Integer samples = null;

    public Range(Number start, Number end){
        this.start = start;
        this.end = end;
    }


    public Range(Number start, Number end, Integer samples){
        this.start = start;
        this.end = end;
        this.samples = samples;
    }

    public Number getEnd() {
        return end;
    }

    public Number getStart() {
        return start;
    }

    public Integer getSamples() {
        return samples;
    }
    public void setSamples(int samples){this.samples=samples;}

    public String toString(){
        return "["+start.toString()+"|"+end.toString()+"]";
    }
}
