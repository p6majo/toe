package com.p6majo.geogebra;

public class GeogebraPointProperties {
	private int size = 5;
	private int[] rgb = new int[]{0,0,255};
	private boolean showLabel = true;
	private int labelMode = 0;
	private double x;
	private double y;
	private String geogebraId;
	private String caption="";
	private String script="";
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int[] getRGB() {
		return rgb;
	}
	public void setRGB(int[] rgb) {
		this.rgb = rgb;
	}
	public boolean isShowLabel() {
		return showLabel;
	}
	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}
	public int getLabelMode() {
		return labelMode;
	}
	public void setLabelMode(int labelMode) {
		this.labelMode = labelMode;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public String getGeogebraId() {
		return geogebraId;
	}
	public void setGeogebraId(String geogebraId) {
		this.geogebraId = geogebraId;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	
	
	
	
	
}
