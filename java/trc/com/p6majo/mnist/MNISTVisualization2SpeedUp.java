package com.p6majo.mnist;

import com.p6majo.math.network2.Data;
import com.p6majo.math.network2.Network;
import com.p6majo.math.network2.TestResult;
import com.p6majo.math.network2.layers.*;
import org.nd4j.linalg.factory.Nd4j;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class MNISTVisualization2SpeedUp {

    private static Data[] dataList;
    private static Data[] testList;


    private static Function<int[][],Data> converter = new Function<int[][], Data>() {
        @Override
        public Data apply(int[][] ints) {
            float[][] input = new float[28][28];
            for (int width=0;width<28;width++)
                for (int height=0;height<28;height++)
                    input[width][height]=(float) (ints[width][height]/255.);
            float[] expectations = new float[10];
            Data data = new Data(Nd4j.create(input),Nd4j.create(expectations));
            return data;
        }
    };

    private static void generateData(){

        final int[] labels;
        final int[] labels2;
        List<int[][]> images;

        String LABEL_FILE = "/home/p6majo/workbase/toe/src/com/p6majo/mnist/train-labels-idx1-ubyte";
        String IMAGE_FILE = "/home/p6majo/workbase/toe/src/com/p6majo/mnist/train-images-idx3-ubyte";

        long start = System.currentTimeMillis();

        labels = MNISTDataProvider.getLabels(LABEL_FILE);
        images = MNISTDataProvider.getImages(IMAGE_FILE);

        System.out.println("Parsing of the training data took "+(System.currentTimeMillis()-start)+" ms.");

        start = System.currentTimeMillis();
        System.out.println("Now conversion into TrainingsData ...");
        dataList= images.stream().map(converter).toArray(Data[]::new);
        //add the expectations
        IntStream.range(0,dataList.length).boxed().forEach(i->dataList[i].setExpectationToUnity(labels[i]));
        System.out.println("Time for conversion: "+(System.currentTimeMillis()-start)+" ms.");

        LABEL_FILE = "/home/p6majo/workbase/toe/src/com/p6majo/mnist/t10k-labels-idx1-ubyte";
        IMAGE_FILE = "/home/p6majo/workbase/toe/src/com/p6majo/mnist/t10k-images-idx3-ubyte";

        start = System.currentTimeMillis();

        labels2 = MNISTDataProvider.getLabels(LABEL_FILE);
        images = MNISTDataProvider.getImages(IMAGE_FILE);

        System.out.println("Parsing of the training data took "+(System.currentTimeMillis()-start)+" ms.");

        start = System.currentTimeMillis();
        System.out.println("Now conversion into TrainingsData ...");
        testList=images.stream().map(converter).toArray(Data[]::new);
        //add the expectations
        IntStream.range(0,testList.length).boxed().forEach(i->testList[i].setExpectationToUnity(labels2[i]));
        System.out.println("Time for conversion: "+(System.currentTimeMillis()-start)+" ms.");

    }


    public static void main(String[] args){

        Network network = new Network(false);

        FlattenLayer flat = new FlattenLayer(new int[]{28,28});

        LinearLayer ll = new LinearLayer(784,20);
        network.addLayer(ll);


        SigmoidLayer sig = new SigmoidLayer(new int[]{20});
        network.addLayer(sig);



        LinearLayer ll2 = new LinearLayer(20,10);
        network.addLayer(ll2);


        SigmoidLayer sig2 = new SigmoidLayer(new int[]{10});
        network.addLayer(sig2);

        CrossEntropyLayer cel = new CrossEntropyLayer(new int[]{10});
        network.addLayer(cel);

        //L2Layer l2 = new L2Layer(new int[]{10});
        //network.addLayer(l2);

        generateData();
       // System.out.println(testList[0].toString());



        //System.out.println(network.gradientCheck(batch, 0, 0));



        TestResult test = network.test(testList);
       System.out.println("Success rate before: "+test.getSuccessRate());

        long start = System.currentTimeMillis();
        network.train(dataList, testList, 1,1,100);
        System.out.println("Time: " + (System.currentTimeMillis() - start));


        //network.stochasticGradientDescent(dataList,testList, 1,0.01,8);
        //network.stochasticGradientDescent(dataList,testList, 1,0.001,400);

       /*
       int batchSize = 100;
       double learningRate =0.1;
       network.stochasticGradientDescent(dataList,testList, batchSize,learningRate,400);
        network.stochasticGradientDescent(dataList,testList, batchSize,learningRate/2,2);
        network.stochasticGradientDescent(dataList,testList, batchSize,learningRate/4,4);
        network.stochasticGradientDescent(dataList,testList, batchSize,learningRate/8,8);
        network.stochasticGradientDescent(dataList,testList, batchSize,learningRate/16,10);
        network.stochasticGradientDescent(dataList,testList, batchSize,learningRate/32,10);
        */


    }

}