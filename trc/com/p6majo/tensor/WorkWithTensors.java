package com.p6majo.tensor;

import com.p6majo.math.utils.Utils;
import org.junit.Test;
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Arrays;

public class WorkWithTensors {


    @Test
    public void combiningTest() {

        INDArray one = Nd4j.create(new float[]{1,2,3,4},new int[]{2,2});
        System.out.println(one);

        INDArray two = Nd4j.create(new float[]{5,6,7,8},new int[]{2,2});
        System.out.println(two);

        INDArray three = Nd4j.create(new float[]{9,10,11,12},new int[]{2,2});
        System.out.println(three);




    INDArray stack = Nd4j.vstack(one,two,three);
    System.out.println(stack);
    System.out.println("Index structure of stack: "+Utils.intArray2String(stack.shape(),",","[]"));
    INDArray reshaped = stack.reshape(3,2,2);
    System.out.println(reshaped);
    System.out.println("Index structure of reshaped stack: "+Utils.intArray2String(reshaped.shape(),",","[]"));


        INDArray four = Nd4j.create(new float[]{1,2,3,4,5,6,7,8},new int[]{2,2,2});
        System.out.println(four);




    }


}
