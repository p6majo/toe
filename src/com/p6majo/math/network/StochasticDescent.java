package com.p6majo.math.network;

import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.Selector;
import java.util.stream.IntStream;

public class StochasticDescent implements Runnable {

    //flags that control the performance of the Thread
    private boolean suspended;
    private boolean singleStep;

    private Thread t;
    private String name;

    private final DoubleTrainingsDataList dataList;
    private final int batchSize;
    private final double learningRate;

    private final Network network;


    public StochasticDescent(Network network,DoubleTrainingsDataList dataList, int batchSize, double learningRate){
        this.name = "Stochastic descent thread";
        this.network = network;
        this.batchSize= batchSize;
        this.dataList = dataList;
        this.learningRate = learningRate;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.println("Running ..."  );
        int steps = 0;
        double errorSum = 100.;

        while (errorSum>0.001 && steps<1000000){
            synchronized(this) {
                while(suspended) {
                    System.out.println("Waiting ...");
                    try {
                        wait();
                    }  catch (InterruptedException e) {
                        System.out.println(name + " interrupted.");
                    }
                }
            }

            //here is the implementation of the stochastic descent

            if (dataList.size()<100) dataList.shuffle();

            IntStream.range(0,dataList.size()/batchSize)
                    .mapToObj(i->dataList.subList(i*batchSize,(i+1)*batchSize))
                    .forEach(batch->{
                        network.resetErrors();
                        batch.stream().forEach(data->network.accumulateErrors(data));//TODO this might not work in parallel
                        //adjust parameters of the network
                        for (int layer = 1; layer < network.getNumberOfLayers(); layer++) {
                            final int l = layer;
                            network.getNeuronsOfLayer(l)
                                    .stream()//todo parallel
                                    .forEach(n -> {
                                        n.bias -= learningRate * n.error/batchSize;
                                        //use neuron stream of the previous layer to stream through the weights//TODO parallel
                                        network.getNeuronsOfLayer(l-1).stream().forEach(m->n.weights[m.index]-=learningRate*m.value*n.error/batchSize);
                                    });
                        }

                    });

            //sum all error terms of the biases
            errorSum = 0;
            for (int layer=1;layer<network.getNumberOfLayers();layer++)
                errorSum+=network.getNeuronsOfLayer(layer).stream().mapToDouble(n->Math.abs(n.error)).sum();//todo parallel

            steps++;
            if (steps % 10 == 0) {
                String infoString = "Error sum: " + errorSum+" after "+steps+" steps.";
                System.out.println(infoString);
            }
        }
        System.out.println(name + " exiting.");
    }

    public void start(){
        System.out.println("Starting the "+name+" ...");
        if (t == null) {
            t = new Thread (this,name);
            t.start ();
        }
    }

    public void suspend(){
        this.suspended=true;
        this.singleStep = false;
    }

    public synchronized void resume(){
        this.suspended=false;
        this.singleStep = false;
        notify();
        System.out.println("Continuing ...");
    }

    public void singleStep(){
        this.singleStep = true;
        this.suspended = false;
    }

}
