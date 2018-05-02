package com.p6majo.tensor;

import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GPUCPUTest {

    @Test
    public void multiplicationTest() {
                INDArray m1 = Nd4j.rand(new int[]{1000,1000});
                INDArray m2 = Nd4j.rand(new int[]{1000,1000});

                System.out.println("Running matrix multiplication: ");
                long start = System.currentTimeMillis();

                for (int i=0;i<1000;i++) {
                    m2 = m1.mul(m2);
                }
                System.out.println("done in "+(System.currentTimeMillis()-start)+" ms.");
    }


}
