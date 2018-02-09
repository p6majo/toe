package com.p6majo.math.complexode;

import com.p6majo.math.complex.Complex;

/**
 * Container class providing initial conditions for a set of ordinary differential equations
 *
 * @author jmartin
 * @version 1.0
 */
public class ComplexInitialConditions {
    private Complex z;
    private Complex[] ics;

    /**
     * @param z is the value of the independent variable where the initial conditions are specified
     * @param ics  is the array of complex values that contains the values for each dependent variable
     */
    public ComplexInitialConditions(Complex z,Complex[] ics){
        this.z = z;
        this.ics = ics;
    }

    public Complex getZ() {
        return z;
    }

    public Complex[] getIcs() {
        return ics;
    }
}
