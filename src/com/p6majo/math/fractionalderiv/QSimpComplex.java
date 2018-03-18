package com.p6majo.math.fractionalderiv;

import com.p6majo.math.complex.Complex;

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
public class QSimpComplex extends QuadratureComplex{
  /**
   * Limits of integration and current value of integral.
   */
  private Complex a,b,s;

  private Function<Complex,Complex> func;

  /**
   * The constructor takes as inputs func, the function or functor to be
   * integrated between limits a and b, also input.
   *
   * @param funcc
   * @param aa
   * @param bb
   */
  public QSimpComplex(final Function<Complex,Complex> funcc, final Complex aa, final Complex bb) {
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
  public Complex next() {
    Complex x,sum,del;
    double tnm;
    int it,j;
    n++;
    if (n == 1) {
      s=b.minus(a).scale(0.5).times(func.apply(a).plus(func.apply(b)));
      return s;
    } 
    else {
      for (it=1,j=1;j<n-1;j++) it <<= 1;
      tnm=it;
      del=(b.minus(a)).scale(1./tnm);
      x=a.plus(del.scale(0.5));
      for (sum=Complex.NULL,j=0;j<it;j++,x=x.plus(del)) sum = sum.plus(func.apply(x));
      s=s.plus((b.minus(a).times(sum).scale(1./tnm))).scale(0.5);
      return s;
    }
  }


  public static Complex qsimp(final Function<Complex,Complex> func, final Complex a, final Complex b) {
    return qsimp(func, a,b,1.0e-4);
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
  public static Complex qsimp(final Function<Complex,Complex> func, final Complex a, final Complex b, final double eps) {
    final int JMAX=20;
    Complex s,st,ost=Complex.NULL,os=Complex.NULL;
    QSimpComplex t = new QSimpComplex(func,a,b);
    for (int j=0;j<JMAX;j++) {
        st=t.next();
        s=st.scale(4.0).minus(ost).divides(Complex.ONE.scale(3.0));
        if (j > 5)
            if (s.minus(os).abs() < eps*os.abs() ||
                (s.equals(Complex.NULL,eps) && os.equals(Complex.NULL,eps))) return s;
        os=s.clone();
        ost=st.clone();
    }
    throw new IllegalArgumentException("Too many steps in routine qsimp");
  }


}
