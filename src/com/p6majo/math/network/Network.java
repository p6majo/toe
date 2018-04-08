package com.p6majo.math.network;

import com.p6majo.math.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * A network is a List of {@link Layer}
 * Each {@link Layer} itself is a list of {@link Neuron}s<br>
 *
 * Each {@Neuron} of one {@link Layer} is connected to each Neuron of the preceding layer.
 * The strength of this connection is represented by a weight value. Moreover, each neuron consists of a bias value,
 * which shifts the sensitivity of the neuron with respect to activations transmitted by the weights.<br>
 *
 * With this structure, a set of signals perceived by the neurons of the first layer are processed through the entire network
 * and result in a pattern of activations in the last layer.<br>
 *
 * The network can be trained by {@link Data} to match a given set of input signals
 * with an expected set of activations in the last layer.<br>
 *
 * @author jmartin
 * @version 1.0
 * @data 3.4.18
 *
 */
public class Network {

    //cutoff for zero activations to prevent log-overflow in the cross-entropy cost function
    private static final double EPS = 1e-9;

    /**
     * different flags to determine the initial values of the neurons
     */
    public static enum Seed {NO_SEED, RANDOM, NO_BIAS, NO_BIAS_UNITY};

    /**
     * a list of layers
     * so far layers are just a list of neurons
     */
    private final List<Layer> neuronStructure;

    /**
     * an array of integer numbers that provides the number of neurons in each layer
     */
    private final int[] signature;

    // private Structure network;
    private final SigmoidFunction sf;
    protected final CostFunction cf;

    //private NetworkVisualizer visualizer;
    private final NetworkVisualizer visualizer;
    private final boolean visual;

    private StochasticGradientDescentRunnable stochasticGradientDescentRunnable = null;

    private DataList trainingData;
    private DataList testData;


    /**
     * short Constructor of the network
     * default is {@link #crossEntropy} cost function and the {@link NetworkVisualizer} is deactivated
     * @param signature layer signature of the network
     * @param seed seeding of the initial values
     * @param sf sigmoid function
     */
    public Network(int[] signature, Seed seed, SigmoidFunction sf, CostFunction cf) {
        this(signature,seed,sf,crossEntropy,false);
    }

    /**
     * extended version of the Constructor that allows for a user defined cost function and provides a flag
     * to activate the {@link NetworkVisualizer}
     * @param signature layer signature of the network
     * @param seed seeding of the initial values
     * @param sf sigmoid function
     * @param cf cost function
     * @param visual flag for visualization
     */
    public Network(int[] signature, Seed seed, SigmoidFunction sf, CostFunction cf, boolean visual) {

        this.signature = signature;

        this.neuronStructure = new ArrayList<Layer>();

        //add input layer
        Layer inputLayer = new Layer();
        for (int n = 0; n < signature[0]; n++)
            inputLayer.add(new InputNeuron(0.,n));
        this.neuronStructure.add(inputLayer);

        //add remaining layers
        for (int layer = 1; layer < signature.length; layer++) {
            Layer layerList = new Layer();
            for (int n = 0; n < signature[layer]; n++) {
                //each neuron is equipped with a number of weights that correspond to
                //the number of neurons of the previous layer
                layerList.add(new Neuron(seed, this.signature[layer - 1],n));
            }
            neuronStructure.add(layerList);
        }

        this.sf = sf;
        this.cf = cf;
        this.visual = visual;

        if (visual) {
            System.out.println("init visualizer");
            visualizer= new NetworkVisualizer(this,NetworkVisualizer.VisualizerModus.TRAINED_EDGES,1);
            System.out.println("visualizer initialized");
        }
        else visualizer = null;

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
        for (int l = 1; l < signature.length; l++) neurons += signature[l];
        return neurons;
    }

    public List<Neuron> getListOfDynamicNeurons() {
        //return this.neuronStructure.stream().flatMap(List::stream).filter(Neuron.dynamicTest).collect(Collectors.toList());
        //improved version
        //the neurons of all layers except the input layer are collected into a new list
        return this.neuronStructure.stream().skip(1).flatMap(List::stream).collect(Collectors.toList());
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

        System.out.println("Fertig");
    }


    public void suspendStochasticDescent(){
        if (stochasticGradientDescentRunnable !=null) this.stochasticGradientDescentRunnable.suspend();
    }

    public void resumeStochasticDescent(){
        if (stochasticGradientDescentRunnable !=null) this.stochasticGradientDescentRunnable.resume();
    }

    public void performSingleStep(){
        if (stochasticGradientDescentRunnable !=null)
            if (stochasticGradientDescentRunnable.isSuspended())
                this.stochasticGradientDescentRunnable.singleStep();
    }


    /**
     * Evaluate the network for a given piece of {@link Data}
     * The output of the network is stored in the activations of {@code data}
     * @param data piece of {@link Data}
     * @return the piece of {@link Data}
     */
    protected Data feedForward(Data data) {
        Double[] input = data.getInput();

        if (input.length != this.signature[0])
            Utils.errorMsg("The input data does not match with the input layer of the network.");
        for (int i = 0; i < input.length; i++) {
            this.neuronStructure.get(0).get(i).value = input[i];
        }

        int lastLayerIndex = this.signature.length - 1;
        int numberOfOutputNeurons = this.signature[lastLayerIndex];
        Double[] out = new Double[numberOfOutputNeurons];

        for (int l = 1; l < this.signature.length; l++) {
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
        data.setActivations(out);
        return data;
    }


    /**
     * Calculate for a given piece of {@link Data} the error term for each neuron
     * that it contributes to the total error in the sigmoid function<br>
     *
     * This method also contains the heart of the neural network, the back-propagation of the error
     * and the adjustment of the weights and biases of each dynamic neuron to reduce the error of the network
     * for the given piece of data.
     *
     * @param data
     */
    public void accumulateErrors(Data data){

        //calculate the output of the network for a given piece of data
        //the output is stored in the Data
        this.feedForward(data);

        int lastLayerIndex = this.signature.length - 1;
        int numberOfOutputNeurons = this.signature[lastLayerIndex];

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
         for (int layer = 1; layer < this.signature.length; layer++)
            for (Neuron m : this.getNeuronsOfLayer(layer)) m.error = 0;
          */
    }

    /**
     * returns the number of layers of the network
     * @return
     */
    public int getNumberOfLayers() {
        return this.signature.length;
    }

    /**
     * returns the signature of the network
     * @return
     */
    public int[] getSignature() {
        return this.signature;
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
    public static CostFunction standardCostFunction = new CostFunction() {

        @Override
        public Double eval(Data data) {
            double sum = 0.;
            Double[] activations = data.getActivations();
            Double[] expectations = data.getExpectations();

            for (int i = 0; i < activations.length; i++)
                sum += (activations[i] - expectations[i]) * (activations[i] - expectations[i]);
            return sum;
        }

        @Override
        public Double[] gradient(Data data) {
            Double[] activations = data.getActivations();
            Double[] expectations = data.getExpectations();
            Double[] grad = new Double[activations.length];
            for (int i = 0; i < activations.length; i++)
                grad[i] = 2 * (activations[i] - expectations[i]);
            return grad;
        }
    };

    /**
     * The cross entropy function generically provides fast learning independent of the state of the neuron
     */
    public static CostFunction crossEntropy = new CostFunction() {

        @Override
        public Double eval(Data data) {
            double cost = 0.;
            Double[] activations = data.getActivations();
            Double[] expectations = data.getExpectations();
            for (int a = 0; a < activations.length; a++)
                cost = (expectations[a]+EPS) * Math.log(activations[a]+EPS) + (1. - expectations[a]+EPS) * Math.log(1 - activations[a]+EPS);
            return -cost;
        }

        @Override
        public Double[] gradient(Data data) {
            Double[] activations = data.getActivations();
            Double[] expectations = data.getExpectations();
            Double[] grad = new Double[activations.length];
            for (int a = 0; a < activations.length; a++)
                grad[a] = (1. - expectations[a]+EPS) / (1 - activations[a]+EPS) - (expectations[a]+EPS) / (activations[a]+EPS);
            return grad;
        }

    };

    public String runVerboseTest(int counts){
        if (stochasticGradientDescentRunnable.isTestable()) {
            StringBuilder result = new StringBuilder();
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
                result.append("Expectation: " + expectation + " heighest prop: " + prop + " for output: " + out + "\n");
            }
            result.append("Rate: " + ((double) correct / counts));
            return result.toString();
        }
        return "";
    }


    public double runTest(){
        return this.runTest(testData.size());
    }

    /**
     * run counts tests and return the rate of success
     * @param counts
     * @return
     */
    public double runTest(int counts){
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
        return -1.;
    }

    /**
     * outputs the full state of the network
     * @return
     */
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("Neural Network with the following signature:\n");
        out.append("********************************************\n");
        out.append("Input layer with " + this.signature[0] + " neurons:\n");
        out.append("*******************************************************\n");
        out.append(neuronStructure.get(0).toString() + "\n\n");
        for (int layer = 1; layer < this.signature.length; layer++) {
            out.append("Layer " + layer + " with " + this.signature[layer] + " neurons:\n");
            out.append("*******************************************************\n");
            out.append(neuronStructure.get(layer).stream().map(Object::toString).collect(Collectors.joining("\n")) + "\n\n");
        }
        return out.toString();
    }

}
