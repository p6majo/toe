package com.p6majo.math.network2.layers;

import com.p6majo.math.network2.*;
import com.princeton.ExtendedDraw;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.awt.*;

/**
 * A {@link LossLayer} is the basic building block (module) of a neural network
 * This abstract class contains all the required methods and attributes that every layer requires to have
 *
 *
 * @author p6majo
 * @version 2.0
 *
 *
 */
public abstract class LossLayer extends Layer implements Visualizable{
    private final int flattenedDim;

    public LossLayer(int[] inSignature) {
        super(inSignature, inSignature);
        int dim = 1;
        for (int i=0;i<inSignature.length;i++)
            dim *=inSignature[i];
        flattenedDim = dim;
    }

    public void pullBack(){
        pullBack(this.errors);
    }

    public abstract float getLoss();
  
    public abstract INDArray getLossGradient();

    public abstract TestResult getTestResult(Network.Test test);



    @Override
    public void drawVertices(ExtendedDraw frame, int width, int xoffset, int yoffset, int layerIndex, double layerSpacing) {
        double neuronSpacing = (double) width / (this.flattenedDim - 1);
        double r = width / this.flattenedDim / 3; //radius of the vertex
        for (int n = 0; n < flattenedDim; n++) {
            frame.setPenColor(value2color(this.activations.getRow(0).getFloat(n))); //only the first data of a batch is visualized
            frame.filledCircle(xoffset + n * neuronSpacing, yoffset + layerIndex * layerSpacing, r);
        }
    }

    @Override
    public void drawEdges(ExtendedDraw frame, int width, int xoffset, int yoffset, int layerIndex, double layerSpacing, NetworkVisualizer2.VisualizerModus vModus) {
            //nothing to do here, since there are no trainable parameters to visualize
    }

    @Override
    public void drawText(ExtendedDraw frame, int width, int xoffset, int yoffset, int layerIndex, double layerSpacing) {
        int neurons = this.flattenedDim;
        double radius = width / neurons / 3 + 1;
        double neuronSpacing = (double) width / (neurons - 1);
        {
            //only show labels for the dynamic neurons if there are fewer than 15
            frame.setPenColor(Color.RED);
            if (neurons < 15)
                for (int n = 0; n < neurons; n++)
                    frame.text(xoffset + n * neuronSpacing , yoffset + (layerIndex) * layerSpacing+80, n + DELIMITER + neurons);

            // frame.setPenRadius(0.5);
            for (int n = 0; n < neurons; n++)
                frame.circle(xoffset + n * neuronSpacing, yoffset + (layerIndex) * layerSpacing, radius);
        }
    }


}
