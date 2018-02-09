package com.p6majo.plot;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.complexode.*;
import com.p6majo.math.function.ComplexExp;

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
    final private ComplexDerivativeInf imagOdes;
    final private ComplexInitialConditions ics;

    /*
    private double xmin;
    private double ymin;
    */

    public ComplexOdeDataProvider(ComplexDerivativeInf odes,ComplexDerivativeInf imagOdes, ComplexInitialConditions ics){
        this.odes = odes;
        this.ics  =ics;
        this.imagOdes = imagOdes;
    }

    @Override
    public synchronized void start() {

        //setup of the domain
        Range xRange = super.range.getRange(0);
        Range yRange = super.range.getRange(1);

        System.out.println(xRange.toString());
        System.out.println(yRange.toString());

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

        //value of the function at (xmin,ymin)
        Complex z0 = null;
        z0 = getValueAtLowerLeft(xmin,ymin);
        integrateDataFromLowerLeft(z0,xmin,ymin,dx,dy,xSamples,ySamples);

       // System.out.println("Value at the lower left domain boundary: "+z0.toString()+" "+new Complex(xmin,ymin).exp().toString());

    }

    public Number[] getData(){
        return super.data;
    }

    private void integrateDataFromLowerLeft(Complex z0,double xmin,double ymin,double dx,double dy,int xSamples, int ySamples){

        //integrate the first row with high accuracy, since it is used as initial values for the integration along the columns
        int i,nvar=1;
        int nouts=xSamples-1;
        double atol=1.0e-8,rtol=atol,h1=0.01,hmin=0.0;

        double x1=xmin,x2=xmin+dx*(xSamples-1);
        Complex[] y = new Complex[nvar];
        Complex[] yout=new Complex[nvar];

        ComplexOutput out = new ComplexOutput(nouts);
        ComplexStepperBS s = new ComplexStepperBS();

        y[0]=z0;
        ComplexOdeint ode = new ComplexOdeint(y, x1, x2, atol, rtol, h1, hmin, out, odes, s);
        ode.integrate();

        //first row
        for (i=0;i<xSamples;i++)
            super.data[i]=out.ysave[0][i];

        //calculate columns
        nouts = ySamples-1;
        //reduce accuracy for graphical data
        atol=1.0e-4;
        rtol=atol;
        double t1 = ymin,t2 = ymin+dy*(ySamples-1);
        for (int c = 0;c<xSamples;c++){
            y[0]=(Complex) super.data[c];
            out = new ComplexOutput(nouts);
            ode = new ComplexOdeint(y,t1,t2,atol,rtol,h1,hmin,out,imagOdes,s);
            ode.integrate();
            for (int r=0;r<ySamples;r++) super.data[r*xSamples+c]=out.ysave[0][r];
        }

    }

    private Complex getValueAtLowerLeft(double xmin,double ymin){

        int i,nvar=1;
        int nouts=1;
        final double atol=1.0e-8,rtol=atol,h1=0.01,hmin=0.0;
        double x1=0,x2=xmin;
        Complex[] y = new Complex[nvar];
        Complex[] yout=new Complex[nvar];

        //move to lower left corner
        double x0 = ics.getZ().re();
        double y0 = ics.getZ().im();

        Complex z0=ics.getIcs()[0];

        ComplexOutput out = new ComplexOutput(nouts);
        ComplexStepperBS s = new ComplexStepperBS();

        if (x0!=xmin) {
            x1 = x0;
            x2 = xmin;
            y[0]=z0;
            ComplexOdeint ode = new ComplexOdeint(y, x1, x2, atol, rtol, h1, hmin, out, odes, s);
            ode.integrate();
            z0 = out.ysave[0][out.count-1];
        }

        if (y0!=ymin){
            x1 = y0;
            x2 = ymin;
            y[0]=z0;
            ComplexOdeint ode = new ComplexOdeint(y, x1, x2, atol, rtol, h1, hmin, out, imagOdes, s);
            ode.integrate();
            z0 = out.ysave[0][out.count-1];
        }

        return z0;
    }

}