package com.p6majo.math.primes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Construct all possible factor partitions of a given number
 * @author jmartin
 *
 */
public class SetOfPartitions extends TreeSet<ArrayList<Long>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -310265500030710092L;
	private Long toBePartitioned = null;
	private PrimeMachine pm = null;
	
	private static Comparator<ArrayList<Long>> partitionComparator = new Comparator<ArrayList<Long>>(){
		@Override
		public int compare(ArrayList<Long> a1, ArrayList<Long> a2){
			int i = 0;
			while (i<Math.min(a1.size(),a2.size())-1 && a1.get(i).compareTo(a2.get(i))==0) i++;
			return Long.compare(a1.get(i),a2.get(i));
		}
	};
	
	/**
	 * Construct the SetOfPartitions with an appropriate Comparator
	 */
	public SetOfPartitions(long number, PrimeMachine pm){
		super(partitionComparator);
		this.toBePartitioned = number;
		this.pm = pm;
		this.generatePartitions(true);
	}
	/**
	 * Construct the SetOfPartitions with an appropriate Comparator
	 */
	public SetOfPartitions(long number, PrimeMachine pm,boolean expansion){
		super(partitionComparator);
		this.toBePartitioned = number;
		this.pm = pm;
		this.generatePartitions(expansion);
	}
	

	private void generatePartitions(boolean fullExpansion){	
		ArrayList<Long> primeFactors = this.pm.primeFactors(this.toBePartitioned);
		
		/*
		if (!fullExpansion){
			if (primeFactors.size()>2){
				//merge equal factors to power again
				boolean doubles = true;
				int pos = 0;
				while (pos<primeFactors.size()){
					doubles = false;
					Long factor = primeFactors.get(pos);
					for (int i=pos+1;i<primeFactors.size();i++){
						Long tmpFactor = primeFactors.get(i);
						if (tmpFactor == factor){
							doubles = true;
							break;
						}
					}
					if (doubles){
						primeFactors.remove(pos);
						primeFactors.set(pos, (long) Math.pow(primeFactors.get(pos),2));
					}
					else pos++;
				}
			}
		}
		*/
		ArrayList<Long> tmpPartition=null;
		SetOfPartitions tmpPartitions = new SetOfPartitions();
		
		int partitionSize = primeFactors.size();
		this.add(primeFactors);
		tmpPartitions.add(primeFactors);
		
		while (partitionSize>2){
		//Contract partitions
			SetOfPartitions tmp2Partitions = new SetOfPartitions();
			for (ArrayList<Long> partition:tmpPartitions){
				for (int i=0;i<partition.size();i++)
					for (int j=i+1;j<partition.size();j++){
						tmpPartition = new ArrayList<Long>();
						for (int k=0;k<partition.size();k++){
							if (k==j) tmpPartition.add(partition.get(i)*partition.get(j));
							else if (k!=i) tmpPartition.add(partition.get(k));
						}
						Collections.sort(tmpPartition);
						tmp2Partitions.add(tmpPartition);
				}
			}
			//add new partitions to the full set
			tmpPartitions.clear();
			for (ArrayList<Long> partition:tmp2Partitions) {this.add(partition);tmpPartitions.add(partition);}
			partitionSize--;
		}
	}
		
	/**
	 * Construct the SetOfPartitions with an appropriate Comparator
	 */
	public SetOfPartitions(){
		super(partitionComparator);
	}


	public String toString(){
		String output = "{";
		for (ArrayList<Long> partition:this) output+=partition.toString()+",";
		output = output.substring(0,output.length()-1)+"}";
		return output;
	}
	
}
