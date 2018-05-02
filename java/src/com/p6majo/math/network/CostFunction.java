package com.p6majo.math.network;

/**
 * Cost function over a generic field
 */
public interface CostFunction{
    /**
     * returns the value of the cost function for a given array of activations and expectations
     * @return
     */
    public abstract Double eval(Data data);
    //public abstract double feedForward(Double[] activations, Double[] expectations);

    /**
     * returns the gradient of the cost function with respect to the activations of the output layer
     * @return
     */
    public abstract Double[] gradient(Data data);
    //public abstract Double[] gradient(Double[] activations, Double[] expectations);

}
