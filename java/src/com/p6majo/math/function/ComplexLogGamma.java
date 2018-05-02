package com.p6majo.math.function;


import com.p6majo.math.complex.Complex;
import com.p6majo.math.nswc.NswcMath;

/**
 * special type of complex function
 * @author jmartin
 *
 */
public class ComplexLogGamma extends ComplexFunction{

		private NswcMath math =new NswcMath();


		@Override
		public Complex eval(Complex z) {
		    return math.LogGamma(z);
		}

		 @Override
		 public ComplexFunction derivative() {
			return null;
		 }

		@Override
		public Complex evalDerivative(Complex z) {
			return null;
		}
		 
		

	}

