package com.p6majo.plot;

import com.p6majo.math.complex.Complex;

public abstract class DoubleDataProvider extends DataProvider<Double> {

    public DoubleDataProvider(){
        super(()->new Double(0.));
    }

}
