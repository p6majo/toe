package com.p6majo.math.network2;


import com.p6majo.math.network2.layers.*;
import com.p6majo.math.utils.Utils;

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

    public static boolean LEARNING = true;

    private final List<Layer> layers;
    //private final List<INDArray> trainableParameters;

    private static Random random = null;

    private  NetworkVisualizer2 visualizer;
    private boolean firstTrainRun = true;
    private final boolean visual;

    public float lambdaW;
    public float lambdaB;
    private boolean isRegularized = false;
    private float learningRate = 0.01f;

    private LossLayer lossLayer =null;

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

    public Layer getLayer(int index){
        return this.layers.get(index);
    }

    public void addLayer(Layer layer){
        int index = this.layers.size();
        layer.setLayerIndex(index);
        this.layers.add(layer);

        if (layer instanceof DynamicLayer)
            ((DynamicLayer) layer).setRegularization(lambdaW,lambdaB);

        if (layer instanceof LossLayer){
            if (this.lossLayer!=null) Utils.errorMsg("Only one loss layer per network possible");
            else this.lossLayer = (LossLayer) layer;
        }
    }


    /**
     * returns the loss layer of the network, which should be the last layer be default
     * @return
     */
    public LossLayer getLossLayer(){
       return this.lossLayer;
    }

    public void setRegularization(float lambdaW,float lambdaB){
        this.lambdaW = lambdaW;
        this.lambdaB = lambdaB;
        if (this.lambdaW !=0f||this.lambdaB!=0f){
            this.isRegularized = true;
            for (DynamicLayer dynamicLayer:this.getDynamicLayers())
                dynamicLayer.setRegularization(lambdaW,lambdaB);
        }
    }

    public void setLearningRate(float learningRate){
        this.learningRate=learningRate;
    }

    public boolean isRegularized() {
        return isRegularized;
    }

    public List<Layer> getVisualizableLayers(){
        List<Layer> visLayers = new ArrayList<Layer>();
        for (Layer layer:layers) if (layer instanceof Visualizable) visLayers.add(layer);
        return visLayers;
    }


    /**
     * train the network
     * @param data
     * @param testData
     * @param batchSize
     * @param trainingsRuns
     * @param percent
     */
    public void train(Data[] data,Data[] testData,int batchSize,int trainingsRuns, int percent){
        long start = System.currentTimeMillis();
        if (visual ){
                System.out.println("init visualizer");
                visualizer = new NetworkVisualizer2(this, NetworkVisualizer2.VisualizerModus.TRAINED_EDGES, 1);
                visualizer.setNumberOfRuns(trainingsRuns);
                visualizer.setNumberOfStepsPerRun(data.length);
        }
        else visualizer = null;

        for (int run = 0; run < trainingsRuns; run++) {
            LEARNING = true;


            shuffle(data);

            int finalRun = run;
            IntStream.iterate(0, i -> i + batchSize)
                    .limit(data.length/batchSize)
                    .boxed()//.parallel() should not be used, leads to backpropagation errorsS
                    .forEach(i -> {
                                Data[] batchData = Arrays.copyOfRange(data, i, i + batchSize);
                                Batch batch = new Batch(batchData);
                                pushforward(batch);
                                pullBack();
                                learn();
                                if (visual) {
                                    visualizer.setProgressOfCurrentRun(i);
                                    long time = (System.currentTimeMillis()-start)/1000;
                                    long estimate = time * trainingsRuns*data.length/(i+1+ finalRun *data.length);
                                    visualizer.showInfo(time+" s von ca. "+estimate+" s.");
                                }
                            }
                    );

            LEARNING = false;
            TestResult test = this.test(testData,percent*testData.length/100);
            if (visual) visualizer.setOverallProgress(run+1);
            System.out.println("Success rate after "+(run+1)+"th run: " + test.getSuccessRate());
        }
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


    /**
     * TODO find a good place for this auxiliary method
     * @param shape
     * @return
     */
    public static int getFullDimensionOfParameter(int[]shape){
        int dim = 1;
        for (int d=0;d<shape.length;d++) dim*=shape[d];
        return dim;
    }


    public TestResult test(Data[] data){
        LEARNING = false;
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

        return lossLayer.getTestResult(MAX_PROBABILITY);
    }

    public void pushforward(Batch batch){
       // System.out.println("input: "+batch.getActivations());
        for (int l=0;l<layers.size();l++){
            layers.get(l).pushForward(batch);
           // System.out.println("after layer "+l+": "+layers.get(l).getActivations());
        }
    }

    public void pullBack(){
        LossLayer last = (LossLayer) layers.get(layers.size()-1);
        //System.out.println("Loss: "+last.getLoss());
        last.pullBack();
        for (int l=layers.size()-2;l>-1;l--)
            layers.get(l).pullBack(layers.get(l+1).getErrorsForPreviousLayer());
    }

    private void learn(){
        List<DynamicLayer> layers = this.getDynamicLayers();
        for (DynamicLayer layer:layers)
            layer.learn(this.learningRate);
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
