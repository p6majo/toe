package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.network2.Data;
import com.p6majo.math.network2.Network;
import com.p6majo.math.network2.TestResult;
import com.p6majo.math.utils.Utils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.accum.Sum;
import org.nd4j.linalg.api.ops.impl.transforms.Log;
import org.nd4j.linalg.api.ops.impl.transforms.Sigmoid;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.BooleanIndexing;
import org.nd4j.linalg.indexing.conditions.Conditions;
import org.nd4j.linalg.indexing.functions.Value;

public class CrossEntropyLayer extends LossLayer {

    private INDArray expectations ;
    private int batchSize;

    public CrossEntropyLayer(int[] signature) {
        //the layer doen't change the data structure
        super(signature);
        super.name = "Cross Entropy Layer";
    }

    @Override
    public void pushForward(Batch batch) {
        super.activations = batch.getActivations();
        this.expectations = batch.getBatchExpectation();
        super.errors = Nd4j.ones(batch.getActivations().shape());
        this.batchSize = batch.getActivations().shape()[0];
        regularize();
        double rnd = Math.random();
       // if (rnd>0.999) System.out.println(activations);
    }

   private void regularize(){
        float EPS = 0.0000001f;
        BooleanIndexing.replaceWhere(expectations,EPS, Conditions.epsEquals(0));
        BooleanIndexing.replaceWhere(expectations,1-EPS,Conditions.epsEquals(1));

        BooleanIndexing.replaceWhere(activations,EPS, Conditions.epsEquals(0));
        BooleanIndexing.replaceWhere(activations,1-EPS,Conditions.epsEquals(1));

    }

    @Override
    public void pullBack(INDArray errors) {
        //set errors for this layer
       // super.errors=errors.dup();
        super.errors = errors;


        super.errorsForPreviousLayer = this.getLossGradient().muli(errors);
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
    public TestResult getTestResult(Network.Test test) {
        TestResult testResult = new TestResult(this.batchSize);

        INDArray copyOfActivations = this.activations.dup();
        //select highest probability state
        //set all maximal values for each row to one
        for (int b = 0; b < batchSize; b++) {
            float max = Nd4j.max(copyOfActivations.getRow(b)).getFloat(0,0);
            testResult.setProbability(b,max);
            BooleanIndexing.applyWhere(copyOfActivations.getRow(b),Conditions.equals(max),new Value(1f));
        }
        //set all other values to zero
        BooleanIndexing.applyWhere(copyOfActivations, Conditions.lessThan(1),new Value(0f));

        //Calculate difference between activations and expectations
        INDArray diff = copyOfActivations.sub(this.expectations);
        //scan through rows to detect deviations
        for (int b = 0; b < batchSize; b++) {
            float max = Nd4j.max(diff.getRow(b)).getFloat(0,0);
            if (Math.abs(max)>0.1f)
                testResult.addFailure(b);

        }

        return testResult;
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
