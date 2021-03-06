package com.p6majo.math.network2.layers;

import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import static org.junit.Assert.assertEquals;

public class LayerUtilsTest {

    @Test
    public void reflectKernel() {

        System.out.println("Weights of rank 4 case: \n");
        int dim = 2*((int) (Math.random()*4))+1;//should be odd;
        int inDepth = (int) Math.max(1,(Math.random()*5));
        int outDepth = (int) Math.max(1,(Math.random()*5));
        System.out.println("with outDepth: "+outDepth+", inDepth: "+inDepth+" and kernel height and width: " + dim+"\n");

        INDArray kernel = Nd4j.rand(new int[]{outDepth, inDepth,dim,dim});
        System.out.println("Kernel: \n" + kernel);

        INDArray reflectedKernel = LayerUtils.reflectKernel(kernel);
        System.out.println("reflected Kernel: \n"+reflectedKernel);


        System.out.println("apply default rot function:\n"+Nd4j.rot(kernel));

        //test a few components randomly
        for (int c = 0; c < 10; c++) {
            int i =(int) (Math.random()*inDepth);
            int o =(int) (Math.random()*outDepth);
            int x=(int) (Math.random()*dim);
            int y=(int) (Math.random()*dim);
            assertEquals(kernel.getRow(o).getRow(i).getFloat(x,y),reflectedKernel.getRow(o).getRow(i).getFloat(dim-1-x,dim-1-y),1.e-5);
        }

    }
}