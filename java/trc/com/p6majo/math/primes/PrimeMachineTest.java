package com.p6majo.math.primes;


import junit.framework.TestCase;
import org.junit.Test;

public class PrimeMachineTest extends TestCase {
    @Test
    public void testCalculatePrimes() throws Exception {
        long start = System.currentTimeMillis();
        long from = 2;
        long to = 10;

        PrimeList primes = PrimeMachine.calculatePrimes(from,to);
        System.out.println(primes.toString());
        System.out.println(primes.size()+" primes calculated from "+from+" to "+to+" in "+(System.currentTimeMillis()-start)+" ms.");

        assertEquals(primes.size(),4);


        start = System.currentTimeMillis();
        primes = PrimeMachine.calculatePrimesWithStream(from,to);
        System.out.println(primes.toString());
        System.out.println(primes.size()+" Primes calculated from "+from+" to "+to+" in "+(System.currentTimeMillis()-start)+" ms.");

        assertEquals(primes.size(),4);

        to = 1000000;

        start =System.currentTimeMillis();
        primes = PrimeMachine.calculatePrimes(from,to);
       // System.out.println(primes.toString());
        System.out.println(primes.size()+" primes calculated from "+from+" to "+to+" in "+(System.currentTimeMillis()-start)+" ms.");

        assertEquals(primes.size(),78498);


        start = System.currentTimeMillis();
        primes = PrimeMachine.calculatePrimesWithStream(from,to);
       // System.out.println(primes.toString());
        System.out.println(primes.size()+" primes calculated with stream from "+from+" to "+to+" in "+(System.currentTimeMillis()-start)+" ms.");

        assertEquals(primes.size(),78498);

        start = System.currentTimeMillis();
        primes = PrimeMachine.calculatePrimesWithParallelStream(from,to);
        // System.out.println(primes.toString());
        System.out.println(primes.size()+" primes calculated with parallel stream from "+from+" to "+to+" in "+(System.currentTimeMillis()-start)+" ms.");

        assertEquals(primes.size(),78498);


        to = 10000000;

        start =System.currentTimeMillis();
        primes = PrimeMachine.calculatePrimes(from,to);
    // System.out.println(primes.toString());
        System.out.println(primes.size()+" primes calculated from "+from+" to "+to+" in "+(System.currentTimeMillis()-start)+" ms.");

        assertEquals(primes.size(),664579);

    start = System.currentTimeMillis();
    primes = PrimeMachine.calculatePrimesWithStream(from,to);
    // System.out.println(primes.toString());
        System.out.println(primes.size()+" primes calculated with stream from "+from+" to "+to+" in "+(System.currentTimeMillis()-start)+" ms.");

    assertEquals(primes.size(),664579);

    start = System.currentTimeMillis();
    primes = PrimeMachine.calculatePrimesWithParallelStream(from,to);
    // System.out.println(primes.toString());
        System.out.println(primes.size()+" primes calculated with parallel stream from "+from+" to "+to+" in "+(System.currentTimeMillis()-start)+" ms.");

    assertEquals(primes.size(),664579);

    }


}