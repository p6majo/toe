package com.p6majo.math.network;

import com.princeton.Draw;
import com.princeton.ExtendedDraw;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import java.util.Timer;

import static com.p6majo.math.network.NetworkVisualizer.VisualizerModus.ALL_VERTICES;


/**
 * A simple visualization tool of the neural network
 * using the princeton princeton package
 *
 * The network is a forrest
 * Each input node is the starting point for a tree
 * all trees are collected into one forest
 *
 * @author p6majo
 */
public class NetworkVisualizer {


    public static  enum VisualizerModus {ALL_VERTICES,TRAINED_VERTICES};
    private VisualizerModus vModus = ALL_VERTICES;
    private ExtendedDraw frame;
    private final static String NEURON_LABEL = "";
    private final static String DELIMITER = "|";
    private final static double COLOR_SATURATION = 5.;

    private final int width = 3300;
    private final int height = 500;
    private final int xoffset = 50;
    private final int yoffset = 50;

    private double layerSpacing ;


    private static Network network;
    private int[] structure;

    //Controls the update of the frame
    //this should be paused when the there is no update requested
    //default update rate is 100 ms.
    private Timer timer=null;



    public NetworkVisualizer(Network net,VisualizerModus modus){

        vModus = modus;
        network = net;
        structure = net.getStructure();

        //layer spacing
        this.layerSpacing = height/(net.getNumberOfLayers()-1);


       // this.mapNeurons();
        frame = new ExtendedDraw();
        frame.setCanvasSize(width+2*xoffset,height+2*yoffset);
        frame.setXscale(0,width+2*xoffset);
        frame.setYscale(0,height+2*yoffset);



        frame.setButton1Label("Pause");
        frame.setButton2Label("Single Step");
        frame.setButton3Label("Resume");

        frame.setActionListenerForButton1(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                network.suspendStochasticDescent();
                /*
                if (timer!=null) {
                    try {
                        timer.wait();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                */
            }
        });

        frame.setActionListenerForButton3(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                network.resumeStochasticDescent();
                //if (timer!=null) timer.notify();
            }
        });

        frame.enableDoubleBuffering();
        this.drawTextForNeurons();
        this.drawNeurons();
        this.update();





    }

    public void setTimer(Timer timer){
        this.timer = timer;
    }

    private void drawTextForNeurons(){
        frame.setPenColor(Draw.BLACK);
        for (int l = 0;l<network.getNumberOfLayers();l++) {
            double neuronSpacing = (double) width / (structure[l] - 1);
            for (int n = 0; n < structure[l]; n++) {
                if (l>0) frame.text(xoffset +n * neuronSpacing + 20 , yoffset + l * layerSpacing, n + DELIMITER + structure[l]);
                frame.circle(xoffset +  n * neuronSpacing, yoffset +l * layerSpacing, 6);
                frame.circle(xoffset +  n * neuronSpacing, yoffset +l * layerSpacing, 7);
            }
        }
    }

    private void drawNeurons(){
        for (int l = 0;l<network.getNumberOfLayers();l++) {
            double neuronSpacing = (double) width / (structure[l] - 1);
            for (int n = 0; n < structure[l]; n++) {
                frame.setPenColor(value2color(network.get(l,n).value));
                frame.filledCircle(xoffset + n * neuronSpacing, yoffset + l * layerSpacing, 5.);
            }
        }
    }


    private void drawVertices(){
        switch (vModus) {
            case ALL_VERTICES:
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
            case TRAINED_VERTICES:
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
    public synchronized void update(){
        drawVertices();
        drawNeurons();
        frame.show();
    }

    /**
     * redraw lines of the network
     *
     */
    public synchronized void update(String info){
        frame.text(100,100,info);
        drawVertices();
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
    private static int[] vertexLabel2Neuron(String label){
        label = label.substring(NEURON_LABEL.length(),label.length());
        StringTokenizer tokenizer = new StringTokenizer(label,DELIMITER);
        int layer = Integer.parseInt(tokenizer.nextToken());
        int pos = Integer.parseInt(tokenizer.nextToken());
        return new int[]{layer,pos};
    }




}
