package com.p6majo.math.newtoniteration;


import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexTan;
import com.p6majo.math.utils.Box;
import com.p6majo.math.utils.Resolution;

import java.util.TreeSet;

public class IterationTan {
	
	public static void main(String[] args){
		
		Resolution res = new Resolution(3440*4,1440*4);
		Box box = new Box(-3.44159,-1.44,3.44159,1.44159);
		box.rescale(1.37);
		
		ComplexTan tan = new ComplexTan();
		
		NewtonIterator iterator = new NewtonIterator(tan,box,res,String.format("tan.ppm"));
		//add 51 roots
		TreeSet<Complex> roots = new TreeSet<Complex>();
		for (int i =-30;i<30;i++) roots.add(Complex.ONE.scale(3.141592654*i*0.5));
		iterator.setRoots(roots);
		iterator.shuffleColors();
		iterator.generateImage();

	}
}
