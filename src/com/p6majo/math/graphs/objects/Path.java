package com.p6majo.math.graphs.objects;

import java.util.TreeSet;

/**
 * 
 * @author p6majo
 * A path is a sorted set of nodes. The points are sorted with respect to their distance from the starting node
 * that should have distance zero by default
 */
@SuppressWarnings("serial")
public class Path extends TreeSet<Node> {
		
		public Path(){
			super(new DistanceComparator());
		}
		
		public String toString(){
			if (this.size()<2) return "singular_path";
			else{
				String returnString = "Path_from_"+this.first().getId().trim()+"_to_"+this.last().getId().trim();
				returnString = returnString.replaceAll(" ", "_")+"\n";
				
				
				return returnString;
			}
		}
			
}
