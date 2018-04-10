package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.network2.Data;
import com.p6majo.math.utils.Utils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.transforms.Sigmoid;
import org.nd4j.linalg.factory.Nd4j;

public class SigmoidLayer extends Layer {


    public SigmoidLayer(int[] signature) {
        //the layer doen't change the data structure
        super(signature, signature);
    }

    @Override
    public void pushForward(Batch batch) {
        //the activations of the batch get overwritten
        super.activations = Nd4j.getExecutioner().execAndReturn(new Sigmoid(batch.getActivations()));
    }


    @Override
    public void pullBack(INDArray errors) {
        //evaluate the derivative of the sigmoid of the activations and multiply it with the incoming error term
        //since this layer is an elementwise scalar function the structure term of the errors must be the same as
        //the structure term of the activations
        //sig'=(1-sig)*sig
        //store sig(error) as intermediate values to save computational power

        INDArray factor = super.activations.sub(1).mul(-1);
        factor = factor.mul(super.activations);

        super.errorsForPreviousLayer = errors.mul(factor);
    }

    @Override
    public void learn(float learningRate) {
        //nothing to do in this layer since it is a passive layer
        //there are not parameters to be adjusted
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("***********************************\n");
        out.append("Sigmoid layer with signature: "+Utils.intArray2String(this.inSignature,",","[]")+"\n");
        out.append("***********************************\n");

        return out.toString();
    }
}
