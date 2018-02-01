package com.p6majo.math.ffteasy;

import com.p6majo.math.complex.Complex;

public class TestFFT {

	private static int N = 8;
	private static int D=5;
	
	public static void main(String[] args) {
		int a;
		
		/* compared with maple, fft.fftc1 works as InverseFourierTransform except for the different normalization
		Complex[] f = new Complex[N];
		for (a=0;a<N;a++) {
			f[a]=new Complex(6*Math.cos(5.*Math.PI*a/N),4*Math.sin(3*Math.PI*a/N));
		}
		//check for purely real data
		FFT fft = new FFT();
		Complex[] ff = fft.fftc1(f, 1, false);
		
		for (a=0;a<N;a++) {
			System.out.println(a+" "+ff[a].scale(1/Math.sqrt(8.)).toString());
		}
		*/
		
		
		double[] f = new double[N];
		
		for (a=0;a<N;a++) { //f(i)=e^(-2 pi i D a/N)
			//f[a] = new Complex(a,a);
			//f[a]=new Complex(3.*Math.sin(-6.*Math.PI*2*a/N) , 3.*Math.sin(-6.*Math.PI*(2*a+1)/N));
			//f[a]=Complex.I.scale(-2.*Math.PI*D*a/N).exp();
			f[a]=3+Math.cos(2.*Math.PI*a/N)+2*Math.sin(6*Math.PI*a/N)+Math.sin(-2*Math.PI*a/N);
		}
		
		FFT fft = new FFT();
		Complex[] ff = fft.fftr1(f);
		
		for (a=0;a<N/2;a++) {
			//System.out.println(a+" "+ff[a]);
		}
	
		double[] fff = fft.fftr1Inv(ff);
		for (a=0;a<N;a++) {
			System.out.println(String.format("%d %f %f", a,f[a],fff[a]));
		}
	
			
	}

}
