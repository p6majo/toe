package com.p6majo.math.network2;

import com.p6majo.math.utils.Utils;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.rng.distribution.impl.NormalDistribution;
import org.nd4j.linalg.factory.Nd4j;

import static com.p6majo.math.network2.NetworkUtils.iterateThroughTensor;
import static org.junit.Assert.*;

public class NetworkUtilsTest {

    @Test
    public void iterateThroughTensorTest() {

        INDArray testTensor = Nd4j.rand(new int[]{2,3,4,5}, new NormalDistribution(0,2));

        iterateThroughTensor(testTensor);
    }
}