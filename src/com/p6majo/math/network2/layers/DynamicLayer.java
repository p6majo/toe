package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import org.nd4j.linalg.api.ndarray.INDArray;

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

    public abstract  float getRegularization();

    public void setRegularization(float lambda){
        this.lambda = lambda;
    }

}
