package com.p6majo.math.network2.layers;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import static com.p6majo.math.utils.Utils.errorMsg;

public class LayerUtils {

    /**
     * multiply all dimensions to get one dimension that can hold the data from all dimensions
     * Remember that the batch index is not part of the signature
     *
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
     * rank 4 weight: ouDepth, inDepth, row, column
     *
     * @param kernel
     * @return
     */
    public static INDArray reflectKernel(INDArray kernel) {
        if (!(kernel.rank() == 4))
            errorMsg("The kernel has to have rank 4 but received: " + kernel.rank() + " instead.");

        int[] shape = kernel.shape();

        //the dup is necessary, otherwise the input kernel is overwritten
        INDArray flattened, tmp2 = null;
        int outDepth = kernel.size(0);
        int inDepth = kernel.size(1);
        int dimx = kernel.size(2);
        int dimy = kernel.size(3);

        flattened = kernel.reshape(new int[]{outDepth, inDepth, dimx * dimy});

        for (int o = 0; o < outDepth; o++){
            for (int i = 0; i < inDepth; i++)
                if (tmp2 == null)
                    tmp2 = Nd4j.reverse(flattened.getRow(o).getRow(i).dup());
                else
                    tmp2 = Nd4j.concat(1, tmp2, Nd4j.reverse(flattened.getRow(o).getRow(i).dup()));
        }

        return tmp2.reshape(shape);
    }
}
