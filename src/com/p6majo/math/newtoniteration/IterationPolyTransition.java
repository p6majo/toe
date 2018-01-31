package com.p6majo.math.newtoniteration;


import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexPolynomial;
import com.p6majo.math.utils.Box;
import com.p6majo.math.utils.Utils;

public class IterationPolyTransition {
	
	
	private static ComplexPolynomial pol;
	
	public static void main(String[] args){
		
		Resolution res = new Resolution(1000,1000);
		Box box = new Box(1.5,1.5);
		
	
		for (int i=1;i<10000;i++)
		{
			//if (i>=6725 && i<=6750) {}
			//else if (i>=6700 && i<=6900) i=i+9;
			//else 
			i=i+9;
			double r = (double ) i/10000;
	
			//Complex[] coeff = new Complex[] {Complex.ONE.scale(4),Complex.NULL,Complex.ONE.scale(-5),Complex.NULL,Complex.ONE};
			Complex[] coeff = new Complex[] {Complex.ONE.scale(-1),Complex.NULL,Complex.NULL,Complex.ONE.scale(r),Complex.NULL,Complex.ONE.scale(1-r)};
			pol = new ComplexPolynomial(coeff);
				
			NewtonIterator iterator = new NewtonIterator(pol,box,res,String.format("transit%05d.ppm",i));
			if (!iterator.generateRoots(100,5)) Utils.errorMsg("Not all roots have been generated");
			iterator.generateImage();
		}
	}
}
