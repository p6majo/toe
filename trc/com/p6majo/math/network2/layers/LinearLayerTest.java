package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.Batch;
import com.p6majo.math.network2.Data;
import org.junit.Before;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.rng.distribution.impl.NormalDistribution;
import org.nd4j.linalg.factory.Nd4j;

import static org.junit.Assert.*;

public class LinearLayerTest {

    @Before
    public void setup() {
    }

    @Test
    public void reshapingTest() {
        System.out.println("Test the flattening process: ");
        System.out.println("There are two ways of flattening: toFlattened and reshape. The latter method seems to be more flexible, since it allows a partial flattening. Start with a full flattening example with either methods:");

        int[] signature = new int[]{2, 3};
        INDArray test = Nd4j.create(new float[][]{{1, 2}, {3, 4}, {5, 6}});
        System.out.println("original tensor:\n " + test);
        INDArray flat1 = Nd4j.toFlattened(test);
        System.out.println("flattened tensor: (with toFlattened)\n" + flat1);
        INDArray flat2 = test.reshape(1, 6);
        System.out.println("flattened tensor: (with reshape)\n" + flat2);
        System.out.println("check, whether the input tensor is overwritten: \n" + test);

        System.out.println("Check, which method is faster: \n");
        long start = System.currentTimeMillis();
        test = Nd4j.rand(new int[]{500, 500, 500}, new NormalDistribution(0, 2));
        System.out.println("Time for creation of a 1000x1000x1000 tensor: " + (System.currentTimeMillis() - start) + " ms.");
        start = System.currentTimeMillis();
        Nd4j.toFlattened(test);
        System.out.println("Time for flattening with toFlattend: " + (System.currentTimeMillis() - start) + " ms.");
        start = System.currentTimeMillis();
        test.reshape(1, 125000000);
        System.out.println("Time for flattening with reshape: " + (System.currentTimeMillis() - start) + " ms.");
        start = System.currentTimeMillis();
        test.reshape(new int[]{500, 250000});
        System.out.println("Time for a partial flattening with reshape: " + (System.currentTimeMillis() - start) + " ms.");

        System.out.println("reshape is the method that we want to use. Let's see how it actually flattens partially.");

        float[] floatarray = new float[2 * 3 * 4];
        for (int n = 0; n < 2 * 3 * 4; n++) floatarray[n] = n;

        INDArray test2 = Nd4j.create(floatarray, new int[]{2, 3, 4});
        System.out.println("2,3,4-tensor before flattening: " + test2);
        test2 = test2.reshape(new int[]{2, 12});
        System.out.println("2,12-tensor after flattening: " + test2);
        System.out.println("Works as required!!!\n Conclusion from this test. Reshaping is straightforward and cheap. Let's go for it.");
    }

    @Test
    public void pushForward() {
        System.out.println("Now testing the push Forward of the Linear Layout for a batch of data");
        System.out.println("Generate data set: ");

        Data[] data = new Data[3];
        for (int i = 0; i < 3; i++) {
            float[] pod = new float[8]; //piece of data
            for (int n = 0; n < 8; n++) pod[n] = (n + 1) * (i + 1);
            data[i] = new Data(Nd4j.create(pod, new int[]{4, 2}), Nd4j.create(new float[]{1}, new int[]{1}));
        }

        Batch batch = new Batch(data);
        System.out.println(batch);

        System.out.println("Set up a (4,2)->(1,10)-Linear Layer");
        LinearLayer ll = new LinearLayer(new int[]{4, 2}, 10);
        System.out.println("The following weights: ");
        System.out.println(ll.getWeights());

        System.out.println("Access weights component wise:");
        for (int a = 0; a < 10; a++) {
            for (int r = 0; r < 4; r++)
                for (int c = 0; c < 2; c++) {
                    System.out.print(ll.getWeights().getFloat(c + 2 * r, a) + " ");
                }
            System.out.println();
        }


        System.out.println("and biases are generated: ");
        System.out.println(ll.getBiases());
        System.out.println("Test the tensor product: ");
        ll.pushForward(batch);
        System.out.println(ll.getActivations());

        System.out.println("Check calculations: ");

        float[][] test = new float[3][10];

        for (int b = 0; b < 3; b++) {
            System.out.println("transformation for data " + b + " of the batch:");
            String out = "[";
            for (int a = 0; a < 10; a++) {
                float sum = 0f;
                for (int r = 0; r < 4; r++)
                    for (int c = 0; c < 2; c++) {
                        sum += data[b].getInput().getFloat(r, c) * ll.getWeights().getFloat(c + 2 * r, a);
                    }
                sum += ll.getBiases().getFloat(a);
                test[b][a] = sum;
                out += sum + ",";
            }
            out = out.substring(0, out.length() - 1) + "]";
            System.out.println(out);
        }

        INDArray testArray = Nd4j.create(test);

        boolean testresult = testArray.equals(ll.getActivations());
        //Test for the calculation of activations
        assertTrue(testresult);
        if (testresult) System.out.println("Push forward of batch for linear layer was successful.");
        System.out.println("****\n\n");
    }


    @Test
    public void pullBack() {
        System.out.println("Check the backward pass for the errors:\n");
        INDArray errors = Nd4j.ones(10);
        System.out.println(errors);

        LinearLayer ll = new LinearLayer(new int[]{2, 3}, 10);
        ll.pullBack(errors);
        System.out.println(ll.toString());
        System.out.println("The following errors are calculated:\n");
        System.out.println(ll.getErrorsForPreviousLayer());
        System.out.println("The fact, that the dimensions are obtained correctly, shows, that the multiplication is performed correctly.\n");
        System.out.println("With this simple setup of errors, this just corresponds to summing all weights row-wise.\n");
        System.out.println("Let's apply the corresponding command to the weights and reshape.\n");

        INDArray test = ll.getWeights().sum(1);
        test = test.reshape(new int[]{2,3});
        boolean testresult = test.equals(ll.getErrorsForPreviousLayer());
        System.out.println(test);
        if (testresult) {
            System.out.println("Error calculation of a single piece of data successful\n\n\n");
        }

        System.out.println("Pull back for a batch of data:\n");
        errors = Nd4j.ones(3,10);
        System.out.println("We need to pass a batch of data first, that the system now\n");

        System.out.println(errors);

        ll.pullBack(errors);

        System.out.println(ll.getErrorsForPreviousLayer()+"\n\n");

        INDArray fac1 = Nd4j.create(new float[]{1f,2f,3f});
        INDArray fac2 = Nd4j.create(new float[]{4f,5f});


        System.out.println("Test tensor product of two vectors to form a matrix: \n");
        System.out.println("factor 1:\n" + fac2);
        System.out.println("factor 2:\n"+fac1+"\n");
        INDArray prod = Nd4j.tensorMmul(fac2,fac1,new int[][]{{0},{0}});
        System.out.println(prod+"\n");

        System.out.println("factor 1:\n" + fac1);
        System.out.println("factor 2:\n"+fac2+"\n");
        prod = Nd4j.tensorMmul(fac1,fac2,new int[][]{{0},{0}});
        System.out.println(prod+"\n");

        prod = prod.sum(0);
        System.out.println(prod);

    }

    @Test
    public void learningTest(){
        float learningRate = 0.1f;
        LinearLayer ll = new LinearLayer(new int[]{3,4},10);

        INDArray input = Nd4j.ones(3,4);
        System.out.println("Process trivial input data: \n"+input);
        Data data = new Data(input,Nd4j.zeros(10));

        INDArray error = Nd4j.ones(10);

        INDArray input2 = Nd4j.zeros(3,4);
        Data data2 = new Data(input2,Nd4j.ones(10));
        Batch batch = new Batch(data,data2);
        System.out.println("Check finally for a batch of data: \n"+batch+"\n");
        INDArray errors = Nd4j.ones(2,10);

        System.out.println("With the errrors: \n"+errors+"\n");

        ll.pushForward(batch);
        ll.pullBack(errors);
        ll.learn(learningRate);

        System.out.println(ll+"\n\n");
        System.out.println("The constraints on the structure of the tensors simplify concerns of internal consistencies. Once the objects end up having the correct shape, it is very likely that the correct transformations have been performed.");

    }




}