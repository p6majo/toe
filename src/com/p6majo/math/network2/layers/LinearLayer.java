package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.network2.Data;
import com.p6majo.math.utils.Utils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.rng.distribution.impl.NormalDistribution;
import org.nd4j.linalg.factory.Nd4j;

/**
 * A layer that applies a linear transformation to the activations of the previous layer
 * The activation data is stretched out and all spatial information is lost
 * The output of the linear layer so far is a simple one dimensional vector of activations
 *
 * @author p6majo
 * @version 2.0
 *
 * TODO in principle it is possible that a more generic structure of activations is returned by the linear Layer
 * TODO it would require a final reshaping of the activations and an initial reshaping of the incoming errors
 */
public class LinearLayer extends Layer {

    private final INDArray weights;
    private final INDArray biases;

    private final int flattenedInDim;
    private final int flattenedOutDim;

    //stored as intermediate values to train the network
    private INDArray inputData;

    public LinearLayer(int[] inSignature, int outSignature) {
        super(inSignature,new int[] {outSignature});

        flattenedInDim = flatDim(inSignature);
        flattenedOutDim = outSignature;

        //according to the transformation out_i=w_{ij} in_j+b_i the index for the output activations is the row index and the
        //index for the incoming activations is the column index
        weights = Nd4j.rand(new int[]{flattenedInDim,flattenedOutDim},new NormalDistribution(0,2));
        biases = Nd4j.rand(new int[]{flattenedOutDim},new NormalDistribution(0,2));
    }

    private int flatDim(int[] signature){
        int dim = 1;
        for (int d=0;d<signature.length;d++){
            dim*=signature[d];
        }
        return dim;
    }

    @Override
    public void pushForward(Batch batch) {
        //the dup() makes a copy and ensures that the data is not overwritten
        //flatten the batchData
        checkBatchConsistency(batch);

        int [] newShape = new int[]{batch.getBatchInput().shape()[0],flattenedInDim};
        inputData = batch.getBatchInput().reshape(newShape);
        //calculate activations
        //a_{bi}=z_{bj}*w_{ij}+b_i  (b is the batchData index, summation over j)
        //tmp has to be written first, such that the index b is the row index after multiplication
        super.activations = Nd4j.tensorMmul(inputData,this.weights,new int[][]{{1},{0}});
        super.activations.addiRowVector(biases);
    }

    private void checkBatchConsistency(Batch batch){
        int[] batchSignature = batch.getBatchInput().shape();
        int[] dataSignature = new int[batchSignature.length-1];
        int dim = 1;
        for (int n=1;n<batchSignature.length;n++) {
            dataSignature[n-1]=batchSignature[n];
            dim*=batchSignature[n];
        }
        if (dim!=flattenedInDim) Utils.errorMsg("Incompatible data in Linear layer.\n Received data signature: "+Utils.intArray2String(dataSignature,",","[]")+"\n, for the Layer structure "+Utils.intArray2String(this.inSignature,",","\n"));
    }

    @Override
    public void pushForward(Data data) {
        checkDataConsistency(data);
        inputData  = data.getInput().reshape(new int[]{1,flattenedInDim});
        super.activations =inputData.mmul(this.weights);
        super.activations.addi(biases);
    }

    private void checkDataConsistency(Data data){
        int[] dataSignature = data.getInput().shape();
        int dim = 1;
        for (int n=0;n<dataSignature.length;n++) dim*=dataSignature[n];

        if (dim!=flattenedInDim) Utils.errorMsg("Incompatible data in Linear layer.\n Received data signature: "+Utils.intArray2String(dataSignature,",","[]")+"\n, for the Layer structure "+Utils.intArray2String(this.inSignature,",","\n"));
    }

    @Override
    public void pullBack(INDArray errors) {
        //the errors for the neurons of the layer are calculated in the preceding layer
        super.errors = errors;

        //the simple formular for the pullback is given by delta_{l} = w^{T}*delta_{l+1}
        //reshaping follows to obtain the correct structure


        //errors have to be pull back only upto the first layer
        if (layerIndex>1) {
            if (checkErrorConsistency(errors)) {
                //the standard formula applies
                super.errorsForPreviousLayer = errors.mmul(weights.transpose());
                super.errorsForPreviousLayer = super.errorsForPreviousLayer.reshape(this.inSignature);
            } else if (checkErrorBatchConsistency(errors)) {
                //the first index of the errors is the batch index, this index has to be preserved
                //transposing is omitted instead the weights are multiplied with respect to their first index
                //delta_{bi}=delta_{bj}*w_{ji}
                super.errorsForPreviousLayer = Nd4j.tensorMmul(errors, weights, new int[][]{{1}, {1}});

                int[] newShape = new int[this.inSignature.length + 1];
                newShape[0] = errors.shape()[0];
                for (int d = 1; d < newShape.length; d++) newShape[d] = this.inSignature[d - 1];
                super.errorsForPreviousLayer = this.errorsForPreviousLayer.reshape(newShape);
            } else {
                Utils.errorMsg("Wrong signature of error data in linear layer. Received: " + Utils.intArray2String(errors.shape(), ",", "[]") + "\nFor a layer with " + this.flattenedOutDim + " neurons.");
            }
        }
    }

    private boolean checkErrorConsistency(INDArray errors){
        if (errors.shape()[0]==1 && errors.shape()[1]==this.flattenedOutDim) return true;
        return false;
    }

    private boolean checkErrorBatchConsistency(INDArray errors){
        if (errors.shape()[0]>1 && errors.shape()[1]==this.flattenedOutDim) return true;
        return false;
    }

    @Override
    public void learn(float learningRate) {
         //check whether the data is in a batch or single
        INDArray correction;
        if (this.inputData.shape()[0]>1) {
            int batchSize=this.inputData.shape()[0];
            this.biases.subi(this.errors.sum(0).mul(learningRate/batchSize)); //adjust biases
            //this is a bit tricky at this point
            //we have to combine the inputData with the errors to get a correction matrix with the dimensions of the weights
            //the input data is of the form bxm and the errors are of the form bxn
            //they are supposed to form an mxn correction matrix
            //the input and errors are reshaped and then the tensor product is performed batch-element-wise and summed over all batch elements
            //the input is reshaped into a b-vector of a row vector in each component
            int[] newInputShape = new int[3];
            newInputShape[0]=this.inputData.shape()[0];
            newInputShape[1]=1;
            newInputShape[2]=this.inputData.shape()[1];
            //the error data is reshaped into a b-vector of a column vector in each component
            int[] newErrorShape = new int[3];
            newErrorShape[0]=this.errors.shape()[0];
            newErrorShape[2]=1;
            newErrorShape[1]=this.errors.shape()[1];



            correction = Nd4j.tensorMmul(this.inputData.reshape(newInputShape), this.errors.reshape(newErrorShape), new int[][]{{0,1}, {0,2}});
            this.weights.subi(correction.mul(learningRate/batchSize));//adjust weights
        }
        else {
            this.biases.subi(this.errors.mul(learningRate));//adjust biases
            correction = Nd4j.tensorMmul(this.inputData, this.errors, new int[][]{{0}, {0}});
            this.weights.subi(correction.mul(learningRate));//adjust weights
        }
    }

    public INDArray getWeights(){
        return this.weights;
    }

    public INDArray getBiases(){
        return this.biases;
    }

    public String toString(){
        StringBuilder out = new StringBuilder();
        out.append("Linear Layer with the following specifications:\n");
        out.append("***********************************************\n");
        out.append("Input signature: "+Utils.intArray2String(this.inSignature,",","[]")+"\n");
        out.append("Output length: "+this.flattenedOutDim+"\n");
        out.append("***********************************************\n");
        out.append("The weights are given by:\n");
        out.append(this.weights+"\n");
        out.append("***********************************************\n");
        out.append("The biases are given by:\n");
        out.append(this.biases+"\n");
        out.append("***********************************************\n");
        return out.toString();
    }

}
