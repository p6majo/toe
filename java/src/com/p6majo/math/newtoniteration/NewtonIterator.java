package com.p6majo.math.newtoniteration;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexFunction;
import com.p6majo.math.utils.Box;
import com.p6majo.math.utils.Color;
import com.p6majo.math.utils.Resolution;
import com.p6majo.math.utils.Utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NewtonIterator {
	
	private static final int ITERATIONDEPTH = 50;
	
	private final Resolution resolution;
	private final Box box;
	private final String filename;
	private final ComplexFunction function;
	
	private TreeSet<Complex> roots = new TreeSet<Complex>();
	private List<Complex> indexedRoots;
	private Color[] colors;
	private double EPS  = 1.e-4;
	
	
	public NewtonIterator(ComplexFunction function,Box box, Resolution resolution, String name) {
		this.function = function;
		this.box = box;
		this.resolution = resolution;
		this.filename = name;
	}
	
	public void setRoots(TreeSet<Complex> roots) {
		this.roots = roots;
		this.generateColors();
	}
	
	public TreeSet<Complex> getRoots() {
		if (roots.size()==0) this.generateRoots(100);
		return this.roots;
	}
	
	/**
	 * at most maxAttempts are undertaken to determine minNumber of roots
	 * @param maxAttempts
	 * @param minNumber
	 * @return true, when atleast the given number of roots have been found
	 */
	public boolean generateRoots(int maxAttempts, Integer minNumber) {
		
		int counter = 0;
		while (roots.size()<minNumber && counter<maxAttempts ) {
			Complex rnd = Complex.random(box.scalex,box.scaley);
			
			
			Iteration iteration = new Iteration(this.function,rnd,ITERATIONDEPTH);
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
	public void generateRoots(int maxAttempts) {
		int counter = 0;
		while (counter<maxAttempts ) {
			Complex rnd = Complex.random(box);
			Iteration iteration = new Iteration(this.function,rnd,ITERATIONDEPTH);
			Complex root = iteration.getResult();
			if (root!=null) roots.add(root);
			counter++;
		}
	}
	/**
	 * thoughtlessly and randomly a set of roots is generated without ensuring completeness,a box can be provided for the region under consideration
	 * @param maxAttempts
	 */
	public void generateRoots(int maxAttempts,Box box) {
		int counter = 0;
		while (counter<maxAttempts ) {
			Complex rnd = Complex.random(box);
			Iteration iteration = new Iteration(this.function,rnd,ITERATIONDEPTH);
			Complex root = iteration.getResult();
			if (root!=null) roots.add(root);
			counter++;
		}
	}
	
	
	private void generateColors() {
		int delta = (int) Math.pow(roots.size(), 0.34);
		int nColors = (delta+1)*(delta+1)*(delta+1);
		int bD = 256/delta;
		
		this.colors = new Color[nColors];
		
		
		int count = 0;
		for (int b = 0;b<delta+1;b++) 
			for (int g=0;g<delta+1;g++)
				for (int r=0;r<delta+1;r++)
				{
					this.colors[count]=new Color(r*bD,g*bD,b*bD);
					count++;
				}
	}
	
	public void shuffleColors() {
		if (this.colors!=null) {
			int n = this.colors.length;
			Color[] newColors = new Color[n];
			
			ArrayList<Integer> ints = new ArrayList<Integer>();
			for (int i=1;i<n;i++) ints.add(i);
			
			//black remains black
			newColors[0]=this.colors[0];
			
			for (int i=1;i<n;i++) {
				int select = (int) Math.floor(Math.random()*ints.size());
				newColors[i]=this.colors[ints.get(select)];
			}

			this.colors = newColors;
		}
	}
	

	private class Coordinates{
		public int x;
		public int y;
		public Coordinates(int x,int y){
			this.x=x;
			this.y=y;
		}
	}

	Function<Complex,String> pixel2color = new Function<Complex, String>() {
		@Override
		public String apply(Complex z) {
			int index=-1;
			double alpha = 1;

			if (!closeToRoot(z,0.05)) {
				Iteration it = new Iteration(function,z,ITERATIONDEPTH);
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

			return colors[index+1].toString(alpha);
		}
	};

    public void generateImageWithStreamAndColorFix(Integer[] colors) {
        String out = "";
        long start = System.currentTimeMillis();
        indexedRoots = new ArrayList<Complex>();

        //TODO should be a stream version as well
        if (this.roots == null) {
            this.generateRoots(100);
        }

        if (this.roots.size()==0){
            this.generateRoots(100);
        }

        if (this.colors==null) generateColors();

        //include a user request for the colors
        Color[] newColors = new Color[this.colors.length];
        newColors[0]=this.colors[0];
        for (int i=1;i<this.colors.length;i++){
            if (i<colors.length+1) newColors[i]=this.colors[colors[i-1]];
            else newColors[i]=this.colors[i];
        }
        this.colors = newColors;

        //convert to a list of roots
        indexedRoots.addAll(roots);
        System.out.println(Arrays.toString(colors));

        double dx = (this.box.uprightx - this.box.lowleftx) / this.resolution.width;
        double dy = (this.box.uprighty - this.box.lowlefty) / this.resolution.height;

        //construct file header
        out+="P3\n";
        out+=this.resolution.width+" "+this.resolution.height+"\n";
        out+="256\n";


        PrintWriter writer = null;
        try {
            writer = new PrintWriter(this.filename, "UTF-8");
            writer.write(out);
            out="";
            out = IntStream.range(0,resolution.height).
                    boxed().
                    parallel().
                    map(h->IntStream.
                            range(0,resolution.width).
                            boxed().
                            map(w->pixel2color.apply(
                                    new Complex(this.box.lowleftx+dx*w,this.box.lowlefty+dy*h)
                                    )
                            )
                            .collect(Collectors.joining(" ")))
                    .collect(Collectors.joining("\n"));
            writer.write(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Utils.errorMsg(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Utils.errorMsg(e.getMessage());
        }
    }


	public void generateImageWithStream() {
		String out = "";
		long start = System.currentTimeMillis();
		indexedRoots = new ArrayList<Complex>();

		//TODO should be a stream version as well
		if (this.roots == null) {
			this.generateRoots(100);
		}

		if (this.roots.size()==0){
		    this.generateRoots(100);
        }

		if (this.colors==null) generateColors();
		//convert to a list of roots
		indexedRoots.addAll(roots);
        System.out.println(Arrays.toString(colors));

		double dx = (this.box.uprightx - this.box.lowleftx) / this.resolution.width;
		double dy = (this.box.uprighty - this.box.lowlefty) / this.resolution.height;

        //construct file header
        out+="P3\n";
        out+=this.resolution.width+" "+this.resolution.height+"\n";
        out+="256\n";


        PrintWriter writer = null;
		try {
			writer = new PrintWriter(this.filename, "UTF-8");
			writer.write(out);
			out="";
			out = IntStream.range(0,resolution.height).
					boxed().
					parallel().
					map(h->IntStream.
							range(0,resolution.width).
							boxed().
							map(w->pixel2color.apply(
											new Complex(this.box.lowleftx+dx*w,this.box.lowlefty+dy*h)
									)
							)
							.collect(Collectors.joining(" ")))
					.collect(Collectors.joining("\n"));
			writer.write(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Utils.errorMsg(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Utils.errorMsg(e.getMessage());
		}
	}


	public void generateImage() {
		String out = "";
		long start = System.currentTimeMillis();
		indexedRoots = new ArrayList<Complex>();
		
		if (this.roots == null) {
			this.generateRoots(100);
		}
		else if(this.roots.size()==0)
			this.generateRoots(100);

		generateColors();
		
		//convert to a list of roots
		indexedRoots.addAll(roots);

		//construct file header
		out+="P3\n";
		out+=this.resolution.width+" "+this.resolution.height+"\n";
		out+="256\n";

		
		/*
		System.out.println(roots.toString());
		System.out.println(this.colors.length+" colors for "+roots.size()+" roots.");
		for (int c=0;c<this.colors.length;c++) {
			System.out.println(c+": "+colors[c].toString());
		}
		*/
		

		double dx = (this.box.uprightx-this.box.lowleftx)/this.resolution.width;
		double dy = (this.box.uprighty-this.box.lowlefty)/this.resolution.height;

		PrintWriter writer=null;
		try {
			writer = new PrintWriter(this.filename, "UTF-8");
			writer.write(out);
			
			for (int h=0;h<this.resolution.height;h++) {
				out="";
				for (int w=0;w<this.resolution.width;w++) {
					double x = this.box.lowleftx+dx*w;
					double y = this.box.lowlefty+dy*h;
				
					Complex z = new Complex(x,y);
					int index=-1;
					double alpha = 1;
					
					if (!closeToRoot(z,0.01)) {		
						Iteration it = new Iteration(this.function,z,ITERATIONDEPTH);
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

}
