package com.p6majo.mnist;

import com.p6majo.math.network.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MNISTVisualization {

    private static DataList dataList= new DataList();
    private static DataList testList= new DataList();


    private static Function<int[][],Data> converter = new Function<int[][], Data>() {
        @Override
        public Data apply(int[][] ints) {
            Double[] input = new Double[28*28];
            for (int width=0;width<28;width++)
                for (int height=0;height<28;height++)
                    input[height+width*28]=(double) ints[width][height]/255.;
            Double[] expectations = new Double[10];
            Data data = new Data(input,expectations) ;
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
        dataList.addAll(images.stream().map(converter).collect(Collectors.toList()));
        //add the expectations
        IntStream.range(0,dataList.size()).boxed().forEach(i->dataList.get(i).setExpectation(labels[i]));
        System.out.println("Time for conversion: "+(System.currentTimeMillis()-start)+" ms.");

        LABEL_FILE = "/home/p6majo/workbase/toe/src/com/p6majo/mnist/t10k-labels-idx1-ubyte";
        IMAGE_FILE = "/home/p6majo/workbase/toe/src/com/p6majo/mnist/t10k-images-idx3-ubyte";

        start = System.currentTimeMillis();

        labels2 = MNISTDataProvider.getLabels(LABEL_FILE);
        images = MNISTDataProvider.getImages(IMAGE_FILE);

        System.out.println("Parsing of the training data took "+(System.currentTimeMillis()-start)+" ms.");

        start = System.currentTimeMillis();
        System.out.println("Now conversion into TrainingsData ...");
        testList.addAll(images.stream().map(converter).collect(Collectors.toList()));
        //add the expectations
        IntStream.range(0,testList.size()).boxed().forEach(i->testList.get(i).setExpectation(labels2[i]));
        System.out.println("Time for conversion: "+(System.currentTimeMillis()-start)+" ms.");

    }


    public static void main(String[] args){
       Network network = new Network(new int[]{28*28,40,20,10},Network.Seed.RANDOM,Network.sigmoid,Network.crossEntropy,true);

        generateData();


       long start = System.currentTimeMillis();
        network.stochasticGradientDescent(dataList,testList, 1,0.01,8);
        network.stochasticGradientDescent(dataList,testList, 1,0.001,400);

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
       System.out.println("Time: "+(System.currentTimeMillis()-start));

    }

}
