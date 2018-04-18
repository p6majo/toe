package com.p6majo.tensor;

import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.transforms.VectorFFT;
import org.nd4j.linalg.convolution.Convolution;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.fft.FFTInstance;

import java.util.Arrays;

import static org.junit.Assert.*;


public class ConvolutionOperations {
    @Test
    public void convolutionTest(){

    /*
        INDArray nd = Nd4j.ones(5,5);
        INDArray kernel = Nd4j.ones(3,3);
        INDArray conv = Nd4j.getConvolution().conv2d(nd, kernel, Convolution.Type.VALID);
        System.out.println(conv);



        INDArray image = Nd4j.create(new double[][]{
                {3, 2, 5, 6, 7, 8},
                {5, 4, 2, 10, 8, 1}
        });

        INDArray kernel = Nd4j.create(new double[][]{
                {4, 5},
                {1, 2}
        });


        INDArray r = Convolution.conv2d(image, kernel, Convolution.Type.VALID);

        System.out.println(Arrays.toString(r.shape()));
        System.out.println(r.toString());


        INDArray preImage = Nd4j.rand(new int[]{8,8});
        System.out.println("preImage:\n"+preImage);
        INDArray filter = Nd4j.create(new int[]{3,3},new float[]{-1,0,1,-1,0,1,-1,0,1});
        System.out.println("Filter:\n"+filter);
        INDArray image = Nd4j.getConvolution().convn(preImage,filter, Convolution.Type.FULL);
        System.out.println("image:\n" + image);
        */
    }

    @Test
    public void fftTest(){

        INDArray arr = Nd4j.ones(new int[]{1,16});
        System.out.println(arr+" "+arr.length());

          VectorFFT fft = new VectorFFT(arr);
       // fft.exec();
       // System.out.println(fft.z());
       // INDArray assertion = Nd4j.create(5);
       // assertion.putScalar(0, 5);
        //assertEquals(getFailureMessage(), assertion, fft.z());

    }
}
