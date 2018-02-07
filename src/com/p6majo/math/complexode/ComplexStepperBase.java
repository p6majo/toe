package com.p6majo.math.complexode;


import com.nr.ode.DerivativeInf;
import com.p6majo.math.complex.Complex;

public abstract class ComplexStepperBase {
  double x;
  double xold;
  Complex[] y, dydx;
  double atol,rtol;
  boolean dense;
  double hdid;
  double hnext;
  double EPS;
  int n,neqn;
  Complex[] yout,yerr;
  
  public ComplexStepperBase(){
    
  }
  
  /**
   * Input to the constructor are the dependent variable vector y[0..n-1] and
   * its derivative dydx[0..n-1] at the starting value of the independent
   * variable x. Also input are the absolute and relative tolerances, atol and
   * rtol, and the boolean dense, which is true if dense output is required.
   * 
   * @param yy
   * @param dydxx
   * @param xx
   * @param atoll
   * @param rtoll
   * @param dense
   */
  void setParam(final Complex[] yy, final Complex[] dydxx, final double xx, final double atoll, // XXX reference xx.
    final double rtoll, final boolean dense) {
    x = xx;
    y = yy;
    dydx = dydxx;
    atol = atoll;
    rtol = rtoll;
    this.dense = dense;
    n = y.length;
    neqn = n;
    yout = new Complex[n];
    yerr = new Complex[n];
  }
  
  public ComplexStepperBase(final Complex[] yy, final Complex[] dydxx, final double xx, final double atoll, // XXX reference xx.
                     final double rtoll, final boolean dense) {
    setParam(yy, dydxx, xx, atoll, rtoll, dense);
  }
  
  public abstract void step(final double htry, final ComplexDerivativeInf derivs);
  
  public abstract Complex dense_out(final int i,final double x,final double h);
  
  // public abstract void dy(double[] y, final double htot, final int k, double[] yend, intW ipt, DInf derivs);
  
  // public abstract void polyextr(final int k, double[][] table, double[] last);
  
  // public abstract void prepare_dense(final double h,double[] dydxnew, double[] ysav, double[] scale, final int k, doubleW error);
  
  
  // public abstract void dense_interp(final int n, double[] y, final int imit);
}
