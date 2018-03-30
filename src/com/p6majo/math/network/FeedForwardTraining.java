package com.p6majo.math.network;


/**
 * This interface allows to calculate the network output for a provided data set
 * @param <F> the field over which the data is provided
 */
public interface FeedForwardTraining<F> {
    public TrainingsData<F> eval(TrainingsData<F> data);
}
