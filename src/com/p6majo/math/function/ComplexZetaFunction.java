package com.p6majo.math.function;


import com.p6majo.math.complex.Complex;

/**
 * this is the implementation of the alternating series, 
 * which should be valid for real parts larger than zero.
 * @author jmartin
 *
 */
public class ComplexZetaFunction extends ComplexFunction {
	
	protected Complex coefficient= Complex.ONE;
	double[] logN=new double[1000000];
	
	public ComplexZetaFunction() {
		long start = System.currentTimeMillis();
		for (int i=1;i<logN.length;i++)
			logN[i] = Math.log(i);
		System.out.println("Time for initializing the zeta function class:  "+(System.currentTimeMillis()-start));
	}
	
	@Override
	public Complex eval(Complex z) {
		Complex sum = Complex.NULL;
		Complex tmp = Complex.ONE.minus(z).scale(Math.log(2));
		Complex factor = Complex.ONE.minus(tmp.exp()).reciprocal();
		
		int sign = -1;
		int k = 1;
		Complex summand = Complex.ONE;
		while (summand.abs()>1.e-4) {
			summand =z.neg().scale(Math.log(k)).exp();
			sign *=-1;
			sum = sum.plus(summand.scale(sign));
			//System.out.println(summand.toString()+" "+sum.toString());
			k++;
		}
		//improve accuracy by undoing half of the sum
		sum=sum.minus(summand.scale(sign*0.5));
		
		return sum.times(factor);
	}

	@Override
	public ComplexFunction derivative() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Complex evalDerivative(Complex z) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
