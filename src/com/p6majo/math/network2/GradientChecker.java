package com.p6majo.math.network2;

import com.p6majo.math.network2.layers.*;
import com.p6majo.math.utils.Utils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.random.impl.GaussianDistribution;
import org.nd4j.linalg.api.rng.distribution.Distribution;
import org.nd4j.linalg.api.rng.distribution.impl.NormalDistribution;
import org.nd4j.linalg.factory.Nd4j;

import java.util.List;
import java.util.function.Function;

import static com.p6majo.math.network2.Network.getFullDimensionOfParameter;
import static com.p6majo.math.network2.NetworkUtils.iterateThroughTensor;

/**
 * Test class to verify the back propagation for various types of dynamical layers
 * A network is set up, where the layers can be used
 *
 * Artificial data is generated with input data and expectations
 *
 * The errors are calculated numerically and via back propagation such that the comparison can be seen directly
 *
 */
public class GradientChecker {


    private final Network network;
    private final Batch batch;

    //input and output dimensions
    private static int inDepth=2;
    private static int inRows = 6;
    private static int inCols = 6;
    private static int outClasses = 2;



    public GradientChecker(){
        this(setupNetwork(),setupData());
    }

    public  GradientChecker(Network network,Batch batch){
        this.network = network;
        this.batch =batch;
    }

    private static  Network setupNetwork(){

        //input and output dimensions
       int inDepth=2;
       int inRows = 6;
       int inCols = 6;
       int outClasses = 2;

        Network network = new Network(false);

        SigmoidLayer sig = new SigmoidLayer(new int[]{inDepth,inRows,inCols});
        network.addLayer(sig);

        ConvolutionLayer conv = new ConvolutionLayer(new int[]{inDepth,inRows,inCols},3,3,3,1,1,0,0, Network.Seed.RANDOM);
        network.addLayer(conv);

        ConvolutionLayer conv2 = new ConvolutionLayer(new int[]{3,inRows-2,inCols-2},3,3,4,1,1,0,0, Network.Seed.RANDOM);
        network.addLayer(conv2);

        FlattenLayer flat = new FlattenLayer(new int[]{4,2,2});
        network.addLayer(flat);

        LinearLayer ll = new LinearLayer(16,outClasses);
        network.addLayer(ll);

        SigmoidLayer sig2 = new SigmoidLayer(new int[]{outClasses});
        network.addLayer(sig2);

        CrossEntropyLayer cel = new CrossEntropyLayer(new int[]{outClasses});
        network.addLayer(cel);

        network.setLearningRate(0.01f);
        network.setRegularization(0.f);
        return network;
    }

    private static Batch setupData(){

        INDArray input = Nd4j.rand(new int[]{inDepth,inRows,inCols},new NormalDistribution(0.,2.));
        INDArray output = Nd4j.zeros(new int[]{2});
        output.put(0,1,1f);

        Data data =new Data(input,output);
        Batch batch = new Batch(data);
        System.out.println(batch);
        return batch;
    }

    public void iterateThroughTensor(INDArray gradient, INDArray params){
        float epsilon = 1.e-2f;
        int[] shape = gradient.shape();
        int[] shape2 = params.shape();
        if (shape.equals(shape2)) Utils.errorMsg("Both tensors have to have the same shape, but received "+Utils.intArray2String(shape,"x","")+" and "+Utils.intArray2String(shape2,"x","")+" instead.");

        if (shape.length==2 && gradient.size(0)==1){
            for (int c = 0; c < gradient.size(1); c++) {
                //calc gradient component numerically
                LossLayer lossLayer = network.getLossLayer();
                batch.resetBatch();
                network.pushforward(batch);
                float loss1 = lossLayer.getLoss();

                params.put(0,c,(params.getFloat(0,c)+epsilon));
                batch.resetBatch();
                network.pushforward(batch);
                float loss2 = lossLayer.getLoss();
                gradient.put(0,c,(loss2-loss1)/epsilon);

                params.put(0,c,(params.getFloat(0,c)-epsilon));
            }
        }
        else{
            //System.out.println(Utils.intArray2String(tensor.shape(), "x", ""));
           // System.out.println("[");
            for (int r=0;r<gradient.size(0);r++)
                iterateThroughTensor(gradient.getRow(r),params.getRow(r));
            //System.out.println("]");
        }
    }

    /**
     * This is an auxiliary method that allows to compute the gradient for parameters of dynamical layers directly from
     * two forward passes for each parameter
     * The method is very buggy and is not generic for all tensor like parameters
     * @param layerIndex the layer under consideration
     * @param param the trainable parameter
     * @return
     */
    public String gradientCheck(int layerIndex,int param){
        //TODO only take first batch element, if many are provided

        DynamicLayer dynLayer = null;
        for (DynamicLayer layer:network.getDynamicLayers()){
            if (layer.getLayerIndex()==layerIndex) dynLayer = layer;
        }

        if (dynLayer!=null){
            List<INDArray> trainableParams = dynLayer.getTrainableParameters();

            INDArray params  = trainableParams.get(param);

            int[] shape = params.shape();
            int dim = getFullDimensionOfParameter(shape);



            StringBuilder out = new StringBuilder();

            out.append("calculated gradients:\n");


            INDArray gradients = Nd4j.zeros(shape);

            //System.out.println(params);

            iterateThroughTensor(gradients,params);

            out.append(gradients);
            //calculate gradients
            network.pullBack();
            out.append("\nPullback of errors:\n"+dynLayer.getErrors());
            //out.append("\nPullback of errors:\n"+dynLayer.getDetailedErrors());

            return out.toString();
        }
        else Utils.errorMsg("The provided layer index "+layerIndex+" does not correspond to a dynamic layer.\n It belongs to "+network.getLayer(layerIndex).toShortString()+" instead.");
        return null;
    }
}
