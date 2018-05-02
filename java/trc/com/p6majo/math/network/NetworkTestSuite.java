package com.p6majo.math.network;

import junit.framework.TestFailure;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.Enumeration;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	NetworkTest.class,NetworkStreamTest.class
})

public class NetworkTestSuite {
	
	public static void main(String[] a) {
	    
		TestResult result = new TestResult();
	      TestSuite suite = new TestSuite(
	      		NetworkTest.class,NetworkStreamTest.class
		  );
	      suite.run(result);
	      System.out.println("Number of test cases = " + result.runCount());
	     
	      Enumeration<TestFailure> failures = result.failures();
	      while(failures.hasMoreElements()) {
		         TestFailure failure = failures.nextElement();
		         System.out.println(failure.toString());
		      }
				
	      System.out.println(result.wasSuccessful());

	   }


}