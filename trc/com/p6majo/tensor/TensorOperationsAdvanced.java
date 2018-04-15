package com.p6majo.tensor;

import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.BooleanIndexing;
import org.nd4j.linalg.indexing.conditions.Conditions;
import org.nd4j.linalg.indexing.functions.Value;
import org.nd4j.linalg.lossfunctions.ILossFunction;

public class TensorOperationsAdvanced {
    @Test
    public void advancedOperations(){
        INDArray tensor = Nd4j.rand(new int[]{3,2,4});
        System.out.println(tensor);

        System.out.println("Select a single row at top level");


        for (int r = 0;r<tensor.shape()[0];r++)
            System.out.println(tensor.getRow(r));

        System.out.println("Select a single row at the next level");

        for (int r=0;r<tensor.shape()[0];r++)
            for (int c=0;c<tensor.shape()[1];c++)
                System.out.println(tensor.getRow(r).getRow(c));

        System.out.println("This might be simulated by the dimension arg");

        System.out.println("Overall max: "+Nd4j.max(tensor));
        System.out.println("Rows with the largest row sum?"+Nd4j.max(tensor, 1));
        System.out.println("Max of each row: "+Nd4j.max(tensor,2));

        System.out.println("Find max in each row at top level");

        for (int r = 0;r<tensor.shape()[0];r++)
            System.out.println(Nd4j.max(tensor.getRow(r)));

        System.out.println("Find max in each row at next level");
        for (int r=0;r<tensor.shape()[0];r++)
            for (int c=0;c<tensor.shape()[1];c++)
                System.out.println(Nd4j.max(tensor.getRow(r).getRow(c)));


        System.out.println("Now have a closer look at manipulating our batch data in the loss layer");

        INDArray lossData = Nd4j.rand(new int[]{3,1,4});
        System.out.println(lossData);

        System.out.println("Find maximum in each row");
        for (int r = 0;r<lossData.shape()[0];r++)
            System.out.println(Nd4j.max(lossData.getRow(r)));

        System.out.println("Setting the max values in each row to one");
        System.out.println("before:\n" + lossData);

        for( int b = 0;b<lossData.shape()[0]; b++)
            BooleanIndexing.applyWhere(lossData.getRow(b), Conditions.equals(Nd4j.max(lossData.getRow(b)).getFloat(0,0)),new Value(1f));
        System.out.println("after:\n"+lossData);
        System.out.println("Setting other values to zero");
        System.out.println("before:\n" + lossData);
        BooleanIndexing.applyWhere(lossData, Conditions.lessThan(1),new Value(0f));
        System.out.println("after:\n"+lossData);

        System.out.println("Which tensor is overwritten with addi operations:");
        INDArray tens1 = Nd4j.rand(1,5);
        INDArray tens2 = Nd4j.rand(1,5);
        System.out.println(tens1);
        System.out.println(tens2);
        tens1.addi(tens2);
        System.out.println(tens1);
        System.out.println(tens2);


        System.out.println("Check, whether there is inefficiency introduced through multidimensional tensors");
        INDArray v1 = Nd4j.rand(new int[]{1,10000000});
        INDArray v2 = Nd4j.rand(new int[]{10000000,1});
        long start = System.currentTimeMillis();
        System.out.println(v1.mmul(v2));
        System.out.println((System.currentTimeMillis() - start) + " ms.");


        v1 = Nd4j.rand(new int[]{1,1,10000000});
        v2 = Nd4j.rand(new int[]{1,10000000,1});
        start = System.currentTimeMillis();
        System.out.println(Nd4j.tensorMmul(v1,v2,new int[][]{{1,2},{2,1}}));
        System.out.println((System.currentTimeMillis() - start) + " ms.");

        System.out.println("Access components: ");

    }

}
