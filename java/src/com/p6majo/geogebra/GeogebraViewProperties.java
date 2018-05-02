package com.p6majo.geogebra;

/**
 * allows for customization of the view properties,
 * e.g. origin, scaling, grid, colors, etc.
 * @author p6majo
 *
 */
public class GeogebraViewProperties {
	//geogebra default values
	
	private int width = 600;
	private int height = 450;
	private int xZero = 215;
	private int yZero = 315;
	private float scale =50.f;
	private float yscale =50.f;
	private boolean axes = true;
	private boolean grid = false;
	
	
	public void setWidth(int width) {
		this.width = width;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getxZero() {
		return xZero;
	}
	public int getyZero() {
		return yZero;
	}
	public float getScale() {
		return scale;
	}
	public float getYscale() {
		return yscale;
	}
	public boolean isAxes() {
		return axes;
	}
	public boolean isGrid() {
		return grid;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public void setxZero(int xZero) {
		this.xZero = xZero;
	}
	public void setyZero(int yZero) {
		this.yZero = yZero;
	}
	public void setScale(float scale) {
		this.scale = scale;
	}
	public void setYscale(float yscale) {
		this.yscale = yscale;
	}
	public void setAxes(boolean axes) {
		this.axes = axes;
	}
	public void setGrid(boolean grid) {
		this.grid = grid;
	}
	
	
}
