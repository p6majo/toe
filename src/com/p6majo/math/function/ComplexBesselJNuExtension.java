package com.p6majo.math.function;


import com.p6majo.math.complex.Complex;

/**
 * special type of complex function
 * @author jmartin
 *
 */
public class ComplexBesselJNuExtension extends ComplexFunction{

		private Complex order;
		private static double EPS = 1.e-4;

		public ComplexBesselJNuExtension(Complex order){
			this.order = order;
		}
	
		@Override
		public Complex eval(Complex z) {
			/*
			 * The value of the Bessel functions with complex arguments and integer order 
			 * are calculated from the following integral
			 * 
			 * J_n(z)=1/pi*\int_0^pi cos(z*sinx-n*x) dx
			 * 
			 * The integral is performed with trapez intervals the number of integration steps
			 * is increased until the relative change between two iteration steps is smaller than Complex.EPS
			 */
			
		
			int k=1; //number of intervals is given by 2^k+1
			int steps = 2;
			//values of the integrand at the boundary of the integration interval (prefactor 1/pi excluded)
			Complex[] f= new Complex[2];
			f[0] = Complex.ONE;
			f[1] = order.scale(Math.PI).cos();

			Complex Istart = Complex.ONE.scale(2.*Math.PI).reciprocal().times(f[0].plus(f[1]));
			Complex Inext = Istart;
			
			while (Inext.minus(Istart).divides(Inext).abs()>EPS || k==1) {
				Istart=Inext;
				k++;
				steps = (steps-1)*2+1;
				Complex[] fnext = new Complex[steps];
				double dx = Math.PI/(steps-1);
				
				for (int i=0;i<steps;i++) {
					double x=i*dx;
					int ih = i/2;
					if (i%2==0) fnext[i]=f[ih]; 
					else {
						fnext[i] = z.times(Complex.ONE.scale(Math.sin(x))).minus(order.scale(x)).cos();
					}	
				}
				
				Inext = Complex.NULL;
				//calcluate integral
				for (int i=0;i<steps-1;i++) {
					Inext = Inext.plus(fnext[i].plus(fnext[i+1]));
				}
				Inext = Inext.scale(1./Math.PI/2.*dx);
				
				f = fnext;
				if (Inext.abs()<EPS) return Complex.NULL;
				//if (Inext.abs()<Complex.EPS0) return Complex.NULL;
			}
			//System.out.println(k);
			return Inext;
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

