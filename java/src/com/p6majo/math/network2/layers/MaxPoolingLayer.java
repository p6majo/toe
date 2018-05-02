package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.accum.Max;
import org.nd4j.linalg.api.ops.impl.indexaccum.IMax;
import org.nd4j.linalg.api.shape.Shape;
import org.nd4j.linalg.convolution.Convolution;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.BooleanIndexing;
import org.nd4j.linalg.indexing.conditions.Conditions;

import static com.p6majo.math.network2.Network.LEARNING;

public class MaxPoolingLayer extends Layer {

    private final int inDepth;
    private final int outDepth;
    private final int cols;
    private final int rows;
    private final int kW;
    private final int kH;
    private final int sh;
    private final int sw;
    private final int outH;
    private final int outW;

    private INDArray pullBackMask;

    public MaxPoolingLayer(int[] inSignature, int poolingWidth, int poolingHeight) {
        super(inSignature, new int[]{inSignature[0], inSignature[1] / poolingHeight, inSignature[2] / poolingWidth});

        //grep dimensions for pooling
        this.inDepth = inSignature[0];
        this.outDepth = inSignature[0];
        this.cols = inSignature[2];
        this.rows = inSignature[1];
        this.kH = poolingHeight;
        this.kW = poolingWidth;
        this.sh = kH;
        this.sw = kW;

        this.outH = (rows - kH) / sh + 1;
        this.outW = (cols - kW) / sw + 1;

    }

    @Override
    public void pushForward(Batch batch) {
        super.pushForward(batch);

        //prepare convolution with max-kernel

        INDArray col = Nd4j.createUninitialized(new int[]{batchSize, outH, outW, inDepth, kH, kW});
        INDArray col2 = col.permute(0, 3, 4, 5, 1, 2);

        super.activations = batch.getActivations();

        Convolution.im2col(activations, kH, kW, sh, sw, 0, 0, false, col2);
        //since the max-kernel is applied to every layer of the activations, the number of columns is given by kH*kW
        INDArray im2col2d = Shape.newShapeNoCopy(col, new int[]{batchSize * outH * outW * inDepth, kH * kW}, false);

        //store positions of maxima in each row
        INDArray index = Nd4j.getExecutioner().exec(new IMax(im2col2d), 1);
        //store maxima of each row (apply max-kernel)
        INDArray poolMax = Nd4j.getExecutioner().exec(new Max(im2col2d), 1);

        if (LEARNING) {
            //prepare the mask for back prop
            //TODO this could be omitted, if the network is not in training mode
            //TODO replace by fractions if there is more than one maximum value (this is very unlikely, and will lead to an error in a backprop, which might be acceptable after all)
            for (int r = 0; r < im2col2d.size(0); r++) {
                BooleanIndexing.applyWhere(im2col2d.getRow(r), Conditions.equals(poolMax.getFloat(r, 0)), 1f);
            }
            BooleanIndexing.applyWhere(im2col2d, Conditions.notEquals(1f), 0);
            pullBackMask = Convolution.col2im(col2, sh, sw, 0, 0, rows, cols);
        }
        //build new activations

        poolMax = Shape.newShapeNoCopy(poolMax, new int[]{outDepth, outW, outH, batchSize}, true);
        batch.setActivations(poolMax.permute(3, 0, 2, 1));
    }

    @Override
    public void pullBack(INDArray errors) {
        //add additional dimension for tensoring
        errors = errors.reshape(batchSize, outDepth, outH, outW, 1);
        //create upsampler
        INDArray upSampler = Nd4j.ones(new int[]{1, kH, kW});

        //tensor errors with upSampler
        INDArray upsampledErrors = Nd4j.tensorMmul(errors, upSampler, new int[][]{{4}, {0}});

        //rearrange sample packages
        //flatten all dimensions except the kHxkW - packages
        upsampledErrors = upsampledErrors.reshape(batchSize * inDepth * outH * outW, kH, kW);
        INDArray tmp = null;

        //combine single column data into outW columns and outH rows
        INDArray[] rowComps = new INDArray[outW];
        for (int b = 0; b < batchSize; b++)
            for (int d = 0; d < outDepth; d++)
                for (int r = 0; r < outH; r++) {
                    for (int c = 0; c < outW; c++)
                        rowComps[c] = upsampledErrors.getRow(c + r * outW + d * outW * outH + b * outDepth * outW * outH);
                    if (tmp == null) tmp = Nd4j.concat(1, rowComps);
                    else tmp = Nd4j.concat(0, tmp, Nd4j.concat(1, rowComps));
                }

        super.errorsForPreviousLayer = tmp.reshape(batchSize,inDepth,rows,cols).mul(pullBackMask);

    }

}
