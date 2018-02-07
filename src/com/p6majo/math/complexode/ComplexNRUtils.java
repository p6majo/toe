package com.p6majo.math.complexode;

import com.p6majo.math.complex.Complex;

public class ComplexNRUtils {

    public static Complex[][] buildComplexMatrix(final Complex[][] b) {
        int nn = b.length;
        int mm = b[0].length;
        Complex[][] v = new Complex[nn][mm];
        for(int i=0; i<nn; i++)
            for(int j=0; j<mm; j++)
                v[i][j] = b[i][j];
        return v;
    }
}
