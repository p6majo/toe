package com.p6majo.tensor;

import com.p6majo.math.utils.Utils;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.transforms.Exp;
import org.nd4j.linalg.factory.Nd4j;

public class TensorProducts {
    @Test
    public void productTest(){
        INDArray fac1 = Nd4j.rand(3,5);
        INDArray fac2 = Nd4j.rand(3,2);

        INDArray result = Nd4j.tensorMmul(fac1,fac2,new int[][] {{0},{0}});
        System.out.println(result);

        INDArray fac3 = Nd4j.rand(1,3);
        INDArray fac4 = Nd4j.rand(4,1);
        result = Nd4j.tensorMmul(fac3,fac4,new int[][] {{0},{1}});
        System.out.println(result);

        System.out.println("Multiply 3x5-tensor with 3x2-tensor to obtain: ");
        System.out.println(fac1+"\n*\n"+fac2);
        int[] shape1 = new int[]{3,1,5};
        fac1=fac1.reshape(shape1);
        int[] shape2 = new int[]{3,2,1};
        fac2 = fac2.reshape(shape2);
        result = Nd4j.tensorMmul(fac1,fac2,new int[][]{{0,1},{0,2}});
        System.out.println("tensor of shape: "+ Utils.intArray2String(result.shape(),",","[]"));
        System.out.println(result);

        System.out.println("Check whether executioners overwrite the initial data");
        INDArray tmp = Nd4j.rand(4,3);
        System.out.println(tmp);
        INDArray tmp2 = Nd4j.getExecutioner().execAndReturn(new Exp(tmp.dup()));
        System.out.println(tmp);
        System.out.println(tmp2);
        System.out.println(tmp2.sumNumber().floatValue()+" summe");

    }
}
