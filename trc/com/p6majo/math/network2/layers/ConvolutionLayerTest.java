package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.network2.Data;
import com.p6majo.math.network2.GradientChecker;
import com.p6majo.math.network2.Network;
import org.junit.Before;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.rng.distribution.impl.NormalDistribution;
import org.nd4j.linalg.factory.Nd4j;

import static org.junit.Assert.*;

public class ConvolutionLayerTest {

    private Batch batch;



    @Before
    public void setUpBatch(){
        INDArray inputData1 = Nd4j.zeros(new int[]{3,6,6});
        for (int i = 0; i < 5; i++) {
            int d = (int) (Math.random()*3);
            int x = (int) (Math.random()*6);
            int y = (int) (Math.random()*6);
            inputData1.getRow(d).put(x,y,1);
        }
        INDArray inputData2 = Nd4j.zeros(new int[]{3,6,6});
        for (int i = 0; i < 5; i++) {
            int d = (int) (Math.random()*3);
            int x = (int) (Math.random()*6);
            int y = (int) (Math.random()*6);
            inputData2.getRow(d).put(x,y,1);
        }
        Data data1 = new Data(inputData1,Nd4j.ones(3));
        Data data2 = new Data(inputData2,Nd4j.ones(3));
        batch = new Batch(data1,data2);
    }

    @Test
    public void pushForward() {
        ConvolutionLayer convLayer = new ConvolutionLayer(new int[]{3,6,6},5,3,2,1,1,0,0,Network.Seed.NO_BIAS);
        System.out.println(convLayer.toString());

        convLayer.pushForward(batch);
        System.out.println("batch:\n"+batch.toString());
        System.out.println("output:\n"+convLayer.getActivations());
    }

    @Test
    public void pullBack() {
        int outDepth = 2;
        int inDepth = 3;
        int inWidth = 6;
        int inHeight= 6;
        int kernelWidth = 5;
        int kernelHeight= 3;
        int paddingWidth=0;
        int paddingHeight=0;

        ConvolutionLayer convLayer = new ConvolutionLayer(new int[]{inDepth,inHeight,inWidth},kernelWidth,kernelHeight,outDepth,1,1,paddingWidth,paddingHeight,Network.Seed.NO_BIAS);
        System.out.println(convLayer.toString());

        convLayer.pushForward(batch);
        System.out.println("batch:\n"+batch.toString());
        System.out.println("output:\n"+convLayer.getActivations());


        int outWidth = (inWidth-kernelWidth+2*paddingWidth)/1+1;
        int outHeight = (inHeight-kernelHeight+2*paddingHeight)/1+1;

        INDArray errors  = Nd4j.ones(new int[]{batch.getBatchSize(),outDepth,outHeight,outWidth});
        convLayer.pullBack(errors);
        System.out.println("pullback:\n"+convLayer.getErrorsForPreviousLayer());
        System.out.println("Finished!");
    }

    @Test
    public void learn() {
        //input and output dimensions
        int inDepth=2;
        int inRows = 6;
        int inCols = 6;
        int outClasses = 2;

        Network network = new Network(false);

        SigmoidLayer sig = new SigmoidLayer(new int[]{inDepth,inRows,inCols});
        network.addLayer(sig);

        ConvolutionLayer conv = new ConvolutionLayer(new int[]{inDepth,inRows,inCols},3,3,3,1,1,0,0, Network.Seed.RANDOM);
        network.addLayer(conv);

        ConvolutionLayer conv2 = new ConvolutionLayer(new int[]{3,inRows-2,inCols-2},3,3,4,1,1,0,0, Network.Seed.RANDOM);
        network.addLayer(conv2);

        FlattenLayer flat = new FlattenLayer(new int[]{4,2,2});
        network.addLayer(flat);

        LinearLayer ll = new LinearLayer(16,outClasses);
        network.addLayer(ll);

        SigmoidLayer sig2 = new SigmoidLayer(new int[]{outClasses});
        network.addLayer(sig2);

        CrossEntropyLayer cel = new CrossEntropyLayer(new int[]{outClasses});
        network.addLayer(cel);

        network.setLearningRate(0.01f);
        network.setRegularization(0.f,0.f);

        System.out.println(network);

        INDArray input = Nd4j.rand(new int[]{inDepth,inRows,inCols},new NormalDistribution(0.,2.));
        INDArray output = Nd4j.zeros(new int[]{2});
        output.put(0,1,1f);

        Data data =new Data(input,output);
        Batch batch = new Batch(data);
        System.out.println(batch);

        GradientChecker gc = new GradientChecker(network,batch);
        System.out.println(gc.gradientCheck(1, 0));

    }
}