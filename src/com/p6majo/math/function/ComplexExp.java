package com.p6majo.math.function;


import com.p6majo.math.complex.Complex;

/**
 * special type of complex function
 * @author jmartin
 *
 */
public class ComplexExp extends ComplexFunction{
	

		@Override
		public Complex eval(Complex z) {
			return z.exp().times(super.coefficient);
		}

		 @Override
		 public ComplexFunction derivative() {
			 ComplexFunction derivative = new ComplexExp();
			 derivative.setCoefficient(super.coefficient);
			 return derivative;
		 }

		@Override
		public Complex evalDerivative(Complex z) {
			return z.exp().times(super.coefficient);
		}

	}

