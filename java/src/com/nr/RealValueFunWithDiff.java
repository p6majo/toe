package com.nr;

public interface RealValueFunWithDiff extends RealValueFun{
	
	void df(double[] x, double[] df);
}
