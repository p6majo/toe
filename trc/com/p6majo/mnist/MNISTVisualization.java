package com.p6majo.mnist;

import com.intellij.history.core.StreamUtil;
import com.p6majo.math.network.*;
import com.sun.xml.internal.ws.util.StreamUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MNISTVisualization {

    private static DoubleTrainingsDataList dataList= new DoubleTrainingsDataList();
    private static int[] trainLabels;
    private static List<int[][]> trainImages;

    private static Function<int[][],DoubleTrainingsData> converter = new Function<int[][], DoubleTrainingsData>() {
        @Override
        public DoubleTrainingsData apply(int[][] ints) {
            Double[] input = new Double[28*28];
            for (int width=0;width<28;width++)
                for (int height=0;height<28;height++)
                    input[height+width*28]=(double) ints[width][height]/255.;
            Double[] expectations = new Double[10];
            DoubleTrainingsData data = new DoubleTrainingsData(input,expectations) ;
            return data;
        }
    };

    private static void generateTrainingsData(){

        String LABEL_FILE = "/home/p6majo/workbase/toe/src/com/p6majo/mnist/train-labels-idx1-ubyte";
        String IMAGE_FILE = "/home/p6majo/workbase/toe/src/com/p6majo/mnist/train-images-idx3-ubyte";

        long start = System.currentTimeMillis();

        trainLabels = MNISTDataProvider.getLabels(LABEL_FILE);
        trainImages = MNISTDataProvider.getImages(IMAGE_FILE);

        System.out.println("Parsing of the training data took "+(System.currentTimeMillis()-start)+" ms.");

        start = System.currentTimeMillis();
        System.out.println("Now conversion into TrainingsData ...");
        dataList.addAll(trainImages.stream().limit(20000).map(converter).collect(Collectors.toList()));
        //add the expectations
        IntStream.range(0,dataList.size()).boxed().forEach(i->dataList.get(i).setExpectation(trainLabels[i]));
        System.out.println("Time for conversion: "+(System.currentTimeMillis()-start)+" ms.");
    }


    public static void main(String[] args){
       Network network = new Network(new int[]{28*28,16,10},Network.Seed.RANDOM,Network.sigmoid,Network.standardCostFunction,false);

        generateTrainingsData();


       long start = System.currentTimeMillis();
        network.stochasticGradientDescentStream(dataList,20,0.01*Network.ETA);
        System.out.println("Time: "+(System.currentTimeMillis()-start));

    }

}
