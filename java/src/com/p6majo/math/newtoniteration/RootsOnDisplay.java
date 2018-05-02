package com.p6majo.math.newtoniteration;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexPolynomial;
import com.p6majo.math.utils.Utils;
import com.princeton.Draw;

import java.awt.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

/**
 * This class generates all roots of a class of polynomials of a fixed degree n
 * and integer coefficients, whose absolute value is bounded by d
 */
public class RootsOnDisplay {
   private final int n;
   private final int d;
   double max;

   public class ColoredRoot implements Comparable<ColoredRoot>{
       public Complex root;
       public Color color;
       public ColoredRoot(Complex r,Color c){
           this.root = r;
           this.color = c;
       }

       @Override
       public int compareTo(ColoredRoot o) {
           return this.root.compareTo(o.root);
       }
   }

   private Set<ColoredRoot> roots = Collections.synchronizedSortedSet(new TreeSet<ColoredRoot>());


   private Draw frame;

   public RootsOnDisplay(int degree, int maxValue){
       this.n = degree;
       this.d = maxValue;
       if (d<1) Utils.errorMsg("The bound on the coefficients must be at least 1.");
       if (n<1) Utils.errorMsg("The degree of the polynomial must be at least 2.");
       this.roots = new TreeSet<ColoredRoot>();
   }

   public void generateRoots(){
       TreeSet<Integer[]> newPolynomials=null;
       TreeSet<Integer[]> polynomials = new TreeSet<Integer[]>(new IntegerArrayComparator());
       Integer[] polStart = new Integer[n+1];
       polStart[n]=1;
       polynomials.add(polStart);
       Integer[] polStart2 = new Integer[n+1];
       polStart2[n]=-1;
       polynomials.add(polStart2);

       for (int position =0;position<n+1;position++){
           newPolynomials = new TreeSet<Integer[]>(new IntegerArrayComparator());
           int start;
           if (position<n) start=0;else start=2;
           for (int c=start;c<d+1;c++){
               for (Integer[] pol:polynomials) {
                   Integer[] newPol = pol.clone();
                   newPol[position] = c;
                   newPolynomials.add(newPol);
                   if (c!=0) {
                       newPol = pol.clone();
                       newPol[position] = -c;
                       newPolynomials.add(newPol);
                   }
               }
           }
           polynomials = newPolynomials;
       }

       System.out.println("Calculate roots for "+newPolynomials.size()+" polynomials.");
       newPolynomials.stream().parallel().forEach(i->getRoots(i));
      // System.out.println(newPolynomials.stream().map(LayerUtils::arrayOfIntegerToString).collect(Collectors.joining("\n")));

   }

   private void getRoots(Integer[] coeff){

       Complex[] coeffs = new Complex[n+1];
       for (int c=0;c<n+1;c++) coeffs[c]= Complex.ONE.scale(coeff[c]);

       ComplexPolynomial pol = new ComplexPolynomial(coeffs);
       TreeSet<Complex> tmpRoots = new RootFinder().findRoots(pol);
       Color color = new Color((int) (127+1.0*coeff[n-2]/d*127),(int) (127+1.0*coeff[n-1]/d*127),(int) (127+1.0*coeff[n]/d*127));
       tmpRoots.stream().forEach(r->this.roots.add(new ColoredRoot(r,color)));
   }

   public Predicate<ColoredRoot> imgPositive = (cr)->cr.root.im()>=0;

   public void displayRoots(){
        Iterator<ColoredRoot> it = roots.iterator();
        //get the smallest element to get the absolute value
        Complex maxRoot = null;
        while (it.hasNext()) maxRoot =it.next().root;
        max = maxRoot.abs()/2;
         //roots.stream().mapToDouble(r->r.root.abs()).collect(Collectors.maxBy(Double::compare));
        int size =2800;
        System.out.println("Groesse: "+max);
        frame = new Draw();
        frame.setCanvasSize(size,size/2);
        frame.clear(Color.BLACK);
        frame.setXscale(-max,max);
        frame.enableDoubleBuffering();
        frame.setYscale(-0.01,max);
        this.roots.stream().filter(imgPositive).forEach(r->{frame.setPenColor(r.color);frame.filledCircle(r.root.re(),r.root.im(),0.0005);});
        frame.show();
   }

   public void showGrid(){
       int m = (int) Math.round(max)*2;
       frame.circle(0,0,1);

       //grid
       frame.setPenColor(Color.WHITE);
       for (int i=-m;i<m+1;i++){
           frame.line(-m,i,m,i);
           frame.line(i,-m,i,m);
       }
       //subgrid
       frame.setStroke(Draw.STROKE_TYPE.Dotted);

       for (int i=-m*10;i<m*10+1;i++){
           frame.line(-m,(float)i/10,m,(float) i/10);
           frame.line((float) i/10,-m,(float) i/10,m);
       }


       frame.show();
   }

}
