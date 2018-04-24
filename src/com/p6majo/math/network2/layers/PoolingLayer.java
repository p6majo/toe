package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import org.nd4j.linalg.api.ndarray.INDArray;

public class PoolingLayer extends Layer {
    public PoolingLayer(int[] inSignature, int poolingWidth, int poolingHeight) {
        super(inSignature, new int[]{inSignature[0],inSignature[1]/poolingHeight,inSignature[2]/poolingWidth});
    }

    @Override
    public void pushForward(Batch batch) {

    }

    @Override
    public void pullBack(INDArray errors) {

    }

    @Override
    public void learn(float learningRate) {

    }
}
