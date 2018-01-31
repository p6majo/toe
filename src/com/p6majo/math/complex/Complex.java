package com.p6majo.math.complex;

import com.p6majo.html.Html;
import com.p6majo.math.utils.Box;

public class Complex extends Number implements Html,Comparable<Complex>,Cloneable{

	/*
	 * accuracy used for comparisons
	 */
	public static final double EPS_FLOAT= 1e-6;
	/*
	 * accuracy that should be reached in calculations, when comparisons are involved
	 */
	public static final double EPS_DOUBLE= 1e-12;

	public static final Complex ONE = new Complex(1,0);
	public static final Complex NULL = new Complex(0,0);
	public static final Complex I = new Complex(0,1);
	
	public double real;
	public double imag;
	public Complex(double re, double im) {
		this.real = re;
		this.imag = im;
	}

	public static Complex random(double scale) {
		return new Complex(-scale+Math.random()*2*scale,-scale+Math.random()*2*scale);
	}
	
	public static Complex random(double scaleRe, double scaleIm) {
		return new Complex(-scaleRe+Math.random()*2*scaleRe,-scaleIm+Math.random()*2*scaleIm);
	}
	
	
	public static Complex random(Box box) {
		return new Complex(box.lowleftx+Math.random()*(box.uprightx-box.lowleftx),box.lowlefty+Math.random()*(box.uprighty-box.lowlefty));
	}
	
	public Complex times(Complex factor){
		return new Complex(this.real*factor.real-this.imag*factor.imag,this.real*factor.imag+this.imag*factor.real);
	}
	
	public Complex plus(Complex summand){
		return new Complex(this.real+summand.real, this.imag+summand.imag);
	}
	
	
	/**
	 * Compatability class for math-it complex numbers
	 * @param s
	 * @return
	 */
	public static  double[] mathIt(Complex s){
		return new double[]{s.real,s.imag};
	}
	
	/**
	 * Compatability class for math-it complex numbers
	 * @return
	 */
	public static Complex mathIt(double[] s){
		return new Complex(s[0],s[1]);
	}
	
	
	public double re() { return this.real; }
    public double im() { return this.imag; }

    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }

    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.real - b.real;
        double imag = a.imag - b.imag;
        return new Complex(real, imag);
    }
	
	public Complex conjugate(){
		return new Complex(this.real,-this.imag);
	}
	
	public Complex neg(){
		return new Complex(-this.real,-this.imag);
	}
	
	public Complex scale(double lambda){
		return new Complex(lambda*this.real,lambda*this.imag);
	}
	
	
    public double abs() {
        return Math.hypot(this.real, this.imag);
    }

    public double phase() {
        return Math.atan2(this.imag, this.real);
    }
	
    public Complex reciprocal() {
        double scale = this.real*this.real + this.imag*this.imag;
        return new Complex(this.real / scale, -this.imag / scale);
    }

    public Complex exp() {
        return new Complex(Math.exp(this.real) * Math.cos(this.imag), Math.exp(this.real) * Math.sin(this.imag));
    }
    
    public Complex log() {
    	return new Complex(Math.log(Math.sqrt(this.real*this.real+this.imag*this.imag)),this.phase());
    }

    // return a new Complex object whose value is the complex sine of this
    public Complex sin() {
        return new Complex(Math.sin(this.real) * Math.cosh(this.imag), Math.cos(this.real) * Math.sinh(this.imag));
    }

    // return a new Complex object whose value is the complex cosine of this
    public Complex cos() {
        return new Complex(Math.cos(this.real) * Math.cosh(this.imag), -Math.sin(this.real) * Math.sinh(this.imag));
    }

    // return a new Complex object whose value is the complex tangent of this
    public Complex tan() {
        return this.sin().divides(this.cos());
    }
    
    
    public Complex clone() {
    	return new Complex(this.real,this.imag);
    }
    
	public String toString(){
		double im = this.imag;
		double re = this.real;
		if (Math.abs(this.imag)<EPS_FLOAT) im=0.;
		if (Math.abs(this.real)<EPS_FLOAT) re=0.;
		
		if (re==0 && im==0.) return "0";
		else if (re==0.) return String.format("%.5fi", im);
		else if (im==0.) return String.format("%.5f",re);
		else if (im>0) return String.format("(%.5f+%.5fi)", re,im);
		else return String.format("(%.5f - %.5fi)",re,Math.abs(im));
	}

	@Override
	public String toHtmlString() {
		return String.format("(%f+%f<b>i</b>)", this.real,this.imag);
	}

	@Override
	public int compareTo(Complex other) {
		Complex diff = this.minus(other);
		if (diff.abs()<EPS_FLOAT) return 0;
		else {
		    //check for the relative difference
            //one of them has to be larger than zero
            Complex max = (this.abs()>other.abs())? this:other;
		    diff = diff.divides(max);
		    if (diff.abs()<EPS_FLOAT) return 0;
		    //check for similar absolute values
            //they are compared by  the value of the phase
			double abs1 = this.abs();
			double abs2 = other.abs();
			if (Math.abs(abs1-abs2)/abs1<EPS_FLOAT) {
				double phase1 = this.phase();
				double phase2 = other.phase();
				return Double.compare(phase1, phase2);
			}
			else return Double.compare(abs1, abs2);
		}
	}

	public boolean isReal(){
		if (Double.compare(this.im(),0)==0) return true;else return false;
	}


	@Override
	public boolean equals(Object other) {
		if (other instanceof Complex)
			if (this.compareTo((Complex) other)==0) return true; else return false;
		return false;
	}


    /**
     * Returns the value of the specified number as an {@code int},
     * which may involve rounding or truncation.
     *
     * @return the numeric value represented by this object after conversion
     * to type {@code int}.
     */
    @Override
    public int intValue() {
        return (int) Math.round(real);
    }

    /**
     * Returns the value of the specified number as a {@code long},
     * which may involve rounding or truncation.
     *
     * @return the numeric value represented by this object after conversion
     * to type {@code long}.
     */
    @Override
    public long longValue() {
        return (long) Math.round(real);
    }

    /**
     * Returns the value of the specified number as a {@code float},
     * which may involve rounding.
     *
     * @return the numeric value represented by this object after conversion
     * to type {@code float}.
     */
    @Override
    public float floatValue() {
        return (float) real;
    }

    /**
     * Returns the value of the specified number as a {@code double},
     * which may involve rounding.
     *
     * @return the numeric value represented by this object after conversion
     * to type {@code double}.
     */
    @Override
    public double doubleValue() {
        return real;
    }
}
