package com.p6majo.math.graphs.io;


import com.p6majo.geogebra.GeogebraBuilder;
import com.p6majo.geogebra.GeogebraViewProperties;
import com.p6majo.math.graphs.objects.DijkstraLogger;
import com.p6majo.math.graphs.objects.Graph;
import com.p6majo.math.graphs.objects.Node;
import com.p6majo.math.graphs.objects.Path;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class GraphGui implements ActionListener, ListSelectionListener {

	/*
	 * Views
	 */
	private static JFrame frame=null;
	private static JTextPane outputPane = null;
	private static JScrollPane scrollPaneLeft = null;
	private static JScrollPane scrollPaneRight = null;
	
	private static JScrollPane scrollPaneBottom = null;
	
	private static JPanel gridPanel = null;
	private static JPanel interactionPanel = null;
	
	private static JButton calcButton= null;
	private static JButton clearButton= null;
	private static JButton saveButton= null;
	
	public static JProgressBar progressBar = null;

	public static JTextArea processors = null;
	public static JTextArea progressInfo = null;
	
	private static DefaultListModel<String> listModel ;
	private JList<String> startList;
	private JList<String> endList;
	
	private Graph graph;
	private Path path;
	private DijkstraLogger logger;
	private String startId = null;
	private String stopId = null;
	
	public GraphGui(Graph graph){
		frame = new JFrame("Groups and Co");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(2000, 1000);
		this.graph = graph;
	}
	
	public void populateGui(){
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(3,1,10,0	));
		frame.add(gridPanel);
	
		//the frame is split into two parts
		//the left-hand side shows the interaction possibilities and the log
		//he right-hand side outputs the results
		
		//interactionPanel for the left-hand side
		
		interactionPanel = new JPanel();
		interactionPanel.setLayout(new GridBagLayout());
	
		gridPanel.add(interactionPanel);
		
		int row=0;
		GridBagConstraints c = new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
	
		//titles
		JLabel title = new JLabel();
		title.setText("Wähle Startpunkt\n");
		title.setFont(title.getFont().deriveFont((float) 22));
		c.gridx=0;
		c.gridy=row;
		c.gridwidth=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		interactionPanel.add(title,c);
	
		JLabel title2 = new JLabel();
		title2.setText("Wähle Zielpunkt\n");
		title2.setFont(title2.getFont().deriveFont((float) 22));
		c.gridx=1;
		c.gridy=row;
		c.gridwidth=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		interactionPanel.add(title2,c);
		
		//lists
		row++;
		listModel = new DefaultListModel<String>();
		for (Map.Entry<String,Node> entry:graph.getNodeMap().entrySet())
			listModel.addElement(entry.getKey());
		
		startList = new JList<String>(listModel);
		startList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		startList.setSelectedIndex(0);
		startList.addListSelectionListener(this);
		startList.setVisibleRowCount(5);
		startList.setFont(startList.getFont().deriveFont((float) 16));
		
		scrollPaneLeft = new JScrollPane(startList);
		
		c.gridx=0;
		c.gridy=row;
		c.ipady=5;
		c.gridwidth=1;
		c.fill=GridBagConstraints.BOTH;
		interactionPanel.add(scrollPaneLeft,c);
	
		endList = new JList<String>(listModel);
		endList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	
		endList.setSelectedIndex(0);
		endList.addListSelectionListener(this);
		endList.setVisibleRowCount(5);
		endList.setFont(endList.getFont().deriveFont((float) 16));
		c.gridx=1;
		c.gridy=row;
		c.ipady=5;
		c.gridwidth=1;
		c.fill=GridBagConstraints.BOTH;
		scrollPaneRight = new JScrollPane(endList);		
		interactionPanel.add(scrollPaneRight,c);	
		
		//ButtonRow
		row++;
		calcButton = new JButton();
		calcButton.setText("Start");
		calcButton.addActionListener(this);
		c.gridx=0;
		c.gridwidth = 1;
		c.fill=GridBagConstraints.NONE;
		c.gridy=row;
		
		interactionPanel.add(calcButton, c);

		clearButton = new JButton();
		clearButton.setText("Clear");
		clearButton.addActionListener(this);
		c.gridx=2;
		c.gridy=row;
		interactionPanel.add(clearButton, c);

		saveButton = new JButton();
		saveButton.setText("Geogebra");
		saveButton.addActionListener(this);
		c.gridx=1;
		c.gridy=row;
		interactionPanel.add(saveButton, c);
			
		outputPane = new JTextPane();
		outputPane.setEditable(true);
		outputPane.setContentType("text/html");
		
		scrollPaneBottom = new JScrollPane(outputPane);
		gridPanel.add(scrollPaneBottom);
		frame.pack();
		frame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		
		//check for calcButton
		if (obj == calcButton) {
			if (startId!=null && stopId!=null){
				logger = new DijkstraLogger();
				path = graph.dijkstra(graph.getNodeMap().get(startId), graph.getNodeMap().get(stopId),logger);
				Iterator<Node> it = path.iterator();
				int counter = 0;
				String outString = "";
				while (it.hasNext()) {
					counter++;
					Node next = it.next();
					outString+="<b> "+counter+":</b> "+next.getId()+" distance: "+Math.round(next.getDistance())+"km<br>\n";
				}
				this.out(outString);
			}
		}
		
		//check for clearButton
		if (obj == clearButton) this.clear();
		
		if (obj == saveButton) this.save();
		
	}	

	public void clear(){
		outputPane.setText("");	
	}
	
	
	/**
	 * Display results on the output screen
	 * @param output
	 */
	public void out(String output){
		new Thread() {
            @Override
            public void run() {
            	/*
            	outputPane.setText("TestText");
            	String oldText = outputPane.getText();
            	String start =  "body";
            	
            	StringTokenizer token = new StringTokenizer(oldText,start);
            	int count=0;
            	while (token.hasMoreTokens()){
            		System.out.print(count+": "+token.nextToken()+"\n");
            		count++;
            	}
            	*/
            	
            	outputPane.setEditable(false);
            	HTMLDocument doc = (HTMLDocument)outputPane.getDocument();
            	StyleSheet styleSheet = new StyleSheet();
            	/*
            	styleSheet.addRule(".matrix{position:relative;} ");
            	styleSheet.addRule("body{padding:20px;}");
            	styleSheet.addRule(".matrix:before,.matrix:after{content:\"\";position:absolute;top:0;border:1px solid#000;width:6px;height:100%;}");
            	styleSheet.addRule(".matrix:before{left:-6px;border-right:0px;}");
            	styleSheet.addRule(".matrix:after{right:-6px;border-left:0px;text-align:center;}");
            	styleSheet.addRule(".matrix td{padding:5px;text-align:center;}");
            	*/
            	HTMLEditorKit editorKit = (HTMLEditorKit) outputPane.getEditorKit();
            	try {
            		editorKit.setStyleSheet(styleSheet);
					editorKit.insertHTML(doc, doc.getLength(), output, 0, 0, null);
				} catch (BadLocationException | IOException e) {
					e.printStackTrace();
					System.err.print(e.getMessage()+"\n");
				}
            	
            	
            	/*
            	String start = "<html>";
            	String end = "</html>";
            	
            	if (oldText==""){
            		oldText = start;
            		oldText+="<h1>My First Heading</h1><p>My first paragraph.</p>";
            		oldText+="Hallo noch was Text";
            		//text+=output;
            		oldText+="</html>";
            	}
            	else{
            		oldText = oldText.substring(0,oldText.length()-end.length());
            		oldText+="<p> Hallo neuer Text</p>";
            		oldText+=end;
            	}
            	System.out.print(oldText+"\n");
            	outputPane.setContentType("text/html");
            	outputPane.setText(oldText);
            	*/
            }
		}.start();
	}
	
	
	private void save(){
		GeogebraViewProperties viewProps= new GeogebraViewProperties();
		viewProps.setWidth(1600);
		viewProps.setHeight(850);
		viewProps.setxZero(-400);
		viewProps.setyZero(6000);
		viewProps.setScale(1.05f);
		viewProps.setYscale(1.05f);
			
		GeogebraBuilder geogebra = new GeogebraBuilder(GeogebraBuilder.VIEWMODE.twoD,viewProps);
		geogebra.setFilename("DijkstraIllustration");
		geogebra.setPath("/home/p6majo/workbase/toe/data/graphs");
			
		geogebra.addGraph(graph,5,3,GeogebraBuilder.BLUE, GeogebraBuilder.RED,false,3,false, 3);
			
			
		geogebra.highlightPath(path, graph, 7, 5, GeogebraBuilder.GREEN, GeogebraBuilder.GREEN,true,3,true,3);
		
		String sliderId = "Schritte";
		String resetButtonId = "resetSchritte";
		geogebra.addSlider(100, 500, 400, 0, 0, logger.size(), 1,sliderId,"Dijkstra-Algorithmus", GeogebraBuilder.BLACK, false, true, true, true, true, 3, logger.toGeogebraSliderScript(graph,sliderId));	
		geogebra.addButton(100,550,resetButtonId,"Schritte zurücksetzen",GeogebraBuilder.RED,logger.toGeogebraResetButtonScript(graph, path, resetButtonId, sliderId, "blue", "red", "green"));		
		
		geogebra.export(true);	
	 }

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		if (arg0.getSource()==this.startList) this.startId=this.startList.getSelectedValue();
		if (arg0.getSource()==this.endList) this.stopId = this.endList.getSelectedValue();
	}
	
}
