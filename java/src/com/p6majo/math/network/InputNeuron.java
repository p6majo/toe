package com.p6majo.math.network;

import static com.p6majo.math.network.Network.*;

public class InputNeuron extends Neuron {

    public InputNeuron(double value,int index){
        super(Seed.NO_SEED,0,index);
        this.value = value;
    }

    @Override
    public String toString(){
         return String.format("(%.4f)",value);
    }
}
