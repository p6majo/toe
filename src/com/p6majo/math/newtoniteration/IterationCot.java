package com.p6majo.math.newtoniteration;


import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexCot;
import com.p6majo.math.utils.Box;

import java.util.TreeSet;

public class IterationCot {
	
	public static void main(String[] args){
		
		Resolution res = new Resolution(1260,2400);
		Box box = new Box(-3.14159*0.5,-3,3.14159*0.5,3);
		
		ComplexCot cot = new ComplexCot();
		
		NewtonIterator iterator = new NewtonIterator(cot,box,res,String.format("cot.ppm"));
		//add 31 roots
		TreeSet<Complex> roots = new TreeSet<Complex>();
		for (int i =-15;i<15;i++) roots.add(Complex.ONE.scale(3.141592654*i*0.5));
		iterator.setRoots(roots);
		iterator.generateImage();

	}
}
