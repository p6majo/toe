package com.p6majo.math.network;

import java.util.ArrayList;
import java.util.List;

public class Structure {
    private List<List<Neuron>> neuronStructure ;
    private int[] structure;

    public Structure(int[] structure, Network.Seed seed){
        this.structure = structure;
        this.neuronStructure = new ArrayList<List<Neuron>>();

        //add input layer
        List<Neuron> inputLayer = new ArrayList<Neuron>();
        for (int n=0;n<structure[0];n++)
            inputLayer.add(new InputNeuron(0.,n));
        this.neuronStructure.add(inputLayer);

        //add remaining layers
         for (int layer =1;layer<structure.length;layer++){
            List<Neuron> layerList = new ArrayList<Neuron>();
            for (int n=0;n<structure[layer];n++){
                //each neuron is equipped with a number of weights that correspond to
                //the number of neurons of the previous layer
                layerList.add(new Neuron(seed,this.structure[layer-1],n));
            }
            neuronStructure.add(layerList);
        }
    }

    /**
     * set values for the input neurons of the network
     * @param input
     */
    public void setInput(Double[] input){
        for (int i=0;i<input.length;i++){
            this.neuronStructure.get(0).get(i).value=input[i];
        }
    }


    public List<Neuron> getNeuronsOfLayer(int layer){
        return this.neuronStructure.get(layer);
    }

    /**
     * return nth neuron of lth layer
     * @param l
     * @param n
     * @return
     */
    public Neuron get(int l,int n){
        return this.neuronStructure.get(l).get(n);
    }


    public String toString(){
        StringBuilder out = new StringBuilder();
        out.append("Input layer with "+this.structure[0]+" neurons:\n");
        out.append("*******************************************************\n");
        out.append(neuronStructure.get(0).toString()+"\n\n");
        for (int layer =1;layer<this.structure.length;layer++){
            out.append("Layer "+layer+" with "+this.structure[layer]+" neurons:\n");
            out.append("*******************************************************\n");
            out.append(neuronStructure.get(layer).toString()+"\n\n");
        }
        return out.toString();
    }

}
