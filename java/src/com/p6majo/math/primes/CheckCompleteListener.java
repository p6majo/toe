package com.p6majo.math.primes;

public interface CheckCompleteListener {
	    void notifyOfCalculationFinished(final Thread thread, boolean result);
}
