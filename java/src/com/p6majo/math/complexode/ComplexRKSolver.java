package com.p6majo.math.complexode;

import com.nr.ode.DerivativeInf;
import com.p6majo.math.complex.Complex;

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
public class ComplexRKSolver {

    final private double x1;
    final private double x2;
    final private int nvars;
    final private Complex[] ystart;
    final private double rtol;
    final private ComplexDerivativeInf derivs;
    private Complex[][] result;
    final private int nouts;
    /**
     * Constructor and initializer of the integrator
     * @param ystartt initial conditions for the y values
     * @param yend array of values that stores the results of integration (including the start values
     * @param noutss number of data points stored
     * @param xx1 start value of integration
     * @param xx2 end value of integration
     * @param atol absolute tolerance
     * @param rtol relative tolerance
     * @param hminn minimal step size, can be zero
     * @param derivss system of derivatives
     */
    public ComplexRKSolver(final Complex[] ystartt, final Complex[][] yend,int noutss, final double xx1, final double xx2,
                           final double atol, final double rtol,
                           final double hminn, final ComplexDerivativeInf derivss){

        this.ystart = ystartt;
        this.nvars = ystart.length;
        this.result = yend;
        this.nouts = noutss;
        this.x1 = xx1;
        this.x2 = xx2;
        this.rtol = rtol;
        this.derivs = derivss;
    }

    /**
     * establish the required steps of integration, to
     */
    public void integrate(){
        for (int n=0;n<nvars;n++) result[n][0]=ystart[n];//copy initial conditions
        double h = (x2-x1)/(nouts-1);
        for (int s=0;s<nouts-1;s++){
            double xstart = x1+s*h;
            double xend = xstart+h;
            Complex[] y=new Complex[nvars];
            for (int n=0;n<nvars;n++) y[n]=result[n][s];
            Complex[] yend = rk(y,xstart,xend);
            for (int n=0;n<nvars;n++) result[n][s+1]=yend[n];
        }
    }

    /**
     * recursive application of the runge-kutta step until the required relative accuracy is reached
     *
     * @param y1 initial conditions
     * @param x1 start point
     * @param x2 end point
     * @return
     */
    private Complex[] rk(Complex[] y1,double x1,double x2){
        Complex[] y2 = rks(y1,x1,x2);
        double x12 = (x2+x1)/2.;
        Complex[] y12 = rks(y1,x1,x12);
        Complex[] y2check = rks(y12,x12,x2);
        if (cmaxel(cvecsub(y2check,y2))/Math.max(cmaxel(y2),cmaxel(y2check))<rtol){
            return y2check;
        }
        else{
            Complex[] ytmp = rk(y1,x1,x12);
            return rk(ytmp,x12,x2);
        }
    }

    private Complex[] rks(final Complex[] y, final double x1, final double x2) {
        int n=y.length;
        final double h = x2-x1;
        if (h<rtol*rtol) throw new IllegalArgumentException("Step size too small");
        Complex[] dydx = new Complex[n];
        Complex[] yout = new Complex[n];
        derivs.derivs(x1,y,dydx);
        Complex[] dym = new Complex[n],dyt = new Complex[n],yt = new Complex[n];
        double hh=h*0.5;
        double h6=h/6.0;
        double xh=x1+hh;
        for (int i=0;i<n;i++) yt[i]=y[i].plus(dydx[i].scale(hh));
        derivs.derivs(xh,yt,dyt);
        for (int i=0;i<n;i++) yt[i]=y[i].plus(dyt[i].scale(hh));
        derivs.derivs(xh,yt,dym);
        for (int i=0;i<n;i++) {
            yt[i]=y[i].plus(dym[i].scale(h));
            dym[i] = dym[i].plus(dyt[i]);
        }
        derivs.derivs(x1+h,yt,dyt);
        for (int i=0;i<n;i++)
            yout[i]=y[i].plus(dydx[i].plus(dyt[i]).plus(dym[i].scale(2.0)).scale(h6));
        return yout;
    }

    public static double cmaxel(final Complex[] a) {
        int i,m=a.length;
        double max = 0.;
        for (i=0;i<m;i++) {
            if (a[i].abs() > max) max = a[i].abs();
        }
        return max;
    }

    public static Complex[] cvecsub(final Complex[] a, final Complex[] b) {
        int i,m=a.length;
        if (a.length != b.length) throw new IllegalArgumentException("bad vecsub");
        Complex[] c = new Complex[m];
        for (i=0;i<m;i++) {
            c[i] = a[i].minus(b[i]);
        }
        return c;
    }


}
