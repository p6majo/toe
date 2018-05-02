package com.p6majo.geogebra;

public class GeogebraButton {
	private GeogebraButtonProperties properties;
	
	public GeogebraButton(GeogebraButtonProperties props){
		this.properties = props;
	}

	public GeogebraButtonProperties getProperties() {
		return properties;
	}

	public void setProperties(GeogebraButtonProperties properties) {
		this.properties = properties;
	}
	
}
