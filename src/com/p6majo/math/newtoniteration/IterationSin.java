package com.p6majo.math.newtoniteration;


import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexSin;
import com.p6majo.math.utils.Box;

import java.util.TreeSet;

public class IterationSin {
	
	public static void main(String[] args){
		
		Resolution res = new Resolution(3200,2400);
		Box box = new Box(1.12,-0.35,2.02,0.35);
		
		ComplexSin sin = new ComplexSin();
		
		NewtonIterator iterator = new NewtonIterator(sin,box,res,String.format("sin.ppm"));
		//add 31 roots
		TreeSet<Complex> roots = new TreeSet<Complex>();
		for (int i =-15;i<15;i++) roots.add(Complex.ONE.scale(3.141592654*i));
		iterator.setRoots(roots);
		iterator.generateImage();

	}
}
