package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.network2.Network;
import com.p6majo.math.network2.TestResult;
import com.p6majo.math.utils.Utils;
import org.lwjgl.system.CallbackI;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.transforms.Log;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.BooleanIndexing;
import org.nd4j.linalg.indexing.conditions.Conditions;
import org.nd4j.linalg.indexing.functions.Value;

public class L2Layer extends LossLayer {

    private INDArray expectations ;

    public L2Layer(int[] signature) {
        //the layer doen't change the data structure
        super(signature);
        super.name = "L2 Loss Layer";
    }

    @Override
    public void pushForward(Batch batch) {
        super.pushForward(batch);

        super.activations = batch.getActivations();
        this.expectations = batch.getBatchExpectation();
        //System.out.println("expectations: "+this.expectations);
        super.errors = Nd4j.ones(batch.getActivations().shape());
    }

    public void pullBack(){
        this.pullBack(this.errors);
    }

    @Override
    public void pullBack(INDArray errors) {
        //set errors for this layer
        super.errors=errors;
       // System.out.println("Gradient: "+this.getLossGradient());
        //super.errorsForPreviousLayer = this.getLossGradient().mul(errors);
        super.errorsForPreviousLayer = this.getLossGradient();//for efficiency
    }

    @Override
    public float getLoss() {
       INDArray lossTmp = this.activations.sub(this.expectations);
       lossTmp.muli(lossTmp);
       float loss = Nd4j.sum(lossTmp).getFloat(0,0)/batchSize;
       return loss;
    }


    @Override
    public INDArray getLossGradient() {
       return this.activations.sub(this.expectations).mul(2);
    }

    @Override
    public TestResult getTestResult(Network.Test test) {
        TestResult testResult = new TestResult(this.batchSize);

        //select highest probability state
        //set all maximal values for each row to one
        for (int b = 0; b < batchSize; b++) {
            float max = Nd4j.max(this.activations.getRow(b)).getFloat(0,0);
            testResult.setProbability(b,max);
            BooleanIndexing.applyWhere(this.activations.getRow(b),Conditions.equals(max),new Value(1f));
        }
        //set all other values to zero
        BooleanIndexing.applyWhere(this.activations, Conditions.lessThan(1),new Value(0f));

        //Calculate difference between activations and expectations
        INDArray diff = this.activations.sub(this.expectations);
        //scan through rows to detect deviations
        for (int b = 0; b < batchSize; b++) {
            float max = Nd4j.max(diff.getRow(b)).getFloat(0,0);
            if (Math.abs(max)>0.1f)
                testResult.addFailure(b);

        }

        return testResult;
    }


    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("***********************************\n");
        out.append("L2 layer with signature: "+ Utils.intArray2String(this.inSignature,",","[]")+"\n");
        out.append("***********************************\n");

        return out.toString();
    }
}
