package com.p6majo.math.network2.layers;

import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void reflectKernel() {
        System.out.println("Start with rank 2 case: \n");
        int dim = 2*((int) (Math.random()*4))+1;//should be odd;
        System.out.println("and dimension: " + dim+"\n");
        INDArray kernel = Nd4j.rand(dim,dim);
        System.out.println("Kernel: \n" + kernel);

        INDArray reflectedKernel = Utils.reflectKernel(kernel);
        System.out.println("reflected Kernel: \n"+reflectedKernel);

        //test a few components randomly
        for (int i = 0; i < 10; i++) {
            int x=(int) (Math.random()*dim);
            int y=(int) (Math.random()*dim);
            assertEquals(kernel.getFloat(x,y),reflectedKernel.getFloat(dim-1-x,dim-1-y),1.e-5);
        }

        System.out.println("Continue with rank 3 case: \n");
        dim = 2*((int) (Math.random()*4))+1;//should be odd;
        int depth = (int) (Math.random()*5);
        System.out.println("and dimension: " + dim+" and depth "+depth+"\n");

        kernel = Nd4j.rand(new int[]{depth,dim,dim});
        System.out.println("Kernel: \n" + kernel);

        reflectedKernel = Utils.reflectKernel(kernel);
        System.out.println("reflected Kernel: \n"+reflectedKernel);

        //test a few components randomly
        for (int i = 0; i < 10; i++) {
            int d =(int) (Math.random()*depth);
            int x=(int) (Math.random()*dim);
            int y=(int) (Math.random()*dim);
            assertEquals(kernel.getRow(d).getFloat(x,y),reflectedKernel.getRow(d).getFloat(dim-1-x,dim-1-y),1.e-5);
        }

    }
}