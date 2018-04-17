package com.p6majo.math.network2;


import com.p6majo.math.network2.layers.DynamicLayer;
import com.p6majo.math.network2.layers.Layer;
import com.p6majo.math.network2.layers.LossLayer;
import com.p6majo.math.utils.Utils;
import com.p6majo.plot.PlotUtils;
import com.princeton.Draw;
import com.princeton.ExtendedDraw;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.p6majo.plot.PlotUtils.findMax;
import static com.p6majo.plot.PlotUtils.findMin;

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

   private final Draw plot;
   private final int plotWidth=500;
   private final int plotHeight = 500;


    private final double layerSpacing ;
    private final List<Layer> visibleLayers;

    private final Network network;
    private LossLayer lossLayer = null;

    //Controls the update of the frame
    //this should be paused when the there is no update requested
    private final Timer graphTimer;
    private final Timer lossTimer;

    private ArrayList<Float> lossData;
    private ArrayList<Float> regulData;

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


        //setup graphTimer
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {

            }
        };
        //setup lossTimer
        TimerTask lossTimerTask = new TimerTask(){
            @Override
            public void run(){
                collectLossData();
                graphUpdate();
                plotUpdate();
            }
        };

        graphTimer = new Timer("GraphTimer");
        graphTimer.scheduleAtFixedRate(timerTask,1000,500/fps);

        lossTimer = new Timer("LossRegularizationTimer");
        lossTimer.scheduleAtFixedRate(lossTimerTask,1000,100/fps); //the loss graphTimer is ten times faster than the graph graphTimer

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

        //loss plot

        plot = new Draw();
        plot.setCanvasSize(plotWidth,plotHeight);
        plot.setXscale(0,plotWidth);
        plot.setYscale(0,plotHeight);
        plot.enableDoubleBuffering();

        this.lossData = new ArrayList<Float>();
        this.regulData = new ArrayList<Float>();


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
    private void graphUpdate(){
        this.graphUpdate("");
    }

    /**
     * redraw lines of the network
     *
     */
    private void graphUpdate(String info){
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

    private void plotGrid(float xmin,float ymin,float xmax,float ymax,float dx, float dy){
        plot.setPenColor(Color.GRAY);
        float x = Math.round(xmin);
        while (x<xmax){
            plot.line((xmin+(x-xmin))*dx,0,(xmin+(x-xmin))*dx,plotHeight);
            x+=1;
        }
        float y = Math.round(ymin);
        while (y<ymax){
            plot.line(0,(ymin+(y-ymin))*dy,plotWidth,(ymin+(y-ymin))*dy);
            y+=1;
        }
    }

    private void plotUpdate(){
        plot.clear();
        if (lossData.size()>0 && regulData.size()>0) {
            float[] lossData = PlotUtils.binData(this.lossData, plotWidth);
            float[] regData = PlotUtils.binData(this.regulData, plotWidth);

            float lg10 = (float) (1. / Math.log(10));
            float maxLoss = Math.round(lg10 * Math.log(findMax(lossData)) + 0.49f);
            float maxReg = Math.round(lg10 * Math.log(findMax(regData)) + 0.49f);
            float minLoss = Math.round(Math.log(findMin(lossData) * lg10) - 0.49f);
            float minReg = Math.round(Math.log(findMin(regData) * lg10) - 0.49f);

            float plotMax = Math.max(maxLoss, maxReg);
            float plotMin = Math.min(minLoss, minReg);

            float minX = 0;
            float maxX = Math.round(Math.log(lossData.length) * lg10 + 0.49f);

            float dx = (float) plotWidth / (maxX - minX);
            float dy = (float) plotHeight / (plotMax - plotMin);

            plotGrid(minX, plotMin, maxX, plotMax, dx, dy);

            plot.setPenColor(Color.RED);
            for (int i = 0; i < lossData.length - 1; i++) {
                plot.line(minX + ((float) Math.log(i) * lg10 - minX) * dx, ((float) Math.log(lossData[i]) * lg10 - plotMin) * dy, minX + (float) Math.log(i + 1) * lg10 * dx, ((float) Math.log(lossData[i + 1]) * lg10 - plotMin) * dy);
            }

            plot.setPenColor(Color.GREEN);
            for (int i = 0; i < regData.length - 1; i++) {
                plot.line(minX + ((float) Math.log(i) * lg10 - minX) * dx, ((float) Math.log(regData[i]) * lg10 - plotMin) * dy, minX + (float) Math.log(i + 1) * lg10 * dx, ((float) Math.log(regData[i + 1]) * lg10 - plotMin) * dy);
            }

            plot.show();
        }
    }



    /**
     * periodically collect the loss and regularization term
     */
    private void collectLossData(){
        if (lossLayer==null)  lossLayer = network.getLossLayer();

        lossData.add(lossLayer.getLoss());
        float regterm = 0f;
        for (DynamicLayer layer:network.getDynamicLayers())
            regterm+=layer.getRegularization();
        regulData.add(regterm);
    }

}
