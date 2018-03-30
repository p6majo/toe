package com.p6majo.math.graphs.objects;

import java.util.TreeMap;

public class DijkstraStep {
	private Node node;
	private TreeMap<Double,Node> updatedNeighbours;
	
	public DijkstraStep(Node node){
		this.node = node;
		this.updatedNeighbours = new TreeMap<Double,Node>();
	}
	
	public void addNeighbour(Double distance,Node neighbour){
		this.updatedNeighbours.put(distance, neighbour);
	}
	
	public Node getNode(){
		return this.node;
	}
	
	public TreeMap<Double,Node> getNeighbours(){
		return this.updatedNeighbours;
	}
}
