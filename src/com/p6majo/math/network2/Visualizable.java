package com.p6majo.math.network2;

import com.princeton.ExtendedDraw;

import java.awt.*;

public interface Visualizable {
    public static final boolean isVisualizable = true;
    public static final double COLOR_SATURATION = 2.;
    public static final String DELIMITER ="|";


    public void drawVertices(ExtendedDraw frame,int width, int xoffset,int yoffset,int layerIndex, double layerSpacing);

    /**
     * draw the edges of the network
     * the color of each edge represents the value of the corresponding weight
     * Depending on the selected {@link NetworkVisualizer2.VisualizerModus} all edges are shown or only selected edges
     *
     */
    public void drawEdges(ExtendedDraw frame, int width, int xoffset, int yoffset, int layerIndex, double layerSpacing, NetworkVisualizer2.VisualizerModus vModus);
    public void drawText(ExtendedDraw frame, int width, int xoffset,int yoffset, int layerIndex, double layerSpacing);

    /**
     * color codes the weights of the neurons
     * the colors range from green for zero to red for negative and blue for positive values
     *
     * The saturation value for the color is defined as a parameter of this class
     */
    default Color weight2color(double weight){

        int r=0;
        int g=255;
        int b=0;

        int contribution = (int) Math.min(255,(Math.abs(weight)/COLOR_SATURATION*255));

        g-=contribution;
        if (weight<=0)  r+=contribution;
        else b+=contribution;

        return new Color(r,g,b);
    }


    default Color value2color(double value){

        int r=(int) (255*(1.-value));
        int g=(int) (255*(1.-value));
        int b=(int) (255*(1.-value));

        return new Color(r,g,b);
    }
}
