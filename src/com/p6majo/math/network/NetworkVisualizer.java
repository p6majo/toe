package com.p6majo.math.network;

import com.princeton.Draw;
import com.princeton.ExtendedDraw;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple visualization tool of the neural network
 * using the princeton princeton package
 *
 * It allows to follow the process of training the network
 *
 * The network is a forrest
 * Each input node is the starting point for a tree
 * all trees are collected into one forest
 *
 * @author p6majo
 * @version 1.0
 * @date 3.4.18
 */
public class NetworkVisualizer {


    /**
     * ALL_EDGES all edges are shown
     * TRAINED_EDGES only edges with weights above a certain threshold are shown
     */
    public static  enum VisualizerModus {ALL_EDGES, TRAINED_EDGES};

    private final VisualizerModus vModus;

    private final static String NEURON_LABEL = "";
    private final static String DELIMITER = "|";
    private final static double COLOR_SATURATION = 5.;

    private final ExtendedDraw frame;
    private final int width = 1300;
    private final int height = 800;
    private final int xoffset = 50;
    private final int yoffset = 50;

    private final double layerSpacing ;

    private final Network network;
    private final int[] structure;

    //Controls the update of the frame
    //this should be paused when the there is no update requested
    private final Timer timer;

    /**
     *
     * Constructor for the network visualizer
     * It is usually invoked, if the corresponding flag is set
     * during the construction of the network
     *
     * @param net associated network
     * @param modus modi, selecting edges for display
     * @param fps frames per second
     */
    public NetworkVisualizer(Network net,VisualizerModus modus, int fps){

        vModus = modus;
        network = net;
        structure = net.getSignature();

        //layer spacing
        this.layerSpacing = height/(net.getNumberOfLayers()-1);


        //setup timer
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                update();
            }
        };
        timer = new Timer("MyTimer");
        timer.scheduleAtFixedRate(timerTask,1000,1000/fps);


        // setup graphical environment
        frame = new ExtendedDraw();
        frame.setCanvasSize(width+2*xoffset,height+2*yoffset);
        frame.setXscale(0,width+2*xoffset);
        frame.setYscale(0,height+2*yoffset);

        frame.setButton1Label("Pause");
        frame.setButton2Label("Single Step");
        frame.setButton3Label("Resume");
        frame.setButton4Label("Test");
        frame.setButton5Label("Exit");


        frame.setActionListenerForButton1(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                network.suspendStochasticDescent();
               // frame.showInfo("Rate: "+network.runTest(10000));
               // frame.setInfoVisible(true);
            }
        });

        frame.setActionListenerForButton2(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                network.performSingleStep();
               // frame.showInfo("Rate: "+network.runTest(10000));
               // frame.setInfoVisible(true);
            }
        });

        frame.setActionListenerForButton3(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                network.resumeStochasticDescent();
                frame.setInfoVisible(false);
            }
        });

        frame.setActionListenerForButton4(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                double rate = network.runTest();
                if (rate!=-1.)
                    frame.showInfo("test finished with a rate: "+rate);
                else
                    frame.showInfo("testing failed, you have to pause the training first.");
                frame.setInfoVisible(true);
            }
        });

        frame.setActionListenerForButton5(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               System.exit(0);
            }
        });

        frame.enableDoubleBuffering();
        this.drawTextForNeurons();
        this.drawNeurons();
        this.update();

    }

    /**
     * Allows to process information onto the graphical environment
     * @param info
     */
    public void showInfo(String info){
        this.frame.showInfo(info);
    }


    /**
     * this method is called only once during initialization
     * and does not cause time constraints due to additional rendering of graphical objects
     */
    private void drawTextForNeurons(){
        frame.setPenColor(Draw.BLACK);
        for (int l = 0;l<network.getNumberOfLayers();l++) {
            double neuronSpacing = (double) width / (structure[l] - 1);
            for (int n = 0; n < structure[l]; n++) {
                //only show labels for the dynamic neurons if there are fewer than 15
                if (l>0  && structure[l]<15) frame.text(xoffset +n * neuronSpacing + 20 , yoffset + l * layerSpacing, n + DELIMITER + structure[l]);
                frame.circle(xoffset +  n * neuronSpacing, yoffset +l * layerSpacing, 6);
                frame.circle(xoffset +  n * neuronSpacing, yoffset +l * layerSpacing, 7);
            }
        }
    }

    /**
     * draw neurons as vertices of the network graph
     * the color of the neuron represents its value
     */
    private void drawNeurons(){
        for (int l = 0;l<network.getNumberOfLayers();l++) {
            double neuronSpacing = (double) width / (structure[l] - 1);
            for (int n = 0; n < structure[l]; n++) {
                frame.setPenColor(value2color(network.get(l,n).value));
                frame.filledCircle(xoffset + n * neuronSpacing, yoffset + l * layerSpacing, 5.);
            }
        }
    }

    /**
     * draw the edges of the network
     * the color of each edge represents the value of the corresponding weight
     * Depending on the selected {@link VisualizerModus} all edges are shown or only selected edges
     *
     */
    private void drawEdges(){
        switch (vModus) {
            case ALL_EDGES:
                for (int l=1;l<network.getNumberOfLayers();l++) {
                    double nNeuronSpacing = (double) width/ (structure[l] - 1);
                    double mNeuronSpacing = (double) width / (structure[l - 1] - 1);
                    for (int n = 0; n < structure[l]; n++)
                        for (int m = 0; m < structure[l - 1]; m++) {
                            frame.setPenColor(this.weight2color(network.get(l, n).weights[m]));
                            frame.line(xoffset + m * mNeuronSpacing, yoffset + (l - 1) * layerSpacing, xoffset + n * nNeuronSpacing, yoffset + l * layerSpacing);
                        }
                }
                break;
            case TRAINED_EDGES:
                for (int l = 1; l < network.getNumberOfLayers(); l++) {
                    double nNeuronSpacing = (double) width / (structure[l] - 1);
                    double mNeuronSpacing = (double) width / (structure[l - 1] - 1);
                    for (int n = 0; n < structure[l]; n++)
                        for (int m = 0; m < structure[l - 1]; m++) {
                            if (Math.abs(network.get(l, n).weights[m]) > COLOR_SATURATION / 2) {
                                frame.setPenColor(this.weight2color(network.get(l, n).weights[m]));
                                frame.line(xoffset + m * mNeuronSpacing, yoffset + (l - 1) * layerSpacing, xoffset + n * nNeuronSpacing, yoffset + l * layerSpacing);
                            }
                        }
                }
                break;
        }
    }

    /**
     * redraw lines of the network
     *
     */
    private void update(){
        drawEdges();
        drawNeurons();
        frame.show();
    }

    /**
     * redraw lines of the network
     *
     */
    private void update(String info){
        frame.text(100,100,info);
        drawEdges();
        drawNeurons();
        frame.show();
    }


    /**
     * color codes the weights of the neurons
     * the colors range from green for zero to red for negative and blue for positive values
     *
     * The saturation value for the color is defined as a parameter of this class
     */
    private Color weight2color(double weight){

        int r=0;
        int g=255;
        int b=0;

        int contribution = (int) Math.min(255,(Math.abs(weight)/COLOR_SATURATION*255));

        g-=contribution;
        if (weight<=0)  r+=contribution;
        else b+=contribution;

        return new Color(r,g,b);
    }


    private Color value2color(double value){

        int r=(int) (255*(1.-value));
        int g=(int) (255*(1.-value));
        int b=(int) (255*(1.-value));

        return new Color(r,g,b);
    }

    /**
     * returns the vertex label of the neuron (0 index base)
     * @param layer
     * @param pos
     * @return
     */
    private static String neuron2VertexLabel(int layer, int pos) {
        String label = "";

        label = NEURON_LABEL;
        label += layer+DELIMITER+pos;

        //
        // label +="_";
        // label += Utils.getIndices(pos+"");
        // label += layer+"";
        // label += pos+"";
        return label;
    }

    /**
     * Extracts the postion of the neuron for the given label of the vertex
     * @param label
     * @return
     */
    private static int[] vertexLabel2Neuron(String label) {
        label = label.substring(NEURON_LABEL.length(), label.length());
        StringTokenizer tokenizer = new StringTokenizer(label, DELIMITER);
        int layer = Integer.parseInt(tokenizer.nextToken());
        int pos = Integer.parseInt(tokenizer.nextToken());
        return new int[]{layer, pos};
    }
}
