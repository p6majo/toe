package com.p6majo.geogebra;

public class GeogebraPoint {
	private GeogebraPointProperties properties;
	
	public GeogebraPoint(GeogebraPointProperties props){
		this.properties = props;
	}

	public GeogebraPointProperties getProperties() {
		return properties;
	}

	public void setProperties(GeogebraPointProperties properties) {
		this.properties = properties;
	}
	
}
