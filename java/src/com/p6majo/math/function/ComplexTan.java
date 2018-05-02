package com.p6majo.math.function;


import com.p6majo.math.complex.Complex;

/**
 * special type of complex function
 * @author jmartin
 *
 */
public class ComplexTan extends ComplexFunction{
	

		@Override
		public Complex eval(Complex z) {
			return z.tan().times(super.coefficient);
		}

		 @Override
		 public ComplexFunction derivative() {
			return null;
		 }

		@Override
		public Complex evalDerivative(Complex z) {
			return z.cos().times(z.cos()).reciprocal().times(super.coefficient);
		}
		 
		

	}

