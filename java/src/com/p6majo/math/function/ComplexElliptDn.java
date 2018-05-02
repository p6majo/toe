package com.p6majo.math.function;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.nswc.NswcMath;

/**
 * special type of complex function
 * @author jmartin
 *
 */
public class ComplexElliptDn extends ComplexFunction{

		private double k,l;
		private NswcMath math = new NswcMath();

		public ComplexElliptDn(double k, double l){
			this.k=k;this.l=l;
		}
	
		@Override
		public Complex eval(Complex z) {
			return math.Dn(k,l,z);
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

