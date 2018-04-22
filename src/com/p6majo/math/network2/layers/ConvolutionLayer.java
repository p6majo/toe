package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.network2.Network;
import com.p6majo.math.utils.Utils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.rng.distribution.impl.NormalDistribution;
import org.nd4j.linalg.api.shape.Shape;
import org.nd4j.linalg.convolution.Convolution;
import org.nd4j.linalg.factory.Nd4j;

import static com.p6majo.math.network2.layers.Utils.reflectKernel;

public class ConvolutionLayer extends DynamicLayer {

    private final int inDepth;
    private final int inWidth;
    private final int inHeight;
    private final int outDepth;
    private final int outWidth;
    private final int outHeight;

    private final int paddingWidth;
    private final int paddingHeight;
    private final int strideWidth;
    private final int strideHeight;
    private final int kernelHeight;
    private final int kernelWidth;

    private int batchSize;

    private INDArray inputData=null; //used for backpropagation

    /**
     * Convolutional Layer
     *      * some care has to be taken with the order of components
     *      * Tensor components are always sorted by rows and columns, i.e. 3x4 tensors have 3 columns and 4 rows, this corresponds to a height of 3 and a width of 4
     *      * Width and Height always indicate spatial components and they are always sorted like coordinates ie.: widthxheight
     *      * The kernel, the padding and the stride are specified in width and height
     * @param inSignature
     * @param kernelWidth
     * @param kernelHeight
     * @param numberOfFilters
     * @param strideWidth
     * @param strideHeight
     * @param paddingWidth
     * @param paddingHeight
     * @param seed
     */
    public ConvolutionLayer(int[] inSignature, int kernelWidth,int kernelHeight, int numberOfFilters,int strideWidth, int strideHeight, int paddingWidth, int paddingHeight, Network.Seed seed) {
        super(inSignature, new int[] {numberOfFilters,(inSignature[2]-kernelHeight+2*paddingHeight)/strideHeight+1,(inSignature[1]-kernelWidth+2*paddingWidth)/strideWidth+1});


        if (inSignature.length<3)
            Utils.errorMsg("The input of a convolution layer has to be a rank 3 tensor. The first index holds the depth of the input, e.g. 3 for red, green, blue, the second index holds the height (number of rows) and the last index the width.");

        this.inDepth = inSignature[0];
        this.inWidth = inSignature[2];
        this.inHeight = inSignature[1];
        this.outDepth = numberOfFilters;
        this.outHeight = (inSignature[1]-kernelHeight+2*paddingHeight)/strideHeight+1;
        this.outWidth = (inSignature[2]-kernelWidth+2*paddingWidth)/strideWidth+1;


        this.strideHeight=strideHeight;
        this.strideWidth=strideWidth;
        this.paddingHeight=paddingHeight;
        this.paddingWidth=paddingWidth;
        this.kernelHeight=kernelHeight;
        this.kernelWidth = kernelWidth;

        switch(seed){
            case ALL_UNITY:
                super.weights = Nd4j.ones(new int[]{outDepth,inDepth,kernelHeight,kernelWidth});
                super.biases = Nd4j.ones(new int[]{outDepth,outHeight,outWidth});
                break;
            case NO_BIAS:
                super.weights = Nd4j.rand(new int[]{outDepth,inDepth,kernelHeight,kernelWidth}, new NormalDistribution(0, 0.5));
                super.biases = Nd4j.zeros(new int[]{outDepth,outHeight,outWidth});
                break;
            default:
                super.weights = Nd4j.rand(new int[]{outDepth,inDepth,kernelHeight,kernelWidth}, new NormalDistribution(0, 0.5));
                super.biases = Nd4j.rand(new int[]{outDepth,outHeight,outWidth}, new NormalDistribution(0, 0.5));
                break;
        }

        super.trainableParameters.add(weights);
        super.trainableParameters.add(biases);


    }

    /**
     * This method is mainly used for testing purposes
     * It explicitly gives the corrections to the adjustable parameters like weights and biases
     * of the layer
     *
     * @return
     */
    @Override
    public String getDetailedErrors() {
        return null;
    }

    @Override
    public float getRegularization() {
        return 0;
    }

    /**
     * performing the convolution
     * @param batch
     */
    @Override
    public void pushForward(Batch batch) {

        this.batchSize = batch.getBatchSize();
        this.inputData = batch.getActivations();

        INDArray col = Nd4j.createUninitialized(new int[] {batchSize, outHeight, outWidth, inDepth, kernelHeight, kernelWidth}, 'c');
        INDArray col2 = col.permute(0, 3, 4, 5, 1, 2);
        Convolution.im2col(batch.getActivations(), kernelHeight, kernelWidth, strideHeight, strideWidth, paddingHeight, paddingWidth,  false, col2);
        INDArray im2col2d = Shape.newShapeNoCopy(col, new int[] {batchSize * outHeight * outWidth, inDepth * kernelHeight * kernelWidth}, false);

        INDArray permutedW = this.weights.permute(3,2,1,0);
        INDArray reshapedW = permutedW.reshape('f', kernelWidth * kernelHeight * inDepth, outDepth);

        this.activations = im2col2d.mmul(reshapedW);
        this.activations = Shape.newShapeNoCopy(this.activations, new int[] {outWidth, outHeight, batchSize, outDepth}, true);
        this.activations = activations.permute(2,3,1,0); //batchSize, outDepth, outHeight, outWidth
        batch.setActivations(this.activations);
    }

    @Override
    public void pullBack(INDArray errors) {
        INDArray col = Nd4j.createUninitialized(new int[]{batchSize,inHeight,inWidth,outDepth,kernelHeight,kernelWidth},'c');
        INDArray col2= col.permute(0,3,4,5,1,2);
        Convolution.im2col(errors,kernelHeight,kernelWidth,strideHeight,strideWidth,paddingHeight+kernelHeight-1,paddingWidth+kernelWidth-1,false,col2);
        INDArray im2col2d = Shape.newShapeNoCopy(col, new int[]{batchSize*inHeight*inWidth,outDepth*kernelHeight*kernelWidth},false);

        INDArray permutedW = reflectKernel(this.weights).permute(3,2,1,0);
        INDArray reshapedW = permutedW.reshape('f',kernelWidth*kernelHeight*outDepth,inDepth);

        this.errorsForPreviousLayer = im2col2d.mmul(reshapedW);
        this.errorsForPreviousLayer=Shape.newShapeNoCopy(this.errorsForPreviousLayer,new int[]{inWidth,inHeight,batchSize,inDepth},true);
        this.errorsForPreviousLayer = this.errorsForPreviousLayer.permute(2,3,1,0);
    }

    @Override
    public void learn(float learningRate) {

    }

    private INDArray getCorrectionsForWeights(){
        /*
        The back propagation is a convolution of the kernel from the errors with the input activations of this layer
        out dimensions become kernel dimensions
        and kernel dimensions become the new out dimensions
         */
        INDArray col = Nd4j.createUninitialized(new int[]{batchSize,this.kernelHeight,kernelWidth,outDepth,outHeight,outWidth},'c');
        INDArray col2 = col.permute(0,3,4,5,1,2);
        Convolution.im2col(this.errors,outHeight,outWidth,strideHeight,strideWidth,paddingHeight,paddingWidth,false,col2);
        INDArray im2col2d = Shape.newShapeNoCopy(col,new int[]{batchSize*kernelHeight*kernelWidth*inDepth,outDepth*outHeight*outWidth},false);

        INDArray permutedW = inputData.permute(3,2,1,0);
        INDArray reshapedW=permutedW.reshape('f',outWidth*outHeight*outDepth,inDepth);
        INDArray corrections = im2col2d.mmul(reshapedW);
        corrections = Shape.newShapeNoCopy(corrections,new int[]{kernelWidth,kernelHeight,batchSize,inDepth},true);
        corrections = corrections.permute(2,3,1,0);
        return corrections;
    }

    @Override
    public String toString(){
        StringBuilder out = new StringBuilder();
        out.append("Convolutional Layer with the following specifications:\n");
        out.append("***********************************************\n");
        out.append("Input signature (depth x rows x cols): " + Utils.intArray2String(this.inSignature, "x", "") + "\n");
        out.append("Output signature (depth x rows x cols): " + Utils.intArray2String(new int[]{this.outDepth,this.outHeight,this.outWidth},"x","") + "\n");
        out.append("Padding (rows x cols): "+Utils.intArray2String(new int[]{paddingHeight,paddingWidth},"x","")+"\n");
        out.append("Stride (rows x cols): "+Utils.intArray2String(new int[]{strideHeight,strideWidth},"x","")+"\n");
        out.append("***********************************************\n");
        out.append("The "+outDepth+" filters are specified by: "+Utils.intArray2String(new int[]{inDepth,kernelHeight,kernelWidth},"x","")+"\n");
        out.append("***********************************************\n");
        for(int i = 0; i <outDepth; i++) {
            out.append("Filter "+(i+1)+": \n");
            out.append(this.weights.getRow(i)+"\n");
            out.append("***********************************************\n");
        }
        out.append("And the "+outDepth+" biases are specified by: "+Utils.intArray2String(new int[]{outDepth,outHeight,outWidth},"x","")+"\n");
        out.append("***********************************************\n");
        for(int i = 0; i <outDepth; i++) {
            out.append("Bias "+(i+1)+": \n");
            out.append(this.biases.getRow(i)+"\n");
            out.append("***********************************************\n");
        }

        return out.toString();
    }
}
