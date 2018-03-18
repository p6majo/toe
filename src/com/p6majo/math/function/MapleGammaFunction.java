package com.p6majo.math.function;

import com.maplesoft.externalcall.MapleException;
import com.maplesoft.openmaple.ComplexNumeric;
import com.maplesoft.openmaple.Engine;
import com.p6majo.math.complex.Complex;

import java.util.Locale;

public class MapleGammaFunction extends MapleFunction {

	public MapleGammaFunction(Engine t) throws MapleException {
		super(t);
	}

	@Override
	public Complex eval(Complex z) throws MapleException {
		String number = "";
		if (z.im()<0) number = String.format(Locale.ROOT,"%f-%f*I",z.re(),Math.abs(z.im()));
		else number = String.format(Locale.ROOT,"%f+%f*I",z.re(),z.im());
		ComplexNumeric n = (ComplexNumeric) super.engine.evaluate(String.format("GAMMA(%s);", number) );
		return new Complex(n.realPart().doubleValue(),n.imaginaryPart().doubleValue());
	}

	@Override
	public MapleFunction derivative() {
		return null;
	}

	@Override
	public Complex evalDerivative(Complex z) throws MapleException{
		String number = "";
		if (z.im()<0) number = String.format(Locale.ROOT,"%f-%f*I",z.re(),Math.abs(z.im()));
		else number = String.format(Locale.ROOT,"%f+%f*I",z.re(),z.im());
		ComplexNumeric n = (ComplexNumeric) super.engine.evaluate(String.format("evalf(Psi(%s)*GAMMA(%s));",number,number ));
		return new Complex(n.realPart().doubleValue(),n.imaginaryPart().doubleValue());
	}

	
	
	
	
}
