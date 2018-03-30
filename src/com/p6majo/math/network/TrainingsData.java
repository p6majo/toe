package com.p6majo.math.network;

/**
 * generic training data class over a generic field F
 * @param <F>
 */
public abstract class TrainingsData<F> {
    protected F[] input;
    protected F[] expectation;
    //a storage place for network activations that result from a given input data
    protected F[] activation;

    public TrainingsData(F[] input, F[] expectation){
        this.input=input;
        this.expectation=expectation;
    }

    public F[] getExpectation(){
        return this.expectation;
    }

    public F[] getInput() { return input; }

    public F[] getActivations(){return activation;}

    public void setActivations(F[] activation){this.activation=activation;}
    public void setExpectations(F[] expectation){this.expectation=expectation;}

}
