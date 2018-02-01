package com.p6majo.math.mandelbrot;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexFunction;
import com.p6majo.math.utils.Resolution;
import com.p6majo.math.utils.Box;
import com.p6majo.math.utils.Color;
import com.p6majo.math.utils.Utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MandelIterator {

	private static final int ITERATIONDEPTH = 100;
	private static final double MAXVAL = 50;
	private static final int BORDER = 0;

	private final Resolution resolution;
	private final Box box;
	private final String filename;
	private final ComplexFunction function;

	private TreeSet<Complex> roots = new TreeSet<Complex>();
	private List<Complex> indexedRoots;
	private Color[] colors;


	public MandelIterator(ComplexFunction function, Box box, Resolution resolution, String name) {
		this.function = function;
		this.box = box;
		this.resolution = resolution;
		this.filename = name;
	}


	/**
	 * generate the color depending on the number of steps of iteration
	 * @param iterations
	 * @return
	 */
	private Color generateColor(int iterations) {
		return new Color((int) (255.*Math.abs(Math.log(iterations)/Math.log(ITERATIONDEPTH))),0,0);
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
            Iteration it = new Iteration(function,z);
            if (it.getSteps()<ITERATIONDEPTH-BORDER) return generateColor(it.getSteps()).toString();
            else if(it.getSteps()<ITERATIONDEPTH) return Color.WHITE.toString();
            else return Color.BLACK.toString();
		}
	};

    public void generateImage() {
        String out = "";
        long start = System.currentTimeMillis();


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




	
	/************************/
	/*     auxiliary class  */
	/************************/
	
	private class Iteration{
		int steps = -1;

		public Iteration(ComplexFunction function,Complex z) {
			Complex z1 = z;
			double distance = z1.abs();
			
			while (distance < MAXVAL && steps<ITERATIONDEPTH) {
				z1 = function.eval(z1).plus(z);
				distance = z1.abs();
				steps++;
			}
		}
		
		public int getSteps() {
			return this.steps;
		}

	}
	
}
