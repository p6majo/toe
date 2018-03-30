package com.p6majo.geogebra;

public class GeogebraSegment {
	private GeogebraSegmentProperties properties;
	
	public GeogebraSegment(GeogebraSegmentProperties props){
		this.properties = props;
	}

	public GeogebraSegmentProperties getProperties() {
		return properties;
	}

	public void setProperties(GeogebraSegmentProperties properties) {
		this.properties = properties;
	}
	
}
