package com.nr.ode;

public interface DerivativeInf {
  void derivs(final double x, double[] y, double[] dydx);
  void jacobian(final double x, double[] y, double[] dfdx, double[][] dfdy);
}
