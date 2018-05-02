package com.p6majo.geogebra;

public class GeogebraSlider {
	private GeogebraSliderProperties properties;
	
	public GeogebraSlider(GeogebraSliderProperties props){
		this.properties = props;
	}

	public GeogebraSliderProperties getProperties() {
		return properties;
	}

	public void setProperties(GeogebraSliderProperties properties) {
		this.properties = properties;
	}
	
}
