package com.p6majo.math.newtoniteration;


import com.p6majo.math.function.ComplexBesselJ;
import com.p6majo.math.function.ComplexFunction;
import com.p6majo.math.utils.Box;
import com.p6majo.math.utils.Resolution;

public class IterationBesselJ {
	
	public static void main(String[] args){
		
		Resolution res = new Resolution(1000,1000);
		Box box = new Box(-5,-5,5,5);
		Box rootBox = new Box(-5,0,5,0);
		
		ComplexFunction besselJ = new ComplexBesselJ(0);
		
		//System.out.println(besselJ.eval(new Complex(0.1,0.3)).toString()+" "+besselJ.evalDerivative(new Complex(0.1,0.3)).toString());
		
		
		NewtonIterator iterator = new NewtonIterator(besselJ,box,res,String.format("besselj0.ppm"));
		iterator.generateRoots(100,rootBox);
		System.out.println(iterator.getRoots().size()+": "+iterator.getRoots().toString());
		
		iterator.generateImage();
	}
}
