package com.p6majo.math.graphs.objects;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("serial")
public class DijkstraLogger extends TreeMap<Integer, DijkstraStep> {
	
	public String toGeogebraResetButtonScript(Graph graph,Path path,String buttonId,String sliderId,String pointcolor,String segmentcolor,String pathcolor){
		String outString = "SetValue["+sliderId+",0]&#10;";
		for (Map.Entry<String, Node> entry:graph.getNodeMap().entrySet()){
			String nodeId = entry.getValue().getGeogebraId();
			outString+="SetColor["+nodeId+","+pointcolor+"]&#10;";
			outString+="SetPointSize["+nodeId+",5]&#10;";
			outString+="ShowLabel["+nodeId+",false]&#10;";
			outString+="SetCaption["+nodeId+",&quot;"+entry.getValue().getId()+"&quot;]&#10;";
		}
		for (Map.Entry<String, Edge> entry:graph.getEdges().entrySet()){
			String segmentId = entry.getValue().getGeogebraId();
			outString+="SetColor["+segmentId+","+segmentcolor+"]&#10;";
		}
		Iterator<Node> it = path.iterator();
		Node start = it.next();
		outString+="SetColor["+start.getGeogebraId()+","+pathcolor+"]&#10;";
		outString+="ShowLabel["+start.getGeogebraId()+",true]&#10;";
		outString+="SetCaption["+start.getGeogebraId()+",&quot;"+start.getId()+" "+Math.round(start.getDistance())+"km&quot;]&#10;";
		while (it.hasNext()){
			Node next = it.next();
			outString+="SetColor["+next.getGeogebraId()+","+pathcolor+"]&#10;";
			outString+="ShowLabel["+next.getGeogebraId()+",true]&#10;";
			outString+="SetCaption["+next.getGeogebraId()+",&quot;"+next.getId()+" "+Math.round(next.getDistance())+"km&quot;]&#10;";
			Edge edge = graph.findEdgeBetweenNodes(start, next);
			outString+="SetColor["+edge.getGeogebraId()+","+pathcolor+"]&#10;";
			start = next;
		}
		return outString;
	}
	
	public String toGeogebraSliderScript(Graph graph,String sliderId){
		String outString="";
		
		for (Map.Entry<Integer,DijkstraStep> entry : super.entrySet()){
			DijkstraStep step = entry.getValue();
			Node focusNode = step.getNode();
			outString+="If["+sliderId+"&gt;"+(entry.getKey()-1)+",{SetColor["+focusNode.getGeogebraId()+",&quot;violett&quot;],SetPointSize["+focusNode.getGeogebraId()+",7]";
			for (Map.Entry<Double,Node> stepEntry:step.getNeighbours().entrySet()){
				Node neighbour = stepEntry.getValue();
				String neighbourId = neighbour.getGeogebraId();
				String lineToPrecessorId=neighbour.getLineToPrecessorId();
				if (lineToPrecessorId!=null) outString+=",SetColor["+lineToPrecessorId+",&quot;cyan&quot;]";
				Edge newLineToPrecessor = graph.findEdgeBetweenNodes(focusNode, neighbour);
				String newLineToPrecessorId=newLineToPrecessor.getGeogebraId();
				neighbour.setLineToPrecessorId(newLineToPrecessorId);
				outString+=",SetColor["+newLineToPrecessorId+",&quot;black&quot;]";
				outString+=",SetCaption["+neighbourId+",&quot;"+Math.round(stepEntry.getKey())+"km&quot;],ShowLabel["+neighbourId+",true],SetColor["+neighbourId+",&quot;magenta&quot;],SetPointSize["+neighbourId+",7]";
			}
			outString+="}]&#10;";
		}
		return outString;
	}
}
