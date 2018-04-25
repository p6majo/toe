package com.p6majo.tensor;

import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class TensorIMethods {
    @Test
    public void iMethods(){
        INDArray test = Nd4j.rand(new int[]{2,2});
        System.out.println(test);
        test.subi(test.mul(2));
        System.out.println(test);
    }
}
