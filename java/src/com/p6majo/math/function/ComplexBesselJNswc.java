package com.p6majo.math.function;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.nswc.NswcMath;

/**
 * special type of complex function
 * @author jmartin
 *
 */
public class ComplexBesselJNswc extends ComplexFunction{

		private Complex order;
		private NswcMath math = new NswcMath();

		public ComplexBesselJNswc(Complex order){
			this.order = order;
		}
	
		@Override
		public Complex eval(Complex z) {
			return math.BesselJ(order,z);
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

