package com.p6majo.math.network2;


import com.p6majo.math.network2.layers.DynamicLayer;
import com.p6majo.math.network2.layers.Layer;
import com.p6majo.math.network2.layers.LinearLayer;
import com.p6majo.math.network2.layers.LossLayer;
import com.princeton.Draw;
import com.princeton.ExtendedDraw;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
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
public class NetworkVisualizer2 {


    /**
     * ALL_EDGES all edges are shown
     * TRAINED_EDGES only edges with weights above a certain threshold are shown
     */
    public static  enum VisualizerModus {ALL_EDGES, TRAINED_EDGES};

    private final VisualizerModus vModus;

    private final ExtendedDraw frame;
    private final int width = 1300;
    private final int height = 800;
    private final int xoffset = 50;
    private final int yoffset = 50;

    private final double layerSpacing ;
    private final List<Layer> visibleLayers;

    private final Network network;

    //Controls the update of the frame
    //this should be paused when the there is no update requested
    private final Timer timer;
    private final Timer lossTimer;


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
    public NetworkVisualizer2(Network net, VisualizerModus modus, int fps){

        vModus = modus;
        network = net;


        //layer spacing
        this.visibleLayers = net.getVisualizableLayers();
        this.layerSpacing = (height-yoffset)/(visibleLayers.size()-1);


        //setup timer
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                update();
            }
        };


        TimerTask lossTimerTask = new TimerTask(){
            @Override
            public void run(){
                collectLossData();
            }
        };

        timer = new Timer("MyTimer");
        timer.scheduleAtFixedRate(timerTask,1000,1000/fps);

        lossTimer = new Timer("LossTimer");
        lossTimer.scheduleAtFixedRate(lossTimerTask,1000,100/fps); //the loss timer is ten times faster than the graph timer

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

        int l = 0;
        for (Layer layer:this.visibleLayers){
            ((Visualizable) layer).drawText(frame,width,xoffset,yoffset,l,layerSpacing);
            l++;
        }

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
     * redraw lines of the network
     *
     */
    private void update(){
        this.update("");
    }

    /**
     * redraw lines of the network
     *
     */
    private void update(String info){
        frame.text(100,100,info);
        int l = 0;
       for (Layer layer:this.visibleLayers) {
           Visualizable vis = (Visualizable) layer;
           vis.drawEdges(frame, width, xoffset, yoffset, l, layerSpacing,vModus);
           l++;
       }
       l=0;
        for (Layer layer:this.visibleLayers) {
            Visualizable vis = (Visualizable) layer;
            vis.drawVertices(frame,width,xoffset,yoffset,l,layerSpacing);
            l++;
        }
        frame.show();
    }


    private void collectLossData(){
        LossLayer llayer = network.getLossLayer();
    }



}
