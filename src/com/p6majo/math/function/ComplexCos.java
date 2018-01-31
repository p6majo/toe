package com.p6majo.math.function;


import com.p6majo.math.complex.Complex;

/**
 * special type of complex function
 * @author jmartin
 *
 */
public class ComplexCos extends ComplexFunction{
	

		@Override
		public Complex eval(Complex z) {
			return z.cos().times(super.coefficient);
		}

		 @Override
		 public ComplexFunction derivative() {
			 ComplexFunction derivative = new ComplexSin();
			 derivative.setCoefficient(super.coefficient.neg());
			 
			 return derivative;
		 }

		@Override
		public Complex evalDerivative(Complex z) {
			return z.sin().neg();
		}

	}

