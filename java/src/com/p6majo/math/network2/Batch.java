package com.p6majo.math.network2;

import com.p6majo.math.utils.Utils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.UnaryOperator;

/**
 * A batch of {@link Data} is the smallest unit that is processed through the network
 * The class provides a tensor object with one additional dimension that loops through the List
 * This dimension is the first dimension
 *
 * @author p6majo
 * @version 2.0
 *
 *
 */
public class Batch {

    private INDArray batchInput;
    private INDArray activations;
    private INDArray batchExpectation;
    private int batchSize;

    /**
    * The conversion is performed with build-in functions of the library
    * The collection of data is transformed into a vertical stack and finally the tensor is reshaped
    */

    /**
     * The batch is constructed with full data
     * Later adding or removing of data is not allowed
     * to avoid unnecessary reshaping
     *
     * @param data ArrayList of {@link Data}
     */
    public Batch(ArrayList<Data> data){
        Data[] myData = new Data[data.size()];
        data.toArray(myData);
        convert(myData);
    }

    /**
     * The batch is constructed with full data
     * Later adding or removing of data is not allowed
     * to avoid unnecessary reshaping
     * @param data Array of {@link Data}
     */
    public Batch(Data... data){
        convert(data);
    }

    private void convert(Data... data){
        if (data.length==0) Utils.errorMsg("Empty batches are not allowed.");
        this.batchSize = data.length;
        INDArray[] inputs =new INDArray[batchSize];
        INDArray[] expectations=new INDArray[batchSize];

        for (int n=0;n<data.length;n++){
            inputs[n]=data[n].getInput();
            expectations[n]=data[n].getExpectations();
        }

        INDArray inputStack = Nd4j.vstack(inputs);
        INDArray expectationStack = Nd4j.vstack(expectations);

        int[] shape = inputs[0].shape();
        int[] newShape = new int[shape.length+1];
        newShape[0]=batchSize;
        for (int n=0;n<shape.length;n++) newShape[n+1]=shape[n];

        batchInput = inputStack.reshape(newShape);
        this.activations = batchInput.dup();

        //expectations do not need a depth index, it is replaced with the batchSize index
        shape = expectations[0].shape();
        newShape = new int[shape.length];
        newShape[0]=batchSize;
        for (int n=1;n<shape.length;n++) newShape[n]=shape[n];

        batchExpectation = expectationStack.reshape(newShape);
    }

    /**
     * The batch is used to store intermediate values of the network, while it is passed along through the layers
     * This method resets the batch to its initial state
     *
     */
    public void resetBatch(){
        this.activations=this.batchInput.dup();
    }

    public int getBatchSize(){
        return this.batchSize;
    }

    public void setActivations(INDArray activations){
        this.activations = activations;
    }

    public INDArray getBatchExpectation(){
        return this.batchExpectation;
    }

    public INDArray getActivations() {
        return activations;
    }

    public String toString(){
        return "input:\n"+batchInput+"\n\noutput:\n"+batchExpectation;
    }
}
