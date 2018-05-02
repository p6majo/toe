package com.p6majo.tensor;

import com.p6majo.math.utils.Utils;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.accum.AMax;
import org.nd4j.linalg.api.ops.impl.accum.Max;
import org.nd4j.linalg.api.ops.impl.indexaccum.IAMax;
import org.nd4j.linalg.api.ops.impl.indexaccum.IMax;
import org.nd4j.linalg.api.rng.distribution.impl.NormalDistribution;
import org.nd4j.linalg.api.shape.Shape;
import org.nd4j.linalg.convolution.Convolution;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.BooleanIndexing;
import org.nd4j.linalg.indexing.conditions.Conditions;

import java.util.function.Function;

public class PoolingOperations {
    @Test
    public void maxPooling() {
        System.out.println("Workout the max pooling operation by convolution:");
        int batchSize = 2;
        int inDepth = 3;
        int outDepth = 3;
        int cols = 12;
        int rows = 15;
        int kW = 3;
        int kH = 5;
        int pH = 0;
        int pW = 0;
        int sh = kH;
        int sw = kW;

        int outH = (rows - kH + 2 * pH) / sh + 1;
        int outW = (cols - kW + 2 * pW) / sw + 1;

        INDArray src = Nd4j.rand(new int[]{batchSize, inDepth, rows, cols}, new NormalDistribution(0f, 2f));
        System.out.println("Source:\n" + src);


        INDArray col = Nd4j.createUninitialized(new int[]{batchSize, outH, outW, inDepth, kH, kW});
        INDArray col2 = col.permute(0, 3, 4, 5, 1, 2);

        Convolution.im2col(src, kH, kW, sh, sw, pH, pW, false, col2);

        System.out.println("prepared data:\n" + col);

        //if the kernel is convoluted in depth -> new int[]{batchSize*outH*outW,inDepth*kH*kW}
        //if the kernel is convoluted with each layer ->new int[]{batchSize*outH*outW*inDepth,kH*kW}
        INDArray im2col2d = Shape.newShapeNoCopy(col, new int[]{batchSize * outH * outW * inDepth, kH * kW}, false);

        System.out.println("prepared matrix:\n" + im2col2d);

        INDArray index = Nd4j.getExecutioner().exec(new IMax(im2col2d), 1);
        INDArray poolMax = Nd4j.getExecutioner().exec(new Max(im2col2d), 1);
        System.out.println(index);
        System.out.println(poolMax);
        INDArray maskPoolMax = poolMax.dup();


        poolMax = Shape.newShapeNoCopy(poolMax, new int[]{outDepth, outW, outH, batchSize}, true);
        poolMax = poolMax.permute(3, 0, 2, 1);

        System.out.println("result:\n" + poolMax + "\nfrom:\n" + src);

        System.out.println("Creating a mask that shows the position of the maximum:");


        for (int r = 0; r < im2col2d.size(0); r++) {
            BooleanIndexing.applyWhere(im2col2d.getRow(r), Conditions.equals(maskPoolMax.getFloat(r, 0)), 1f);
        }
        BooleanIndexing.applyWhere(im2col2d, Conditions.notEquals(1f), 0);


        System.out.println("preparing mask:\n" + im2col2d);

        System.out.println("Undo im2col:\n" + col);

        INDArray backTrans = Convolution.col2im(col2, sh, sw, pH, pW, rows, cols);
        System.out.println(backTrans);
        System.out.println("from the original:\n" + src);

        System.out.println("Last step, we need to upsample the error term from the pooling layer for the backpropagation and apply the mask:\n");

        INDArray error = Nd4j.rand(new int[]{batchSize, outDepth, outH, outW}, new NormalDistribution(0, 2.));
        System.out.println("error terms:\n" + error);


        error = error.reshape(batchSize, outDepth, outH, outW, 1);
        INDArray upSampler = Nd4j.ones(new int[]{1, kH, kW});
        System.out.println(Utils.intArray2String(error.shape(), "x", ""));
        INDArray upsampled = Nd4j.tensorMmul(error, upSampler, new int[][]{{4}, {0}});
        System.out.println("upsampled error term:\n" + upsampled + "\n" + Utils.intArray2String(upsampled.shape(), "x", ""));
        //reduce to a column of kHxkW-packages with equal numbers
        upsampled = upsampled.reshape(batchSize * inDepth * outH * outW, kH, kW);
        System.out.println(upsampled);
        INDArray tmp = null;

        //combine single column data into outW columns and outH rows
        INDArray[] rowComps = new INDArray[outW];
        for (int b = 0; b < batchSize; b++)
            for (int d = 0; d < outDepth; d++)
                for (int r = 0; r < outH; r++) {
                    for (int c = 0; c < outW; c++)
                        rowComps[c] = upsampled.getRow(c + r * outW + d * outW * outH+b*outDepth*outW*outH);
                    if (tmp == null) tmp = Nd4j.concat(1, rowComps);
                    else tmp = Nd4j.concat(0, tmp, Nd4j.concat(1, rowComps));
                }

        System.out.println("backprop:\n" + tmp.reshape(batchSize, inDepth, rows, cols).mul(backTrans));
        System.out.println("source:\n" + src);

    }
}
