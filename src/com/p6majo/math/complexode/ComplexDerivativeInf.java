package com.p6majo.math.complexode;

import com.p6majo.math.complex.Complex;

public interface ComplexDerivativeInf {
  void derivs(final double x, Complex[] y, Complex[] dydx);
  void jacobian(final double x, Complex[] y, Complex[] dfdx, Complex[][] dfdy);
}
