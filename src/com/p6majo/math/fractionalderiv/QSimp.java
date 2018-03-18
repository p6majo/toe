package com.p6majo.math.fractionalderiv;

import com.nr.UniVarRealValueFun;
import com.nr.fi.Trapzd;
import com.nr.interp.Poly_interp;

import java.util.function.Function;

import static java.lang.Math.abs;

/**
 * Routine implementing the extended trapezoidal rule.
 * 
 * Copyright (C) Numerical Recipes Software 1986-2007
 * Java translation Copyright (C) Huang Wen Hui 2012
 *
 * @author hwh
 *
 */
public class QSimp extends Quadrature{
  /**
   * Limits of integration and current value of integral.
   */
  private double a,b,s;

  private Function<Double,Double> func;

  /**
   * The constructor takes as inputs func, the function or functor to be
   * integrated between limits a and b, also input.
   *
   * @param funcc
   * @param aa
   * @param bb
   */
  public QSimp(final Function<Double,Double> funcc, final double aa, final double bb) {
    func = funcc;
    a = aa;
    b = bb;
    n = 0;
  }
  
  /**
   * Returns the nth stage of refinement of the extended trapezoidal rule. On
   * the first call (n=1),the routine returns the crudest estimate of S(a,b)f(x)dx.
   * Subsequent calls set n=2,3,... and improve the accuracy by adding 2n-2
   * additional interior points.
   */
  public double next() {
    double x,tnm,sum,del;
    int it,j;
    n++;
    if (n == 1) {
      return (s=0.5*(b-a)*(func.apply(a)+func.apply(b)));
    } 
    else {
      for (it=1,j=1;j<n-1;j++) it <<= 1;
      tnm=it;
      del=(b-a)/tnm;
      x=a+0.5*del;
      for (sum=0.0,j=0;j<it;j++,x+=del) sum += func.apply(x);
      s=0.5*(s+(b-a)*sum/tnm);
      return s;
    }
  }


  public static double qsimp(final Function<Double,Double> func, final double a, final double b) {
    return qsimp(func, a,b,1.0e-10);
  }

  /**
   * Returns the integral of the function or functor func from a to b. The
   * constants EPS can be set to the desired fractional accuracy and JMAX so
   * that 2 to the power JMAX-1 is the maximum allowed number of steps.
   * Integration is performed by Simpson's rule.
   *
   * @param func
   * @param a
   * @param b
   * @param eps
   * @return
   */
  public static double qsimp(final Function<Double,Double> func, final double a, final double b, final double eps) {
    final int JMAX=20;
    double s,st,ost=0.0,os=0.0;
    QSimp t = new QSimp(func,a,b);
    for (int j=0;j<JMAX;j++) {
      st=t.next();
      s=(4.0*st-ost)/3.0;
      if (j > 5)
        if (abs(s-os) < eps*abs(os) ||
                (s == 0.0 && os == 0.0)) return s;
      os=s;
      ost=st;
    }
    throw new IllegalArgumentException("Too many steps in routine qsimp");
  }


}
