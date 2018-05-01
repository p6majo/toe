package com.p6majo.math.convolution;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Convolution wrapper class that utilizes native fftw3 library
 * @author p6majo (wrapper)
 */
public class Convolutionwrapper {

    public INDArray convolution(INDArray image, INDArray kernel){
        float[] imagef = image.data().asFloat();
        float[] kernelf = kernel.data().asFloat();

        int rowsImg = image.size(0);
        int colsImg = image.size(1);
        int rowsKer = kernel.size(0);
        int colsKer = kernel.size(1);

        int rowsResult = rowsImg - rowsKer+1;
        int colsResult = colsImg - colsKer+1;
        float[] resultf = new float[rowsResult*colsResult];

        convolute(imagef,rowsImg,colsImg,kernelf,rowsKer,rowsImg,resultf);

        INDArray result =Nd4j.create(new int[]{rowsResult,colsResult},resultf);
        return result;
    }

    public native void convolute(float[] image,int rowsImg, int colsImg,float[] kernel,int rowsKer, int colsKer,float[] convolution);
    static{
        System.loadLibrary("ConvolutionMath");
    }
}
