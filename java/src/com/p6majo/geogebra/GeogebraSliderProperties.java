package com.p6majo.geogebra;

public class GeogebraSliderProperties {
	private double value=0;
	private double min = 0;
	private double max = 10;
	private double step = 1;
	private String geogebraId;
	private String caption="";
	private int[] rgb = new int[]{0,0,255};
	private boolean showLabel = true;
	private int labelMode = 1;
	private boolean horizontal = true;
	private boolean absoluteScreenLocation = true;
	private float width = 200f;
	private float x = 150f;
	private float y = 200f;
	private boolean fixed = true;
	private boolean showAlgebra = true;
	private String updateScript = "";
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public double getStep() {
		return step;
	}
	public void setStep(double step) {
		this.step = step;
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
	public boolean isHorizontal() {
		return horizontal;
	}
	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}
	public boolean isAbsoluteScreenLocation() {
		return absoluteScreenLocation;
	}
	public void setAbsoluteScreenLocation(boolean absoluteScreenLocation) {
		this.absoluteScreenLocation = absoluteScreenLocation;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public boolean isFixed() {
		return fixed;
	}
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
	public boolean isShowAlgebra() {
		return showAlgebra;
	}
	public void setShowAlgebra(boolean showAlgebra) {
		this.showAlgebra = showAlgebra;
	}
	public String getUpdateScript() {
		return updateScript;
	}
	public void setUpdateScript(String updateScript) {
		this.updateScript = updateScript;
	}
	
	
}
