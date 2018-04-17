package com.p6majo.math.network2;


import com.p6majo.math.network.DataList;
import com.p6majo.math.network.NetworkVisualizer;
import com.p6majo.math.network2.layers.*;
import com.p6majo.math.utils.Utils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.p6majo.math.network2.Network.Test.MAX_PROBABILITY;

/**
 * A network is a List of {@link Layer}
 *
 * @author jmartin
 * @version 2.0
 * @data 10.4.18
 *
 * TODO Check whether setup is consistent,i.e. layer dimensions match, loss layer at the end, at least one dynamic layer, etc.
 * TODO find bottle necks, performance is less than expected
 *
 */
public class Network {

     /**
     * different flags to determine the initial values of the neurons
     */
    public static enum Seed {NO_SEED, RANDOM, NO_BIAS, NO_BIAS_UNITY,ALL_UNITY};
    public static enum Test {MAX_PROBABILITY};

    private final List<Layer> layers;
    //private final List<INDArray> trainableParameters;

    private static Random random = null;

    private  NetworkVisualizer2 visualizer;
    private boolean firstTrainRun = true;
    private final boolean visual;


    /**
     *
     */
    public Network(boolean visual) {

        layers = new ArrayList<Layer>();
        //trainableParameters= new ArrayList<INDArray>();
        this.visual = visual;
    }

    public int getNumberOfLayers(){
        return this.layers.size();
    }

    public int getNumberOfDynamicLayers(){
        int dynLayers = 0;
        for (Layer layer:this.layers) if (layer instanceof DynamicLayer) dynLayers++;
        return dynLayers;
    }

    public List<DynamicLayer> getDynamicLayers(){
        List<DynamicLayer> dynLayers = new ArrayList<DynamicLayer>();
        for (Layer layer:layers) if (layer instanceof DynamicLayer) dynLayers.add((DynamicLayer) layer);
        return dynLayers;
    }

    public void addLayer(Layer layer){
        int index = this.layers.size();
        layer.setLayerIndex(index);
        this.layers.add(layer);

        /*
        if (layer instanceof DynamicLayer){
            this.trainableParameters.addAll(((DynamicLayer) layer).getTrainableParameters());
        }
        */
    }


    public List<Layer> getVisualizableLayers(){
        List<Layer> visLayers = new ArrayList<Layer>();
        for (Layer layer:layers) if (layer instanceof Visualizable) visLayers.add(layer);
        return visLayers;
    }

    /*
    public void addTrainableParameter(List<INDArray> params){
        this.trainableParameters.addAll(params);
    }*/

    public void train(Data[] data,Data[] test,int batchSize){


        if (visual && firstTrainRun) {
            System.out.println("init visualizer");
            visualizer= new NetworkVisualizer2(this,NetworkVisualizer2.VisualizerModus.TRAINED_EDGES,1);
            firstTrainRun = false;
        }
        else visualizer = null;

        shuffle(data);

        /*
        //Gradient check
        Batch batch = new Batch(data[0]);
        System.out.println(this.gradientCheck(batch, 0, 1));
        System.out.println(layers.get(0).getErrors());
        System.out.println("Loss gradient: "+((LossLayer) layers.get(2)).getLossGradient());
        System.out.println("Activations of sigmoid: "+layers.get(1).getActivations());
        */

        /*
        //no gain in efficiency
        if (batchSize==1){
            for (int i=0;i<data.length;i++){
                Batch batch  =new Batch(data[i]);
                pushforward(batch);
                pullBack();
                learn();
            }
        }
        else {
            */

        IntStream.iterate(0,i->i+batchSize)
                .limit(data.length)
                .boxed()//.parallel() should not be used, leads to backpropagation errorsS
                .forEach(i->{
                    Data[] batchData = Arrays.copyOfRange(data, i, i + batchSize);
                    Batch batch = new Batch(batchData);
                    pushforward(batch);
                    pullBack();
                    learn();}
                    );

        /*
            for (int i = 0; i + batchSize <= data.length; i += batchSize) {
                Data[] batchData = Arrays.copyOfRange(data, i, i + batchSize);
                Batch batch = new Batch(batchData);
                pushforward(batch);
                pullBack();
                learn();
            }
            */
       // }
    }


     private static void shuffle(Data[] array) {

        if (random == null) random = new Random();
        int count = array.length;
        for (int i = count; i > 1; i--)
            swap(array, i - 1, random.nextInt(i));
    }

    private static void swap(Data[] array, int i, int j) {
        Data temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private int getFullDimension(int[]shape){
        int dim = 1;
        for (int d=0;d<shape.length;d++) dim*=shape[d];
        return dim;
    }

    /**
     * returns the loss layer of the network, which should be the last layer be default
     * @return
     */
    public LossLayer getLossLayer(){
        int lastPos = layers.size()-1;
        if (layers.get(lastPos) instanceof LossLayer){
            return (LossLayer) layers.get(lastPos);
        }
        else return null;
    }

    /**
     * This is an auxiliary method that allows to compute the gradient for parameters of dynamical layers directly from
     * two forward passes for each parameter
     * The method is very buggy and is not generic for all tensor like parameters
     * @param batch
     * @param layer the layer under consideration
     * @param param the trainable parameter
     * @return
     */
    public String gradientCheck(Batch batch,int layer,int param){
        //TODO only take first batch element, if many are provided

        DynamicLayer dynLayer = null;
        if (this.layers.get(layer) instanceof DynamicLayer){
            dynLayer = (DynamicLayer) this.layers.get(layer);

            float epsilon = 1.e-3f;
            List<INDArray> trainableParams = dynLayer.getTrainableParameters();

            INDArray params  = trainableParams.get(param);

            int[] shape = params.shape();
            int dim = getFullDimension(shape);

            LossLayer lossLayer = (LossLayer) (this.layers.get(layers.size() - 1));

            StringBuilder out = new StringBuilder();

            out.append("calculated gradients:\n[");

            float[] gradient = new float[dim];
            for (int i=0;i<dim;i++) {
                float[] perturbationData = new float[dim];
                perturbationData[i] = epsilon;

                INDArray perturbations = Nd4j.create(perturbationData, params.shape());

                batch.resetBatch();
                pushforward(batch);


                float loss = lossLayer.getLoss();

                //shift
                params.addi(perturbations);

                batch.resetBatch();
                pushforward(batch);

                float loss2 = lossLayer.getLoss();
                gradient[i] = (loss2 - loss) / epsilon;
                String gradientString = String.format("%.2f",gradient[i]);

                out.append(gradientString + " ");
                if ( (i+1)%16==0) out.append("\n");
                //undo shift
                params.subi(perturbations);
            }
            out.append("]\n");

            //calculate gradients
            pullBack();
            String errors = dynLayer.getDetailedErrors();

            if (param==0) {
                INDArray weightErrors = ((LinearLayer) dynLayer).getWeightCorrections();
                INDArray numGradient = Nd4j.create(gradient, weightErrors.shape());

                INDArray diff = weightErrors.sub(numGradient);
                float maxDiff = Nd4j.max(diff).getFloat(0, 0);
                float minDiff = Nd4j.min(diff).getFloat(0, 0);
                out.append("Pullback of errors:\n" + errors + "\nmaximum deviation: " + maxDiff + " or " + minDiff);
            }

            return out.toString();
        }
        else Utils.errorMsg("The provided layer index "+layer+" does not correspond to a dynamic layer.\n It belongs to "+this.layers.get(layer).toShortString()+" instead.");
        return null;
    }

    public TestResult test(Data[] data){
        return test(data,data.length);
    }

    public TestResult test(Data[] data, int numberOfTests){
        List<Integer> intList = IntStream.range(0,data.length).boxed().collect(Collectors.toList());
        Collections.shuffle(intList);

        //convert random sample into batch
        Data[] dataTest = new Data[numberOfTests];
        for (int n =0;n<numberOfTests;n++) dataTest[n]=data[intList.get(n)];
        Batch testBatch = new Batch(dataTest);

        pushforward(testBatch);
        //get activations of the last layer and compare with the expectations
        LossLayer lastLayer =  (LossLayer) layers.get(layers.size()-1);
        return lastLayer.getTestResult(MAX_PROBABILITY);
    }

    private void pushforward(Batch batch){
       // System.out.println("input: "+batch.getActivations());
        for (int l=0;l<layers.size();l++){
            layers.get(l).pushForward(batch);
           // System.out.println("after layer "+l+": "+layers.get(l).getActivations());
        }
    }

    private void pullBack(){
        LossLayer last = (LossLayer) layers.get(layers.size()-1);
        //System.out.println("Loss: "+last.getLoss());
        last.pullBack();
        for (int l=layers.size()-2;l>-1;l--)
            layers.get(l).pullBack(layers.get(l+1).getErrorsForPreviousLayer());
    }

    private void learn(){

        for (int l =0;l<layers.size();l++)
            layers.get(l).learn(0.01f);
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
