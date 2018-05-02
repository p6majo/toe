package com.p6majo.tensor;

import com.p6majo.math.utils.Utils;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.shape.Shape;
import org.nd4j.linalg.convolution.Convolution;
import org.nd4j.linalg.factory.Nd4j;


public class ConvolutionBasics {
    @Test
    public void convolutionTest(){
        float[] comps = new float[4096];
        System.out.println("\n\nApply two filters at once: \n");

        for (int i=0;i<comps.length;i++) comps[i]=i;
        int[] inputShape=new int[]{1,1,64,64};
        INDArray preImage = Nd4j.create(inputShape,comps);
        preImage = preImage.reshape(inputShape);
       // System.out.println("preImage:\n"+preImage);

        int[] filterShape = new int[]{1,1,5,5}; //outDepth, inDepth,filterHeight, filterWidth
        float[] comps2 = new float[25];
        for (int i = 0; i < 25; i++) comps2[i]=i;
        INDArray filter = Nd4j.create(filterShape,comps2);
        filter = filter.reshape(filterShape);
       // System.out.println("Filter:\n"+filter);

        int miniBatch = preImage.size(0);//batchSize
        int inDepth = preImage.size(1); //depth of data
        int outDepth = filter.size(0);

        int[] pad=new int[]{0,0};
        int[] strides = new int[]{1,1};


        //watch out: the height is given by the number of rows
        //and the width is given by the number of cols
        int kH = filter.size(2); //number of rows
        int kW = filter.size(3); //number of cols

        int outH = (preImage.size(2)-kH+2*pad[1])/strides[1]+1; //height of data
        int outW = (preImage.size(3)-kW+2*pad[0])/strides[0]+1; //width of data


        long start =System.currentTimeMillis();

        INDArray col = Nd4j.createUninitialized(new int[] {miniBatch, outH, outW, inDepth, kH, kW}, 'c');
        INDArray col2 = col.permute(0, 3, 4, 5, 1, 2);

        Convolution.im2col(preImage, kH, kW, strides[0], strides[1], pad[0], pad[1],  false, col2);

        //System.out.println(col2);

        INDArray im2col2d = Shape.newShapeNoCopy(col, new int[] {miniBatch * outH * outW, inDepth * kH * kW}, false);

       // System.out.println("Factor1: "+im2col2d);


        //Current order of weights: [depthOut,depthIn,kH,kW], c order
        //Permute to give [kW,kH,depthIn,depthOut], f order
        //Reshape to give [kW*kH*depthIn, depthOut]. This should always be zero-copy reshape, unless weights aren't in c order for some reason
        INDArray permutedW = filter.permute(3,2,1,0);
       // System.out.println("unpermuted filter:\n" + filter);
        //System.out.println("permuted filter:\n"+permutedW);
        INDArray reshapedW = permutedW.reshape('f', kW * kH * inDepth, outDepth);

        //System.out.println("reshaped filter: " + reshapedW);


        //Do the MMUL; c and f orders in, f order out. output shape: [miniBatch*outH*outW,depthOut]
        INDArray z = im2col2d.mmul(reshapedW);

        //Now, reshape to [outW,outH,miniBatch,outDepth], and permute to have correct output order: [miniBath,outDepth,outH,outW];
        z = Shape.newShapeNoCopy(z, new int[] {outW, outH, miniBatch, outDepth}, true);
        z = z.permute(2,3,1,0);
        long end = System.currentTimeMillis();
        System.out.println("Final result after "+(end-start)+" ms:\n" +z.getRow(0).getRow(0).getFloat(0,0));


    }



}
