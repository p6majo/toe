package com.p6majo.plot;

import java.util.List;

/**
 * base class for plots
 * @author p6majo
 * @version 1.0
 * @param <D> type of data (i.e. Double, Complex)
 */
public class Plot<D extends Number> {

    private PlotData<D> plotData;
    private PlotRange<D> plotRange;
    private List<PlotOption> plotOptionList;
    private OutputChannel out;

}
