package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A {@link DynamicLayer} is the basic building block (module) of a neural network
 * This abstract class contains all the required methods and attributes that every layer requires to have
 *
 *
 * @author p6majo
 * @version 2.0
 *
 *
 */
public abstract class DynamicLayer extends Layer{

    protected List<INDArray> trainableParameters;
    protected float lambda;

    protected  INDArray weights;
    protected  INDArray biases;


    public DynamicLayer(int[] inSignature, int[] outSignature) {
        super(inSignature, outSignature);
        trainableParameters = new ArrayList<INDArray>();
    }

    public List<INDArray> getTrainableParameters(){
        return this.trainableParameters;
    }

    /**
     * This method is mainly used for testing purposes
     * It explicitly gives the corrections to the adjustable parameters like weights and biases
     * of the layer
     *
     * @return
     */
    public abstract String getDetailedErrors();

    /**
     * returns the square sum of all weights and biases multiplied by the regularization parameter lambda
     * @return
     */
    public float getRegularization() {
            if (lambda==0f) return 0f;
            else {
            float reg = 0f;
            reg += Nd4j.sum(this.weights.mul(this.weights)).getFloat(0, 0) * lambda;
            reg += Nd4j.sum(this.biases.mul(this.biases)).getFloat(0) * lambda;
            return reg;
        }
    }

    public void setRegularization(float lambda){
        this.lambda = lambda;
    }

}
