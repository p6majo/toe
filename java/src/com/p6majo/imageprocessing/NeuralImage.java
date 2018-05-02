package com.p6majo.imageprocessing;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.ffteasy.FFT;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.Hashtable;

/**
 * Extension for a BufferedImage that contains a range of image processing methods
 * The neural image is simplest invoked by passing a BufferedImage as an argument of the constructor
 * i.e.: {@code new NeuralImage(BufferedImage img)} or {@code new NeuralImage(ImageIO.read(File filename))}
 * @author jmartin
 *
 */
public class NeuralImage extends BufferedImage {
	
	//variables for re-scaling of the Fourier transformed image
	protected double fftmax=0.;
	protected double fftmin=0.;
	
	private int[] centerOfImage = null;
	
	public NeuralImage(int width, int height, int imageType, IndexColorModel cm) {
		super(width, height, imageType, cm);
	}

	public NeuralImage(int width, int height, int imageType) {
		super(width, height, imageType);
	}
	
	public NeuralImage(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied,
			Hashtable<?, ?> properties) {
		super(cm, raster, isRasterPremultiplied, properties);
	}
	
	public NeuralImage(BufferedImage img) {
		super(img.getColorModel(), img.copyData(null), img.isAlphaPremultiplied(), null);
	}
	
	/**
	 * convert bufferedImage into a greyScale picture. The red, green and blue components are averaged
	 */
	public NeuralImage convertToGreyScale() {
		NeuralImage returnImage = new NeuralImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = returnImage.getGraphics();
		g.drawImage(this, 0, 0, null);
		return returnImage;
	}
	
	/**
	 * returns a scaled image with the scale parameters fWidth and fHeight
	 * @param fWidth
	 * @param fHeight
	 * @return
	 */
	public NeuralImage scale(double fWidth, double fHeight) {
	    BufferedImage dbi = null;
	    dbi = new BufferedImage((int) (this.getWidth()*fWidth),(int) (this.getHeight()*fHeight), this.getType());
	    Graphics2D g = dbi.createGraphics();
	    AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
	    g.drawRenderedImage(this, at);
	    return new NeuralImage(dbi);
	}
	
	
	/**
	 * private function, which is used to calculate the edges 
	 * @param pixelMatrix
	 * @return
	 */
	private static double convolution(int[][] pixelMatrix){

	    int gy=(pixelMatrix[0][0]*-1)+(pixelMatrix[0][1]*-2)+(pixelMatrix[0][2]*-1)+(pixelMatrix[2][0])+(pixelMatrix[2][1]*2)+(pixelMatrix[2][2]*1);
	    int gx=(pixelMatrix[0][0])+(pixelMatrix[0][2]*-1)+(pixelMatrix[1][0]*2)+(pixelMatrix[1][2]*-2)+(pixelMatrix[2][0])+(pixelMatrix[2][2]*-1);
	    return Math.sqrt(gy*gy+gx*gx);

	}
	
	/**
	 * convert bufferedImage into an image where only edges are displayed
	 * The edges are detected with a two dimensional gradient calculator
	 */
	public NeuralImage convertToEdges() {
		NeuralImage greyImage = this.convertToGreyScale();
		NeuralImage returnImage = new NeuralImage(this.getWidth(),this.getHeight(),this.getType());
		int[][] pixelMatrix=new int[3][3];

		
		for(int i=1;i<this.getWidth()-1;i++){
			for(int j=1;j<this.getHeight()-1;j++){
				pixelMatrix[0][0]=greyImage.getRGB(i-1,j-1)&0xff;
				pixelMatrix[0][1]=greyImage.getRGB(i-1,j)&0xff;
				pixelMatrix[0][2]=greyImage.getRGB(i-1,j+1)&0xff;
				pixelMatrix[1][0]=greyImage.getRGB(i,j-1)&0xff;
				pixelMatrix[1][2]=greyImage.getRGB(i,j+1)&0xff;
				pixelMatrix[2][0]=greyImage.getRGB(i+1,j-1)&0xff;
				pixelMatrix[2][1]=greyImage.getRGB(i+1,j)&0xff;
				pixelMatrix[2][2]=greyImage.getRGB(i+1,j+1)&0xff;
				
				int edge=(int) convolution(pixelMatrix);
				returnImage.setRGB(i,j,(edge<<16|edge<<8|edge));
			}
		}
		
		return returnImage;
	}
	
	
	/**
	 * calculate parameter to prepare the conversion function that maps the logarithm of the abs-values to byte values
	 * @param ff
	 */
	private void setRescaleParameter(Complex[] ff) {
		double abs;
		for (int i=0;i<ff.length;i++) {
			abs = ff[i].abs();
			if (abs!=0) {
				fftmin = Math.min(Math.log(abs), fftmin);
				fftmax = Math.max(Math.log(abs), fftmax);
			}
		}
	}
	
	protected byte abs2byte(double abs) {
		if (abs!=0) {
			return (byte) (254.*(Math.log(abs)-fftmin)/(fftmax-fftmin)+1.);
		}
		else return 0;
	}
	
	protected double byte2abs(byte grey) {
		if (grey!=0) {
			double val = 0;
			if (grey<0) val =(double) grey+256.;else val = (double) grey;
			return Math.exp((val-1.)/254*(fftmax-fftmin)+fftmin);
		}
		else return 0.;
	}
	
	protected byte phase2byte(double phase) {
		return (byte) (127./Math.PI*phase);
	}
	
	protected double byte2phase(byte ph) {
		return Math.PI/127*ph;
	}
	
	
	/**
	 * returns the center of the image, assuming that the image contains one large object
	 * The object is detected from its edges and the center of mass of all edges is returned 
	 * @return
	 */
	public int[] getCenterOfImage() {
		if (this.centerOfImage!=null) return this.centerOfImage;
		else {
			double[] center = new double[]{0.,0.};
			double mass = 0.;
			NeuralImage edges = null;
			if (this.getType()!=BufferedImage.TYPE_BYTE_GRAY) {
				edges = this.convertToGreyScale();
				edges = edges.convertToEdges();
			}
			else edges = this.convertToEdges();
			int width = edges.getWidth();
			int height = edges.getHeight();
			
			byte[] grayData = ((DataBufferByte) edges.getRaster().getDataBuffer()).getData();
			
			for (int h=0;h<height;h++)
				for (int w=0;w<width;w++) {
					byte b = grayData[h*width+w];
					double gr = (b<0?b+256.:b);
					center[0]+= gr*w;
					center[1]+=gr*h;
					mass+=gr;
				}
			center[0]/=mass;
			center[1]/=mass;
			return new int[] {(int) center[0],(int) center[1]};
		}
	}
	
	/**
	 * the image is converted into a grey image, afterwards the Fourier transform is performed and returned as an image with
	 * double width. The left half of the image shows the absolute values of the Fourier transform and the right half of the image
	 * contains the information of the phase
	 * @return
	 */
	public FourierImage fft() {
		NeuralImage greyImage = this.convertToGreyScale();
		FFT fft = new FFT();
		int width = greyImage.getWidth();
		int height = greyImage.getHeight();
		FourierImage returnImage = new FourierImage(width*2,height,BufferedImage.TYPE_BYTE_GRAY);
		
		
		byte[] greyData = ((DataBufferByte) greyImage.getRaster().getDataBuffer()).getData();
		double[] f = new double[width*height];
		for (int j=0;j<height;j++)
			for (int i=0;i<width;i++)
				f[i+j*width]=(double) greyData[i+j*width];//-(int) average;//subtract the average grey value to obtain values that oscillate around zero
		
		Complex[] ff = fft.fftrn(f, new int[] {width,height}); 
		
	
		//determine re-scaling parameters value to re-scale amplitudes into the byte range
		this.setRescaleParameter(ff);
		
		//prepare output
		//rearrange data from -N/2 .. 0 .. N/2-1
		int w,h;//data coordinates 0 .. N
		int i,j; //system coordinates -N/2 .. N/2-1 
		byte abs,phase;
		byte[] fftData =((DataBufferByte) returnImage.getRaster().getDataBuffer()).getData();
		for (j=-height/2;j<height/2;j++)
			for (i=-width/2;i<width/2;i++) {
				if (j<0) h=j+height;else h=j;
				if (i<0) w=i+width;else w=i;
				Complex value = ff[w+h*width];
				abs =this.abs2byte(value.abs());//re-scale absolute values into byte values
				phase = this.phase2byte(value.phase());//re-scale phase into byte values
				fftData[i+width/2+(height/2-1-j)*2*width] = abs;
				fftData[width+i+width/2+(height/2-1-j)*2*width]= phase; //show positive values above negative values, account for screen vs system
			}
	
		//save transformation parameters for possible inverse Fourier transformation
		returnImage.setfftmax(fftmax);
		returnImage.setfftmin(fftmin);
		return returnImage;
	}
	
}
