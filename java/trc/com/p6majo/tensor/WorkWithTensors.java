package com.p6majo.tensor;

import com.p6majo.math.utils.Utils;
import org.junit.Test;
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.BooleanIndexing;
import org.nd4j.linalg.indexing.conditions.Conditions;

import java.util.Arrays;

import static org.nd4j.linalg.factory.Nd4j.min;

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


        System.out.println("Regularize tensors for the cross entropy calculations:");
        float[] tmp = new float[15];
        for (int i=0;i<15;i=i+3)  tmp[i]=(float) Math.random();

        tmp[0]=1f;
        tmp[5]=1f;
        float EPS = 0.0000001f;

        INDArray values= Nd4j.create(tmp,new int[]{3,5});
        System.out.println(values);
        System.out.println("Now regularization: ");
        BooleanIndexing.replaceWhere(values,EPS, Conditions.equals(0));
        BooleanIndexing.replaceWhere(values,1-EPS,Conditions.equals(1));
        System.out.println(values);

        values = values.mul(5);
        System.out.println(values);
        values = values.div(5);
        System.out.println(values);

        float[] t = new float[]{EPS,1-EPS,EPS,1-EPS,0.5f,0.5f};
        float[] e = new float[]{EPS,1-EPS,1-EPS,EPS,0.5f,EPS};
        INDArray ee = Nd4j.create(e);
        INDArray tt= Nd4j.create(t);
        System.out.println(ee);
        System.out.println(tt);
        System.out.println(ee.sub(1).div(tt.sub(1)).sub(ee.div(tt)));

        ee = ee.put(0,3,5);
        System.out.println(ee);
    }


}
