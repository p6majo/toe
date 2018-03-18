package com.p6majo.math.newtoniteration;


import com.maplesoft.externalcall.MapleException;
import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.MapleFunction;
import com.p6majo.math.utils.Box;
import com.p6majo.math.utils.Color;
import com.p6majo.math.utils.Resolution;
import com.p6majo.math.utils.Utils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeSet;

public class NewtonMapleIterator {
	
	private static final int ITERATIONDEPTH = 50;
	
	private final Resolution resolution;
	private final Box box;
	private final String filename;
	private final MapleFunction function;
	
	private TreeSet<Complex> roots = new TreeSet<Complex>();
	private Color[] colors;
	private static double EPS = 1.e-4;
	
	/**
	 * for efficiency the expected function is f(x)/f'(x)
	 * 
	 * @param function
	 * @param box
	 * @param resolution
	 * @param name
	 */
	public NewtonMapleIterator(MapleFunction function,Box box, Resolution resolution, String name){
		this.function = function;
		this.box = box;
		this.resolution = resolution;
		this.filename = name;
	}
	
	public void setRoots(TreeSet<Complex> roots) {
		this.roots = roots;
	}
	
	public TreeSet<Complex> getRoots() {
		return this.roots;
	}
	
	/**
	 * at most maxAttempts are undertaken to determine minNumber of roots
	 * @param maxAttempts
	 * @param minNumber
	 * @return true, when atleast the given number of roots have been found
	 */
	public boolean generateRoots(int maxAttempts, Integer minNumber) throws MapleException {
		
		int counter = 0;
		while (roots.size()<minNumber && counter<maxAttempts ) {
			Complex rnd = Complex.random(box.scalex,box.scaley);
			
			
			Iteration iteration = new Iteration(this.function,rnd);
			Complex root = iteration.getResult();
			if (root!=null) roots.add(root);
		}
		
		if (roots.size()<minNumber) return false;
		else return true;
	}
	
	/**
	 * thoughtlessly and randomly a set of roots is generated without ensuring completeness
	 * @param maxAttempts
	 */
	public void generateRoots(int maxAttempts) throws MapleException{
		int counter = 0;
		while (counter<maxAttempts ) {
			Complex rnd = Complex.random(box);
			Iteration iteration = new Iteration(this.function,rnd);
			Complex root = iteration.getResult();
			if (root!=null) roots.add(root);
			counter++;
		}
	}
	/**
	 * thoughtlessly and randomly a set of roots is generated without ensuring completeness,a box can be provided for the region under consideration
	 * @param maxAttempts
	 */
	public void generateRoots(int maxAttempts,Box box) throws MapleException {
		int counter = 0;
		while (counter<maxAttempts ) {
			Complex rnd = Complex.random(box);
			Iteration iteration = new Iteration(this.function,rnd);
			Complex root = iteration.getResult();
			if (root!=null) roots.add(root);
			counter++;
		}
	}
	
	
	public void generateColors() {
		int delta = (int) Math.pow(roots.size(), 0.34);
		int nColors = (delta+1)*(delta+1)*(delta+1);
		int bD = 256/delta;
		
		this.colors = new Color[nColors];
		
		
		int count = 0;
		for (int r = 0;r<delta+1;r++) 
			for (int g=0;g<delta+1;g++)
				for (int b=0;b<delta+1;b++)
				{
					this.colors[count]=new Color(r*bD,g*bD,b*bD);
					count++;
				}
	}
	
	
	public void generateImage() throws MapleException{
		String out = "";
		long start = System.currentTimeMillis();
		
		if (this.roots == null) this.generateRoots(100);
		
		//convert to a list of roots
		ArrayList<Complex> indexedRoots = new ArrayList<Complex>();
		indexedRoots.addAll(roots);
		
		generateColors();
		
		/*
		System.out.println(roots.toString());
		System.out.println(this.colors.length+" colors for "+roots.size()+" roots.");
		for (int c=0;c<this.colors.length;c++) {
			System.out.println(c+": "+colors[c].toString());
		}
		*/
		
		//construct file header
		out+="P3\n";
		out+=this.resolution.width+" "+this.resolution.height+"\n";
		out+="256\n";
		
		double dx = (this.box.uprightx-this.box.lowleftx)/this.resolution.width;
		double dy = (this.box.uprighty-this.box.lowlefty)/this.resolution.height;
		
		PrintWriter writer=null;
		try {
			writer = new PrintWriter(this.filename, "UTF-8");
			writer.write(out);
			
			for (int h=0;h<this.resolution.height;h++) {
				out="";
				if (h%10==0) System.out.println("height "+h);
				for (int w=0;w<this.resolution.width;w++) {
					double x = this.box.lowleftx+dx*w;
					double y = this.box.lowlefty+dy*h;
				
					Complex z = new Complex(x,y);
					//System.out.println("Start with "+z.toString());
					int index=-1;
					double alpha = 1;
					
					if (!closeToRoot(z,0.03)) {		
						Iteration it = new Iteration(this.function,z);
						alpha = 1.-((double) it.getSteps())/ITERATIONDEPTH;
						
						if (it.getResult()!=null) {
							for (Complex root:indexedRoots){
								if (root.equals(it.getResult(),EPS)) {
									index = indexedRoots.indexOf(root);
									break;
								}
							}
						}
					}
						
					out+=colors[index+1].toString(alpha)+" ";
				
				}
				out+="\n";
				writer.write(out);
		}
		
			writer.close();
			
			

			//Runtime rt = Runtime.getRuntime();
			
			//Process proc = rt.exec("xv "+name);
			//proc.waitFor();
			
			System.out.println("Image generated with name "+this.filename+" in "+(System.currentTimeMillis()-start)+" sec.");
			
		}
		catch(Exception ex) {
			Utils.errorMsg(ex.getMessage());
		}
		//System.out.println("file generated successfully!");
	}
	
	private  boolean closeToRoot(Complex z,double radius) {
		for (Complex root:roots) {
			if (root.minus(z).abs()<radius) return true;
		}
		return false;
	}
	
	/************************/
	/*     auxiliary class  */
	/************************/
	
	private class Iteration{
		int steps = -1;
		Complex result = null;
		double MAXVAL = 1.E99;
		
		public Iteration(MapleFunction function,Complex z) throws MapleException{
			//System.out.println("Iteration with start: "+z.toString());
			Complex z1 = null;
			double distance = 1;
			
			while (distance > EPS && steps<ITERATIONDEPTH) {
				z1 = z.minus(function.eval(z));
				Complex diff = z1.minus(z);
				distance = diff.abs();
				steps++;
				//System.out.println(function.evalDerivative(z)+" "+function.eval(z)+" "+z.toString());
				z = z1;
				if (z.abs()>MAXVAL) {
					z=null;
					break;
				}
			}
			//System.out.println();
			//in case of successful iteration set result
			if (steps<ITERATIONDEPTH) this.result = z;
		}
		
		public int getSteps() {
			return this.steps;
		}
		
		public Complex getResult() {
			return this.result;
		}
	}
	
}
