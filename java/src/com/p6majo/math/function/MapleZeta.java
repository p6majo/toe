package com.p6majo.math.function;

import com.maplesoft.externalcall.MapleException;
import com.maplesoft.openmaple.ComplexNumeric;
import com.maplesoft.openmaple.Engine;
import com.p6majo.math.complex.Complex;

import java.util.Locale;

public class MapleZeta extends MapleFunction {

	public MapleZeta(Engine t) throws MapleException {
		super(t);
	}

	@Override
	public Complex eval(Complex z) throws MapleException {
		String number = "";
		if (z.im()<0) number = String.format(Locale.ROOT,"%f-%f*I",z.re(),Math.abs(z.im()));
		else number = String.format(Locale.ROOT,"%f+%f*I",z.re(),z.im());
		//System.out.print(number+" ");
		ComplexNumeric n = (ComplexNumeric) super.engine.evaluate(String.format("evalf(Zeta(%s)):", number) );
		//System.out.println(n.toString());
		return new Complex(n.realPart().doubleValue(),n.imaginaryPart().doubleValue());
	}

	@Override
	public MapleFunction derivative() {
		return null;
	}

	@Override
	public Complex evalDerivative(Complex z) throws MapleException{
		return null;
	}

	
	
	
	
}
