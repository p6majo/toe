package com.p6majo.math.primes;


import com.p6majo.math.utils.Params;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PrimeGenerator {
	
	static TreeSet<Long> primes=new TreeSet<Long>();
	
	public static synchronized void addPrime(long prime){
		primes.add(prime);
	}
	public static void main(String[] args) {
		
		//the idea is to generate primes an store them in files 
		//the step size is 50 000 000
		
		long stepsize = 50000000l;
		long top = stepsize; 
		 
		 while (true){
			 
			 if (load(Params.PATH+"Primes_"+top+".dat")) top+=stepsize;
			 else {
				 //perform generation
				 primes.clear();
				 
				 //The following lines take care of the consecutive execution of the the calculation threads for the table rows
				 //at most the number of processors threads run simultaneously
				 int processors  = Runtime.getRuntime().availableProcessors();
					
				 ExecutorService service = Executors.newFixedThreadPool(processors);
				 List<Future<ThreadForPrimeCalculation>> futures = new ArrayList<Future<ThreadForPrimeCalculation>>();
				
				 
				 long start=Math.max(2,top-stepsize);
				 long delta = stepsize/processors;
				 for (int p=0;p<processors;p++){
					 long threadStart = start+p*delta;
					 long finish=0;
					 if (p==(processors-1)) 
						 finish = start+stepsize;
					 else 
						 finish = start+(p+1)*delta;
					 ThreadForPrimeCalculation calcThread = new ThreadForPrimeCalculation(threadStart,finish);
					 @SuppressWarnings("unchecked")
					 Future<ThreadForPrimeCalculation> f = (Future<ThreadForPrimeCalculation>) service.submit(calcThread);
					 futures.add(f);
					}
				
					 // wait for all tasks to complete before continuing
				    try{
				    	for (Future<ThreadForPrimeCalculation> f : futures)
				    		f.get();
				    }
				    catch(Exception ex){
				    	System.err.println(ex.getMessage());
				    }
				 
				 save(top,primes);
			}
		 }
	}

	
	public static boolean save(long currentStep, TreeSet<Long> primes){
		String filename = Params.PATH+"Primes_"+currentStep+".dat";
		
		PrintWriter writer=null;
		try {
			FileWriter fw = new FileWriter(filename,true); //true option for appending
			BufferedWriter bw = new BufferedWriter(fw);
			writer = new PrintWriter(bw);
		} catch (IOException e) {
			//file does not exist
			
			try{
				writer = new PrintWriter(filename,"UFT-8");
			}
			catch (IOException e2){
				e2.printStackTrace();
				System.err.print(e2.getMessage());
			}
			e.printStackTrace();
			System.err.print(e.getMessage());
		}
		
		if (writer!=null) {
			Iterator<Long> it = primes.iterator();
			while (it.hasNext())
					writer.println(it.next().toString());
			writer.close();
			
			System.out.println(primes.size()+" primes added.");
			return true;
		}
		return false;
	}

	public static boolean load(String filename){
			
			//read last prime from file
			
			try{
				RandomAccessFile fh = new RandomAccessFile(filename,"r");
				long fileLength = fh.length()-1;
				StringBuilder lastPrimeString = new StringBuilder();
				
				//read last line
				for(long filePointer = fileLength; filePointer != -1; filePointer--){
					fh.seek( filePointer );
					int readByte = fh.readByte();
					
					if( readByte == 0xA ) {
						if( filePointer == fileLength ) {
							continue;
						}
						break;

					} 
					else if( readByte == 0xD ) {
						if( filePointer == fileLength - 1 ) {
							continue;
						}
						break;
					}

					lastPrimeString.append( ( char ) readByte );
				}
				
				fh.close();
				return true;
			}catch (Exception e){
				e.getStackTrace();
				System.err.print(e.getMessage()+"\n");
				return false;
			}
			
	}
	
	
}
