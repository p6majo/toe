package com.p6majo.math.convolution;

import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import static org.junit.Assert.*;

public class ConvolutionWrapperTest {

    @Test
    public void convolution() {
        float[] img = new float[64];
        for (int i = 0; i < img.length; i++)
            img[i]=i;
        INDArray image = Nd4j.create(new int[]{8,8},img);
        float[] ker = new float[25];
        for (int i = 0; i < ker.length; i++)
            ker[i]=i;
        INDArray kernel = Nd4j.create(new int[]{5,5},ker);

        ConvolutionWrapper wrapper = new ConvolutionWrapper();
        INDArray convolution = wrapper.convolution(image,kernel);

        System.out.println(convolution);
    }
}