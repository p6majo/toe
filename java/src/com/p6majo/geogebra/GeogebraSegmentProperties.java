package com.p6majo.geogebra;

public class GeogebraSegmentProperties {
	private int thickness = 3;
	private int[] rgb = new int[]{0,0,255};
	private boolean showLabel = true;
	private int labelMode = 0;
	private String caption="";
	private String script="";
	private String startId;
	private String endId;
	private String geogebraId;
	
	
	
	
	
	public int getThickness() {
		return thickness;
	}
	public void setThickness(int thickness) {
		this.thickness = thickness;
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
	
	public String getStartId() {
		return startId;
	}
	public void setStartId(String startId) {
		this.startId = startId;
	}
	public String getEndId() {
		return endId;
	}
	public void setEndId(String endId) {
		this.endId = endId;
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
