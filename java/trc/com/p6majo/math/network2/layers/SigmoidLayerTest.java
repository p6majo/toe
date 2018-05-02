package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.network2.Data;
import org.junit.Before;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.rng.distribution.impl.NormalDistribution;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;

public class SigmoidLayerTest {

    private Batch batch;
    private Data data;
    private INDArray error;
    private int[] inputShape;


    @Before
    public void setup(){
        System.out.println("Setup batch for testing ");

        inputShape = new int[]{2,3};

        ArrayList<Data> dataList = new ArrayList<Data>();

        //create a single piece of data
        INDArray input = Nd4j.rand(inputShape,new NormalDistribution(0.,2.));
        float[] outs = new float[10];
        outs=new float[]{0f,0f,0f,1f,1f,0f,0f,1f,0f,1f};
        INDArray expectation = Nd4j.create(outs, new int[]{10});
        // System.out.println(n + ": " + input + " " + expectation);
        data = new Data(input,expectation);

        //create a batch
        for (int n=0;n<10;n++) {
            input = Nd4j.rand(inputShape,new NormalDistribution(0.,2.));
            outs = new float[10];
            outs[n] = 1f;
            expectation = Nd4j.create(outs, new int[]{10});
            // System.out.println(n + ": " + input + " " + expectation);
            dataList.add(new Data(input,expectation));
        }
        this.batch = new Batch(dataList);

        error = Nd4j.ones(inputShape);
        error=error.add(1);
    }



    @Test
    public void pushForward() {
        SigmoidLayer sigmoid = new SigmoidLayer(inputShape);

        System.out.println("Batch of data: "+batch);
        sigmoid.pushForward(batch);
        System.out.println("Batch of data passed through sigmoid: "+sigmoid.getActivations());
    }




    @Test
    public void pullBack() {

        System.out.println("\n\n now back pass test: \n");
        SigmoidLayer sigmoid = new SigmoidLayer(inputShape);



        System.out.println("Now test backpass of errors: "+error);
        sigmoid.pullBack(error);
        System.out.println("pull back of errors gives: "+sigmoid.getErrorsForPreviousLayer());
        System.out.println("make sure that the activations are not overwritten: "+sigmoid.getActivations());
        System.out.println("make sure that the initial errors are not overwritten: "+error);


    }

}