package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.network2.Network;
import com.p6majo.math.network2.NetworkVisualizer2;
import com.p6majo.math.network2.Visualizable;
import com.p6majo.math.utils.Utils;
import com.princeton.ExtendedDraw;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.rng.distribution.impl.NormalDistribution;
import org.nd4j.linalg.factory.Nd4j;

import java.awt.*;

import static com.p6majo.math.network2.layers.LayerUtils.getFlattenedDimFromBatchShape;

/**
 * A layer that applies a linear transformation to the activations of the previous layer
 * The activation data is stretched out and all spatial information is lost
 * The output of the linear layer so far is a simple one dimensional vector of activations
 *
 * @author p6majo
 * @version 2.0
 * <p>
 * TODO in principle it is possible that a more generic structure of activations is returned by the linear Layer
 * TODO it would require a final reshaping of the activations and an initial reshaping of the incoming errors
 */
public class LinearLayer extends DynamicLayer implements Visualizable {

    private final int flattenedInDim;
    private final int flattenedOutDim;

    //stored as intermediate values to train the network
    private INDArray inputData;

    public LinearLayer(int inSignature, int outSignature, Network.Seed seed) {
        super(new int[] {inSignature}, new int[]{outSignature});
        super.name = "Linear Layer";

        flattenedInDim = inSignature;
        flattenedOutDim = outSignature;

        //according to the transformation out_i=w_{ij} in_j+b_i the index for the output activations is the row index and the
        //index for the incoming activations is the column index
        switch (seed) {
            case ALL_UNITY:
                super.weights = Nd4j.ones(new int[]{flattenedInDim, flattenedOutDim});
                super.biases = Nd4j.ones(new int[]{1, flattenedOutDim});
                break;
            case NO_BIAS:
                super.weights = Nd4j.rand(new int[]{flattenedInDim, flattenedOutDim}, new NormalDistribution(0, 0.5));
                super.biases = Nd4j.zeros(new int[]{1, flattenedOutDim});
                break;
            default:
                super.weights = Nd4j.rand(new int[]{flattenedInDim, flattenedOutDim}, new NormalDistribution(0, 0.5));
                super.biases = Nd4j.rand(new int[]{1, flattenedOutDim}, new NormalDistribution(0, 0.5));
                break;
        }
        super.trainableParameters.add(weights);
        super.trainableParameters.add(biases);
    }

    public LinearLayer(int inSignature, int outSignature) {
        this(inSignature, outSignature, Network.Seed.RANDOM);
    }


    @Override
    public void pushForward(Batch batch) {
        super.pushForward(batch);

        inputData = batch.getActivations().dup();
        //calculate activations
        //a_{bi}=z_{bj}*w_{ij}+b_i  (b is the batchData index, summation over j)
        //tmp has to be written first, such that the index b is the row index after multiplication
        super.activations = Nd4j.tensorMmul(inputData, this.weights, new int[][]{{1}, {0}});
        super.activations.addiRowVector(biases);

        batch.setActivations(activations);
    }

    private void checkBatchConsistency(Batch batch) {
        int[] batchSignature = batch.getActivations().shape();
        int[] dataSignature = new int[batchSignature.length - 1];
        int dim = 1;
        for (int n = 1; n < batchSignature.length; n++) {
            dataSignature[n - 1] = batchSignature[n];
            dim *= batchSignature[n];
        }
        if (dim != flattenedInDim)
            Utils.errorMsg("Incompatible data in Linear layer.\n Received data signature: " + Utils.intArray2String(dataSignature, ",", "[]") + "\n, for the Layer structure " + Utils.intArray2String(this.inSignature, ",", "\n"));
    }


    @Override
    public void pullBack(INDArray errors) {
        //the errors for the neurons of the layer are calculated in the preceding layer
        //super.errors = errors.dup();
        super.errors = errors;

        //the simple formular for the pullback is given by delta_{l} = w^{T}*delta_{l+1}
        //reshaping follows to obtain the correct structure

        if (layerIndex > 0) {
            if (checkErrorBatchConsistency(errors)) {
                //the first index of the errors is the batch index, this index has to be preserved
                //transposing is omitted instead the weights are multiplied with respect to their first index
                //delta_{bi}=delta_{bj}*w_{ji}
                super.errorsForPreviousLayer = Nd4j.tensorMmul(errors, weights, new int[][]{{1}, {1}});
                // int[] newShape = new int[this.inSignature.length + 1];
                // newShape[0] = errors.shape()[0];
                // for (int d = 1; d < newShape.length; d++) newShape[d] = this.inSignature[d - 1];
                // super.errorsForPreviousLayer = this.errorsForPreviousLayer.reshape(newShape);
            } else {
                Utils.errorMsg("Wrong signature of error data in linear layer. Received: " + Utils.intArray2String(errors.shape(), ",", "[]") + "\nFor a layer with " + this.flattenedOutDim + " neurons.");
            }
        }
    }

    private boolean checkErrorBatchConsistency(INDArray errors) {
        if (getFlattenedDimFromBatchShape(errors.shape()) == this.flattenedOutDim) return true;
        return false;
    }

    @Override
    public void learn(float learningRate) {
        INDArray corrections = this.errors.sum(0).div(batchSize);
        if (lambdaB!=0) corrections.addi(this.biases.mul(2f*lambdaB));
        this.biases.subi(corrections.muli(learningRate));//adjust biases
        //at this point there was some strange behaviour of the nd4j method mul. If muli is replaced by mul, somehow the components of the correction tensor are strangely shuffled





        this.weights.subi(getWeightCorrections().muli(learningRate));//adjust weights
    }

    public INDArray getWeights() {
        return this.weights;
    }

    public INDArray getBiases() {
        return this.biases;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("Linear Layer with the following specifications:\n");
        out.append("***********************************************\n");
        out.append("Input signature: " + Utils.intArray2String(this.inSignature, ",", "[]") + "\n");
        out.append("Output length: " + this.flattenedOutDim + "\n");
        out.append("***********************************************\n");
        out.append("The weights are given by:\n");
        out.append(this.weights + "\n");
        out.append("***********************************************\n");
        out.append("The biases are given by:\n");
        out.append(this.biases + "\n");
        out.append("***********************************************\n");
        return out.toString();
    }


    public INDArray getWeightCorrections() {

        INDArray corrections = inputData.getRow(0).transpose().mmul(this.errors.getRow(0));
        for (int b = 1; b < batchSize; b++) {
            corrections.addi(inputData.getRow(b).transpose().mmul(this.errors.getRow(b)));
        }
        corrections.divi(batchSize);
        if (lambdaW!=0f) corrections.addi(this.weights.mul(2f*lambdaW));
        return corrections;
    }


    @Override
    public String getDetailedErrors() {
        StringBuilder out = new StringBuilder();

        out.append("Corrections to the biases:\n" + this.getErrors());
        out.append("\nCorrections to the weights:\n" + this.getWeightCorrections());

        return out.toString();
    }


    @Override
    public void drawVertices(ExtendedDraw frame, int width, int xoffset, int yoffset, int layerIndex, double layerSpacing) {
        double neuronSpacing = (double) width / (this.flattenedInDim - 1);
        double r = width / this.flattenedInDim / 3; //radius of the vertex
        for (int n = 0; n < flattenedInDim; n++) {
            frame.setPenColor(value2color(this.inputData.getRow(0).getFloat( n))); //only the first data of a batch is visualized
            frame.filledCircle(xoffset + n * neuronSpacing, yoffset + layerIndex * layerSpacing, r);
        }
    }

    @Override
    public void drawEdges(ExtendedDraw frame, int width, int xoffset, int yoffset, int layerIndex, double layerSpacing, NetworkVisualizer2.VisualizerModus vModus) {
        double nNeuronSpacing = (double) width/ (flattenedOutDim - 1);
        double mNeuronSpacing = (double) width / (flattenedInDim - 1);

        switch (vModus) {
            case ALL_EDGES:
                   for (int n = 0; n < flattenedOutDim; n++)
                        for (int m = 0; m < flattenedInDim; m++) {
                            frame.setPenColor(this.weight2color(this.weights.getFloat(m,n)));
                            frame.line(xoffset + m * mNeuronSpacing, yoffset + (layerIndex ) * layerSpacing, xoffset + n * nNeuronSpacing, yoffset + (layerIndex+1) * layerSpacing);
                        }
                break;
            case TRAINED_EDGES:
                   for (int n = 0; n < flattenedOutDim; n++)
                        for (int m = 0; m < flattenedInDim; m++) {
                            float weight = this.weights.getFloat(m,n);
                            if (Math.abs(weight) > COLOR_SATURATION / 2) {
                                frame.setPenColor(this.weight2color(weight));
                                frame.line(xoffset + m * mNeuronSpacing, yoffset + (layerIndex ) * layerSpacing, xoffset + n * nNeuronSpacing, yoffset + (layerIndex+1) * layerSpacing);
                            }
                        }
                break;
        }


    }

    @Override
    public void drawText(ExtendedDraw frame, int width, int xoffset, int yoffset, int layerIndex, double layerSpacing) {
        int neurons = this.flattenedInDim;
        double radius = width / neurons / 3 + 1;
        double neuronSpacing = (double) width / (neurons - 1);
        for (int n = 0; n < neurons; n++) {
            //only show labels for the dynamic neurons if there are fewer than 15
            if (neurons < 15)
                frame.text(xoffset + n * neuronSpacing + 20, yoffset + layerIndex * layerSpacing, n + DELIMITER + neurons);
            frame.setPenColor(Color.black);
           // frame.setPenRadius(0.5);
            frame.circle(xoffset + n * neuronSpacing, yoffset + layerIndex * layerSpacing, radius);
        }
    }
}
