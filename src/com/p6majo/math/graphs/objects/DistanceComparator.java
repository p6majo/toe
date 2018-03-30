package com.p6majo.math.graphs.objects;

import java.util.Comparator;

public class DistanceComparator implements Comparator<Node> {

	@Override
	public int compare(Node arg0, Node arg1) {
		return arg0.getDistance().compareTo(arg1.getDistance());
	}

}
