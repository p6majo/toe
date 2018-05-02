package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.network2.Data;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * A {@link Layer} is the basic building block (module) of a neural network
 * This abstract class contains all the required methods and attributes that every layer requires to have
 *
 *
 * @author p6majo
 * @version 2.0
 *
 *
 */
public abstract class Layer  {

    protected int layerIndex=0;
    protected String name ="";

    protected INDArray activations;
    protected INDArray errors;
    protected INDArray errorsForPreviousLayer;

    protected final int[] inSignature;
    protected final int[] outSignature;

    protected int batchSize;

    public void pushForward(Batch batch){
        batchSize = batch.getBatchSize();
    };

    public abstract void pullBack(INDArray errors);


    public Layer(int[] inSignature, int[] outSignature) {
        this.inSignature = inSignature;
        this.outSignature = outSignature;
    }

    /*********************/
    /*** Getter/Setter ***/
    /*********************/


    public INDArray getActivations(){
        return this.activations;
    }

    public INDArray getErrorsForPreviousLayer(){
        return this.errorsForPreviousLayer;
    }

    public INDArray getErrors(){
        return this.errors;
    }

    public int getLayerIndex(){
        return this.layerIndex;
    }

    public void setLayerIndex(int layerIndex){
        this.layerIndex = layerIndex;
    }

    public String toShortString(){
        return this.name;
    }

}
