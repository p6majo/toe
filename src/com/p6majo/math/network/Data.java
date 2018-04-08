package com.p6majo.math.network;

import com.p6majo.math.utils.Utils;

public class Data {

    private Double[] input;
    private Double[] expectation;
    private Double[] output;

    /**
     *
     * @param input    array of input data
     * @param expectation array of expected output data
     */
    public Data(Double[] input, Double[] expectation) {
        this.input = input;
        this.expectation = expectation;
    }

    /**
     * sets all expecations to zero except the one at the position value
     * this function is particularly used for zipping input data and output data
     * in streams
     * @param value
     */
    public Data setExpectation(int value){
        for (int i=0;i<expectation.length;i++){
            expectation[i]=0.;
        }
        expectation[value]=1.;
        return this;
    }

    /**
     * store the actual output of the network
     * @param activations
     */
    public void setActivations(Double[] activations){
        this.output = activations;
    }

    /**
     * return the output of the network that corresponds to the given input
     * @return
     */
    public Double[] getActivations(){
        return this.output;
    }

    /**
     * return the expected output
     * @return
     */
    public Double[] getExpectations(){
        return this.expectation;
    }

    public Double[] getInput(){
        return this.input;
    }

    public String toString(){
        String out = "i: "+ Utils.array2String(input);
        out+="  o: "+ Utils.array2String(expectation);
        return out;
    }
}
