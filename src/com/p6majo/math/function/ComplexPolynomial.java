package com.p6majo.math.function;


import com.p6majo.math.complex.Complex;

/**
 * special type of complex function
 * @author jmartin
 *
 */
public class ComplexPolynomial extends ComplexFunction{
	/*This class does some basic manipulations with
	polynomials with Complex coefficients.  I set the
	maximum degree at 100*/

	Complex[] z=new Complex[100];   //the coefficients
	int D;  //the degree


	/**basic constructors*/
	public ComplexPolynomial() {
		D=0;
		for(int i=0;i<100;++i) z[i]=new Complex(0,0);
	}


	public ComplexPolynomial(Complex[] w) {
		D=w.length-1;
		for(int i=0;i<100;++i) z[i]=new Complex(0,0);
		for(int i=0;i<w.length;++i) z[i]=new Complex(w[i].re(),w[i].im());
	}

	public int getDegree() {
		return this.D;
	}
	
	public String toString() {
		String out = "";
		for(int i=0;i<=D;++i) {
			Integer I=new Integer(i);
		    if (z[i].compareTo(Complex.NULL)!=0) out+="("+z[i].toString()+")z^"+I.toString()+"+";
		}
		//remove trailing plus sign
		if (out.substring(out.length()-1, out.length()).compareTo("+")==0) out = out.substring(0,out.length()-1);
		return out;
	}

	/**vector space operations*/

	public ComplexPolynomial scale(Complex w) {
		ComplexPolynomial P=new ComplexPolynomial();
		P.D=D;
		for(int i=0;i<=D;++i) {
		    P.z[i]=w.times(this.z[i]);
		}
		return(P);
	    }

	    public ComplexPolynomial plus(ComplexPolynomial P) {
		int d;
		d=this.D;
		if(d<P.D) d=P.D;
		ComplexPolynomial Q=new ComplexPolynomial();
		Q.D=d;
		for(int i=0;i<99;++i)
		    Q.z[i]=z[i].plus(P.z[i]);
		return(Q);
	    }


	    public ComplexPolynomial minus(ComplexPolynomial P) {
		int d;
		d=this.D;
		if(d<P.D) d=P.D;
		ComplexPolynomial Q=new ComplexPolynomial();
		Q.D=d;
		for(int i=0;i<99;++i)
		    Q.z[i]=z[i].minus(P.z[i]);
		return(Q);
	    }


	    /**In this routine we encode a finite sequence as
	       the coefficients of a ComplexPolynomial.  Then we take the
	       difference sequence.*/

	    public ComplexPolynomial difference() {
		ComplexPolynomial P=new ComplexPolynomial();
		P.D=D-1;
		for(int i=0;i<D;++i) {
		    P.z[i]=z[i+1].minus(z[i]);
		}
		return(P);
	    }



	    /**This routine assumes that the effective degree of a
	       sequence f(0),f(1),...,f(d) is d.  Then it computes
	       the highest term (coeff of x^d) of the degree d
	       ComplexPolynomial which generates the sequence.  We only use
	       the routine after computing the effective degree of
	       the sequence*/

	    public ComplexPolynomial termFromGrowth(int d) {
		int test=0;
		ComplexPolynomial P=new ComplexPolynomial(this.z);
		for(int i=0;i<d;++i) {
		    P=P.difference();
		}
		Complex Z=P.z[0];
		ComplexPolynomial Q=new ComplexPolynomial();
		Q.z[0]=new Complex(0,0);
		Q.D=d;
		for(int i=1;i<=d;++i) {
	            Z=new Complex(Z.re()*1.0/i,Z.im()*1.0/i);
		    Q.z[i]=new Complex(0,0);
		}
		Q.z[d]=Z;
		return(Q);
	    }



	    /*Given a mononial of the form f(x)=Cx^D, this
	      routine computes f(0),...,f(d), and returns these
	      values encoded as a ComplexPolynomial.*/

	    public ComplexPolynomial createValues(int d) {
		ComplexPolynomial P=new ComplexPolynomial();
		if(d==0) {
		    P.D=0;
		    return(P);
		}
		P.D=d;
		Complex Z=z[D];
		for(int i=0;i<=d;++i) {
		    Complex X=new Complex(Math.pow(1.0*i,D),0);
		    P.z[i]=Z.times(X);
		}
		return(P);
	    }


		@Override
		public Complex eval(Complex z) {
			Complex returnValue = Complex.ONE.times(this.z[0]);
			Complex power = Complex.ONE;
			for (int i=1;i<this.D+1;i++) {
				Complex part = Complex.ONE;
				power = power.times(z);
				part = part.times(this.z[i]).times(power);
				returnValue = returnValue.plus(part);
			}
			return returnValue;
		}

		 @Override
		 public ComplexPolynomial derivative() {
			 ComplexPolynomial P=new ComplexPolynomial();
			 P.D=D-1;
			 for(int i=1;i<=D;++i) {
				 P.z[i-1]=this.z[i].times(new Complex(i,0));
			 }
			 return(P);
		 }


		@Override
		public Complex evalDerivative(Complex z) {
			return this.derivative().eval(z);
		}

	}

