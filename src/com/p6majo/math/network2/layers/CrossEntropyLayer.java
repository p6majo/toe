package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.network2.Data;
import com.p6majo.math.utils.Utils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.accum.Sum;
import org.nd4j.linalg.api.ops.impl.transforms.Log;
import org.nd4j.linalg.api.ops.impl.transforms.Sigmoid;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.BooleanIndexing;
import org.nd4j.linalg.indexing.conditions.Conditions;

public class CrossEntropyLayer extends LossLayer {

    private INDArray expectations ;
    private int batchSize;

    public CrossEntropyLayer(int[] signature) {
        //the layer doen't change the data structure
        super(signature);
    }

    @Override
    public void pushForward(Batch batch) {
        super.activations = batch.getActivations();
        this.expectations = batch.getBatchExpectation();
        super.errors = Nd4j.ones(batch.getActivations().shape());
        this.batchSize = batch.getActivations().shape()[0];
        regularize();
    }

   private void regularize(){
        float EPS = 0.0000001f;
        BooleanIndexing.replaceWhere(expectations,EPS, Conditions.epsEquals(0));
        BooleanIndexing.replaceWhere(expectations,1-EPS,Conditions.epsEquals(1));

        BooleanIndexing.replaceWhere(activations,EPS, Conditions.epsEquals(0));
        BooleanIndexing.replaceWhere(activations,1-EPS,Conditions.epsEquals(1));

    }

    public void pullBack(){
        this.pullBack(this.errors);
    }

    @Override
    public void pullBack(INDArray errors) {
        super.errorsForPreviousLayer = this.getLossGradient().mul(errors);
    }

    @Override
    public float getLoss() {
        //cross entropy formula

        //attention! Executioners overwrite the initial data
        INDArray lna = Nd4j.getExecutioner().execAndReturn(new Log(this.activations.dup()));
        INDArray ln1ma =Nd4j.getExecutioner().execAndReturn(new Log(this.activations.dup().sub(1).mul(-1)));

       return (expectations.mul(lna).add(ln1ma.mul(expectations.sub(1).mul(-1)))).mul(-1).sumNumber().floatValue()/batchSize;
    }


    @Override
    public INDArray getLossGradient() {
       return this.expectations.sub(1).div(this.activations.sub(1)).sub(this.expectations.div(this.activations));
    }


    @Override
    public void learn(float learningRate) {
        //nothing to do in this layer since it is a passive layer
        //there are not parameters to be adjusted
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("***********************************\n");
        out.append("Cross entropy layer with signature: "+ Utils.intArray2String(this.inSignature,",","[]")+"\n");
        out.append("***********************************\n");

        return out.toString();
    }
}
