package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.network2.Data;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.accum.Sum;
import org.nd4j.linalg.api.ops.impl.transforms.Log;
import org.nd4j.linalg.api.ops.impl.transforms.Sigmoid;
import org.nd4j.linalg.factory.Nd4j;

public class CrossEntropyLayer extends LossLayer {

    private INDArray expectations ;
    private int batchSize;

    public CrossEntropyLayer(int[] signature) {
        //the layer doen't change the data structure
        super(signature);
    }

    @Override
    public void pushForward(Batch batch) {
        super.activations = batch.getBatchInput();
        this.expectations = batch.getBatchExpectation();
        super.errors = Nd4j.zeros(batch.getBatchInput().shape());
        this.batchSize = batch.getBatchInput().shape()[0];
    }

    @Override
    public void pushForward(Data data) {
        super.activations =  data.getInput();
        this.expectations = data.getExpectations();
        super.errors = Nd4j.ones(data.getInput().shape());
        this.batchSize = 1;
    }

    public void pullBack(){
        this.pullBack(this.errors);
    }

    @Override
    public void pullBack(INDArray errors) {
        super.errorsForPreviousLayer = this.getLossGradient();
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
}
