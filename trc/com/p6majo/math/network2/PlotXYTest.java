package com.p6majo.math.network2;

import com.p6majo.info.aachen.Kon;
import com.p6majo.plot.PlotUtils;
import com.princeton.Draw;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static com.p6majo.plot.PlotUtils.findMax;
import static com.p6majo.plot.PlotUtils.findMin;

public class PlotXYTest {

    private  Draw plot;
    private final int plotWidth=1000;
    private final int plotHeight = 1000;



    @Test
    public void plotTest(){

        plot = new Draw();
        plot.setCanvasSize(plotWidth,plotHeight);
        plot.setXscale(0,plotWidth);
        plot.setYscale(0,plotHeight);

        plot.enableDoubleBuffering();
        this.plotUpdate();

        Kon.readInt();
    }

    private void plotUpdate(){

        float[] lossData = new float[plotWidth];
        float[] regData = new float[plotWidth];

        for (int i = 0; i <plotWidth ; i++) {
            lossData[i]=(float) Math.random()*10;
            regData[i] = (float) Math.random()*100;
        }

        float lg10 = (float) (1./Math.log(10));
        float maxLoss = Math.round(lg10*Math.log(findMax(lossData))+0.5f);
        float maxReg =Math.round(lg10*Math.log(findMax(regData))+0.5f);
        float minLoss =Math.round(Math.log(findMin(lossData)*lg10)-0.5f);
        float minReg = Math.round(Math.log(findMin(regData)*lg10)-0.5f);

        float plotMax =Math.max(maxLoss,maxReg);
        float plotMin = Math.min(minLoss,minReg);

        float minx= 0;
        float maxx=Math.round(Math.log(lossData.length)*lg10+0.49);

        float dx = (float)plotWidth/(maxx-minx);
        float dy =(float)  plotHeight/(plotMax-plotMin);

         plot.setPenColor(Color.RED);

        for (int i = 0; i <lossData.length-1 ; i++) {
            plot.line(minx + (float) Math.log(i)*lg10*dx,((float) Math.log(lossData[i])*lg10-plotMin)*dy,minx + (float) Math.log(i+1)*lg10*dx,((float) Math.log(lossData[i+1])*lg10-plotMin)*dy);
            System.out.println((minx + (float) Math.log(i)*lg10*dx)+" "+(plotMin+(float) Math.log(lossData[i])*lg10*dy)+" "+(minx + (float) Math.log(i+1)*lg10*dx)+" "+(plotMin+(float) Math.log(lossData[i+1])*lg10*dy));
        }

        plot.setPenColor(Color.BLUE);
        for (int i = 0; i <lossData.length-1 ; i++) {
            plot.line(minx + (float) Math.log(i)*lg10*dx,((float) Math.log(regData[i])*lg10-plotMin)*dy,minx + (float) Math.log(i+1)*lg10*dx,((float) Math.log(regData[i+1])*lg10-plotMin)*dy);
        }

        plot.show();
    }


}
