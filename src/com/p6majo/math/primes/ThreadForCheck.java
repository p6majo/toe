package com.p6majo.math.primes;

import org.apfloat.Apint;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ThreadForCheck extends Thread {
	
	private long prime;
	private Apint twoTopm1;
	
	private final Set<CheckCompleteListener> listeners = new CopyOnWriteArraySet<CheckCompleteListener>();
	
	public final void addListener(final CheckCompleteListener listener) {
		listeners.add(listener);
	}

	public final void removeListener(final CheckCompleteListener listener) {
		listeners.remove(listener);
	}
	
	private final void notifyListeners(boolean result) {
		for (CheckCompleteListener listener : listeners) {
			listener.notifyOfCalculationFinished(this, result);
		}
	}
	
	public ThreadForCheck(long prime, Apint twoTopm1){
		this.prime = prime;
		this.twoTopm1 = twoTopm1;
	}
	
	@Override
	public final void run(){
		final long begin = System.currentTimeMillis();
		
		boolean result = false;
		Apint seq = new Apint(4);
		for (int i=1;i<prime-1;i++){
			seq = seq.multiply(seq).add(new Apint(2).negate());
			seq = seq.mod(this.twoTopm1);
			//System.out.println(prime+": "+seq.toString());
		}
		if (seq.compareTo(Apint.ZERO)==0) result =  true;
		
		System.out.printf("Calculation time for prime %d: %d ms.\n",this.prime, System.currentTimeMillis()-begin);
		this.notifyListeners(result);
	}
	
	
}