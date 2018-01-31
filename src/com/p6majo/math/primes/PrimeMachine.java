package com.p6majo.math.primes;



import com.p6majo.math.utils.Params;

import java.io.*;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class PrimeMachine {

	private PrimeList primes = null;
	
	public PrimeMachine() {
		if(!load()){
			this.initialize();
		}
	}
	
	/**
	 * load range of primes that contains number
	 * @param number
	 * @return
	 */
	private boolean loadAppropriateRange(long number){
		//primes are stored in files, one file every 50 Million numbers
		if (primes!=null)
			if (number>=primes.smallestInList() && number<=primes.largestInList()) 
				return true;
		
		//load appropriate file		
		long multiplier = number/50000000;
		long filelabel = (multiplier+1)*50000000;
		String filename = Params.PATH+"Primes_"+filelabel+".dat";
		File file = new File(filename);
		if (file.exists()) 
			return load(filename);
		else 
			return false;
	}
	
	/**
	 * returns a list of the first primes smaller than 50 Mio
	 * @return
	 */
	public PrimeList getPrimes(){
		if (this.primes.first()!=2) this.load();
		return this.primes;
	}
		
	public void putPrime(long n){
		if (!primes.contains(n)) primes.add(n);
		this.save();
	}
	
	public boolean isPrime(long primeCandidate){
		if (this.loadAppropriateRange(primeCandidate))
			if (this.primes.contains(primeCandidate)) 
				return true; 
			else 
				return false;
		else
			for (int i=2;i<Math.sqrt(primeCandidate)+1;i++) 
				if (primeCandidate%i==0) 
					return false;
			return true;
	}
	
	/**
	 * returns the smallest prime larger than a given number assuming that it is stored already
	 * @param number
	 * @return
	 */
	public long nextPrime(long number){
		//primes are stored in files, one file every 50 Million numbers
		if (this.loadAppropriateRange(number))
				return primes.primeCeil(number);
		else return primeLargerThan(number);	
	}
	
	/**
	 * returns the smallest prime larger than a given number by brute force calculation
	 * @param number
	 * @return
	 */
	private long primeLargerThan(long number){
		long start = number;
		while (true){
			start++;
			boolean nonprime = false;
			for (long i=2;i<Math.sqrt(start)+1;i++){
				if (start%i==0) {nonprime = true;break;}
			}
			if (!nonprime) return start;
		}
	}
	
	public ArrayList<Long> primeFactors(Long number){
		ArrayList<Long> factors = new ArrayList<Long>();
		
		for (Long prime:this.primes){
			//check whether it is a prime number
			if (this.primes.indexOf(number)>-1) {
				factors.add(number);
				break;
			}
			else{
				while(number%prime==0){
					factors.add(prime);
					number /=prime;
				}
			}
		}
		return factors;
	}
	
	public SetOfPartitions getSetOfPartitions(Integer number){
		SetOfPartitions setOfPartitions = new SetOfPartitions(number,this);
		return setOfPartitions;
	}
	
public void initialize(){
	this.primes = new PrimeList();
	for (long i=2;i<Params.MAXPRIMENUMBER;i++){
		boolean prime = true;
		for (int j=2;j<=Math.sqrt(i);j++){
			if (i%j==0){
				prime = false;
				break;
			}
		}
		if (prime) this.primes.add(i);
	}
	this.save();
}

    public static PrimeList calculatePrimes(long from, long to){
	PrimeList primes = new PrimeList();
	for (long i=from;i<to;i++){
		boolean prime = true;
		for (int j=2;j<=Math.sqrt(i);j++){
			if (i%j==0){
				prime = false;
				break;
			}
		}
		if (prime) primes.add(i);
	}
	return primes;
}


    public static PrimeList calculatePrimesWithStream(long from, long to){
        PrimeList primes = new PrimeList();

        Predicate<Long> primePredicate = new Predicate<Long>(){

            /**
             * Evaluates this predicate on the given argument.
             *
             * @param aLong the input argument
             * @return {@code true} if the input argument matches the predicate,
             * otherwise {@code false}
             */
            @Override
            public boolean test(Long aLong) {
                for (long l = 2;l<=Math.sqrt(aLong);l++)
                    if (aLong%l==0) return false;
                return true;
            }
        };

        primes.addAll(LongStream.range(from,to+1).boxed().filter(primePredicate).collect(Collectors.toList()));
        return primes;
    }

	public static PrimeList calculatePrimesWithParallelStream(long from, long to){
		PrimeList primes = new PrimeList();

		Predicate<Long> primePredicate = new Predicate<Long>(){

			/**
			 * Evaluates this predicate on the given argument.
			 *
			 * @param aLong the input argument
			 * @return {@code true} if the input argument matches the predicate,
			 * otherwise {@code false}
			 */
			@Override
			public boolean test(Long aLong) {
				for (long l = 2;l<=Math.sqrt(aLong);l++)
					if (aLong%l==0) return false;
				return true;
			}
		};

		primes.addAll(LongStream.range(from,to+1).parallel().boxed().filter(primePredicate).collect(Collectors.toList()));

		return primes;
	}


	public static PrimeList calculatePrimesWithParallelRandomizedStream(long from, long to){
		PrimeList primes = new PrimeList();

		Predicate<Long> primePredicate = new Predicate<Long>(){

			/**
			 * Evaluates this predicate on the given argument.
			 *
			 * @param aLong the input argument
			 * @return {@code true} if the input argument matches the predicate,
			 * otherwise {@code false}
			 */
			@Override
			public boolean test(Long aLong) {
				for (long l = 2;l<=Math.sqrt(aLong);l++)
					if (aLong%l==0) return false;
				return true;
			}
		};

		primes.addAll(LongStream.range(from,to+1).parallel().boxed().filter(primePredicate).collect(Collectors.toList()));

		return primes;
	}

public boolean save(){
	String filename = this.toNameString()+".dat";

	
	PrintWriter writer=null;
	try {
		writer = new PrintWriter(Params.PATH+filename, "UTF-8");
	} catch (FileNotFoundException | UnsupportedEncodingException e) {
		e.printStackTrace();
		System.err.print(e.getMessage());
	}
	
	if (writer!=null) {
		for (Long prime:this.primes)
				writer.println(prime.toString());
		writer.close();
		return true;
	}
	return false;
}


public boolean load(String filename){
		
		primes = new PrimeList();
		try{
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while ((s=br.readLine())!=null){
				primes.add(Long.valueOf(s));
			}
			if (Params.LOGGING) System.out.print("Primes initialized between "+primes.first()+" and "+primes.last()+".\n");
			br.close();
			return true;
		}catch (Exception e){
			System.err.print(e.getMessage()+"\n");
			return false;
		}
}

/**
 * load the default file for primes (all primes smaller than 50 Mio)
 * @return
 */
public boolean load(){
		String filename = this.toNameString()+".dat";
		
		primes = new PrimeList();
		try{
			FileReader fr = new FileReader(Params.PATH+filename);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while ((s=br.readLine())!=null){
				primes.add(Long.valueOf(s));
			}
			System.out.print("Primes initialized up to size "+Params.MAXPRIMENUMBER+".\n");
			br.close();
			return true;
		}catch (Exception e){
			System.err.print(e.getMessage()+"\n");
			return false;
		}
	}

	public String toNameString(){
		return "Primes"+Params.MAXPRIMENUMBER;
	}

}
