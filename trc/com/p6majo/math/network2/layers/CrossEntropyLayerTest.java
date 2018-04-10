package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.network2.Data;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class CrossEntropyLayerTest {

    @Test
    public void allFeaturesTest() {
        int batchSize=1;
        Data[] data = new Data[batchSize];
        for (int b = 0;b<batchSize;b++){
            float[] values = new float[5];
            for (int v=0;v<5;v++) values[v]=(float) Math.random()*0.001f;
            int pos = (int) Math.floor(Math.random()*5);
            values[pos]=1;

            float[] expectations = new float[5];
            pos = (int) Math.floor(Math.random()*5);
            expectations[pos]=1;
            data[b] =new Data(Nd4j.create(values),Nd4j.create(expectations));
        }
        Batch batch = new Batch(data);

        CrossEntropyLayer cel = new CrossEntropyLayer(new int[]{5});
        System.out.println(cel.toString());
        System.out.println("Push forward: \n"+batch.toString());
        cel.pushForward(batch);
        cel.pullBack();
        INDArray errors = cel.getErrorsForPreviousLayer();
        System.out.println("Errors: "+errors);
        System.out.println("Loss of the batch: "+cel.getLoss());

    }

}