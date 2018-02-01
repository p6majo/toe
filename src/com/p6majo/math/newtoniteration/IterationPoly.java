package com.p6majo.math.newtoniteration;


import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexPolynomial;
import com.p6majo.math.utils.Box;
import com.p6majo.math.utils.Resolution;
import com.p6majo.math.utils.Utils;

public class IterationPoly {
	
	
	private static ComplexPolynomial pol;
	
	public static void main(String[] args){
		
		Resolution res = new Resolution(500,500);
		Box box = new Box(-3,-3,3,3);
		
	    //x^3+x^2+x+1
        Complex[] coeff = new Complex[] {Complex.ONE.scale(1),Complex.ONE,Complex.ONE,Complex.ONE};
        pol = new ComplexPolynomial(coeff);


        NewtonIterator iterator = new NewtonIterator(pol,box,res,"comparison.ppm");
			if (!iterator.generateRoots(100,4)) Utils.errorMsg("Not all roots have been generated");
			iterator.generateImage();
    }
}
