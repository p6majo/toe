package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.utils.Utils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.transforms.LeakyReLU;
import org.nd4j.linalg.api.ops.impl.transforms.LeakyReLUDerivative;
import org.nd4j.linalg.api.ops.impl.transforms.Sigmoid;
import org.nd4j.linalg.factory.Nd4j;

public class LeakyReLULayer extends Layer {

    private INDArray inputData;

    public LeakyReLULayer(int[] signature) {
        //the layer doen't change the data structure
        super(signature, signature);
        super.name = "LeakReLU Layer";
    }

    @Override
    public void pushForward(Batch batch) {
        super.pushForward(batch);
        inputData = batch.getActivations().dup();
        //the activations of the batch get overwritten by the Executioner
        super.activations = Nd4j.getExecutioner().execAndReturn(new org.nd4j.linalg.api.ops.impl.transforms.LeakyReLU(batch.getActivations()));
    }


    @Override
    public void pullBack(INDArray errors) {
        //set errors for this layer
        super.errors=errors;
        //evaluate the derivative of the sigmoid of the activations and multiply it with the incoming error term
        //since this layer is an elementwise scalar function the structure term of the errors must be the same as
        //the structure term of the activations
        //sig'=(1-sig)*sig
        //store sig(error) as intermediate values to save computational power

       super.errorsForPreviousLayer = Nd4j.getExecutioner().execAndReturn(new LeakyReLUDerivative(inputData)).mul(errors);
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("***********************************\n");
        out.append("Leaky ReLU layer with signature: "+Utils.intArray2String(this.inSignature,",","[]")+"\n");
        out.append("***********************************\n");

        return out.toString();
    }
}
