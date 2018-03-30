package com.p6majo.math.graphs.objects;


import com.p6majo.math.graphs.io.ErrorHandler;
import com.p6majo.math.graphs.io.XMLGraphParser;
import com.p6majo.math.utils.Params;
import com.p6majo.math.utils.Utils;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;


public class Graph {
	private TreeMap<String,Node> nodeMap;
	private TreeMap<String,Edge> edgeMap;
	private String label;
	private String id;
	//by default the more generic case are directed graphs
	private Boolean directed = true;
	
	
	public void setDirected(boolean directed){
		this.directed = directed;
	}
	
	public boolean isDirected(){
		return this.directed;
	}
	
	public Graph(String label){
		this.nodeMap = new TreeMap<String,Node>();
		this.edgeMap = new TreeMap<String,Edge>();
		this.label = label;
		this.loadGraph();
		this.setupNeighbours();
	}
	
	public void setLabel(String label){
		this.label = label;
		this.id = label;
	}
	
	public void addNode(Node node){
		this.nodeMap.put(node.getId(),node);
	}
	
	public void addEdge(Edge edge){
		if (!this.isDirected())
			if (edge.getSource().compareTo(edge.getTarget())>0) edge.switchSourceAndTarget();
		this.edgeMap.put(edge.toString(), edge);
	}
	
	public void setEdgeMap(TreeMap<String,Edge> edges){
		this.edgeMap = edges;
	}
		
	public void removeEdges(){
		this.edgeMap.clear();
	}
	
	/**
	 * remove all nodes that are only connected to two other nodes
	 */
	public void reduceNodes(){
		int removedNodes = 0;
		int mergedEdges = 0;
		if (!this.isDirected()){
			
			Node redundantNode;
			Edge edge1;
			Edge edge2;
			boolean removedNode = true;
			
			while (removedNode){
				for(Map.Entry<String,Node> entry:this.nodeMap.entrySet()){
					redundantNode = entry.getValue();
					if (redundantNode.getNeighbours().size()==2){
						Map.Entry<Edge,Node> neighbourEntry = redundantNode.getNeighbours().firstEntry();
						Node first =  neighbourEntry.getValue();
						edge1 = neighbourEntry.getKey();
					
						//System.out.println(first.getId()+" "+redundantNode.getId()+" connected by "+edge1.toString());
				
						neighbourEntry = redundantNode.getNeighbours().lastEntry();
						Node second =  neighbourEntry.getValue();
						edge2 = neighbourEntry.getKey();
				
						//System.out.println(redundantNode.getId()+" "+second.getId()+" connected by "+edge1.toString());	

						Edge newEdge = new Edge(this);
						newEdge.setLabel(edge1.getLabel());
						newEdge.setSource(first.getId());
						newEdge.setTarget(second.getId());
						newEdge.setWeight(edge1.getWeight()+edge2.getWeight());
					
						this.nodeMap.remove(redundantNode.getId());
						System.out.println(this.edgeMap.size());
						System.out.println("Remove "+edge1.toString()+" "+this.edgeMap.remove(edge1.toString()).toString()+"");
						System.out.println("Remove "+edge2.toString()+" "+this.edgeMap.remove(edge2.toString()).toString()+"");
						System.out.println(this.edgeMap.size());
						//update neighbours
						first.getNeighbours().remove(edge1);
						first.addNeighbour(second, newEdge);
						
						second.getNeighbours().remove(edge2);
						second.addNeighbour(first, newEdge);
						
						this.edgeMap.put(newEdge.toString(),newEdge);
						System.out.println(this.edgeMap.size());
						
						//System.out.println(redundantNode.toString()+" is removed.");
						//System.out.println("Edge "+edge1.toString()+" with weight "+edge1.getWeight()+" and edge "+edge2.toString()+" with weight "+edge2.getWeight());
						//System.out.println("is replaced with edge "+newEdge.toString()+" with weight "+newEdge.getWeight()+".");
						removedNodes ++;
						mergedEdges ++;
						removedNode = true;
						break;
					}
					//if no node is removed the loop is stopped
					removedNode = false;
				}
			}
			System.out.println(removedNodes+" nodes removed and "+mergedEdges+" edges merged.");
			System.out.println("The new graph is "+this.info());
		}
		else System.out.println("The reduction is not implemented for directed graphs");
	}
	
	/**
	 * This id is set by the xml data 
	 * @param id
	 */
	public void setId(String id){
		this.id = id;
	}
	
	public int getNumberOfNodes(){
		return this.nodeMap.size();
	}
	
	public int getNumberOfEdges(){
		return this.edgeMap.size();
	}
	
	public TreeMap<String,Node> getNodeMap(){
		return this.nodeMap;
	}
	
	
	public TreeMap<String,Edge> getEdges(){
		return this.edgeMap;
	}
	
	public Edge findEdgeBetweenNodes(Node source,Node target){
		if (source.getNeighbours()==null) Utils.errorMsg("The edge cannot be determined. Run Graph.setupNeighbours() first.");
		if (!source.getNeighbours().containsValue(target)) Utils.errorMsg("There is no direct connection between the node "+source.toString()+" and "+target.toString());
		for (Map.Entry<Edge,Node> entry:source.getNeighbours().entrySet()){
			if (entry.getValue()==target) return entry.getKey();
		}
		return null;
	}
	
	public Path dijkstra(Node start, Node end, DijkstraLogger logger){
		ArrayList<Node> nodeList = new ArrayList<Node>();
		Path returnPath=null;
		//check whether both nodes are elements of the graph
		if (!this.nodeMap.containsKey(start.getId()))Utils.errorMsg("The start node is not contained inside the graph");
		if (!(this.nodeMap.containsKey(end.getId()))) Utils.errorMsg("The end node is not contained inside the graph");
		
		//initialize neighbours
		this.setupNeighbours();
		if (start.getNeighbours().size()<1) Utils.errorMsg("The start node is disconnected from the graph");
		if (end.getNeighbours().size()<1) Utils.errorMsg("The end node is disconnected from the graph");
		//initialize distances and precessors
		for (Map.Entry<String,Node> entry:this.nodeMap.entrySet()) {
			entry.getValue().setDistance(Double.POSITIVE_INFINITY);
			entry.getValue().setPrecessor(null);
			nodeList.add(entry.getValue());
		}
		start.setDistance(0.0);
		
		//the actual algorithm
		int counter = 0;
		while (nodeList.size()>0){
			Collections.sort(nodeList,new DistanceComparator());
			
			Node u=nodeList.get(0);
			nodeList.remove(0);
			DijkstraStep dStep = new DijkstraStep(u);
			
			TreeMap<Edge,Node> neighbours = u.getNeighbours();
			for (Map.Entry<Edge,Node> entry:neighbours.entrySet()){
				Double distanceUpdate =  u.getDistance()+entry.getKey().getWeight();
				Node v=entry.getValue();
				if (distanceUpdate.compareTo(v.getDistance())<0) {
					v.setDistance(distanceUpdate);
					v.setPrecessor(u);
					dStep.addNeighbour(distanceUpdate,v);
				}
			}
			if (logger!=null) logger.put(counter,dStep);
			if(u==end) break;
			counter++;
		}
		
		//construct the shortest path
		
		returnPath = new Path();
		Node last = end;
		returnPath.add(last);
		while (last.getPrecessor()!=null){
			last = last.getPrecessor();
			returnPath.add(last);
		}
		
		return returnPath;
	}
	
	public String toString(){
		return this.id;
	}
	
	public String info(){
		String directed = "directed";
		if (!this.isDirected()) directed="un"+directed;
		return directed+" with "+this.getNumberOfNodes()+" nodes "+" and "+this.getNumberOfEdges()+" edges.";
	}
	
	/**
	 * run through all segments and setup relations between neighbours
	 */
	private void setupNeighbours(){
		for (Map.Entry<String,Edge> entry:this.edgeMap.entrySet()){
			Edge next = entry.getValue();
			String sourceId = next.getSource();
			String targetId = next.getTarget();
			this.nodeMap.get(sourceId).addNeighbour(this.nodeMap.get(targetId),next);
			if (!this.isDirected()) this.nodeMap.get(targetId).addNeighbour(this.nodeMap.get(sourceId),next);
		}
	}
	
	
	private String toXMLString(){
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
		sb.append("<graph id=\""+this.id+"\" directed=\""+this.directed.toString()+"\">\n");
		
		for (Map.Entry<String,Node> entry:this.nodeMap.entrySet()){
			Node node = entry.getValue();
			sb.append("\t<node id=\""+node.getId()+"\">\n");
			sb.append("\t\t<data key=\"lat\">"+node.getLatitude().toString()+"</data>\n");
			sb.append("\t\t<data key=\"lon\">"+node.getLongitude().toString()+"</data>\n");
			sb.append("\t</node>\n");
		}
		
		for (Map.Entry<String,Edge> entry:this.edgeMap.entrySet()){
			Edge edge = entry.getValue();
			sb.append("\t<edge source=\""+edge.getSource()+"\" target=\""+edge.getTarget()+"\">\n");
			sb.append("\t\t<data key=\"A\">"+edge.getLabel()+"</data>\n");
			sb.append("\t\t<data key=\"gewicht\">"+edge.getWeight().toString()+"</data>\n");
			sb.append("\t</edge>\n");
		}
		
		sb.append("</graph>\n");
		return sb.toString();
	}
	
	public boolean saveGraph(){
		String directed="directed";
		if (!this.directed) directed="un"+directed;
		String filename = Params.PATH+"graphs/xml/"+this.id+"_"+directed+".xml";

		PrintWriter writer=null;
		try {
			writer = new PrintWriter(filename, "iso-8859-1");
			writer.write(this.toXMLString());
			writer.close();
			System.out.println("Graph with "+this.nodeMap.size()+" nodes and "+this.edgeMap.size()+" segments saved at "+filename+"!");
			return true;
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return false;
		}
	}
	
	private boolean loadGraph(){
		String filename ="data/graphs/xml/"+this.label+".xml"; 
		String parserUrl = Utils.convertToFileURL(filename);
		 
		 try{
			 SAXParserFactory spf = SAXParserFactory.newInstance();
			 spf.setNamespaceAware(true);
			 SAXParser saxParser = spf.newSAXParser();
		
			 XMLReader xmlReader = saxParser.getXMLReader();
			 xmlReader.setContentHandler(new XMLGraphParser(this));
			 xmlReader.setErrorHandler(new ErrorHandler(System.err));
			 xmlReader.parse(parserUrl);
		 }
		 catch(Exception ex){
			 ex.getStackTrace();
			 Utils.errorMsg(ex.getMessage()+"\n");
		 }
		 return true;
	}
}
