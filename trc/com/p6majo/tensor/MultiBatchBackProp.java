package com.p6majo.tensor;

import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class MultiBatchBackProp {
    @Test
    public void multiBatchBP(){
        int batchSize = 2;
        int m = 5; //layer 1
        int n =4; //layer 2

        INDArray errors  = Nd4j.rand(new int[]{batchSize ,n});
        INDArray activations  = Nd4j.rand(new int[]{batchSize,m});

        System.out.println("errors:\n" + errors);
        System.out.println("activations:\n" + activations);

        INDArray corrections = activations.getRow(0).transpose().mmul(errors.getRow(0));
        for (int b = 1; b < batchSize; b++) {
            corrections.addi(activations.getRow(b).transpose().mmul(errors.getRow(b)));
        }
        corrections.divi(batchSize);

        System.out.println("corrections:\n" + corrections);
    }
}
