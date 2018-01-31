package com.p6majo.math.function;


import com.p6majo.math.complex.Complex;

/**
 * special type of complex function
 * @author jmartin
 *
 */
public class ComplexCot extends ComplexFunction{
	

		@Override
		public Complex eval(Complex z) {
			return z.tan().reciprocal().times(super.coefficient);
		}

		 @Override
		 public ComplexFunction derivative() {
			return null;
		 }

		@Override
		public Complex evalDerivative(Complex z) {
			return z.sin().times(z.sin()).reciprocal().neg().times(super.coefficient);
		}
		 
		

	}

