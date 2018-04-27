package com.p6majo.tensor;

import org.junit.Test;
import org.nd4j.linalg.api.complex.IComplexNumber;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.BaseOp;
import org.nd4j.linalg.api.ops.BaseTransformOp;
import org.nd4j.linalg.api.ops.Op;
import org.nd4j.linalg.api.ops.impl.accum.AMin;
import org.nd4j.linalg.api.ops.impl.transforms.*;
import org.nd4j.linalg.api.rng.distribution.impl.NormalDistribution;
import org.nd4j.linalg.factory.Nd4j;

public class RampOperations {
    @Test
    public void ramping(){
        System.out.println("RectifedLinear");
        INDArray test = Nd4j.rand(new int[]{10,10},new NormalDistribution(0,2));
        System.out.println(test);
        Nd4j.getExecutioner().exec(new RectifedLinear(test,0));
        System.out.println(test);
        test = Nd4j.rand(new int[]{10,10},new NormalDistribution(0,2));
        System.out.println(test);
        //Nd4j.getExecutioner().exec(new LeakyReLU(test));

        //Nd4j.getExecutioner().exec(new LeakyReLUDerivative(test));

        Nd4j.getExecutioner().exec(new Tanh(test));
        test.muli(0.5).addi(0.5);

        System.out.println(test);

        System.out.println("Check derivative:");
        test = Nd4j.rand(new int[]{5,5},new NormalDistribution(0,2));

        INDArray tmp = Nd4j.getExecutioner().execAndReturn(new Tanh(test.dup()));
        tmp.muli(0.5).addi(0.5);

        System.out.println(test);
        System.out.println(tmp);


        System.out.println("derivative: ");
        INDArray deriv = Nd4j.getExecutioner().execAndReturn(new TanhDerivative(test.dup()));
        deriv.muli(0.5);
        System.out.println(deriv);


        INDArray factor = tmp.sub(1);
        factor.muli(tmp.mul(-2));
        System.out.println(factor);

        test = Nd4j.rand(new int[]{5,5},new NormalDistribution(0,2));
        tmp = Nd4j.getExecutioner().execAndReturn(new SoftMax(test.dup()));

        System.out.println(test);
        System.out.println(tmp);


        test = Nd4j.rand(new int[]{5,5},new NormalDistribution(0,2));
        tmp = Nd4j.getExecutioner().execAndReturn(new HardSigmoid(test.dup()));

        System.out.println(test);
        System.out.println(tmp);

    }
}
