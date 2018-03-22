package com.p6majo.math.fraction;

import com.p6majo.math.utils.Utils;
import org.apfloat.Apint;

public class Fraction  {
	private Apint num;
	private Apint den;

	public static Fraction NULL = new Fraction(Apint.ZERO,Apint.ONE);
	public static Fraction ONE =  new Fraction(Apint.ONE,Apint.ONE);

	public Fraction(long num,long den){
		this(new Apint(num),new Apint(den));
	}
	
	public Fraction(Apint num, Apint den) {
		if (den.compareTo(Apint.ZERO)==0) {System.err.println("Dividing by zero in FRACTION");System.exit(0);}
		else if (num.compareTo(Apint.ZERO)==0) {this.num = Apint.ZERO; this.den =Apint.ONE;}
		else{
			
			Apint ggt = Utils.ggT(num,den);
			this.num = num.divide(ggt);
			this.den = den.divide(ggt);
		}
	}

	public Fraction add(Fraction f) {
			return new Fraction(this.num.multiply(f.den).add(f.num.multiply(this.den)),this.den.multiply(f.den));
	}

	public Fraction negative() {
		return new Fraction(this.num.negate(),this.den);
	}

	public Fraction mul(Fraction f) {
			return new Fraction(this.num.multiply(f.num),this.den.multiply(f.den));
	}

	public Fraction inverse() {
		return new Fraction(this.den,this.num);
	}

	public String toString(){
		if (this.den.compareTo(Apint.ONE)==0) return this.num.toString()+""; else return "("+this.num.toString()+"/"+this.den.toString()+")";
	}

	public int compareTo(Fraction o) {
			return this.num.multiply(o.den).compareTo(this.den.multiply(o.num));
	}


	@Override
	public Fraction clone() {
		return new Fraction(this.num,this.den);
	}

    /**
     * returns whether the absolute value of the fraction is smaller than 1
     * @return
     */
    public boolean isProper() {
       if (this.num.compareTo(this.den)<0) return true; else return false;
    }

    public Apint getDenominator(){
        return this.den;
    }

    public long getDenominatorLongValue(){
        return this.den.longValue();
    }

    public int getDenominatorIntValue(){
        return this.den.intValue();
    }

    public double getDoubleValue(){
        return this.num.doubleValue()/this.den.doubleValue();
    }
}
