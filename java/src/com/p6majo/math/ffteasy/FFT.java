package com.p6majo.math.ffteasy;


import com.p6majo.math.complex.Complex;
import com.p6majo.math.utils.Utils;

/**
 * Class that contains all functions of FFTEASY translated from c to java
 * {@ref http://www.felderbooks.com/ffteasy/ffteasy.c}
 * @author jmartin
 *
 * Licence requirements for FFTEASY:
 * 
 * FFTEASY consists of the four C functions fftc1, fftcn, fftr1, and
 * fftrn. FFTEASY is free. I am not in any way, shape, or form expecting
 * to make money off of these routines. I wrote them because I needed
 * them for some work I was doing and I'm putting them out on the
 * Internet in case other people might find them useful. Feel free to
 * download them, incorporate them into your code, modify them, translate
 * the comment lines into Swahili, or whatever else you want. What I do
 * want is the following:
 * 1) Leave this notice (i.e. this entire paragraph beginning with
 * ``FFTEASY consists of...'' and ending with my email address) in with
 * the code wherever you put it. Even if you're just using it in-house in
 * your department, business, or wherever else I would like these credits
 * to remain with it. This is partly so that people can...
 * 2) Give me feedback. Did FFTEASY work great for you and help your
 * work?  Did you hate it? Did you find a way to improve it, or translate
 * it into another programming language? Whatever the case might be, I
 * would love to hear about it. Please let me know at the email address
 * below.
 * 3) Finally, insofar as I have the legal right to do so I forbid you
 * to make money off of this code without my consent. In other words if
 * you want to publish these functions in a book or bundle them into
 * commercial software or anything like that contact me about it
 * first. I'll probably say yes, but I would like to reserve that right.
 * 
 * For any comments or questions you can reach me at
 * gfelder@physics.stanford.edu.
 */
public class FFT {
	
	private Complex[] nyquist=null;
		
	public Complex[] getNyquistFrequencies() {
		return this.nyquist;
	}
	
	/**
	 * Standard method for the fft of a one-dimensional fourier transformation
	 * the skip variable is required to handle multi-dimensional data
	 */	
	public Complex[] fftc1(Complex[] data, boolean inverse) {
		
		//check for power 2 data length
		int l = data.length;
		while (l%2==0) {
			l/=2;
		}
		if (l!=1) Utils.errorMsg("The length of data is not a power of two but "+data.length+" instead.");
		int b,index1, index2, trans_size,trans;
		int forward = (inverse?-1:1);
		int N=data.length;
		double pi2 = 2.*Math.PI;
		double pi2n; // Used in recursive formulas for Re(W^b) and Im(W^b) 
		Complex wb= Complex.ONE; //k = W^k=e^(2 pi i b/N) in the Danielson-Lanczos formula for a transform of length N */
		Complex[] fft = data.clone();
		Complex tmp1, tmp2; //Buffers for implementing recursive formulas 
		
		/* Place the elements of the array data in bit-reversed order */
		for(index1=1,index2=0;index1<N;index1++) /* Loop through all elements of c */
		{
			for(b=N/2;index2>=b;b/=2) /* To find the next bit reversed array index subtract leading 1's from index2 */
				index2-=b;
		    index2+=b; /* Next replace the first 0 in index2 with a 1 and this gives the correct next value */
		    if(index2>index1) /* Swap each pair only the first time it is found */
		    {
		      tmp1 = fft[index2].clone();
		      fft[index2] = fft[index1].clone();
		      fft[index1] = tmp1.clone();
		    }
		}

		/* Next perform successive transforms of length 2,4,...,N using the Danielson-Lanczos formula */
		for(trans_size=2;trans_size<=N;trans_size*=2) /* trans_size = size of transform being computed */
		{
			pi2n = forward*pi2/(float)trans_size; /* +- 2 pi/trans_size */
		    wb = Complex.ONE; /* Initialize W^b for b=0 */
		    for(b=0;b<trans_size/2;b++) /* Step over half of the elements in the transform */
		    {
		    	for(trans=0;trans<N/trans_size;trans++) /* Iterate over all transforms of size trans_size to be computed */
		    	{
		    		index1 = (trans*trans_size+b); /* Index of element in first half of transform being computed */
		    		index2 = index1 + trans_size/2; /* Index of element in second half of transform being computed */
		    		tmp1 = fft[index1].clone();
		    		tmp2 =fft[index2].clone();
		    		fft[index1]=tmp1.plus(wb.times(tmp2));/* Implement D-L formula */
		    		fft[index2]=tmp1.minus(wb.times(tmp2));/* Implement D-L formula */
		    	}
		    	tmp1 = wb.clone();
		    	wb = tmp1.times(Complex.I.scale(pi2n).exp());// e^(2 pi i b/trans_size) used in D-L formula */
		    }
		}

		/* For the transform divide by the number of grid points */
		if(inverse)
			for(index1=0;index1<N;index1++) fft[index1]=fft[index1].scale(1./N);
		return fft;
	}	
	
	/**
	 * 
	 * Do a Fourier transform of an ndims dimensional array of complex numbers
	 * Array dimensions are given by size[0],...,size[ndims-1]. Note that these are sizes of complex arrays.
	 * @param f
	 * @param size number of data points in each dimension
	 * @param inverse flag
	 * @return
	 */
	public Complex[] fftcn(Complex f[], int size[], boolean inverse)
	{
	  int i,j,dim;
	  int planesize=1,skip=1; /* These determine where to begin successive transforms and the skip between their elements (see below) */
	  int totalsize=1; /* Total size of the ndims dimensional array */
	  Complex[] transform = f.clone();
	  int ndims = size.length;
	  
	  for(dim=0;dim<ndims;dim++) /* Determine total size of array */
	    totalsize *= size[dim];

	  for(dim=0;dim<ndims;dim++) /* Loop over dimensions */
	  {
		Complex[] tmp = new Complex[size[dim]];
	    planesize *= size[dim]; /* Planesize = Product of all sizes up to and including size[dim] */
	    for(i=0;i<totalsize;i+=planesize) /* Take big steps to begin loops of transforms */
	      for(j=0;j<skip;j++) { /* Skip sets the number of transforms in between big steps as well as the skip between elements */
	    	  //prepare data for one-dimensional Fourier transformation
	    	  for (int k=0;k<size[dim];k++) 
	    		  tmp[k]=transform[i+j+k*skip];
	    	  //calculate transform
	    	  tmp=this.fftc1(tmp, inverse);
	    	  //write the data back into the array
	    	  for (int k=0;k<size[dim];k++)
	    		  transform[i+j+k*skip]=tmp[k];
	      }
	    skip *= size[dim]; /* Skip = Product of all sizes up to (but not including) size[dim] */
	  }
	  
	  return transform;
	}
	
	
	
	/**
	 * Do a Fourier transform of an array of N real numbers
	 * N must be a power of 2
	 * @param f
	 * @return
	 */
	public Complex[] fftr1(double[] f)
	{
	  int b;
	  int N = f.length;
	  
	  double pi2n = 2.*Math.PI/N;
	  Complex exppi2n = Complex.I.scale(pi2n).exp(); /* pi2n = 2 Pi/N */
	  Complex wb; /* wb = W^b = e^(2 pi i b/N) in the Danielson-Lanczos formula for a transform of length N */
	  Complex temp1,temp2; /* Buffers for implementing recursive formulas */
	  
	  Complex[] d = new Complex[N/2];
	  for (int i=0;i<N/2;i++) d[i]=new Complex(f[2*i],f[2*i+1]); /* Treat f as an array of N/2 complex numbers */
	  
	  Complex[] c=fftc1(d,false); /* Do a transform of f as if it were N/2 complex points */
	    
	  wb=Complex.ONE; /* Initialize W^b for b=0 */
	  for(b=1;b<N/4;b++) /* Loop over elements of transform. See documentation for these formulas */
	  {
	    temp1 =wb.clone(); /*e^(2 pi i b/N) used in D-L formula */
	    wb = temp1.times(exppi2n);
	    temp1 = c[b].clone();
	    temp2 = c[N/2-b].clone();
	    c[b] = temp1.plus(temp2.conjugate()).minus(Complex.I.times(wb).times(temp1.minus(temp2.conjugate())));
	    c[b] = c[b].scale(0.5);
	    c[N/2-b]=temp1.conjugate().plus(temp2).minus(Complex.I.times(wb.conjugate()).times(temp1.conjugate().minus(temp2)));
	    c[N/2-b]=c[N/2-b].scale(0.5);
	  }
	  temp1 = c[0].clone();
	  c[0]= new Complex(temp1.re()+temp1.im(), temp1.re()-temp1.im());
	  /* Set b=0 term in transform */
	  /* Put b=N/2 term in imaginary part of first term */
	  
	  return c;
	}
	
	/**
	 * Do an inverse Fourier transform to obtain an array of N real numbers
	 * N must be a power of 2
	 * @return
	 */
	public double[] fftr1Inv(Complex[] c)
	{
	  int b;
	  int N=2*c.length;
	  
	  double pi2n = 2.*Math.PI/N;
	  Complex exppi2n = Complex.I.scale(pi2n).exp(); /* pi2n = 2 Pi/N */
	  Complex wb; /* wb = W^b = e^(2 pi i b/N) in the Danielson-Lanczos formula for a transform of length N */
	  Complex temp1,temp2; /* Buffers for implementing recursive formulas */
	  
	  Complex[] d = c.clone();
	  
	  wb=Complex.ONE; /* Initialize W^b for b=0 */
	  for(b=1;b<N/4;b++) /* Loop over elements of transform. See documentation for these formulas */
	  {
	    temp1 = wb.clone();
	    wb = temp1.times(exppi2n);/*e^(2 pi i b/N) used in D-L formula */
	    temp1 = c[b];
	    temp2 = c[N/2-b];
	    d[b] = temp1.plus(temp2.conjugate()).plus(Complex.I.times(wb.conjugate()).times(temp1.minus(temp2.conjugate())));
	    d[b]=d[b].scale(0.5);
	    d[N/2-b]=temp1.conjugate().plus(temp2).plus(Complex.I.times(wb).times(temp1.conjugate().minus(temp2)));
	    d[N/2-b]=d[N/2-b].scale(0.5);
	  }
	  temp1 = d[0].clone();
	  d[0]=new Complex( temp1.re()+temp1.im(),temp1.re()-temp1.im());
	  d[0]=d[0].scale(0.5);
	 
	  d=fftc1(d,true);
	  
	  //convert into an array of reals
	  double[] returnReals = new double[N];
	  for (int a=0;a<N/2;a++) {
		  returnReals[2*a]=d[a].re();
		  returnReals[2*a+1]=d[a].im();
	  }
	  return returnReals;
	}
	
	
	/**
	 * quick and dirty fourier transform, the real field is simply extended to a complex field with imaginary part equal to zero
	 * @param f
	 * @param size
	 * @return
	 */
	public Complex[] fftrn(double[] f, int[] size) {
		Complex c[] = new Complex[f.length];
		for (int i=0;i<f.length;i++) c[i]=new Complex(f[i],0);
		
		return fftcn(c,size,false);
	}
	
	
	/**
	 * quick and dirty inverse fourier transform to a real field from a complex field, which is assumed to have the required symmetries
	 * @param f
	 * @param size
	 * @return
	 */
	public double[] fftrnInv(Complex[] f, int[] size) {
		
		Complex[] inv = fftcn(f,size,true);
		double[] returnValues = new double[inv.length];
		for (int i=0;i<inv.length;i++) {
			returnValues[i]=inv[i].re();
		}
		return returnValues;
	}
	
	/**
	 * Do a Fourier transform of an ndims dimensional array of real numbers
	 * Array dimensions are given by size[0],...,size[ndims-1]. All sizes must be powers of 2.
	 * The data is returned without the nyquist frequencies
	 * The are stored and can be retrieved with {@code getNyquistFrequencies()}
	 * @param f
	 * @param size
	 * @return
	
	public Complex[] fftrn(double f[], int size[])
	{
	  
	  int stepsize; // Used in calculating indexneg

	  int ndims = size.length; //number of dimensions
	  int N = size[0]; //size of first dimension which is used treated as real data transformation
	  
	  int[] indices = new int[ndims];
	  int index,indexneg=0; // Positions in the 1-d arrays of points labeled by indices (i0,i1,...,i(ndims-1)); 
      						indexneg gives the position in the array of the corresponding negative frequency 
	  
	  int totalsize=1; // Total number of real points in array 
	  size[0] /= 2; // Set size[] to be the sizes of f viewed as a complex array 
		 
	  for(int dim=0;dim<ndims;dim++) { // Determine total size of array 
		    totalsize *= size[dim];
		    indices[dim]=0;
	  }
	  
	  Complex[] d = new Complex[totalsize];
	  for (int i=0;i<totalsize;i++) d[i]=new Complex(f[2*i],f[2*i+1]); // Treat f as an array of totalsize/2 complex numbers 
	 
	   
	  double pi2n = 2.*Math.PI/N;
	  Complex exppi2n = Complex.I.scale(pi2n).exp(); // pi2n = 2 Pi/N 
	  Complex wb; // wb = W^b = e^(2 pi i b/N) in the Danielson-Lanczos formula for a transform of length N 
	  Complex temp1,temp2; // Buffers for implementing recursive formulas 
	 
	  
	  this.nyquist = new Complex[totalsize/size[0]]; //the fourier transform in the direction of the first dimension is treated as the real one
	 System.out.println(d.length+" "+size[0]*size[1]);
	  d=fftcn(d,size,false); // Do a transform of f as if it were totalsize/2 complex points 
	  
	  System.out.println(totalsize);
	  for(int i=0;i<totalsize/size[0];i++) // Copy b=0 data into nyquist so the recursion formulas below for b=0 and nyquist don't overwrite data they later need 
		 this.nyquist[i] = d[i*size[0]];	// Only copy points where the first array index for d is 0 

	  for(index=0;index<totalsize;index+=size[0]) // Loop over all but the first array index 
	  {
	    // Initialize W^b for b=0 
	    wb = Complex.ONE;
	    for(int b=1;b<N/4;b++) // Loop over elements of transform. See documentation for these formulas 
	    {
	    	temp1 =wb.clone(); //e^(2 pi i b/N) used in D-L formula 
	  	    wb = temp1.times(exppi2n);
	  	    temp1 = d[index+b].clone();
	  	    temp2 = d[indexneg+N/2-b].clone();
	  	    d[index+b] = temp1.plus(temp2.conjugate()).minus(Complex.I.times(wb).times(temp1.minus(temp2.conjugate())));
	  	    d[index+b] = d[index+b].scale(0.5);
	  	    d[indexneg+N/2-b]=temp1.conjugate().plus(temp2).minus(Complex.I.times(wb.conjugate()).times(temp1.conjugate().minus(temp2)));
	  	    d[indexneg+N/2-b]=d[indexneg+N/2-b].scale(0.5);
	    }
	    temp1 = d[index];
	    temp2 = this.nyquist[indexneg/size[0]]; // Index is smaller for cnyquist because it doesn't have the last dimension 
	    // Set b=0 term in transform 
	    d[index].real = .5*(temp1.real+temp2.real +(temp1.imag+temp2.imag));
	    d[index].imag = .5*(temp1.imag-temp2.imag - (temp1.real-temp2.real));
	    // Set b=N/2 transform. 
	    this.nyquist[indexneg/size[0]].real = .5*(temp1.real+temp2.real -(temp1.imag+temp2.imag));
	    this.nyquist[indexneg/size[0]].imag = .5*(-temp1.imag+temp2.imag - (temp1.real-temp2.real));

	    // Find indices for positive and single index for negative frequency. 
	     * In each dimension indexneg[j]=0 if index[j]=0, indexneg[j]=size[j]-index[j] otherwise. 
	    stepsize=size[0]; // Amount to increment indexneg by as each individual index is incremented 
	    int j;
	    for(j=ndims-2;indices[Math.max(0,j)]==size[Math.max(0,j)]-1 && j>=0;j--) // If the rightmost indices are maximal reset them to 0. 
	    														//Indexneg goes from 1 to 0 in these dimensions. 
	    {
	      indices[j]=0;
	      indexneg -= stepsize;
	      stepsize *= size[j];
	    }
	    if(indices[Math.max(0,j)]==0) // If index[j] goes from 0 to 1 indexneg[j] goes from 0 to size[j]-1 
	      indexneg += stepsize*(size[Math.max(0,j)]-1);
	    else // Otherwise increasing index[j] decreases indexneg by one unit. 
	      indexneg -= stepsize;
	    if(j>=0) // This avoids writing outside the array bounds on the last pass through the array loop 
	      indices[j]++;
	  } // End of i loop (over total array) 


	  size[0] *= 2; // Give the user back the array size[] in its original condition 
	  return d;
	}
	*/
	
	
	/**
	 * 
	 * Do a Fourier transform of an ndims dimensional array of complex numbers
	 * Array dimensions are given by size[0],...,size[ndims-1]. Note that these are sizes of complex arrays.
	 * @param f
	 * @param size number of data points in each dimension
	 * @param inverse flag
	 * @return
	 */
	public Complex[] fftrn(Complex f[], int size[], boolean inverse)
	{
	  int i,j,dim;
	  int planesize=1,skip=1; /* These determine where to begin successive transforms and the skip between their elements (see below) */
	  int totalsize=1; /* Total size of the ndims dimensional array */
	  Complex[] transform = f.clone();
	  int ndims = size.length;
	  
	  for(dim=0;dim<ndims;dim++) /* Determine total size of array */
	    totalsize *= size[dim];

	  for(dim=0;dim<ndims;dim++) /* Loop over dimensions */
	  {
		Complex[] tmp = new Complex[size[dim]];
	    planesize *= size[dim]; /* Planesize = Product of all sizes up to and including size[dim] */
	    for(i=0;i<totalsize;i+=planesize) /* Take big steps to begin loops of transforms */
	      for(j=0;j<skip;j++) { /* Skip sets the number of transforms in between big steps as well as the skip between elements */
	    	  //prepare data for one-dimensional Fourier transformation
	    	  for (int k=0;k<size[dim];k++) 
	    		  tmp[k]=transform[i+j+k*skip];
	    	  //calculate transform
	    	  tmp=this.fftc1(tmp, inverse);
	    	  //write the data back into the array
	    	  for (int k=0;k<size[dim];k++)
	    		  transform[i+j+k*skip]=tmp[k];
	      }
	    skip *= size[dim]; /* Skip = Product of all sizes up to (but not including) size[dim] */
	  }
	  
	  return transform;
	}
	
	
}

