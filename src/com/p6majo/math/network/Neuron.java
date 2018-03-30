package com.p6majo.math.network;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Neuron {
    public int index;//index of the neuron in layer
    public Double[] weights;
    public double bias;
    public double value;
    public double error=0.;
    /**
     * all neurons except input neurons are dynamic neurons that can adjust in the process of learning
     */
    private final boolean dynamic;

    public static Predicate<Neuron> dynamicTest=new Predicate<Neuron>(){
        @Override
        public boolean test(Neuron neuron) {
           return neuron.isDynamic();
        }
    };

    public Neuron(Network.Seed seed, int nWeights, int index){
        this.index = index;
        if (nWeights!=0) {
            weights = new Double[nWeights];
            dynamic=true;
        }
        else this.dynamic = false;

        Random r = new Random();
         switch(seed){
             case RANDOM:
                for (int w=0;w<nWeights;w++) weights[w] = r.nextGaussian();
                bias = r.nextGaussian();
                value = 0;
                break;
             case NO_SEED:
                 //input neurons
                 value =0;
                 bias = 0;
                 break;
             case NO_BIAS:
                 for (int w=0;w<nWeights;w++) weights[w] = r.nextGaussian();
                 bias = 0;
                 value = 0;
                 break;
             case NO_BIAS_UNITY:
                 for (int w=0;w<nWeights;w++) weights[w]=1.;
                 bias=0;
                 value=0;
                 break;
        }
    }

    public void setWeights(Double[] weights){
        this.weights = weights;
    }

    public void setWeights(List<Double> weights){
        IntStream.range(0,Math.min(weights.size(),this.weights.length))
                .parallel()
                .forEach(i->this.weights[i]=weights.get(i));
    }

    public void setBias(double bias){
        this.bias = bias;
    }

    public boolean isDynamic(){
        return this.dynamic;

    }


    /**
     * string output of the state of the neuron
     * @return
     */
    public String toString(){
        String weightString = "(";
        for (int w=0;w<weights.length;w++) weightString+=String.format("%4f ",weights[w]);
        weightString=weightString.substring(0,weightString.length()-1);
        weightString+=")";
        return String.format("[%s|%.4f|%.4f]",weightString,bias,value);
    }
}
