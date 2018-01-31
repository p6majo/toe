package com.p6majo.math.function;


import com.p6majo.math.complex.Complex;

/**
 * special type of complex function
 * @author jmartin
 *
 */
public class ComplexSin extends ComplexFunction{
	

		@Override
		public Complex eval(Complex z) {
			return z.sin().times(super.coefficient);
		}

		 @Override
		 public ComplexFunction derivative() {
			 ComplexFunction derivative = new ComplexCos();
			 derivative.setCoefficient(super.coefficient);
			 return derivative;
		 }

		@Override
		public Complex evalDerivative(Complex z) {
			return z.cos().times(super.coefficient);
		}

	}

