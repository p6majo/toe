package com.p6majo.math.primes;

import com.p6majo.math.utils.Utils;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * data field where the primes are stored
 * @author jmartin
 *
 */
@SuppressWarnings("serial")
public class PrimeList extends TreeSet<Long> {
	public long smallestInList(){
		return this.first();
	}
	
	public long largestInList(){
		return this.last();
	}

	/**
	 * should not be used if avoidable
	 * @param position
	 * @return
	 */
	public long get(int position){
		int count = 0;
		if (position<0 || position>this.size()) Utils.errorMsg("position is not occupied in PrimeList");
		Iterator<Long> it = this.iterator();
		long next = -1;
		while (count<position){
			next = it.next();
			count++;
		}
		return next;
	}
	
	/**
	 * should not be used if avoidable
	 * search position of prime in prime list
	 * @param prime
	 * @return
	 */
	public int indexOf(long prime){
		if (prime<this.smallestInList() || prime>this.largestInList()) return -1;
		int count = 0;
		Iterator<Long> it = this.iterator();
		while (it.hasNext()){
			long next = it.next();
			if (next==prime) return count;
			count++;
		}
		return -1;
	}

	/**
	 * return smallest prime in list that is larger than the number
	 * @param number
	 * @return
	 */
	public long primeCeil(long number){
		return this.ceiling(number);
	}
	
	/**
	 * return smallest prime in list that is larger than the number
	 * @param number
	 * @return
	 */
	public long primeFloor(long number){
			return this.floor(number);
	}
}
