package com.p6majo.math.network2;

import com.p6majo.math.utils.Utils;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.util.function.Function;

public class NetworkUtils {

    public static void iterateThroughTensor(INDArray tensor1){
        int[] shape = tensor1.shape();

        if (shape.length==2 && tensor1.size(0)==1){
            System.out.println(tensor1);
        }
        else{
            //System.out.println(Utils.intArray2String(tensor.shape(), "x", ""));
            System.out.println("[");
            for (int r=0;r<tensor1.size(0);r++)
                iterateThroughTensor(tensor1.getRow(r));
            System.out.println("]");
        }
    }
}
