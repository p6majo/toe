package com.p6majo.math.network2;

import com.p6majo.math.network2.layers.*;
import com.p6majo.math.utils.Utils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.random.impl.GaussianDistribution;
import org.nd4j.linalg.api.rng.distribution.Distribution;
import org.nd4j.linalg.api.rng.distribution.impl.NormalDistribution;
import org.nd4j.linalg.factory.Nd4j;

import java.util.List;

import static com.p6majo.math.network2.Network.getFullDimensionOfParameter;

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
       int inRows = 4;
       int inCols = 4;
       int outClasses = 2;



        Network network = new Network(false);

        SigmoidLayer sig = new SigmoidLayer(new int[]{inDepth,inRows,inCols});
        network.addLayer(sig);

        FlattenLayer flat = new FlattenLayer(new int[]{inDepth,inRows,inCols});
        network.addLayer(flat);

        LinearLayer ll = new LinearLayer(inDepth*inRows*inCols,outClasses);
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

        INDArray input = Nd4j.rand(new int[]{2,4,4},new NormalDistribution(0.,2.));
        INDArray output = Nd4j.zeros(new int[]{2});
        output.put(0,1,1f);

        Data data =new Data(input,output);
        Batch batch = new Batch(data);
        System.out.println(batch);
        return batch;
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
            float epsilon = 1.e-3f;
            List<INDArray> trainableParams = dynLayer.getTrainableParameters();

            INDArray params  = trainableParams.get(param);

            int[] shape = params.shape();
            int dim = getFullDimensionOfParameter(shape);

            LossLayer lossLayer = network.getLossLayer();

            StringBuilder out = new StringBuilder();

            out.append("calculated gradients:\n[");

            float[] gradient = new float[dim];
            for (int i=0;i<dim;i++) {
                float[] perturbationData = new float[dim];
                perturbationData[i] = epsilon;

                INDArray perturbations = Nd4j.create(perturbationData, params.shape());

                batch.resetBatch();
                network.pushforward(batch);


                float loss = lossLayer.getLoss();

                //shift
                params.addi(perturbations);

                batch.resetBatch();
                network.pushforward(batch);

                float loss2 = lossLayer.getLoss();
                gradient[i] = (loss2 - loss) / epsilon;
                String gradientString = String.format("%.2f",gradient[i]);

                out.append(gradientString + " ");
                if ( (i+1)%16==0) out.append("\n");
                //undo shift
                params.subi(perturbations);
            }
            out.append("]\n");

            //calculate gradients
            network.pullBack();
            String errors = dynLayer.getDetailedErrors();

            if (param==0) {
                INDArray weightErrors = ((LinearLayer) dynLayer).getWeightCorrections();
                INDArray numGradient = Nd4j.create(gradient, weightErrors.shape());

                INDArray diff = weightErrors.sub(numGradient);
                float maxDiff = Nd4j.max(diff).getFloat(0, 0);
                float minDiff = Nd4j.min(diff).getFloat(0, 0);
                out.append("Pullback of errors:\n" + errors + "\nmaximum deviation: " + maxDiff + " or " + minDiff);
            }
            else{
                out.append("Pullback of errors:\n"+errors);
            }

            return out.toString();
        }
        else Utils.errorMsg("The provided layer index "+layerIndex+" does not correspond to a dynamic layer.\n It belongs to "+network.getLayer(layerIndex).toShortString()+" instead.");
        return null;
    }
}
