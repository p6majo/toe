package com.p6majo.plot;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.utils.Utils;

public abstract class ComplexDataProvider extends DataProvider<Complex> {

    public ComplexDataProvider(){
        super(Complex::new);
    }

}
