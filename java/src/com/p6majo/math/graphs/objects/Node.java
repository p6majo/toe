package com.p6majo.math.graphs.objects;

import java.util.TreeMap;

public class Node implements Comparable<Node>{
	private String id;
	private String geogebraId;
	private Double latitude;
	private Double longitude;
	
	//variables needed for the Dijkstra algorithm
	private Double distance = null;
	private Node precessor = null;
	//the following id is only needed for the dijkstra-algorithm illustration
	private String lineToPrecessorId=null;
	
	/**
	 * The map that captures the information about the connection to the neighbours
	 * The string carries the label of the edge  
	 */
	private TreeMap<Edge,Node> neighbours = null;
	
	
	
	
	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double abstrand) {
		this.distance = abstrand;
	}

	public Node getPrecessor() {
		return precessor;
	}

	public void setPrecessor(Node precessor) {
		this.precessor = precessor;
	}

	public void setId(String id){
		this.id = id;
	}
	
	public String getGeogebraId() {
		return geogebraId;
	}

	public void setGeogebraId(String geogebraId) {
		this.geogebraId = geogebraId;
	}
	
	public void setLatitude(Double lat){
		this.latitude = lat;
	}
	
	public void setLongitude(Double lon){
		this.longitude = lon;
	}
	
	public String getLineToPrecessorId() {
		return lineToPrecessorId;
	}

	public void setLineToPrecessorId(String vectorToPrecessorId) {
		this.lineToPrecessorId = vectorToPrecessorId;
	}

	public void addNeighbour(Node neighbour, Edge edge){
		if (neighbours==null) this.neighbours = new TreeMap<Edge,Node>();
		neighbours.put(edge,neighbour);
	}
	
	public int numberOfNeighbours(){
		if (this.neighbours!=null) return this.neighbours.size();
		else return -1;
	}
	
	public TreeMap<Edge,Node> getNeighbours(){
		return this.neighbours;
	}
	
	public String toString(){
		return this.id+"@("+this.longitude.toString()+","+this.latitude.toString()+")";
	}
	
	public Double getLatitude(){
		return this.latitude;
	}
	
	public Double getLongitude(){
		return this.longitude;
	}
	
	public String getId(){
		return this.id;
	}
	
	/**
	 * in a first attempt the nodes are sorted by their names 
	 */
	@Override
	public int compareTo(Node arg0) {
		//TODO it might be useful to sort them depending on their flying distance to the starting point
		return this.id.compareTo(arg0.id);
	}
}
