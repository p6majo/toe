package com.p6majo.math.primes;

public class ThreadForPrimeCalculation extends Thread {
	private long start;
	private long finish;
	
	public ThreadForPrimeCalculation(long start, long finish){
		this.start = start;
		this.finish = finish;
	}
	
	@Override
	public final void run(){
		final long begin = System.currentTimeMillis();
		
		for (long number = this.start;number<this.finish+1;number++){
			 boolean prime = true;
			 if (number>2 && number%2==0) prime = false;
			 else 
				 for (long j=3;j<=Math.sqrt(number);j=j+2)
						if (number%j==0) {prime = false; break;}
			 if (prime) PrimeGenerator.addPrime(number);
		 }
		
		System.out.printf("Scan for primes in the range %d to %d performed in %d ms.\n",this.start, this.finish, System.currentTimeMillis()-begin);
	}
}
