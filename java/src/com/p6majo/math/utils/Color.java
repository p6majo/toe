package com.p6majo.math.utils;

public class Color  {

	public static final Color WHITE = new Color(255,255,255);
	public static final Color BLACK = new Color(0,0,0);

	private int red;
	private int green;
	private int blue;
	
	public Color(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public String toString(double alpha) {
		return Math.round(alpha*red) +" "+Math.round(alpha*green)+" "+Math.round(alpha*blue);
	}
	
	
	public String toString() {
		return red +" "+green+" "+blue;
	}
	
}
