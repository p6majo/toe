package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.utils.Utils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.transforms.Sigmoid;
import org.nd4j.linalg.api.ops.impl.transforms.Tanh;
import org.nd4j.linalg.api.ops.impl.transforms.TanhDerivative;
import org.nd4j.linalg.factory.Nd4j;

public class TanhLayer extends Layer {

    public TanhLayer(int[] signature) {
        //the layer doen't change the data structure
        super(signature, signature);
        super.name = "Tanh Layer";
    }

    private INDArray inputValues;

    @Override
    public void pushForward(Batch batch) {
        super.pushForward(batch);
        this.inputValues = batch.getActivations().dup();
        //the activations of the batch get overwritten by the Executioner
        super.activations = Nd4j.getExecutioner().execAndReturn(new Tanh(batch.getActivations())).muli(0.5).addi(0.5);
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

       //INDArray factor = activations.sub(1);
       //factor.muli(activations.mul(-2)); //derivative 0.5*(1-tanh(x)^2)
        INDArray factor = Nd4j.getExecutioner().execAndReturn(new TanhDerivative(inputValues));
        factor.muli(0.5);
       super.errorsForPreviousLayer = factor.mul(errors);
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("***********************************\n");
        out.append("Sigmoid layer with signature: "+Utils.intArray2String(this.inSignature,",","[]")+"\n");
        out.append("***********************************\n");

        return out.toString();
    }
}
