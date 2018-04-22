package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.network2.Data;
import com.p6majo.math.network2.Network;
import org.junit.Before;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
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
        ConvolutionLayer convLayer = new ConvolutionLayer(new int[]{3,6,6},5,3,2,1,1,1,0,Network.Seed.NO_BIAS);
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
        int paddingWidth=1;
        int paddingHeight=0;
        ConvolutionLayer convLayer = new ConvolutionLayer(new int[]{inDepth,inHeight,inWidth},kernelWidth,kernelHeight,outDepth,1,1,paddingWidth,paddingHeight,Network.Seed.NO_BIAS);
        System.out.println(convLayer.toString());

        convLayer.pushForward(batch);
        System.out.println("batch:\n"+batch.toString());
        System.out.println("output:\n"+convLayer.getActivations());


        int outWidth = (inWidth-kernelWidth+2*paddingWidth)/1+1;
        int outHeight = (inHeight-kernelHeight+2*paddingHeight)/1+1;

        INDArray errors  = Nd4j.ones(new int[]{outDepth,outHeight,outWidth});
        convLayer.pullBack(errors);
        System.out.println("pullback:\n"+convLayer.getErrorsForPreviousLayer());
    }

    @Test
    public void learn() {
    }
}