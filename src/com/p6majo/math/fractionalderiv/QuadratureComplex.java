package com.p6majo.math.fractionalderiv;

import com.p6majo.math.complex.Complex;

public abstract class QuadratureComplex {
  /**
   * Current level of refinement.
   */
  int n;
  
  /**
   * The function next() must be defined in the derived class.
   * 
   * @return Returns the value of the integral at the nth stage of refinement.
   */
  public abstract Complex next();
  
}
