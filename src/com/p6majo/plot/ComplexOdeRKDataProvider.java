package com.p6majo.plot;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.complexode.*;

/**
 * The data provider performes the integration of a set of ordinary differential equations for a
 * rectangular domain in the complex plane
 * @author jmartin
 * @version 1.0
 */
public class ComplexOdeRKDataProvider extends ComplexDataProvider {

    final private ComplexDerivativeInf odes;
    final private ComplexDerivativeInf imagOdes;
    final private ComplexInitialConditions ics;
    final private int nvar;
    final int fcnIndex ;

    /**
     * store the full data of the system along the first row, which is used as initial data
     * for the integration along the columns
     */
    private Complex[][] initialDataAlongTheFirstRow;

    /*
    private double xmin;
    private double ymin;
    */

    public ComplexOdeRKDataProvider(ComplexDerivativeInf odes, ComplexDerivativeInf imagOdes, ComplexInitialConditions ics){
        this(odes,imagOdes,ics,0);
    }

    public ComplexOdeRKDataProvider(ComplexDerivativeInf odes, ComplexDerivativeInf imagOdes, ComplexInitialConditions ics, int selectedFunction){
        this.odes = odes;
        this.ics  =ics;
        this.nvar = this.ics.getIcs().length;
        this.imagOdes = imagOdes;
        this.fcnIndex = selectedFunction;
    }

    @Override
    public synchronized void start() {

        long dataDuration = System.currentTimeMillis();
        //setup of the domain
        Range xRange = plotRange.getRange(0);
        Range yRange = plotRange.getRange(1);

        System.out.println(xRange.toString());
        System.out.println(yRange.toString());

        int xSamples = xRange.getSamples();
        int ySamples = yRange.getSamples();

        this.initialDataAlongTheFirstRow = new Complex[nvar][xSamples];

        double dx = (xRange.getEnd().doubleValue()-xRange.getStart().doubleValue())/(xSamples-1);
        double dy = (yRange.getEnd().doubleValue()-yRange.getStart().doubleValue())/(ySamples-1);

        double xmin = xRange.getStart().doubleValue();
        double ymin = yRange.getStart().doubleValue();

        //now each sample point of the domain has to be covered by the ode solver
        //check, whether the initial conditions are part of the domain, otherwise integrate to the lower left corner of the domain

        //TODO there should be the possibility of customization, where the integration of the domain should be started from
        //TODO in order to be able to avoid singularities

        //value of the functions at (xmin,ymin)
        Complex[] z0 = getValueAtLowerLeft(xmin,ymin);
        integrateDataFromLowerLeft(z0,xmin,ymin,dx,dy,xSamples,ySamples);

        System.out.println("Data generated in "+(System.currentTimeMillis()-dataDuration)+" ms.");

    }

    private void integrateDataFromLowerLeft(Complex[] z0,double xmin,double ymin,double dx,double dy,int xSamples, int ySamples){

        //integrate the first row with high accuracy, since it is used as initial values for the integration along the columns
        int i;
        int nvar = z0.length;
        int nouts=xSamples-1;
        double atol=1.0e-4,rtol=atol,h1=0.01,hmin=0.0;

        double x1=xmin,x2=xmin+dx*(xSamples-1);
        Complex[][] yout=new Complex[nvar][xSamples];

        ComplexRKSolver solver = new ComplexRKSolver(z0,yout,xSamples,x1,x2,atol,rtol,0,odes);
        solver.integrate();

        //save the data of the first row as initial value data
        for (i=0;i<nvar;i++)
                this.initialDataAlongTheFirstRow[i]=yout[i];

        //save the data for the selected function
        for (int x=0;x<xSamples;x++)
             setData(x,yout[fcnIndex][x]);

        //calculate columns
        //reduce accuracy for graphical data
        atol=1.0e-4;
        rtol=atol;
        Complex[] y = new Complex[nvar];

        double t1 = ymin,t2 = ymin+dy*(ySamples-1);
        for (int c = 0;c<xSamples;c++){
            for (i=0;i<nvar;i++) y[i]= this.initialDataAlongTheFirstRow[i][c];
            yout =new Complex[nvar][ySamples];
            solver = new ComplexRKSolver(y,yout,ySamples,t1,t2,atol,rtol,0,imagOdes);
            try {
                solver.integrate();
                //for (int r=1;r<ySamples;r++) setData(r*xSamples+c,yout[fcnIndex][r]);
            }
            catch (Exception ex){
                //for (int r=1;r<ySamples;r++) setData(r*xSamples+c,Complex.NULL);
            }
            //write data irrespectively
            for (int r=1;r<ySamples;r++){
                if (yout[fcnIndex][r]!=null) setData(r*xSamples+c,yout[fcnIndex][r]);
                else setData(r*xSamples+c,Complex.NULL);
            }
        }

    }

    private Complex[] getValueAtLowerLeft(double xmin,double ymin){

        int i;
        int nouts=1;
        final double atol=1.0e-4,rtol=atol,h1=0.01,hmin=0.0;
        double x1=0,x2=xmin;
        Complex[] y = ics.getIcs();
        int nvar  = y.length;
        Complex[][] yout=new Complex[nvar][2];

        //move to lower left corner
        double x0 = ics.getZ().re();
        double y0 = ics.getZ().im();

        y=ics.getIcs();

        if (x0!=xmin) {
            x1 = x0;
            x2 = xmin;
            ComplexRKSolver solver = new ComplexRKSolver(y,yout,2,x1,x2,atol,rtol,0,odes);
            solver.integrate();
            for (i = 0; i < nvar; i++) y[i] = yout[i][1];
        }

        if (y0!=ymin){
            x1 = y0;
            x2 = ymin;
            ComplexRKSolver solver = new ComplexRKSolver(y,yout,2,x1,x2,atol,rtol,0,imagOdes);
            solver.integrate();
            for (i = 0; i < nvar; i++) y[i] = yout[i][1];
        }

        return y;
    }

}