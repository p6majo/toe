package com.p6majo.math.network;

/**
 * Cost function over a generic field
 */
public interface CostFunction{
    /**
     * returns the value of the cost function for a given array of activations and expectations
     * @return
     */
    public abstract Double eval(DoubleTrainingsData data);
    //public abstract double eval(Double[] activations, Double[] expectations);

    /**
     * returns the gradient of the cost function with respect to the activations of the output layer
     * @return
     */
    public abstract Double[] gradient(DoubleTrainingsData data);
    //public abstract Double[] gradient(Double[] activations, Double[] expectations);

}
