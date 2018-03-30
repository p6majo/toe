package com.p6majo.math.graphs.io;


import com.p6majo.math.graphs.objects.Edge;
import com.p6majo.math.graphs.objects.Graph;
import com.p6majo.math.graphs.objects.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.PrintStream;
import java.util.Stack;


public class XMLGraphParser extends DefaultHandler {

	private Stack<String> elementStack = new Stack<String>();
    private Stack<Object> objectStack  = new Stack<Object>();

    private enum XMLGraphTag {graph,node,data,edge};
    
    private Graph graph = null;
     
    public XMLGraphParser(Graph graph) {                                                                    
		  this.graph = graph;
    }
	
	public void startDocument() throws SAXException {
	}

	public void endDocument() throws SAXException { 
		System.out.println("Read from file: "+this.graph.getNumberOfNodes()+" knots connected with "+this.graph.getNumberOfEdges()+" edges!");
	}
	
	public void startElement(String namespaceURI,
            String localName,
            String qName, 
            Attributes atts) throws SAXException {

		 this.elementStack.push(qName); 
		 
		 switch (XMLGraphTag.valueOf(qName)){
			case graph:
				this.graph.setId(atts.getValue(0));
				this.graph.setDirected(new Boolean(atts.getValue(1)));
				this.objectStack.push(this.graph);
				break;
			case node:
				Node node = new Node();
				node.setId(atts.getValue(0));
				this.objectStack.push(node);
				break;
			case edge:
				Edge edge = new Edge(this.graph);
				edge.setSource(atts.getValue(0));
				edge.setTarget(atts.getValue(1));
				this.objectStack.push(edge);
				break;
			case data:
				if (atts.getValue(0).compareTo("lat")==0) this.objectStack.push(new String("lat"));
				else if (atts.getValue(0).compareTo("lon")==0) this.objectStack.push(new String("lon"));
				else if (atts.getValue(0).compareTo("A")==0) this.objectStack.push(new String("A"));
				else if (atts.getValue(0).compareTo("gewicht")==0) this.objectStack.push(new String("gewicht"));
				break;
			default:
				break;	 
		 }
	}
	
	/**
	 * construct objects from the given data
	 */
	public void endElement(String uri, String localName,
		        String qName) throws SAXException {
		Object obj;
		 	 
		 switch (XMLGraphTag.valueOf(qName)){
		 	case node:
				obj=this.objectStack.pop(); 
				((Graph) this.currentObject()).addNode((Node) obj); 
				break;
			case edge:
				obj=this.objectStack.pop(); 
				((Graph) this.currentObject()).addEdge((Edge) obj); 
				break;
			default:
				break;	 
			
		 }
		 
	 }
	
	 /**
	  * The reading of the data takes place in this function
	  */
	public void characters(char ch[], int start, int length)
		        throws SAXException {

		        String value = new String(ch, start, length).trim();
		        if(value.length() == 0) return; // ignore white space

		        XMLGraphTag elementTag = XMLGraphTag.valueOf(this.currentElement());
		        
		        switch (elementTag){
		        case data:
		        	String key =(String) this.objectStack.pop();
		        	if (key.compareTo("lat")==0)	((Node) this.currentObject()).setLatitude(new Double(value));
		        	else if (key.compareTo("lon")==0) ((Node) this.currentObject()).setLongitude(new Double(value));
		        	else if (key.compareTo("A")==0) ((Edge) this.currentObject()).setLabel(new String(value));
		        	else if (key.compareTo("gewicht")==0) ((Edge) this.currentObject()).setWeight(new Float(value));
		        	break;
		        default:
		        	break;
		        }
		        
	 }
 
	  private String currentElement() {
		  return this.elementStack.peek();
	  }
	  
	  private Object currentObject(){
		  return this.objectStack.peek();
	  }

	public static class ErrorHandler implements org.xml.sax.ErrorHandler {

		private PrintStream out;

		public ErrorHandler(PrintStream out) {
			this.out=out;
		}

		private String getParseExceptionInfo(SAXParseException spe) {
			String systemId = spe.getSystemId();

			if (systemId == null) {
				systemId = "null";
			}

			String info = "URI=" + systemId + " Line="
				+ spe.getLineNumber() + ": " + spe.getMessage();

			return info;
		}

		@Override
		public void error(SAXParseException spe) throws SAXException {
			String message = "Error: " + getParseExceptionInfo(spe);
			throw new SAXException(message);
		}

		@Override
		public void fatalError(SAXParseException spe) throws SAXException {
			String message = "Fatal Error: " + getParseExceptionInfo(spe);
			throw new SAXException(message);
		}

		@Override
		public void warning(SAXParseException spe) throws SAXException {
			out.println("Warning: " + getParseExceptionInfo(spe));
		}

	}
}
