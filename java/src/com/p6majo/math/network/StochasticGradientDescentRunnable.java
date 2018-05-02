package com.p6majo.math.network;

import java.util.stream.IntStream;

public class StochasticGradientDescentRunnable implements Runnable {

    //flags that control the performance of the Thread
    private boolean suspended;
    private boolean singleStep;
    private boolean finished=false;
    private boolean testable=true;

    private Thread t;
    private String name;

    private final Network network;
    private final DataList dataList;
    private final int batchSize;
    private final double learningRate;
    private final boolean verbose;
    private final int maxCycles;




    public StochasticGradientDescentRunnable(Network network, DataList dataList, int batchSize, double learningRate, boolean verbose, int maxCycles){
        this.name = "Stochastic descent thread";
        this.network = network;
        this.batchSize= batchSize;
        this.dataList = dataList;
        this.learningRate = learningRate;
        this.verbose = verbose;
        this.maxCycles=maxCycles;
    }

    public StochasticGradientDescentRunnable(Network network, DataList dataList, int batchSize, double learningRate, boolean verbose){
        this(network,dataList,batchSize,learningRate,true,Integer.MAX_VALUE);
    }

    public StochasticGradientDescentRunnable(Network network, DataList dataList, int batchSize, double learningRate){
       this(network,dataList,batchSize,learningRate,true,Integer.MAX_VALUE);
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
        System.out.println("Success rate at before the training: "+network.runTest(10000));

        int cycles = 0;
        double errorSum = 100.;
        //double dynamicLearningRate = this.learningRate;

        while (errorSum>0.01 && cycles<this.maxCycles){

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

            //here is the implementation of the stochastic gradient descent
            dataList.shuffle();
            testable=false;
            long start = System.currentTimeMillis();
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
            testable = true;
            //sum all error terms of the biases
            errorSum = 0;
            for (int layer=1;layer<network.getNumberOfLayers();layer++)
                errorSum+=network.getNeuronsOfLayer(layer).stream().mapToDouble(n->Math.abs(n.error)).sum();//todo parallel

            //output is given after approximately 10000 stochastic descent steps
            cycles++;
            //dynamicLearningRate = this.learningRate/Math.pow(10.,((double) cycles/10));
            if (verbose && (cycles-1) % Math.max(1,10000/dataList.size()*batchSize) == 0) {
                String infoString = "Error sum: " + errorSum+" after "+cycles+" training cycles in "+(System.currentTimeMillis()-start)+" ms.";
                System.out.println(infoString);
                System.out.println("Success rate based on 1000 tests: "+network.runTest(1000));
            }

            if (singleStep)
                this.suspend();

        }


        //send the message of the finished stochastic gradient descent to the main thread that should be waiting
        synchronized(this){
            this.finished = true;
            System.out.println(name + " exiting.");
            notifyAll();
        }
    }

    /**
     * start the thread that is associated with the runnable
     */
    public void start(){
        System.out.println("Starting the "+name+" ...");
        if (t == null) {
            t = new Thread (this,name);
            t.start ();
        }
    }

    /**
     * suspend the stochastic gradient descent
     */
    public void suspend(){
        this.suspended=true;
    }

    public boolean isSuspended(){
        return this.suspended;
    }

    public boolean isTestable(){ return this.testable;}

    /**
     * resume the stochastic gradient descent
     */
    public synchronized void resume(){
        this.suspended=false;
        this.singleStep = false;
        notifyAll();
        System.out.println("Continuing ...");
    }

    /**
     * only perform one single cycle of training (going through the whole data of training once
     */
    public synchronized void singleStep(){
        this.singleStep = true;
        this.suspended = false;
        notifyAll();
        System.out.println("Continuing for one step ...");
    }

    public boolean isFinished(){
        return this.finished;
    }

}
