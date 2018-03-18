package com.p6majo.math.fractionalderiv;

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
public class QRomb extends Quadrature{
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
  public QRomb(final Function<Double,Double> funcc, final double aa, final double bb) {
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

  public static double qromb(final Function<Double,Double> func, final double a, final double b) {
    return qromb(func, a, b, 1.0e-10);
  }
  
  /**
   * Returns the integral of the function or functor func from a to b.
   * Integration is performed by Romberg's method of order 2K, where, e.g., K=2
   * is Simpson's rule.
   * 
   * @param func
   * @param a
   * @param b
   * @param eps
   * @return
   */
  public static double qromb(final Function<Double,Double> func, final double a, final double b, final double eps) {
    final int JMAX=20, JMAXP=JMAX+1, K=5;
    double[] s = new double[JMAX],h = new double[JMAXP];
    Poly_interp polint = new Poly_interp(h,s,K);
    h[0]=1.0;
    QRomb t = new QRomb(func,a,b);
    for (int j=1;j<=JMAX;j++) {
      s[j-1]=t.next();
      if (j >= K) {
        double ss=polint.rawinterp(j-K,0.0);
        if (abs(polint.dy) <= eps*abs(ss)) return ss;
      }
      h[j]=0.25*h[j-1];
    }
    throw new IllegalArgumentException("Too many steps in routine qromb"+" "+a+" "+b);
  }
}
