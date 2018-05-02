package com.p6majo.imageprocessing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SobelEdgeDetection {

	
	static BufferedImage inputImg,outputImg;
	static int[][] pixelMatrix=new int[3][3];

	public static void main(String[] args) {
	    try {
	    	long start = System.currentTimeMillis();
	        inputImg=ImageIO.read(new File("ahorn.jpg"));
	        outputImg=new BufferedImage(inputImg.getWidth(),inputImg.getHeight(),BufferedImage.TYPE_INT_RGB);

	        for(int i=1;i<inputImg.getWidth()-1;i++){
	            for(int j=1;j<inputImg.getHeight()-1;j++){
	                pixelMatrix[0][0]=new Color(inputImg.getRGB(i-1,j-1)).getBlue();
	                pixelMatrix[0][1]=new Color(inputImg.getRGB(i-1,j)).getBlue();
	                pixelMatrix[0][2]=new Color(inputImg.getRGB(i-1,j+1)).getBlue();
	                pixelMatrix[1][0]=new Color(inputImg.getRGB(i,j-1)).getBlue();
	                pixelMatrix[1][2]=new Color(inputImg.getRGB(i,j+1)).getBlue();
	                pixelMatrix[2][0]=new Color(inputImg.getRGB(i+1,j-1)).getBlue();
	                pixelMatrix[2][1]=new Color(inputImg.getRGB(i+1,j)).getBlue();
	                pixelMatrix[2][2]=new Color(inputImg.getRGB(i+1,j+1)).getBlue();

	                int edge=(int) convolution(pixelMatrix);
	                outputImg.setRGB(i,j,(edge<<16 | edge<<8 | edge));
	                System.out.println(edge+" "+(edge<<16)+" "+(edge<<8));
	            }
	        }

	        File outputfile = new File("ahorn_edge_blue.jpg");
	        ImageIO.write(outputImg,"jpg", outputfile);
	        System.out.println("Edges detected in "+(System.currentTimeMillis()-start)+" ms.");

	    } catch (IOException ex) {System.err.println("Image width:height="+inputImg.getWidth()+":"+inputImg.getHeight());}
	}
	public static double convolution(int[][] pixelMatrix){

	    int gy=(pixelMatrix[0][0]*-1)+(pixelMatrix[0][1]*-2)+(pixelMatrix[0][2]*-1)+(pixelMatrix[2][0])+(pixelMatrix[2][1]*2)+(pixelMatrix[2][2]*1);
	    int gx=(pixelMatrix[0][0])+(pixelMatrix[0][2]*-1)+(pixelMatrix[1][0]*2)+(pixelMatrix[1][2]*-2)+(pixelMatrix[2][0])+(pixelMatrix[2][2]*-1);
	    return Math.sqrt(gy*gy+gx*gx);

	}
	
}
