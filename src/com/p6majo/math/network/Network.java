package com.p6majo.math.network;

import com.p6majo.math.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



/**
 * Network class
 * @author jmartin
 */
public class Network extends AbstractNetwork<Double>{

    private static final double EPS = 1e-9;//cutoff for zero activations to prevent log-overflow
    private final String tag;
    private Double fitness = null;

    /**
     * different flags to determine the initial values of the neurons
     */
    public static enum Seed {
        NO_SEED, RANDOM, NO_BIAS, NO_BIAS_UNITY
    }


    /**
     * a list of layers
     * so far layers are just a list of neurons
     */
    private List<List<Neuron>> neuronStructure;
    /**
     * the number of neurons in each layer
     */
    private int[] structure;


    // private Structure network;
    public static final double ETA = 1;//Learning rate
    private final SigmoidFunction sf;
    private final CostFunction<Double> cf;


    private boolean visual = false;
    //private NetworkVisualizer visualizer;
    private NetworkVisualizer visualizer;

    private Timer timer;
    private int updateInterval=100;

    private StochasticDescent stochasticDescent = null;

    /**
     * Constructor of the network
     * @param structure
     * @param seed
     * @param sf sigmoid function
     * @param cf cost function
     */
    public Network(int[] structure, Seed seed, SigmoidFunction sf, CostFunction cf) {

        this.structure = structure;
        this.createNetwork(seed);
        this.sf = sf;
        this.cf = cf;
        this.tag = "";

    }

    /**
     * Constructor of the network
     * @param structure
     * @param seed
     * @param sf sigmoid function
     * @param cf cost function
     */
    public Network(int[] structure, Seed seed, SigmoidFunction sf, CostFunction cf,String tag) {

        this.structure = structure;
        this.createNetwork(seed);
        this.sf = sf;
        this.cf = cf;
        this.tag = tag;

    }

    /**
     * Constructor of the network
     * @param structure
     * @param seed
     * @param sf sigmoid function
     * @param cf cost function
     */
    public Network(int[] structure, Seed seed, SigmoidFunction sf, CostFunction cf, boolean visual) {

        this.structure = structure;
        this.createNetwork(seed);
        this.sf = sf;
        this.cf = cf;
        this.visual = visual;

        if (visual) {
            System.out.println("init visualizer");
            visualizer= new NetworkVisualizer(this,NetworkVisualizer.VisualizerModus.TRAINED_VERTICES);
            TimerTask timerTask = new TimerTask(){
                @Override
                public void run() {
                    visualizer.update();
                }
            };

            timer = new Timer("MyTimer");
            timer.scheduleAtFixedRate(timerTask,30,updateInterval);

            visualizer.setTimer(timer);
            System.out.println("visualizer initialized");
        }
        this.tag = "";

    }

    /**
     * this method sets up the layers of neurons
     * and seeds the values for weights and biases
     *
     * @param seed
     */
    private void createNetwork(Seed seed) {
        this.neuronStructure = new ArrayList<List<Neuron>>();

        //add input layer
        List<Neuron> inputLayer = new ArrayList<Neuron>();
        for (int n = 0; n < structure[0]; n++)
            inputLayer.add(new InputNeuron(0.,n));
        this.neuronStructure.add(inputLayer);

        //add remaining layers
        for (int layer = 1; layer < structure.length; layer++) {
            List<Neuron> layerList = new ArrayList<Neuron>();
            for (int n = 0; n < structure[layer]; n++) {
                //each neuron is equipped with a number of weights that correspond to
                //the number of neurons of the previous layer
                layerList.add(new Neuron(seed, this.structure[layer - 1],n));
            }
            neuronStructure.add(layerList);
        }
    }

    /**
     * this method sets the values for the input neurons of the network
     *
     * @param input
     */
    private void setInput(Double[] input) {
        if (input.length != this.structure[0])
            Utils.errorMsg("The input data does not match with the input layer of the network.");
        for (int i = 0; i < input.length; i++) {
            this.neuronStructure.get(0).get(i).value = input[i];
        }
    }

    @Override
    public TrainingsData<Double> eval(TrainingsData<Double> data) {
        this.setInput(data.getInput());
        data.setActivations(this.getOutput());
        return data;
    }


    /**
     * Getter returns the layer of neurons
     *
     * @param layer
     * @return
     */
    public List<Neuron> getNeuronsOfLayer(int layer) {
        return this.neuronStructure.get(layer);
    }

    /**
     * Getter returns one particular neuron
     *
     * @param l
     * @param n
     * @return
     */
    public Neuron get(int l, int n) {
        return this.neuronStructure.get(l).get(n);
    }

    /**
     * all neurons except the input neurons from the first layer are considered to be active
     * input neurons do not have weights and biases by default and are therefore not considered
     * to be dynamic
     *
     * @return
     */
    public int getNumberOfDynamicNeurons() {
        int neurons = 0;
        for (int l = 1; l < structure.length; l++) neurons += structure[l];
        return neurons;
    }

    public List<Neuron> getListOfDynamicNeurons() {
        //return this.neuronStructure.stream().flatMap(List::stream).filter(Neuron.dynamicTest).collect(Collectors.toList());
        //improved version
        //the neurons of all layers except the input layer are collected into a new list
        return this.neuronStructure.stream().skip(1).flatMap(List::stream).collect(Collectors.toList());
    }


    /**
     * Calculate the state of the network
     * @return
     */
    private Double[] getOutput() {
        int lastLayerIndex = this.structure.length - 1;
        int numberOfOutputNeurons = this.structure[lastLayerIndex];
        Double[] out = new Double[numberOfOutputNeurons];

        for (int l = 1; l < this.structure.length; l++) {
            for (Neuron n : this.getNeuronsOfLayer(l)) {
                double sum = 0;
                int count = 0;
                for (Neuron m : this.getNeuronsOfLayer(l - 1)) {
                    sum += m.value * n.weights[count];
                    count++;
                }
                n.value = this.sf.eval(sum + n.bias);
            }
        }

        for (int o = 0; o < numberOfOutputNeurons; o++) out[o] = this.get(lastLayerIndex, o).value;
        return out;
    }

    /**
     * apply the method of stochastic gradient descent to the network
     *
     * @param dataList
     * @param batchSize
     * @param learningRate
     */
    public void stochasticGradientDescentWithAdaptiveLearningRate(TrainingsDataList<Double> dataList, int batchSize, double learningRate) {
        double sqrBatch = Math.sqrt(batchSize); //variable to adjust for the error growth depending on the batchsize
        double errorSum = 100;
        long steps = 0;
        double enhancer = 1.;
        double oldErrorSum = 0.;
        while (errorSum > 0.0001) {
            dataList.shuffle();
            errorSum = 0.;
            int dataCounter = 1;
            for (TrainingsData<Double> data : dataList) {
                if (dataCounter == 1) this.calcErrors(data, true);
                else this.calcErrors(data, false);
                if (dataCounter == batchSize) { //if the batchsize is reached
                    //adjust parameters of the network
                    for (int layer = 1; layer < this.getNumberOfLayers(); layer++) {
                        List<Neuron> previousLayer = this.getNeuronsOfLayer(layer - 1);
                        for (Neuron n : this.getNeuronsOfLayer(layer)) {
                            n.bias -= learningRate / enhancer * n.error;
                            for (int k = 0; k < this.structure[layer - 1]; k++)
                                n.weights[k] -= learningRate / enhancer * previousLayer.get(k).value * n.error;
                            errorSum += Math.abs(n.error / sqrBatch);
                        }
                    }
                    dataCounter = 0;//reset dataCounter
                }//if
                dataCounter++;
            }//for
            steps++;
            enhancer = 1.;
            if (steps % 5 == 0) {
                enhancer = 100 * Math.abs(errorSum - oldErrorSum) / Math.max(Math.abs(errorSum), Math.abs(oldErrorSum));
                if (steps % 10000 == 0) System.out.println("Error sum: " + errorSum + " Enhancer: " + enhancer * 100);
            }
            //System.out.println("Error sum: "+errorSum+" Enhancer: "+enhancer);
            oldErrorSum = errorSum;
        }
        System.out.println("Number of steps: " + steps);
    }


    public double calculateErrorSum() {

        double errorSum = 0;
        for (int layer=1;layer<getNumberOfLayers();layer++)
            errorSum+=this.getNeuronsOfLayer(layer).stream().mapToDouble(n->Math.abs(n.error)).sum();//todo parallel

       return errorSum;

    }

    public void suspendStochasticDescent(){
        if (stochasticDescent!=null) this.stochasticDescent.suspend();
    }

    public void resumeStochasticDescent(){
        if (stochasticDescent!=null) this.stochasticDescent.resume();
    }

    /**
     * Do stochastic gradient via stream
     * @param dataList
     * @param learningRate
     */
    public void stochasticDecentFixedSteps(TrainingsDataList<Double> dataList, double learningRate,int steps) {

        long start =System.currentTimeMillis();
        for (int s=0;s<steps;s++) {
            dataList.shuffle();
            int batchSize = (int) Math.max((Math.random() * dataList.size()),1); //vary the batch size in each step

            IntStream.range(0, dataList.size() / batchSize)
                    .mapToObj(i -> dataList.subList(i * batchSize, (i + 1) * batchSize))
                    .forEach(batch -> {
                        this.resetErrors();
                        batch.stream().forEach(data -> this.accumulateErrors(data));//TODO this might not work in parallel
                        //adjust parameters of the network according to the calculated errors for the batch
                        for (int layer = 1; layer < this.getNumberOfLayers(); layer++) {
                            final int l = layer;
                            this.getNeuronsOfLayer(l)
                                    .parallelStream()//todo parallel
                                    .forEach(n -> {
                                        n.bias -= learningRate * n.error / batchSize;
                                        //use neuron stream of the previous layer to stream through the weights//TODO parallel
                                        this.getNeuronsOfLayer(l - 1).stream().forEach(m -> n.weights[m.index] -= learningRate * m.value * n.error / batchSize);
                                    });
                        }

                    });


            //sum all error terms of the biases
            /*
            double errorSum = 0;
            for (int layer=1;layer<getNumberOfLayers();layer++)
                errorSum+=this.getNeuronsOfLayer(layer).stream().mapToDouble(n->Math.abs(n.error)).sum();//todo parallel
            System.out.println("Error sum :"+errorSum/batchSize);
            */
        }
        System.out.println("Mutation with "+steps+" steps in "+(System.currentTimeMillis()-start)+" ms.");

    }

    /**
     * Do stochastic gradient via stream
     * @param dataList
     * @param batchSize
     * @param learningRate
     * @param delta the optimization is finished when the error has dropped to the fraction delta
     */
    public void stochasticGradientDescentStreamDelta(TrainingsDataList<Double> dataList, int batchSize, double learningRate,double delta) {

        double errorSum = 100;
        long steps = 0;
        boolean first = true;
        double firstError = 0;
        long start = System.currentTimeMillis();

        while (errorSum > delta*firstError && steps<1000000) {
            dataList.shuffle();
            int localBatchSize = (int) (Math.random()*batchSize); //vary the batch size in each step

            IntStream.range(0,dataList.size()/batchSize)
                    .mapToObj(i->dataList.subList(i*batchSize,(i+1)*batchSize))
                    .forEach(batch->{
                        this.resetErrors();
                        batch.stream().forEach(data->this.accumulateErrors(data));//TODO this might not work in parallel
                        //adjust parameters of the network according to the calculated errors for the batch
                        for (int layer = 1; layer < this.getNumberOfLayers(); layer++) {
                            final int l = layer;
                            this.getNeuronsOfLayer(l)
                                    .parallelStream()//todo parallel
                                    .forEach(n -> {
                                        n.bias -= learningRate * n.error/batchSize;
                                        //use neuron stream of the previous layer to stream through the weights//TODO parallel
                                        this.getNeuronsOfLayer(l-1).stream().forEach(m->n.weights[m.index]-=learningRate*m.value*n.error/batchSize);
                                    });
                        }

                    });

            //sum all error terms of the biases
            errorSum = 0;
            for (int layer=1;layer<getNumberOfLayers();layer++)
                errorSum+=this.getNeuronsOfLayer(layer).stream().mapToDouble(n->Math.abs(n.error)).sum();//todo parallel

            if (first) {firstError = errorSum;first=false;}

            steps++;
            if (steps % 100 == 0) {
                String infoString = "Error ratio: " + errorSum/firstError+" after "+steps+" steps.";
                System.out.println(infoString+" in "+(System.currentTimeMillis()-start)+" ms.");
                start = System.currentTimeMillis();
            }


            if (steps%1000==0)
                System.out.println("Fitness: "+this.getFitness(dataList));
        }

        System.out.println("Fertig");
    }

    /**
     * Do stochastic gradient via stream
     * @param dataList
     * @param batchSize
     * @param learningRate
     */
    public void stochasticGradientDescentStream(TrainingsDataList<Double> dataList, int batchSize, double learningRate) {
        double sqrBatch = Math.sqrt(batchSize); //variable to adjust for the error growth depending on the batchsize
        double errorSum = 100;
        long steps = 0;



        stochasticDescent = new StochasticDescent(this,dataList,batchSize,learningRate);
        stochasticDescent.start();


        System.out.println("Fertig");
    }


    /**
     * apply the method of stochastic gradient descent to the network
     *
     * @param dataList
     * @param batchSize
     * @param learningRate
     */
    public void stochasticGradientDescent(TrainingsDataList<Double> dataList, int batchSize, double learningRate) {
        double sqrBatch = Math.sqrt(batchSize); //variable to adjust for the error growth depending on the batchsize
        double errorSum = 100;
        long steps = 0;
        while (errorSum > 0.01 && steps<100000) {
            dataList.shuffle();
            errorSum = 0.;
            int dataCounter = 1;
            for (TrainingsData<Double> data : dataList) {
                if (dataCounter == 1)
                   this.resetErrors();
                this.accumulateErrors(data);
                if (dataCounter == batchSize) { //if the batchsize is reached
                    //adjust parameters of the network
                    for (int layer = 1; layer < this.getNumberOfLayers(); layer++) {
                        List<Neuron> previousLayer = this.getNeuronsOfLayer(layer - 1);
                        for (Neuron n : this.getNeuronsOfLayer(layer)) {
                            n.bias -= learningRate * n.error;
                            for (int k = 0; k < this.structure[layer - 1]; k++)
                                n.weights[k] -= learningRate * previousLayer.get(k).value * n.error;
                            errorSum += Math.abs(n.error / sqrBatch);
                        }
                    }
                    dataCounter = 0;//reset dataCounter
                }//if
                dataCounter++;
            }//for

            steps++;
            if (steps % 10000 == 0) {
                String infoString = "Error sum: " + errorSum+" after "+steps+" steps.";
                System.out.println(infoString);
                //if (visualizer != null) visualizer.update();
                if (visualizer!=null) {
                    Runnable update = new Runnable(){
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
                            visualizer.update(infoString);
                        }
                    };
                    update.run();
                }
            }
        }
        System.out.println("Number of steps: " + steps);
    }

    /**
     * apply the method of stochastic gradient descent to the network for a limited amout
     *
     * @param dataList
     * @param learningRate
     * @param limit
     */
    public void stochasticGradientDescentWithLimit(TrainingsDataList<Double> dataList, double learningRate,int limit) {
        int count = 0;
        while (count<limit) {
            dataList.shuffle();
            for (TrainingsData<Double> data : dataList) {
                this.calcErrors(data, true);
                for (int layer = 1; layer < this.getNumberOfLayers(); layer++) {
                    List<Neuron> previousLayer = this.getNeuronsOfLayer(layer - 1);
                        for (Neuron n : this.getNeuronsOfLayer(layer)) {
                            n.bias -= learningRate * n.error;
                            for (int k = 0; k < this.structure[layer - 1]; k++)
                                n.weights[k] -= learningRate * previousLayer.get(k).value * n.error;
                        }
                    }
            }
            count++;
        }
    }

    /**
     * apply the method of stochastic gradient descent to the network
     *
     * @param dataList
     * @param batchSize
     * @param learningRate
     */
    public void stochasticGradientDescent2(TrainingsDataList<Double> dataList, int batchSize, double learningRate) {
        //The idea is to provide the training data bit by bit and always generate a partially trained network
        dataList.shuffle();
        TrainingsDataList<Double> tmpList = new TrainingsDataList<Double>();
        for (TrainingsData<Double> data : dataList) {
            tmpList.add(data);
            stochasticGradientDescent(tmpList, 1, learningRate);
            System.out.println("System trained with " + tmpList.size() + "/" + dataList.size() + " of the data.");
        }
    }

    /**
     * apply the method of stochastic gradient descent to the network
     *
     * @param dataList
     * @param batchSize
     * @param learningRate
     */
    public void stochasticGradientDescentWithAdaptiveLearningRate2(TrainingsDataList<Double> dataList, int batchSize, double learningRate) {
        //The idea is to provide the training data bit by bit and always generate a partially trained network
        dataList.shuffle();
        TrainingsDataList<Double> tmpList = new TrainingsDataList<Double>();
        for (TrainingsData<Double> data : dataList) {
            tmpList.add(data);
            stochasticGradientDescentWithAdaptiveLearningRate(tmpList, batchSize, learningRate);
            System.out.println("System trained with " + tmpList.size() + "/" + dataList.size() + " of the data.");
        }
    }


    /**
     * Calculate the error term for each neuron that it contributes to the total error in the sigmoid function
     * and add it to the existing error
     * @param data
     */
    public void accumulateErrors(TrainingsData<Double> data){
        this.eval(data);

        int lastLayerIndex = this.structure.length - 1;
        int numberOfOutputNeurons = this.structure[lastLayerIndex];

        //z = w*a+b for the output layer
        double[] z = new double[numberOfOutputNeurons];

        Double[] grad = this.cf.gradient(data);


        //accumulation of errors for the last layer
        this.getNeuronsOfLayer(lastLayerIndex).stream().forEach(n->n.error+=grad[n.index] * this.sf.derivative(this.sigmoid.inverse(n.value)));
        /*
        int count=0;
        for (Neuron n : this.getNeuronsOfLayer(lastLayerIndex)) {
            double e = grad[count] * this.sf.derivative(this.sigmoid.inverse(n.value));
            n.error += e;
            count++;
        }
        */

        //back-propagation
        for( int layer = lastLayerIndex - 1; layer > 0; layer--) {
            final int l = layer;
            this.getNeuronsOfLayer(layer).stream()//TODO check parallel advantage
                    .forEach(m -> {
                        double error = this.getNeuronsOfLayer(l + 1)
                                .stream()
                                .mapToDouble(n -> n.error * n.weights[m.index])
                                .sum();
                        error *= this.sf.derivative(this.sf.inverse(m.value));
                        m.error += error;//accumulation of errors
                    });
        }
    }


    /**
     * set error values of all neurons to zero
     */
    public void resetErrors(){
         for (int layer=1;layer<getNumberOfLayers();layer++)
             this.getNeuronsOfLayer(layer).parallelStream().forEach(n->n.error=0);
         /*
         for (int layer = 1; layer < this.structure.length; layer++)
            for (Neuron m : this.getNeuronsOfLayer(layer)) m.error = 0;
          */
    }

    /**
     * Calculate the error term for each neuron that it contributes to the total error in the sigmoid function
     * if reset is true, the error is set to the error value of the neuron
     * otherwise it is added to the error value of the neuron
     *
     * @param data
     */
    public void calcErrors(TrainingsData<Double> data, boolean reset) {
        this.eval(data);

        int lastLayerIndex = this.structure.length - 1;
        int numberOfOutputNeurons = this.structure[lastLayerIndex];

        //z = w*a+b for the output layer
        double[] z = new double[numberOfOutputNeurons];

        Double[] grad = this.cf.gradient(data);
        int count = 0;
        for (Neuron n : this.getNeuronsOfLayer(lastLayerIndex)) {
            double e = grad[count] * this.sf.derivative(this.sigmoid.inverse(n.value));
            if (reset) n.error = e;
            else n.error += e;
            count++;
            // System.out.print(String.format("%.4f ",n.error));
        }
        // System.out.println();

        //back-propagation
        for (int layer = lastLayerIndex - 1; layer > 0; layer--) {
            for (Neuron m : this.getNeuronsOfLayer(layer)) {
                double error = 0.;
                int cli = 0; //current layer index
                for (Neuron n : this.getNeuronsOfLayer(layer + 1))
                    //the errors of the previous layer are in the first position of this.errors
                    error += n.error * n.weights[cli] * this.sf.derivative(this.sf.inverse(m.value));
                if (reset) m.error = error;
                else m.error += error;
                // System.out.print(String.format("%.4f",m.error));
                cli++;
            }
            //System.out.println();
        }
    }

    public int getNumberOfLayers() {
        return this.structure.length;
    }

    public int[] getStructure() {
        return this.structure;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("Neural Network with the following structure:\n");
        out.append("********************************************\n");
        out.append("Input layer with " + this.structure[0] + " neurons:\n");
        out.append("*******************************************************\n");
        out.append(neuronStructure.get(0).toString() + "\n\n");
        for (int layer = 1; layer < this.structure.length; layer++) {
            out.append("Layer " + layer + " with " + this.structure[layer] + " neurons:\n");
            out.append("*******************************************************\n");
            out.append(neuronStructure.get(layer).stream().map(Object::toString).collect(Collectors.joining("\n")) + "\n\n");
        }
        return out.toString();
    }


    /**
     * The Tag is used during the initialization to identify the network uniquely
     * @return
     */
    public String getTag(){
        return this.tag;
    }

    /**
     * calculate the fitness of the network for a given set of data
     * the fitness is used in genetic algorithms
     * @param dataList
     * @return
     */
    public double getFitness(TrainingsDataList<Double> dataList){
        if (this.fitness==null)
            this.fitness = dataList.stream().map(this::eval).mapToDouble(this.cf::eval).sum();
        return fitness;
    }

    /**
     * manually update the fitness of the network if the fitness has changed
     * an automatic update is not performed since it can be very expensive and should only be done if necessary
     * after mutation etc.
     * @param dataList
     */
    public void updateFitness(TrainingsDataList<Double> dataList){
        this.fitness = dataList.stream().map(this::eval).mapToDouble(this.cf::eval).sum();
    }

    public static SigmoidFunction sigmoid = new SigmoidFunction() {
        @Override
        public double eval(double x) {
            return 1. / (1 + Math.exp(-x));
        }

        @Override
        public double derivative(double x) {
            return sigmoid.eval(x) * (1 - sigmoid.eval(x));
        }

        @Override
        public double inverse(double x) {
            return -Math.log(1. / x - 1);
        }
    };


    /**
     * The standard cost function returning the sum of the squares of the differences between
     * activations and expectations
     */
    public static CostFunction<Double> standardCostFunction = new CostFunction<Double>() {

        @Override
        public Double eval(TrainingsData<Double> data) {
            double sum = 0.;
            Double[] activations = data.getActivations();
            Double[] expectations = data.getExpectation();

            for (int i = 0; i < activations.length; i++)
                sum += (activations[i] - expectations[i]) * (activations[i] - expectations[i]);
            return sum;
        }

        @Override
        public Double[] gradient(TrainingsData<Double> data) {
            Double[] activations = data.getActivations();
            Double[] expectations = data.getExpectation();
            Double[] grad = new Double[activations.length];
            for (int i = 0; i < activations.length; i++)
                grad[i] = 2 * (activations[i] - expectations[i]);
            return grad;
        }
    };

    public static CostFunction<Double> crossEntropy = new CostFunction<Double>() {

        @Override
        public Double eval(TrainingsData<Double> data) {
            double cost = 0.;
            Double[] activations = data.getActivations();
            Double[] expectations = data.getExpectation();
            for (int a = 0; a < activations.length; a++)
                cost = (expectations[a]+EPS) * Math.log(activations[a]+EPS) + (1. - expectations[a]+EPS) * Math.log(1 - activations[a]+EPS);
            return -cost;
        }

        @Override
        public Double[] gradient(TrainingsData<Double> data) {
            Double[] activations = data.getActivations();
            Double[] expectations = data.getExpectation();
            Double[] grad = new Double[activations.length];
            for (int a = 0; a < activations.length; a++)
                grad[a] = (1. - expectations[a]+EPS) / (1 - activations[a]+EPS) - (expectations[a]+EPS) / (activations[a]+EPS);
            return grad;
        }

    };



}
