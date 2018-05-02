package com.p6majo.math.primes;


import com.p6majo.math.utils.Params;
import org.apfloat.Apint;

import java.io.*;
import java.util.StringTokenizer;

public class LucasLehmer implements	 CheckCompleteListener{
	
	private long p  = 3;
	private Apint twoTopm1 = new Apint(7);
	private Long prime = null;
	private PrimeLoader loader = null;
	
	public LucasLehmer() {
		if(!load()){
			this.initialize();
		}
	}

	public static void main(String[] args) {
		LucasLehmer lucasLehmer= new LucasLehmer();
		lucasLehmer.loader = new PrimeLoader();
		
		//initialize position in primeLoader
		lucasLehmer.prime = lucasLehmer.loader.next();
		while (lucasLehmer.prime<lucasLehmer.p) {
			lucasLehmer.prime = lucasLehmer.loader.next();
			if (lucasLehmer.prime==null) lucasLehmer.tmpSave();
		}
		
		System.out.println("Current lucasLehmer.p: "+lucasLehmer.p+" current prime: "+lucasLehmer.prime.toString());
		
		int processors  = Runtime.getRuntime().availableProcessors();
		//The following lines take care of the consecutive execution of the the calculation threads for the table rows
		//at most the number of processors threads run simultaneously
		
		for (int i=0;i<processors;i++){
			lucasLehmer.startNextThread();
		}
			
	}

		private void startNextThread(){
			long oldprime = prime;
			prime = loader.next();
			System.out.println(prime+" in progress");
			if (prime == null) tmpSave(); //exit when last prime is used
		
			
			long delta = prime - oldprime;
			Apint twoToP = twoTopm1.add(new Apint(1));
			for (int i=0;i<delta;i++) 
				twoToP = twoToP.multiply(new Apint(2));
			twoTopm1 = twoToP.add(new Apint(1).negate());
			
			ThreadForCheck thread = new ThreadForCheck(prime,twoTopm1);
			thread.addListener(this);
			thread.start();
		}
	
	
	public void initialize(){
		//create file and save initial data
		String filename = toNameString()+".dat";

		PrintWriter writer=null;
		try {
			writer = new PrintWriter(Params.PATH+filename, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			System.err.print(e.getMessage());
		}
		
		if (writer!=null) {
			writer.println(this.p+"|"+this.twoTopm1);
			writer.close();
		}
	}
	
	public void tmpSave(){
		System.err.println("Maximal prime number is reached!"); 
		System.exit(0);
	}
	
	public boolean save(){
		String filename = toNameString()+".dat";
		
		PrintWriter writer=null;
		try {
			FileWriter fw = new FileWriter(Params.PATH+filename,true); //true option for appending
			BufferedWriter bw = new BufferedWriter(fw);
			writer = new PrintWriter(bw);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.print(e.getMessage());
		}
		
		if (writer!=null) {
			writer.println(this.p+"|"+this.twoTopm1);
			writer.close();
			
			System.out.println(this.p);
			System.out.println(this.twoTopm1);
			//System.out.println(this.seqPm1);
			System.out.println("");
			return true;
		}
		return false;
	}

	public boolean load(){
			
			String filename = toNameString()+".dat";
			try{
				FileReader fr = new FileReader(Params.PATH+filename);
				BufferedReader br = new BufferedReader(fr);
				String line="" ;
				String s;
				//get last line
				while ((s=br.readLine())!=null){
					line = s;
				}
				br.close();
				
				StringTokenizer tokens = new StringTokenizer(line,"|");
				while (tokens.hasMoreTokens()){
					this.p = Long.parseLong(tokens.nextToken());
					this.twoTopm1 = new Apint(tokens.nextToken());
				}
				
				System.out.println("Last data read in: "+this.p+" "+this.twoTopm1);//+" "+this.seqPm1);
				return true;
			}catch (Exception e){
				System.err.print(e.getMessage()+"\n");
				return false;
			}
		}
	
	public String toNameString(){
		return "LucasLehmer";
	}

	@Override
	public void notifyOfCalculationFinished(Thread thread, boolean result) {
		if (result){
			System.out.println("New prime found for p = "+prime);
			p = prime;
			save();
		}
		this.startNextThread();
	}
	
}
