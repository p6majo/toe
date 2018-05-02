package com.p6majo.math.function;


import com.maplesoft.externalcall.MapleException;
import com.maplesoft.openmaple.Engine;
import com.p6majo.math.complex.Complex;

public abstract class MapleFunction {
	
	
	public abstract Complex eval(Complex z) throws MapleException;
	public abstract MapleFunction derivative();
	public abstract Complex evalDerivative(Complex z) throws MapleException;
	
	protected final Engine engine ;
	
	/**
	 * constructs a maple function
	 * the engine is passed as a parameter to avoid repeated construction for multiple evaluations
	 * @param t
	 * @throws MapleException
	 */
	public MapleFunction(Engine t){
		this.engine = t;
	}
	
}
