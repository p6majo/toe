package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import org.nd4j.linalg.api.ndarray.INDArray;

public class FlattenLayer extends Layer {

    private final int flattenedDim;
    private final int[] inShape;
    private int batchSize;

    public FlattenLayer(int[] inSignature) {
        super(inSignature,new int[]{LayerUtils.getFlattenedDimFromSignature(inSignature)} );
        this.flattenedDim = LayerUtils.getFlattenedDimFromSignature(inSignature);
        this.inShape = inSignature;
    }


    @Override
    public void pushForward(Batch batch) {
        //flatten the batchData

        batchSize = batch.getActivations().shape()[0];
        int[] newShape = new int[]{batchSize, flattenedDim};
        this.activations = batch.getActivations().reshape(newShape);
        batch.setActivations(this.activations);


    }

    @Override
    public void pullBack(INDArray errors) {
        //reshape errors to match with the incoming signature
        int [] shape = new int[this.inShape.length+1];
        shape[0]=this.batchSize;
        for (int i = 1; i < shape.length; i++)
            shape[i]=this.inShape[i-1];
        super.errorsForPreviousLayer = errors.reshape(shape);
    }

    @Override
    public void learn(float learningRate) {
        //nothing to do here

    }
}
