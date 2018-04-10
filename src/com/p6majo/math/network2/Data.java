package com.p6majo.math.network2;

import com.p6majo.math.utils.Utils;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * A container for data that is applied to neural networks
 * In order to be compatible with the nd4j library the default type is float
 * The data can have an arbitrary tensor structure for the input
 * and an arbitrary tensor structure for the output
 *
 * different to the previous version the activations of the last layer are not stored
 * in the container
 *
 * @author p6majo
 * @version 2.0
 *
 */
public class Data {

    private INDArray input;
    private INDArray expectation;

    /**
     *
     * @param input    array of input data
     * @param expectation array of expected output data
     */
    public Data(INDArray input, INDArray expectation) {
        this.input = input;
        this.expectation = expectation;
    }

    /**
     * return the expected output
     * @return
     */
    public INDArray getExpectations(){
        return this.expectation;
    }

    public INDArray getInput(){
        return this.input;
    }

    public String toString(){
        StringBuilder out = new StringBuilder();
        out.append("i:\n"+ input+"\n");
        out.append("e:\n"+ expectation+"\n");
        return out.toString();
    }

    public void setExpectationToUnity(int position){
        this.expectation.put(0,position,1);
    }

    //TODO there should be helper functions that convert all types of data into the tensor structure
    /**************************/
    /***** Helper function ****/
    /**************************/
}
