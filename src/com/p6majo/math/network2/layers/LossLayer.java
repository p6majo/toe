package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.network2.Data;
import com.p6majo.math.network2.Network;
import com.p6majo.math.network2.TestResult;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * A {@link LossLayer} is the basic building block (module) of a neural network
 * This abstract class contains all the required methods and attributes that every layer requires to have
 *
 *
 * @author p6majo
 * @version 2.0
 *
 *
 */
public abstract class LossLayer extends Layer{


    public LossLayer(int[] inSignature) {
        super(inSignature, inSignature);
    }

    public abstract float getLoss();
  
    public abstract INDArray getLossGradient();

    public abstract TestResult getTestResult(Network.Test test);




}
