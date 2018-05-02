package com.p6majo.math.network;

public interface Fitness {
    /**
     * calculate the fitness of the network for a given set of data
     * the fitness is used in genetic algorithms
     * @param dataList
     * @return
     */
    public double getFitness(DataList dataList);

    /**
     * manually update the fitness of the network if the fitness has changed
     * an automatic update is not performed since it can be very expensive and should only be done if necessary
     * after mutation etc.
     * @param dataList
     */
    public void updateFitness(DataList dataList);
}
