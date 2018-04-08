package com.p6majo.math.network;

/**
 * network that can be used together with genetic algorithms
 * the fitness value is a selector
 */
public class NetworkWithFitnessFunction extends Network implements Fitness {
    public Double fitness = null;

    /**
     * short Constructor of the network
     * default is {@link #crossEntropy} cost function and the {@link NetworkVisualizer} is deactivated
     *
     * @param signature layer signature of the network
     * @param seed      seeding of the initial values
     * @param sf        sigmoid function
     * @param cf
     */
    public NetworkWithFitnessFunction(int[] signature, Seed seed, SigmoidFunction sf, CostFunction cf) {
        super(signature, seed, sf, cf);
    }

    /**
     * extended version of the Constructor that allows for a user defined cost function and provides a flag
     * to activate the {@link NetworkVisualizer}
     *
     * @param signature layer signature of the network
     * @param seed      seeding of the initial values
     * @param sf        sigmoid function
     * @param cf        cost function
     * @param visual    flag for visualization
     */
    public NetworkWithFitnessFunction(int[] signature, Seed seed, SigmoidFunction sf, CostFunction cf, boolean visual) {
        super(signature, seed, sf, cf, visual);
    }

    @Override
    public double getFitness(DataList dataList){
        if (fitness==null)
            fitness = dataList.stream().map(super::feedForward).mapToDouble(super.cf::eval).sum();
        return fitness;
    }


    @Override
    public void updateFitness(DataList dataList){
        this.fitness = dataList.stream().map(super::feedForward).mapToDouble(super.cf::eval).sum();
    }
}
