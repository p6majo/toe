package com.p6majo.math.primes;


import com.p6majo.math.utils.Params;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PrimeLoader {
	BufferedReader loader = null;
	
	
	public PrimeLoader() {
		String filename = this.toNameString()+".dat";
		try{
			FileReader fr = new FileReader(Params.PATH+filename);
			loader = new BufferedReader(fr);
		}
		catch(IOException e){
			System.err.println(e.getMessage());
		}
	}
	
	public Long next() {
		try{
			String s="";
			s=loader.readLine();
			if (s!=null) return Long.parseLong(s); else return null;
		}
		catch(Exception e){
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	public void close() throws Exception{
		loader.close();
	}
	
	public String toNameString(){
		return "Primes";
	}

}
