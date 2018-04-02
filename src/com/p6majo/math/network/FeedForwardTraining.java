package com.p6majo.math.network;


/**
 * This interface allows to calculate the network output for a provided data set
 */
public interface FeedForwardTraining {
    public DoubleTrainingsData eval(DoubleTrainingsData data);
}
