package com.p6majo.tensor;

import com.sun.jna.platform.win32.COM.IStream;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Arrays;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

public class TensorTest {

    @Test
    public void multiplicationTest() {
        int[] dim1 = new int[]{1000,2000};
        int[] dim2 = new int[]{2000,3000};

        final double[][] m1 = new double[dim1[0]][dim1[1]];
        final double[][] m2 = new double[dim2[0]][dim2[1]];

        //initialize with random values
        initMatrix(m1,dim1);
        initMatrix(m2,dim2);

       // printMatrix(m1);
       // System.out.println();
       // printMatrix(m2);

        /*
        long start = System.currentTimeMillis();
        double[][] result = multiply(m1,m2,false);

        System.out.println("new Matrix with "+result.length+" rows and "+result[0].length+" columns");
        System.out.println("calculated serially in "+(System.currentTimeMillis()-start)+" ms.");

        start = System.currentTimeMillis();

        double[][] result2 = multiply(m1,m2,true);

        System.out.println("new Matrix with "+result2.length+" rows and "+result2[0].length+" columns");
        System.out.println("calculated parallelly in "+(System.currentTimeMillis()-start)+" ms.");

        IntStream.range(0,result.length).boxed().parallel().forEach(i->assertArrayEquals("",result[i],result2[i],1e-6));

        start = System.currentTimeMillis();
        double[][] result3 = multiply(m1,m2);


        System.out.println("new Matrix with "+result3.length+" rows and "+result3[0].length+" columns");
        System.out.println("calculated serially with for loops in "+(System.currentTimeMillis()-start)+" ms.");

        IntStream.range(0,result2.length).boxed().parallel().forEach(i->assertArrayEquals("",result2[i],result3[i],1e-6));
        //printMatrix(result);
*/

        final float[][] mf1 = new float[dim1[0]][dim1[1]];
        final float[][] mf2 = new float[dim2[0]][dim2[1]];

        //initialize with random values
        initMatrix(mf1,dim1);
        initMatrix(mf2,dim2);

        // printMatrix(m1);
        // System.out.println();
        // printMatrix(m2);


        long start = System.currentTimeMillis();
        float[][] resultf = multiplyf(mf1,mf2);

        System.out.println("new Matrix with "+resultf.length+" rows and "+resultf[0].length+" columns");
        System.out.println("calculated serially in "+(System.currentTimeMillis()-start)+" ms.");


        INDArray testResult=Nd4j.create(resultf);

        start = System.currentTimeMillis();

        //printMatrix(result);

        System.out.println("Now used nd4j api:");




        INDArray ndM1 = Nd4j.create(mf1);
        INDArray ndM2 = Nd4j.create(mf2);

        start = System.currentTimeMillis();
        INDArray ndM3 = ndM1.mmul(ndM2);

        System.out.println("new Matrix with "+ndM3.shape()[0]+" rows and "+ndM3.shape()[1]+" columns");
        System.out.println("calculated in "+(System.currentTimeMillis()-start)+" ms.");


        INDArray ndM4 = ndM3.sub(testResult);
        System.out.println(ndM4);

        System.out.println("Understand basic operations");

        INDArray m = Nd4j.create(new float[][]{{1,2},{3,4}});
        System.out.println(m);

        System.out.println(m.mul(0.9));

        System.out.println(m);

        System.out.println(m.sub(m.mul(0.999)));
    }

    /**
     * returns the matrix as vector columnwise appended
     * @param m
     * @return
     */
    private float[] flattenMatrix(float[][] m){
        //nd4j builds up matrices columnwise from a vector
        int rows = m.length;
        int cols = m[0].length;
        float[] flattened = new float[rows*cols];
        for (int row =0;row<rows;row++)
            for(int col=0;col<cols;col++)
                flattened[row+col*rows]=m[row][col];
        return flattened;
    }

    private void initMatrix(double[][] m,int[] dim){
        IntStream.range(0,dim[0])
                .boxed()
                .parallel()
                .forEach(x->IntStream
                        .range(0,dim[1]).forEach(y->m[x][y]= Math.random())
                );
    }

    private void initMatrix(float[][] m,int[] dim){
        IntStream.range(0,dim[0])
                .boxed()
                .parallel()
                .forEach(x->IntStream
                        .range(0,dim[1]).forEach(y->m[x][y]=(float) Math.random())
                );
    }

    private double[][] multiply(double[][] m1, double[][] m2, boolean  parallel){
        long start = System.currentTimeMillis();
        Stream<double[]> stream = Arrays.stream(m1);
        stream = parallel? stream.parallel() :stream;

        return stream.map(r->IntStream.range(0,m2[0].length)
                        .mapToDouble(i->IntStream.range(0,m2.length)
                                .mapToDouble(j->r[j]*m2[j][i]).sum()).toArray()).toArray(double[][]::new);
    };


    private  double[][] multiply(double[][] m1,double[][] m2){
        double[][] result = new double[m1.length][m2[0].length];
        for (int row = 0;row<m1.length;row++)
            for (int col = 0;col<m2[0].length;col++){
                double sum = 0.;
                for (int dummy=0;dummy<m1[0].length;dummy++)
                    sum+=m1[row][dummy]*m2[dummy][col];
                result[row][col]=sum;
            }
            return result;
    }

    private  float[][] multiplyf(float[][] m1,float[][] m2){
        float[][] result = new float[m1.length][m2[0].length];
        for (int row = 0;row<m1.length;row++)
            for (int col = 0;col<m2[0].length;col++){
                float sum = 0.f;
                for (int dummy=0;dummy<m1[0].length;dummy++)
                    sum+=m1[row][dummy]*m2[dummy][col];
                result[row][col]=sum;
            }
        return result;
    }


    private String matrix2String(float[][] m){
        return Arrays.deepToString(m).replace("], ","]\n");
    }
    private  void printMatrix(float[][] m){
       System.out.println(matrix2String(m));
    }

    private  void printMatrix(double[][] m){
        System.out.println(Arrays.deepToString(m).replace("], ","]\n"));
    }
}
