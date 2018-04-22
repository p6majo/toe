package com.p6majo.tensor;

import com.p6majo.math.utils.Utils;
import org.junit.Test;
import org.nd4j.linalg.api.memory.MemoryWorkspace;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.transforms.VectorFFT;
import org.nd4j.linalg.api.shape.Shape;
import org.nd4j.linalg.convolution.Convolution;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.fft.FFTInstance;

import java.util.Arrays;

import static org.junit.Assert.*;


public class ConvolutionOperations {
    @Test
    public void convolutionTest(){
        float[] comps = new float[64];
        System.out.println("\n\nApply two filters at once: \n");

        for (int i=0;i<comps.length;i++) comps[i]=i<32? i%8<4?1:2:i%8<4?3:4;
        int[] inputShape=new int[]{1,1,8,8};
        INDArray preImage = Nd4j.create(inputShape,comps);
        preImage = preImage.reshape(inputShape);
        System.out.println("preImage:\n"+preImage);

        int[] filterShape = new int[]{2,1,3,3}; //outDepth, inDepth,filterHeight, filterWidth
        INDArray filter = Nd4j.create(filterShape,new float[]{1,0,-1,1,0,-1,1,0,-1,1,1,1,0,0,0,-1,-1,-1});
        filter = filter.reshape(filterShape);
        System.out.println("Filter:\n"+filter);

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
        System.out.println("Final result:\n" + z);


        System.out.println("\n\nApply the first filter independently:\n");

        filterShape = new int[]{1,1,3,3}; //outDepth, inDepth,filterHeight, filterWidth
        filter = Nd4j.create(filterShape,new float[]{1,0,-1,1,0,-1,1,0,-1});
        filter = filter.reshape(filterShape);

        System.out.println("Filter:\n"+filter);

        outDepth = filter.size(0);
        kH = filter.size(2);
        kW = filter.size(3);

        outW = (preImage.size(2)-kW+2*pad[0])/strides[0]+1; //width of data
        outH = (preImage.size(3)-kH+2*pad[1])/strides[1]+1; //height of data

        col = Nd4j.createUninitialized(new int[] {miniBatch, outH, outW, inDepth, kH, kW}, 'c');
        col2 = col.permute(0, 3, 4, 5, 1, 2);

        Convolution.im2col(preImage, kH, kW, strides[0], strides[1], pad[0], pad[1],  false, col2);

        //System.out.println(col2);

        im2col2d = Shape.newShapeNoCopy(col, new int[] {miniBatch * outH * outW, inDepth * kH * kW}, false);

        // System.out.println("Factor1: "+im2col2d);


        //Current order of weights: [depthOut,depthIn,kH,kW], c order
        //Permute to give [kW,kH,depthIn,depthOut], f order
        //Reshape to give [kW*kH*depthIn, depthOut]. This should always be zero-copy reshape, unless weights aren't in c order for some reason
        permutedW = filter.permute( 3, 2, 1,0);
        reshapedW = permutedW.reshape('f', kW * kH * inDepth, outDepth);

        // System.out.println("reshaped filter: " + reshapedW);


        //Do the MMUL; c and f orders in, f order out. output shape: [miniBatch*outH*outW,depthOut]
        z = im2col2d.mmul(reshapedW);

        //Now, reshape to [outW,outH,miniBatch,outDepth], and permute to have correct output order: [miniBath,outDepth,outH,outW];
        z = Shape.newShapeNoCopy(z, new int[] {outW, outH, miniBatch, outDepth}, true);
        z = z.permute(2,3, 1, 0);
        System.out.println("Final result:\n" + z);


        System.out.println("\n\nApply the second filter independently:\n");

        filter = Nd4j.create(filterShape,new float[]{1,1,1,0,0,0,-1,-1,-1});
        filter= filter.reshape(filterShape);
        System.out.println("Filter:\n"+filter);

        outDepth = filter.size(0);


        kH = filter.size(2);
        kW = filter.size(3);

        outW = (preImage.size(2)-kW+2*pad[0])/strides[0]+1; //width of data
        outH = (preImage.size(3)-kH+2*pad[1])/strides[1]+1; //height of data

        col = Nd4j.createUninitialized(new int[] {miniBatch, outH, outW, inDepth, kH, kW}, 'c');
        col2 = col.permute(0, 3, 4, 5, 1, 2);

        Convolution.im2col(preImage, kH, kW, strides[0], strides[1], pad[0], pad[1],  false, col2);

        //System.out.println(col2);

        im2col2d = Shape.newShapeNoCopy(col, new int[] {miniBatch * outH * outW, inDepth * kH * kW}, false);

        //System.out.println("Factor1: "+im2col2d);


        //Current order of weights: [depthOut,depthIn,kH,kW], c order
        //Permute to give [kW,kH,depthIn,depthOut], f order
        //Reshape to give [kW*kH*depthIn, depthOut]. This should always be zero-copy reshape, unless weights aren't in c order for some reason
        permutedW = filter.permute( 3, 2, 1,0);
        reshapedW = permutedW.reshape('f', kW * kH * inDepth, outDepth);

        System.out.println("reshaped filter: " + reshapedW);


        //Do the MMUL; c and f orders in, f order out. output shape: [miniBatch*outH*outW,depthOut]
        z = im2col2d.mmul(reshapedW);

        //Now, reshape to [outW,outH,miniBatch,outDepth], and permute to have correct output order: [miniBath,outDepth,outH,outW];
        z = Shape.newShapeNoCopy(z, new int[] {outW, outH, miniBatch, outDepth}, true);
        z = z.permute(2,3, 1, 0);
        System.out.println("Final result:\n" + z);


        System.out.println("\n\nApply to an input with depth = 2\n");

        comps = new float[128];
        for (int i=0;i<comps.length;i++) comps[i]=i<64? i%8<4?1:2:i<96?3:4;
        inputShape=new int[]{1,2,8,8};
        preImage = Nd4j.create(inputShape,comps);
        preImage = preImage.reshape(inputShape);
        System.out.println("preImage:\n"+preImage);

        filterShape = new int[]{1,2,3,3}; //outDepth, inDepth,filterHeight, filterWidth
        filter = Nd4j.create(filterShape,new float[]{1,0,-1,1,0,-1,1,0,-1,1,1,1,0,0,0,-1,-1,-1});
        filter = filter.reshape(filterShape);

        System.out.println("Filter of depth 2:\n" + filter);

        //infer dimensions
        inDepth=preImage.size(1);
        miniBatch = preImage.size(0);

        outDepth = filter.size(0);
        kH = filter.size(2);
        kW = filter.size(3);

        outW = (preImage.size(2)-kW+2*pad[0])/strides[0]+1; //width of data
        outH = (preImage.size(3)-kH+2*pad[1])/strides[1]+1; //height of data

        col = Nd4j.createUninitialized(new int[] {miniBatch, outH, outW, inDepth, kH, kW}, 'c');
        col2 = col.permute(0, 3, 4, 5, 1, 2);

        Convolution.im2col(preImage, kH, kW, strides[0], strides[1], pad[0], pad[1],  false, col2);
        im2col2d = Shape.newShapeNoCopy(col, new int[] {miniBatch * outH * outW, inDepth * kH * kW}, false);

        //System.out.println("Factor1: "+im2col2d);

        //Current order of weights: [depthOut,depthIn,kH,kW], c order
        //Permute to give [kW,kH,depthIn,depthOut], f order
        //Reshape to give [kW*kH*depthIn, depthOut]. This should always be zero-copy reshape, unless weights aren't in c order for some reason
        permutedW = filter.permute( 3, 2, 1,0);
        reshapedW = permutedW.reshape('f', kW * kH * inDepth, outDepth);

        //System.out.println("reshaped filter: " + reshapedW);
        //Do the MMUL; c and f orders in, f order out. output shape: [miniBatch*outH*outW,depthOut]
        z = im2col2d.mmul(reshapedW);

        //Now, reshape to [outW,outH,miniBatch,outDepth], and permute to have correct output order: [miniBath,outDepth,outH,outW];
        z = Shape.newShapeNoCopy(z, new int[] {outW, outH, miniBatch, outDepth}, true);
        z = z.permute(2,3, 1, 0);
        System.out.println("Final result:\n" + z);
        System.out.println("As expected, the contributions of the filters for each inDepth dimension superimpose\n");


        System.out.println("Reflect kernel at the mid point:\n");
        comps  = new float[25];
        for (int i = 0; i < 25; i++) {
            comps[i]=i+1;
        }

        INDArray kernel = Nd4j.create(new int[]{5,5},comps);
        System.out.println("kernel: \n"+kernel);
        int[] shape = kernel.shape();
        System.out.println("and its shape: " + Utils.intArray2String(shape,",","[]"));

        INDArray tmp = Nd4j.toFlattened(kernel);
        tmp = Nd4j.reverse(tmp);

        kernel = tmp.reshape(shape);
        System.out.println("the reflected kernel: \n" + kernel);

        System.out.println("Reflect a deep kernel at the mid points of each layer:\n");
        int depth = 3;
        comps  = new float[25*depth];
        for (int i = 0; i < 25*depth; i++) {
            comps[i]=i+1;
        }

        kernel = Nd4j.create(new int[]{depth,5,5},comps);
        System.out.println("kernel: \n"+kernel);
        shape = kernel.shape();
        System.out.println("and its shape: " + Utils.intArray2String(shape,",","[]"));

        tmp = kernel.reshape(new int[]{depth,25});
        System.out.println(tmp+"\n\n");

         INDArray tmp2;
         tmp2 = Nd4j.reverse(tmp.getRow(0));
         for (int i=1;i<depth;i++) tmp2=Nd4j.concat(1,tmp2,Nd4j.reverse(tmp.getRow(i)));

         System.out.println(tmp+"\n");


        kernel = tmp.reshape(shape);
        System.out.println("the reflected kernel: \n" + kernel);


    }

    @Test
    public void convolutionWithPaddingTest(){
        float[] comps = new float[64];
        System.out.println("\n\nApply filter with padding: \n");

        int depth=1;
        int batchSize=1;
        int dimx = 4;
        int dimy = 4;
        int[] inputShape=new int[]{batchSize,depth,dimx,dimy};
        INDArray preImage = Nd4j.zeros(inputShape);
        for (int i = 0; i <3; i++) {
             int b = (int) (Math.random()*batchSize);
             int d = (int) (Math.random() * depth);
             int x = (int) (Math.random() * dimx);
             int y = (int) (Math.random() * dimy);
             preImage.getRow(b).getRow(d).put(x,y,1);
        }

        preImage = preImage.reshape(inputShape);
        System.out.println("preImage:\n"+preImage);

        int kernelWidth =3;
        int kernelHeight=3;
        int outDepth = 1;

        int[] filterShape = new int[]{outDepth,depth,kernelHeight,kernelWidth}; //outDepth, inDepth,filterHeight, filterWidth
        INDArray filter = Nd4j.rand(filterShape);
        System.out.println("Filter:\n"+filter);

        int[] pad=new int[]{1,0};//paddingWidth and paddingHeight
        int[] strides = new int[]{2,1};//strideWidth and strideHeight

        //watch out: the height is given by the number of rows
        //and the width is given by the number of cols

        int outH = (preImage.size(2)-kernelHeight+2*pad[1])/strides[1]+1; //height of data
        int outW = (preImage.size(3)-kernelWidth+2*pad[0])/strides[0]+1; //width of data


        INDArray col = Nd4j.createUninitialized(new int[] {batchSize, outH, outW, depth, kernelHeight, kernelWidth}, 'c');
        INDArray col2 = col.permute(0, 3, 4, 5, 1, 2);

        Convolution.im2col(preImage, kernelHeight, kernelWidth, strides[1], strides[0], pad[1], pad[0],  false, col2);

        //System.out.println(col2);

        INDArray im2col2d = Shape.newShapeNoCopy(col, new int[] {batchSize * outH * outW, depth * kernelHeight * kernelWidth}, false);

        // System.out.println("Factor1: "+im2col2d);


        //Current order of weights: [depthOut,depthIn,kH,kW], c order
        //Permute to give [kW,kH,depthIn,depthOut], f order
        //Reshape to give [kW*kH*depthIn, depthOut]. This should always be zero-copy reshape, unless weights aren't in c order for some reason
        INDArray permutedW = filter.permute(3,2,1,0);
        // System.out.println("unpermuted filter:\n" + filter);
        //System.out.println("permuted filter:\n"+permutedW);
        INDArray reshapedW = permutedW.reshape('f', kernelHeight * kernelWidth * depth, outDepth);

        //System.out.println("reshaped filter: " + reshapedW);


        //Do the MMUL; c and f orders in, f order out. output shape: [miniBatch*outH*outW,depthOut]
        INDArray z = im2col2d.mmul(reshapedW);

        //Now, reshape to [outW,outH,miniBatch,outDepth], and permute to have correct output order: [miniBath,outDepth,outH,outW];
        z = Shape.newShapeNoCopy(z, new int[] {outW, outH, batchSize, outDepth}, true);
        z = z.permute(2,3,1,0);
        System.out.println("Final result:\n" + z);



    }


}
