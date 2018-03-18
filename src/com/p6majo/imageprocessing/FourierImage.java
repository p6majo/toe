package com.p6majo.imageprocessing;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.ffteasy.FFT;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * Extension for a {@code NeuralImage.class} that contains a range of image processing methods
 * The image has very distinct properties, therefore it is not recommended to create it from scratch
 * Rather use the method {@code fft} in  the {@code NeuralImage.class} for construction
 * 
 * The width and height of the Image are powers of two. The picture consists of two parts.
 * The left hand side contains the logarithmically scaled absolute values of the frequency modes.
 * The scaling is performed with the two function {@code byte2abs} and {@code abs2byte}.
 * The right hand side contains the phase information scaled with the functions {@code phase2byte} and {@code byte2phase}
 * 
 * The range of frequencies is encoded in each pixel from -N/2 .. 0 .. N/2-1
 * 
 * The pre-image can be retrieved with the method {@code invfft}
 * 
 * A variety of transformations can be applied to the FourierImage
 * 
 * @author jmartin
 *
 */
public class FourierImage extends NeuralImage {
	
	public void setfftmax(double max) {super.fftmax=max;}
	public void setfftmin(double min) {super.fftmin=min;}
	
	/**
	 * It is not recommended to invoke this constructor explicitly
	 * Use {@code fft} in {@code NeuralImage.class} to great FourierImages
	 * @param width
	 * @param height
	 * @param imageType
	 */
	public FourierImage(int width, int height, int imageType) {
		super(width, height, imageType);
	}
	
	
	public FourierImage(BufferedImage img) {
		super(img.getColorModel(), img.copyData(null), img.isAlphaPremultiplied(), null);
	}
	
	
	/**
	 * The hf-corners of the spectrum are erased 
	 *  
	 * {@param ratio} the size of the corner relative to the image dimension
	 */
	public void reduceHFNoise(double ratio) {
		
		int imgWidth = this.getWidth();
		int imgHeight = this.getHeight();
		
		int cutoffx = (int) (imgWidth/4*ratio);
		int cutoffy = (int) (imgHeight/2*ratio);
		
		
		byte[] data = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();
		for (int h=0;h<cutoffy;h++)
			for (int w=0;w<cutoffx;w++) {
				if (h<cutoffy-1) data[h*imgWidth+w]=0; //erase hf-corner in the second quadrant
				if (w>0) {
					if (h<cutoffy-1) data[h*imgWidth+(imgWidth/2-w)]=0; //erase hf-corner in the first quadrant
					data[(imgHeight-h-1)*imgWidth+(imgWidth/2-w)]=0;//erase hf-corner in the fourth quadrant
				}
				data[(imgHeight-h-1)*imgWidth+w]=0; //erase hf-corner in the third quadrant
			}
		
		int k=0;
		k++;
	}
	
	
	/**
	 * it is assumed that the data is provided as one image consisting of magnitude information on the left side and phase information on the right side
	 * the inverse Fourier transformation is applied to return the pre-image 
	 * @return
	 */
	public NeuralImage invfft() {
		int width = this.getWidth()/2; //combine magnitude and phase information into complex variables
		int height = this.getHeight();
		Complex[] data = new Complex[width*height];
		byte[] fftData = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();
		
		/*
		System.out.println("two: ");
		for (int j=0;j<height;j++) {
			for (int i=0;i<2*width;i++) System.out.print(String.format("%5d", fftData[i+j*width*2]));
			System.out.println( );
		}
		*/
		
		int i,j; //system coordinates -N/2 .. N/2-1 
		int w,h;//data coordinates 0 .. N
		for (j=-height/2;j<height/2;j++)
			for (i=-width/2;i<width/2;i++) {
				//prepare coordinate change
				if (j<0) h=j+height;else h=j;
				if (i<0) w=i+width;else w=i;
				//retrieve information from grey values
				byte val = fftData[i+width/2+(height/2-1-j)*2*width];
				byte ph = fftData[width+i+width/2+(height/2-1-j)*2*width];
				data[w+h*width]=Complex.I.scale(super.byte2phase(ph)).exp().scale(super.byte2abs(val));
			}
		
		NeuralImage returnImage = new NeuralImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
		byte[] returnData = ((DataBufferByte) returnImage.getRaster().getDataBuffer()).getData();
		FFT fft = new FFT();
		
		Complex[] invData = fft.fftcn(data, new int[] {width,height},true); 
		
		//re-scale data
		for (i=0;i<invData.length;i++) 
			returnData[i]=(byte) (Math.signum(invData[i].re())*invData[i].abs());
		
		return returnImage;
	}

}
