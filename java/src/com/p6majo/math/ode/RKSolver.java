package com.p6majo.math.ode;

import com.nr.ode.DerivativeInf;
import com.nr.ode.Output;
import com.nr.ode.StepperBase;

import static java.lang.Math.abs;

/**
 * a simple runge-kutta solver
 * the step size is decreased until the required relative accuracy is reached
 *
 * @author p6majo
 * @version 1.0
 *
 *
 */
public class RKSolver {

     final private double x1;
    final private double x2;
    final private double[] ystart;
    final private double rtol;
    final private DerivativeInf derivs;
    private double[] result;
    /**
     * Constructor and initializer of the integrator
     * @param ystartt initial conditions for the y values
     * @param xx1 start value of integration
     * @param xx2 end value of integration
     * @param atol absolute tolerance
     * @param rtol relative tolerance
     * @param h1 maximal step size
     * @param hminn minimal step size, can be zero
     * @param derivss system of derivatives
     */
    public RKSolver(final double[] ystartt,final double[] yend, final double xx1,final double xx2,
    final double atol,final double rtol,final double h1,
    final double hminn, final DerivativeInf derivss){

        this.ystart = ystartt;
        this.x1 = xx1;
        this.x2 = xx2;
        this.rtol = rtol;
        this.derivs = derivss;
        this.result = yend;


    }

    /**
     * establish the required steps of integration, to
     */
    public void integrate(){
        double[] yend = rk(ystart,x1,x2);
        //piecewise copy is necessary, since the address of the pointer result must not be overridden, for the values to be returned to the main program
        for (int i=0;i<yend.length;i++) result[i]=yend[i];
    }

    /**
     * recursive application of the runge-kutta step until the required accuracy is reached
     *
     * @param y1 initial conditions
     * @param x1 start point
     * @param x2 end point
     * @return
     */
    private double[] rk(double[] y1,double x1,double x2){
        double[] y2 = rks(y1,x1,x2);
        double x12 = (x2+x1)/2.;
        double[] y12 = rks(y1,x1,x12);
        double[] y2check = rks(y12,x12,x2);
        if (maxel(vecsub(y2check,y2))/Math.max(maxel(y2),maxel(y2check))<rtol){
            return y2check;
        }
        else{
            double[] ytmp = rk(y1,x1,x12);
            return rk(ytmp,x12,x2);
        }
    }

    private double[] rks(final double[] y, final double x1, final double x2) {
        int n=y.length;
        final double h = x2-x1;
        double[] dydx = new double[n];
        double[] yout = new double[n];
        derivs.derivs(x1,y,dydx);
        double[] dym = new double[n],dyt = new double[n],yt = new double[n];
        double hh=h*0.5;
        double h6=h/6.0;
        double xh=x1+hh;
        for (int i=0;i<n;i++) yt[i]=y[i]+hh*dydx[i];
        derivs.derivs(xh,yt,dyt);
        for (int i=0;i<n;i++) yt[i]=y[i]+hh*dyt[i];
        derivs.derivs(xh,yt,dym);
        for (int i=0;i<n;i++) {
            yt[i]=y[i]+h*dym[i];
            dym[i] += dyt[i];
        }
        derivs.derivs(x1+h,yt,dyt);
        for (int i=0;i<n;i++)
            yout[i]=y[i]+h6*(dydx[i]+dyt[i]+2.0*dym[i]);

        return yout;
    }

    public static double maxel(final double[] a) {
        int i,m=a.length;
        double max = 0.;
        for (i=0;i<m;i++) {
            if (abs(a[i]) > max) max = abs(a[i]);
        }
        return max;
    }

    public static double[] vecsub(final double[] a, final double[] b) {
        int i,m=a.length;
        if (a.length != b.length) throw new IllegalArgumentException("bad vecsub");
        double[] c = new double[m];
        for (i=0;i<m;i++) {
            c[i] = a[i]-b[i];
        }
        return c;
    }


}
