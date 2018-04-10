package com.p6majo.math.network2;


import com.p6majo.math.network.DataList;
import com.p6majo.math.network2.layers.CrossEntropyLayer;
import com.p6majo.math.network2.layers.Layer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A network is a List of {@link Layer}
 *
 * @author jmartin
 * @version 2.0
 * @data 10.4.18
 *
 */
public class Network {

     /**
     * different flags to determine the initial values of the neurons
     */
    public static enum Seed {NO_SEED, RANDOM, NO_BIAS, NO_BIAS_UNITY};

    private final List<Layer> layers;

    /**
     *
     */
    public Network(boolean visual) {

        layers = new ArrayList<Layer>();

        if (visual) {
            //System.out.println("init visualizer");
           // visualizer= new NetworkVisualizer(this,NetworkVisualizer.VisualizerModus.TRAINED_EDGES,1);
           // System.out.println("visualizer initialized");
        }
       // else visualizer = null;

    }

    public void addLayer(Layer layer){
        this.layers.add(layer);
    }


    public void train(Data[] data,int batchSize){
        Data[] batchData = new Data[batchSize];
        for (int i=0;i+batchSize<data.length;i+=batchSize){
            batchData = Arrays.copyOfRange(data,i,i+batchSize);
            Batch batch = new Batch(batchData);
            System.out.println("next batch");
            pushforward(batch);
            pullBack();
            learn();

        }
    }

    public void pushforward(Batch batch){
        for (int l=0;l<layers.size();l++){
            layers.get(l).pushForward(batch);
        }
    }

    public void pullBack(){
        CrossEntropyLayer last = (CrossEntropyLayer) layers.get(layers.size()-1);
        last.pullBack();
        for (int l=layers.size()-2;l>-1;l--)
            layers.get(l).pullBack(layers.get(l+1).getErrorsForPreviousLayer());
    }

    public void learn(){
        for (int l =0;l>layers.size();l++) layers.get(l).learn(0.01f);
    }

 /**
     * short method call for {@link #stochasticGradientDescent(DataList, DataList, int, double,int)}
     * @param dataList
     * @param batchSize
     * @param learningRate
     */
    public void stochasticGradientDescent(DataList dataList, int batchSize, double learningRate){
        stochasticGradientDescent(dataList,dataList,batchSize,learningRate,Integer.MAX_VALUE);
    }



    /**
     * Do stochastic gradient descent
     * @param trainingData
     * @param testData
     * @param batchSize
     * @param learningRate
     */
    public void stochasticGradientDescent(DataList trainingData, DataList testData, int batchSize, double learningRate,int maxCycles) {
        double sqrBatch = Math.sqrt(batchSize); //variable to adjust for the error growth depending on the batchsize
        double errorSum = 100;
        long steps = 0;

    /*
        this.trainingData = trainingData;
        this.testData = testData;
        stochasticGradientDescentRunnable = new StochasticGradientDescentRunnable(this,trainingData,batchSize,learningRate,true,maxCycles);
        stochasticGradientDescentRunnable.start();

        synchronized(stochasticGradientDescentRunnable){
            while (!stochasticGradientDescentRunnable.isFinished()){
                try{
                    stochasticGradientDescentRunnable.wait();
                }catch(InterruptedException ex){
                    System.out.println("Could not wait for the stochastic descent to finish.");
                }
            }
        }
    */
        System.out.println("Fertig");
    }


    public void suspendStochasticDescent(){
       // if (stochasticGradientDescentRunnable !=null) this.stochasticGradientDescentRunnable.suspend();
    }

    public void resumeStochasticDescent(){
       // if (stochasticGradientDescentRunnable !=null) this.stochasticGradientDescentRunnable.resume();
    }

    public void performSingleStep(){
        /*
        if (stochasticGradientDescentRunnable !=null)
            if (stochasticGradientDescentRunnable.isSuspended())
                this.stochasticGradientDescentRunnable.singleStep();
        */
    }


    /**
     * Evaluate the network for a given piece of {@link com.p6majo.math.network.Data}
     * The output of the network is stored in the activations of {@code data}
     * @param data piece of {@link com.p6majo.math.network.Data}
     * @return the piece of {@link com.p6majo.math.network.Data}
     */
    protected com.p6majo.math.network.Data feedForward(Data data) {
     return null;
    }




    public double runTest(){
        //return this.runTest(testData.size());
        return 0.;
    }

    /**
     * run counts tests and return the rate of success
     * @param counts
     * @return
     */
    public double runTest(int counts){
        /*
        if (stochasticGradientDescentRunnable.isTestable()) {
            int correct = 0;
            for (int r = 0; r < counts; r++) {
                int rnd = (int) (Math.random() * testData.size());
                Data data = testData.get(rnd);
                this.feedForward(data);
                double cost = cf.eval(data);
                int expectation = 0;
                int out = 0;
                double prop = 0.;

                Double[] activations = data.getActivations();
                Double[] expectations = data.getExpectations();
                for (int i = 0; i < activations.length; i++) {
                    if (activations[i] > prop) {
                        prop = activations[i];
                        out = i;
                    }
                    if (expectations[i] == 1.) expectation = i;
                }
                if (expectation == out) correct++;
            }
            return (double) correct / counts;
        }
        */
        return -1.;
    }

    /**
     * outputs the full state of the network
     * @return
     */
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("Neural Network with the following layers:\n");
        out.append("********************************************\n");
        for (Layer layer:layers) out.append(layer.toString());
        out.append("********************************************\n");
        return out.toString();
    }

}
