package com.p6majo.math.graphs.objects;

public class Edge implements Comparable<Edge>{
	private String source=null;
	private String target=null;
	private String label;
	private Float weight;
	
	private Graph graph;
	private String geogebraId;
	
	public Edge(Graph graph){
		this.graph = graph;
	}
	
	public void setSource(String source){
		this.source = source;
		if (this.target!=null && !this.graph.isDirected()) checkSourceTargetOrder();
	}
	
	public void setTarget(String target){
		this.target = target;
		if (this.source!=null && !this.graph.isDirected()) checkSourceTargetOrder();
	}
	
	public void checkSourceTargetOrder(){
		if (this.source.compareTo(this.target)>0){
			String tmp = source;
			this.source = target;
			this.target = tmp;
		}
	}
	
	public void setLabel(String label){
		this.label = label;
	}
	
	public void setWeight(Float weight){
		this.weight = weight;
	}

	public Float getWeight(){
		return this.weight;
	}
	
	public String getSource(){
		return this.source;
	}
	
	public String getTarget(){
		return this.target;
	}
	
	public String getLabel(){
		return this.label;
	}
	
	public String getGeogebraId() {
		return geogebraId;
	}

	public void setGeogebraId(String geogebraId) {
		this.geogebraId = geogebraId;
	}	
	
	public String toString(){
		String link ="->";
		if (!this.graph.isDirected()) link ="<"+link;
		return this.label+": "+this.source+link+this.target;
	}
	
	public void switchSourceAndTarget(){
		String tmp = this.source;
		this.source = this.target;
		this.target = tmp;
	}
	
	@Override
	public int compareTo(Edge arg0) {
		int labelDiff = this.label.compareTo(arg0.label);
		if (labelDiff!=0) return labelDiff;
		int sourceDiff = this.source.compareTo(arg0.source);
		if (sourceDiff!=0) return sourceDiff;
		else return this.target.compareTo(arg0.target);
	}
}
