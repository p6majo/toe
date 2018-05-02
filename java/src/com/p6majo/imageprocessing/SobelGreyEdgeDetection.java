package com.p6majo.imageprocessing;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SobelGreyEdgeDetection {

	
	static NeuralImage inputImg,greyImg,outputImg;
	
	public static void main(String[] args) {
	    try {
	    	long start = System.currentTimeMillis();
	        inputImg=new NeuralImage(ImageIO.read(new File("ahorn.jpg")));
	        System.out.println("TYpe: "+inputImg.getType());
	        outputImg=inputImg.convertToEdges();
	        outputImg = outputImg.scale(0.5, 0.5);
	        
	        File outputfile = new File("ahorn_neural_gray.jpg");
	        ImageIO.write(outputImg,"jpg", outputfile);
	        System.out.println("Edges calculated in "+(System.currentTimeMillis()-start)+" ms.");
	        
	    } 
	    catch (IOException ex) {
	    	System.err.println("Image width:height="+inputImg.getWidth()+":"+inputImg.getHeight());
	    }
	    
	}
	
	
}
