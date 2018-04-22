package com.p6majo.math.network2.layers;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import static com.p6majo.math.utils.Utils.errorMsg;

public class Utils {

    /**
     * multiply all dimensions to get one dimension that can hold the data from all dimensions
     * Remember that the batch index is not part of the signature
     * @param signature
     * @return
     */
    public static int getflattenedDimFromSignature(int[] signature) {
        int dim = 1;
        for (int d = 0; d < signature.length; d++) {
            dim *= signature[d];
        }
        return dim;
    }

    /**
     * reflects a kernel around its mid point
     * rank 2 means:  row, column
     * rank 3 means:  depth, row, column
     * @param kernel
     * @return
     */
    public static INDArray reflectKernel(INDArray kernel){
        if (!(kernel.rank()==3 || kernel.rank()==2))
            errorMsg("The kernel has to have rank 2 or rank 3 but received: "+kernel.rank()+" instead.");

        int[] shape = kernel.shape();

        if (kernel.rank()==2){
            INDArray tmp = Nd4j.toFlattened(kernel);
            tmp = Nd4j.reverse(tmp);
            return tmp.reshape(shape);

        }
        else{
            //the dup is necessary, otherwise the input kernel is overwritten
            INDArray flattened,tmp2;
            int depth = kernel.size(0);
            int dimx = kernel.size(1);
            int dimy = kernel.size(2);
            flattened = kernel.reshape(new int[]{depth,dimx*dimy});
            tmp2 = Nd4j.reverse(flattened.getRow(0).dup());
            for (int i=1;i<depth;i++) tmp2=Nd4j.concat(1,tmp2,Nd4j.reverse(flattened.getRow(i).dup()));
           return tmp2.reshape(shape);
        }
    }
}
