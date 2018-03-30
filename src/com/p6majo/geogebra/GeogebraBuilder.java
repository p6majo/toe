package com.p6majo.geogebra;

import com.p6majo.math.graphs.objects.Edge;
import com.p6majo.math.graphs.objects.Graph;
import com.p6majo.math.graphs.objects.Node;
import com.p6majo.math.graphs.objects.Path;
import com.p6majo.math.utils.Utils;

import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Builder class that allows to generate flexible geogebra files
 * @author p6majo
 *
 */
public class GeogebraBuilder {

	public static final int[] BLUE = new int[]{0,0,255};
	public static final int[] GREEN = new int[]{0,255,0};
	public static final int[] RED = new int[]{255,0,0};
	public static final int[] YELLOW = new int[]{255,255,0};
	public static final int[] BLACK = new int[]{0,0,0};
	
	
	private String path="";
	private String filename="noName";
	
	private final String extension=".ggb";
	
	public static enum VIEWMODE{twoD,twoDNoAlgebra,threeD,threeDNoAlgebra};
	
	private StringBuilder header;
	private StringBuilder views;
	private StringBuilder construction;
	private StringBuilder kernel;
	
	private TreeMap<String,GeogebraPoint> points = new TreeMap<String,GeogebraPoint>();
	private TreeMap<String,GeogebraSegment> segments = new TreeMap<String,GeogebraSegment>();
	private TreeMap<String,GeogebraSlider> slider = new TreeMap<String,GeogebraSlider>();
	private TreeMap<String,GeogebraButton> buttons = new TreeMap<String,GeogebraButton>();
	
	private VIEWMODE viewMode = VIEWMODE.twoD;
	
	private int pointCounter = 1;
	private int lineCounter = 1;
	
	public GeogebraBuilder(VIEWMODE viewMode,GeogebraViewProperties viewProps){
		
		//initialize various stringbuilder
		this.header = new StringBuilder();
		
		this.views = new StringBuilder();
		
		this.kernel = new StringBuilder();
		this.kernel.append("<kernel>\n");
		this.kernel.append("\t<continuous val=\"false\"/>\n");
		this.kernel.append("\t<usePathAndRegionParameters val=\"true\"/>\n");
		this.kernel.append("\t<decimals val=\"2\"/>\n");
		this.kernel.append("\t<angleUnit val=\"degree\"/>\n");
		this.kernel.append("\t<algebraStyle val=\"0\" spreadsheet=\"0\"/>\n");
		this.kernel.append("\t<coordStyle val=\"0\"/>\n");
		this.kernel.append("\t<angleFromInvTrig val=\"false\"/>\n");
		this.kernel.append("</kernel>\n");
		
		this.construction = new StringBuilder();
		construction.append("<construction title=\"java-generated\" author=\"p6majo\" date=\""+Utils.getGMTTimeString()+"\">\n");
		
		this.buildHeader();
		
		this.buildViews(viewProps);
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public void addPoint(double x, double y, String label){
		this.addPoint(x, y, label,"",3,BLUE,true,0,"");
	}
	public void addPoint(double x, double y,String label, String caption,int size, int[] rgb,boolean showLabel, int labelMode, String script ){
		GeogebraPointProperties props = new GeogebraPointProperties();
		props.setX(x);
		props.setY(y);;
		props.setGeogebraId(label);
		props.setCaption(caption);
		props.setRGB(rgb);
		props.setLabelMode(labelMode);
		props.setShowLabel(showLabel);	
		props.setScript(script);
		this.points.put(label,new GeogebraPoint(props));
	}
	
	public void addSegment(String pointId1,String pointId2, String geogebraId){
		this.addSegment(pointId1, pointId2, geogebraId,"",3,RED,true,0,"");
	}
	
	/**
	 * Add a segement to the geogebra construction
	 * @param pointId1
	 * @param pointId2
	 * @param geogebraId
	 * @param caption
	 * @param thickness
	 * @param rgb
	 * @param showLabel
	 * @param labelMode
	 */
	public void addSegment(String pointId1, String pointId2, String geogebraId, String caption,int thickness, int[] rgb, boolean showLabel,int labelMode,String script){
		
		GeogebraSegmentProperties props = new GeogebraSegmentProperties();
		props.setEndId(pointId2);
		props.setStartId(pointId1);
		props.setGeogebraId(geogebraId);
		props.setCaption(caption);
		props.setThickness(thickness);
		props.setRGB(rgb);
		props.setShowLabel(showLabel);
		props.setLabelMode(labelMode);
		props.setScript(script);
		
		this.segments.put(geogebraId, new GeogebraSegment(props));
	}
	
	
	/**
	 * create slider
	 * @param x
	 * @param y
	 * @param width
	 * @param value
	 * @param min
	 * @param max
	 * @param step
	 * @param label
	 * @param caption
	 * @param rgb
	 * @param horizontal
	 * @param absoluteScreenLocation
	 * @param fixed
	 * @param showAlgebra
	 * @param showLabel
	 * @param labelMode
	 * @param updateScript
	 */
	public void addSlider(float x, float y, float width, double value, double min, double max,double step,  String label, String caption, int[] rgb,boolean horizontal, boolean absoluteScreenLocation, boolean fixed, boolean showAlgebra, boolean showLabel, int labelMode, String updateScript ){
		GeogebraSliderProperties props = new GeogebraSliderProperties();
		props.setX(x);
		props.setY(y);
		props.setWidth(width);
		props.setValue(value);
		props.setMin(min);
		props.setMax(max);
		props.setStep(step);
		props.setGeogebraId(label);
		props.setCaption(caption);
		props.setRGB(rgb);
		props.setHorizontal(horizontal);
		props.setAbsoluteScreenLocation(absoluteScreenLocation);
		props.setFixed(fixed);
		props.setShowAlgebra(showAlgebra);
		props.setShowLabel(showLabel);
		props.setLabelMode(labelMode);
		props.setUpdateScript(updateScript);
		this.slider.put(label, new GeogebraSlider(props));
	}
	
	
	public void addButton(float x, float y, String geogebraId, String caption, int[] rgb, String script ){
		GeogebraButtonProperties props = new GeogebraButtonProperties();
		props.setX(x);
		props.setY(y);
		props.setGeogebraId(geogebraId);
		props.setCaption(caption);
		props.setRGB(rgb);
		props.setScript(script);
		this.buttons.put(geogebraId, new GeogebraButton(props));
	}
	
	/**
	 * add graph to geogebra construction
	 * @param graph
	 * @param pointSize
	 * @param thickness
	 * @param pointColor
	 * @param segmentColor
	 * @param showPointLabel
	 * @param pointLabelMode (0 just id, 1 value, 2 id and value, 3 caption)
	 * @param showSegmentLabel
	 * @param segmentLabelMode (0 just id, 1 value, 2 id and value, 3 caption)
	 */
	public void addGraph(Graph graph, int pointSize, int thickness, int[] pointColor, int[] segmentColor, boolean showPointLabel, int pointLabelMode, boolean showSegmentLabel, int segmentLabelMode){
	
		for (Entry<String,Node> entry: graph.getNodeMap().entrySet()){
			Node node = entry.getValue();
			String geogebraId = "P_{"+this.pointCounter+"}";
			node.setGeogebraId(geogebraId);	
			String script = "ShowLabel["+node.getGeogebraId()+",true]&#10;";
			this.addPoint(node.getLongitude()*100, node.getLatitude()*100, geogebraId, node.getId(), pointSize,pointColor,showPointLabel,pointLabelMode,script);
			pointCounter++;
		}	
		
		for (Entry<String,Edge> entry:graph.getEdges().entrySet()){
			Edge edge = entry.getValue();
			Node source = graph.getNodeMap().get(edge.getSource());
			Node target = graph.getNodeMap().get(edge.getTarget());
			String geogebraId = "l_{"+this.lineCounter+"}";
			edge.setGeogebraId(geogebraId);
			String script = "ShowLabel["+edge.getGeogebraId()+",true]&#10;";
			this.addSegment(source.getGeogebraId(), target.getGeogebraId(), geogebraId, edge.getLabel(), thickness, segmentColor,showSegmentLabel,segmentLabelMode,script);
			this.lineCounter++;
		}
	}
	
	public void highlightPath(Path path, Graph graph){
		this.highlightPath(path,graph,5,3,BLUE,RED,true,3,true,3);
	}
	
	public void highlightPath(Path path,Graph graph,int size,int thickness, int[] pointcolor, int[] segmentcolor,boolean showPointLabel, int pointLabelMode, boolean showSegmentLabel, int segmentLabelMode){
		//output of nodes of path
		Iterator<Node> it = path.iterator();
	
		//update properties of the points of the path
		while(it.hasNext()){
			Node node = it.next();
			String geogebraId = node.getGeogebraId();
			GeogebraPointProperties props = this.points.get(geogebraId).getProperties();
			props.setCaption(props.getCaption()+" "+node.getDistance()+"km");
			props.setSize(size);
			props.setRGB(pointcolor);
			props.setShowLabel(showPointLabel);
			props.setLabelMode(pointLabelMode);
		}
		
		//output of edges of path
		Iterator<Node> it2 = path.iterator();
		Node source = it2.next();
		while(it2.hasNext()){
			Node target = it2.next();
			Edge edge = graph.findEdgeBetweenNodes(source, target);
			String geogebraId = edge.getGeogebraId();
			GeogebraSegmentProperties props = this.segments.get(geogebraId).getProperties();
			props.setThickness(thickness);
			props.setRGB(segmentcolor);
			props.setShowLabel(showSegmentLabel);
			props.setLabelMode(segmentLabelMode);
			source = target;
		}
	}

	private void generateConstruction(){
		for (Entry<String,GeogebraPoint> entry:points.entrySet()){
			GeogebraPointProperties props = entry.getValue().getProperties();
			
			this.construction.append("<element type=\"point\" label=\""+props.getGeogebraId()+"\">\n");
			this.construction.append("\t<show object=\"true\" label=\""+props.isShowLabel()+"\"/>\n");
			this.construction.append("\t<objColor r=\""+props.getRGB()[0]+"\" g=\""+props.getRGB()[1]+"\" b=\""+props.getRGB()[2]+"\" alpha=\"0.0\"/>\n");
			this.construction.append("\t<layer val=\"0\"/>\n");
			this.construction.append("\t<labelMode val=\""+props.getLabelMode()+"\"/>\n");
			this.construction.append("\t<animation step=\"1\" speed=\"1\" type=\"1\" playing=\"false\"/>\n");
			this.construction.append("\t<coords x=\""+props.getX()+"\" y=\""+props.getY()+"\" z=\"1.0\"/>\n");
			this.construction.append("\t<pointSize val=\""+props.getSize()+"\"/>\n");
			this.construction.append("\t<pointStyle val=\"0\"/>\n");
			this.construction.append("\t<caption val=\""+props.getCaption()+"\"/>\n");
			this.construction.append("\t<ggbscript val=\""+props.getScript()+"\"/>\n");
			this.construction.append("</element>\n");
		}
		
		for (Entry<String,GeogebraSegment> entry:segments.entrySet()){
			GeogebraSegmentProperties props = entry.getValue().getProperties();
		
			this.construction.append("<command name=\"Segment\">\n");
			this.construction.append("<input a0=\""+props.getStartId()+"\" a1=\""+props.getEndId()+"\"/>\n");
			this.construction.append("<output a0=\""+props.getGeogebraId()+"\"/>\n");
			this.construction.append("</command>\n");
			
			this.construction.append("<element type=\"segment\" label=\""+props.getGeogebraId()+"\">\n");
			this.construction.append("\t<show object=\"true\" label=\""+props.isShowLabel()+"\"/>\n");
			this.construction.append("\t<labelMode val=\""+props.getLabelMode()+"\"/>\n");
			this.construction.append("\t<caption val=\""+props.getCaption()+"\"/>\n");
			this.construction.append("\t<objColor r=\""+props.getRGB()[0]+"\" g=\""+props.getRGB()[1]+"\" b=\""+props.getRGB()[2]+"\" alpha=\"0.0\"/>\n");
			this.construction.append("\t<layer val=\"0\"/>\n");
			this.construction.append("\t<lineStyle thickness=\""+props.getThickness()+"\" type=\"0\" typeHidden=\"1\"/>\n");
			this.construction.append("\t<this.construction.appendlyingIntersections val=\"false\"/>\n");
			this.construction.append("\t<keepTypeOnTransform val=\"true\"/>\n");
			this.construction.append("\t<ggbscript val=\""+props.getScript()+"\"/>\n");
			this.construction.append("</element>\n");
		}
		
		for (Entry<String,GeogebraSlider> entry:this.slider.entrySet()){
			GeogebraSliderProperties props = entry.getValue().getProperties();
			
			this.construction.append("<element type=\"numeric\" label=\""+props.getGeogebraId()+"\">\n");
			this.construction.append("\t<show object=\"true\" label=\""+props.isShowLabel()+"\"/>\n");
			this.construction.append("\t<labelMode val=\""+props.getLabelMode()+"\"/>\n");
			this.construction.append("\t<value val=\""+props.getValue()+"\"/>\n");
			this.construction.append("\t<objColor r=\""+props.getRGB()[0]+"\" g=\""+props.getRGB()[1]+"\" b=\""+props.getRGB()[2]+"\" alpha=\"0.0\"/>\n");
			this.construction.append("\t<layer val=\"0\"/>\n");
			this.construction.append("\t<slider min=\""+props.getMin()+"\" max=\""+props.getMax()+"\" absoluteScreenLocation=\""+props.isAbsoluteScreenLocation()+"\" width=\""+props.getWidth()+"\" x=\""+props.getX()+"\" y=\""+props.getY()+"\" fixed=\""+props.isFixed()+"\" horizontal=\""+props.isHorizontal()+"\" showAlgebra=\""+props.isShowAlgebra()+"\"/>\n");
			this.construction.append("\t<lineStyle thickness=\""+10+"\" type=\"0\" typeHidden=\"1\"/>\n");
			this.construction.append("\t<animation step=\"1\" speed=\"1\" type=\"0\" playing=\"false\"/>\n");
			this.construction.append("\t<ggbscript onUpdate=\""+props.getUpdateScript()+"\"/>\n");
			this.construction.append("\t<caption val=\""+props.getCaption()+"\"/>\n");
			this.construction.append("</element>\n");
		}
		
		for (Entry<String,GeogebraButton> entry:buttons.entrySet()){
			GeogebraButtonProperties props = entry.getValue().getProperties();
			
			this.construction.append("<element type=\"button\" label=\""+props.getGeogebraId()+"\">\n");
			this.construction.append("\t<show object=\"true\" label=\"true\"/>\n");
			this.construction.append("\t<objColor r=\""+props.getRGB()[0]+"\" g=\""+props.getRGB()[1]+"\" b=\""+props.getRGB()[2]+"\" alpha=\"0.0\"/>\n");
			this.construction.append("\t<layer val=\"0\"/>\n");
			this.construction.append("\t<labelMode val=\"3\"/>\n");
			this.construction.append("\t<animation step=\"1\" speed=\"1\" type=\"1\" playing=\"false\"/>\n");
			this.construction.append("\t<labelOffset x=\""+props.getX()+"\" y=\""+props.getY()+"\" z=\"1.0\"/>\n");
			this.construction.append("\t<caption val=\""+props.getCaption()+"\"/>\n");
			this.construction.append("\t<ggbscript val=\""+props.getScript()+"\"/>\n");
			this.construction.append("\t<font serif=\"false\" sizeM=\"1.4\" size=\"8\" style=\"1\"/>\n");
			this.construction.append("</element>\n");
		}
	}
	
	/**
	 * save builder to geogebra file
	 */
	public void export(boolean startFlag){
		String out="";
		out+=this.header.toString();
		out+=this.views.toString();
		out+=this.kernel.toString();
		this.generateConstruction();
		out+=this.construction.toString();
		out+="</construction>\n";
		out+="</geogebra>\n";
	
		System.out.println(this.construction.toString());
		String filename = this.path+"/geogebra.xml";

		PrintWriter writer=null;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			writer.write(out);
			writer.close();
			
			Runtime rt = Runtime.getRuntime();
			File workingDir = new File(this.path);
			
			Process proc = rt.exec("zip "+this.filename+this.extension+" geogebra.xml",null,workingDir);
			proc.waitFor();
			System.out.println("Geogebra-file saved to "+this.filename+this.extension);
			
			if (startFlag){
				Process startProc = rt.exec("/home/p6majo/geogebra/GeoGebra "+this.path+"/"+this.filename+this.extension);
				startProc.waitFor();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}


	private void buildHeader(){
		boolean viewAlgebra = true;
		boolean view2d = true;
		boolean view3d = false;
		
		switch (this.viewMode){
		case twoD:
			break;
		case twoDNoAlgebra:
			viewAlgebra = false;
			break;
		case threeDNoAlgebra:
			viewAlgebra = false;
			//no break needed the lines of the following case are also needed
		case threeD:
			view2d = false;
			view3d = true;
		break;	
		default:
			break;
		}
		
		this.header.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		this.header.append("<geogebra format=\"5.0\" version=\"5.0.253.0\" id=\"afa1336c-01e9-4828-8df4-7b35c171a482\"  xsi:noNamespaceSchemaLocation=\"http://www.geogebra.org/ggb.xsd\" xmlns=\"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" >\n");
		this.header.append("<gui>\n");
		this.header.append("<window width=\"1920\" height=\"1080\" />\n");
		this.header.append("<perspectives>\n");
		this.header.append("\t<perspective id=\"tmp\">\n");
		this.header.append("\t<panes>\n");
		this.header.append("\t\t<pane location=\"\" divider=\"0.14\" orientation=\"1\" />\n");
		this.header.append("\t\t<pane location=\"0\" divider=\"0.14\" orientation=\"1\" />\n");
		this.header.append("\t</panes>\n");
		this.header.append("\t<views>\n");
		this.header.append("\t\t<view id=\"4097\" visible=\"false\" inframe=\"true\" stylebar=\"true\" location=\"1,1,1,1\" size=\"400\" window=\"610,249,700,550\" />\n");
		this.header.append("\t\t<view id=\"4\" toolbar=\"0 || 2020 , 2021 , 2022 , 66 || 2001 , 2003 , 2002 , 2004 , 2005 || 2040 , 2041 , 2042 , 2044 , 2043\" visible=\"false\" inframe=\"false\" stylebar=\"false\" location=\"1,1\" size=\"300\" window=\"100,100,600,400\" />\n");
		this.header.append("\t\t<view id=\"8\" toolbar=\"1001 | 1002 | 1003  || 1005 | 1004 || 1006 | 1007 | 1010 || 1008 1009 || 66 68 || 6\" visible=\"false\" inframe=\"false\" stylebar=\"false\" location=\"1,3\" size=\"300\" window=\"100,100,600,400\" />\n");
		this.header.append("\t\t<view id=\"1\" visible=\""+viewAlgebra+"\" inframe=\"false\" stylebar=\"false\" location=\"1\" size=\"1590\" window=\"100,100,600,400\" />\n");//graphic window
		this.header.append("\t\t<view id=\"2\" visible=\""+view2d+"\" inframe=\"false\" stylebar=\"true\" location=\"3,3\" size=\"300\" window=\"100,100,250,400\" />\n");//algebra window
		this.header.append("\t\t<view id=\"16\" visible=\"false\" inframe=\"false\" stylebar=\"false\" location=\"1\" size=\"150\" window=\"50,50,500,500\" />\n");
		this.header.append("\t\t<view id=\"32\" visible=\"false\" inframe=\"false\" stylebar=\"true\" location=\"1\" size=\"150\" window=\"50,50,500,500\" />\n");
		this.header.append("\t\t<view id=\"64\" toolbar=\"0\" visible=\"false\" inframe=\"true\" stylebar=\"true\" location=\"1\" size=\"150\" window=\"50,50,500,500\" />\n");
		this.header.append("\t\t<view id=\"70\" toolbar=\"0 || 2020 || 2021 || 2022\" visible=\"false\" inframe=\"true\" stylebar=\"true\" location=\"1\" size=\"150\" window=\"50,50,500,500\" />\n");
		this.header.append("\t\t<view id=\"512\" toolbar=\"0 | 1 501 5 19 , 67 | 2 15 45 18 , 7 37 | 514 3 9 , 13 44 , 47 | 16 | 551 550 11 ,  20 22 21 23 , 55 56 57 , 12 | 69 | 510 511 , 512 513 | 533 531 , 534 532 , 522 523 , 537 536 , 535 | 521 520 | 36 , 38 49 560 | 571 30 29 570 31 33 | 17 | 540 40 41 42 , 27 28 35 , 6 , 502\" visible=\""+view3d+"\" inframe=\"false\" stylebar=\"false\" location=\"1\" size=\"1672\" window=\"100,100,600,400\" />\n");
		this.header.append("\t</views>\n");
		this.header.append("\t<toolbar show=\"true\" items=\"0 39 | 1 501 67 , 5 19 , 72 75 76 | 2 15 45 , 18 65 , 7 37 | 4 3 8 9 , 13 44 , 58 , 47 | 16 51 64 , 70 | 10 34 53 11 , 24  20 22 , 21 23 | 55 56 57 , 12 | 36 46 , 38 49  50 , 71 | 30 29 54 32 31 33 | 17 26 62 73 , 14 68 | 25 52 60 61 | 40 41 42 , 27 28 35 , 6\" position=\"1\" help=\"false\" />\n");
		this.header.append("\t<input show=\"true\" cmd=\"true\" top=\"algebra\"/>\n");
		this.header.append("\t<dockBar show=\"true\" east=\"true\"/>\n");
		this.header.append("\t</perspective> \n");
		this.header.append("</perspectives>\n");
		this.header.append("<labelingStyle  val=\"0\"/>\n");
		this.header.append("<font  size=\"18\"/>\n");
		this.header.append("</gui>\n");
	}
	
	private void buildViews(GeogebraViewProperties props){
		int viewCounter=1;
		switch (this.viewMode){
		case twoD:
			this.views.append("<euclidianView>\n");
	        this.views.append("\t<viewNumber viewNo=\""+viewCounter+"\"/>\n");
	        this.views.append("\t<size  width=\""+props.getWidth()+"\" height=\""+props.getHeight()+"\"/>\n");
	        this.views.append("\t<coordSystem xZero=\""+props.getxZero()+"\" yZero=\""+props.getyZero()+"\" scale=\""+props.getScale()+"\" yscale=\""+props.getYscale()+"\"/>\n");
	        this.views.append("\t<evSettings axes=\""+props.isAxes()+"\" grid=\""+props.isGrid()+"\" gridIsBold=\"false\" pointCapturing=\"3\" rightAngleStyle=\"2\" checkboxSize=\"26\" gridType=\"0\"/>\n");
	        this.views.append("\t<bgColor r=\"255\" g=\"255\" b=\"255\"/>\n");
	        this.views.append("\t<axesColor r=\"0\" g=\"0\" b=\"0\"/>\n");
	        this.views.append("\t<gridColor r=\"192\" g=\"192\" b=\"192\"/>\n");
	        this.views.append("\t<lineStyle axes=\"1\" grid=\"0\"/>\n");
	        this.views.append("\t<axis id=\"0\" show=\"true\" label=\"\" unitLabel=\"\" tickStyle=\"1\" showNumbers=\"true\"/>\n");
	        this.views.append("\t<axis id=\"1\" show=\"true\" label=\"\" unitLabel=\"\" tickStyle=\"1\" showNumbers=\"true\"/>\n");
	        this.views.append("</euclidianView>\n");
	        break;
		default:
			break;	
		}
	}

}
