package com.p6majo.math.graphs;

import com.p6majo.math.graphs.io.GraphGui;
import com.p6majo.math.graphs.objects.Graph;

import javax.swing.*;

public class GraphExample {
	
	public static void main(String[] args) {
		
		Graph autobahn =  new Graph("Autobahnkreuze_undirected");
		
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	GraphGui gui = new GraphGui(autobahn);
            	gui.populateGui();
            }
        });	
		
	}
}
