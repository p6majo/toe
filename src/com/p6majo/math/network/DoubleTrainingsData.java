package com.p6majo.math.network;

import com.p6majo.math.utils.Utils;

public class DoubleTrainingsData extends TrainingsData<Double> {


    public DoubleTrainingsData(Double[] input, Double[] expectation) {
        super(input, expectation);
    }

    public String toString(){
        String out = "i: "+ Utils.array2String(super.input);
        out+="  o: "+ Utils.array2String(super.expectation);
        return out;
    }
}
