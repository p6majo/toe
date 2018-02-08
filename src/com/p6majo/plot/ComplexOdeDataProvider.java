package com.p6majo.plot;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.complexode.ComplexDerivativeInf;
import com.p6majo.math.complexode.ComplexInitialConditions;

import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * The data provider performes the integration of a set of ordinary differential equations for a
 * rectangular domain in the complex plane
 * @author jmartin
 * @version 1.0
 */
public class ComplexOdeDataProvider extends DataProvider {

    final private ComplexDerivativeInf odes;
    final private ComplexInitialConditions ics;

    public ComplexOdeDataProvider(ComplexDerivativeInf odes, ComplexInitialConditions ics){
        this.odes = odes;
        this.ics  =ics;
    }

    @Override
    public synchronized void start() {

        //setup of the domain
        Range xRange = super.range.getRange(0);
        Range yRange = super.range.getRange(1);

        int xSamples = xRange.getSamples();
        int ySamples = yRange.getSamples();

        double dx = (xRange.getEnd().doubleValue()-xRange.getStart().doubleValue())/(xSamples-1);
        double dy = (yRange.getEnd().doubleValue()-yRange.getStart().doubleValue())/(ySamples-1);

        double xmin = xRange.getStart().doubleValue();
        double ymin = yRange.getStart().doubleValue();

        //now each sample point of the domain has to be covered by the ode solver
        //check, whether the initial conditions are part of the domain, otherwise integrate to the lower left corner of the domain

        //TODO there should be the possibility of customization, where the integration of the domain should be started from
        //TODO in order to be able to avoid singularities



    }

    public Number[] getData(){
        return super.data;
    }


}