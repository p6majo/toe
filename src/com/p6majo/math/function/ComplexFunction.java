package com.p6majo.math.function;

import com.p6majo.math.complex.Complex;

public abstract class ComplexFunction {
	
	protected Complex coefficient= Complex.ONE;
	
	public abstract Complex eval(Complex z);
	public abstract ComplexFunction derivative();
	public abstract Complex evalDerivative(Complex z);
	
	public Complex getCoefficient() {
		return coefficient;
	}
	public void setCoefficient(Complex coefficient) {
		this.coefficient = coefficient;
	}
}
