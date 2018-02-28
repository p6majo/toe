package com.p6majo.plot;

import com.p6majo.math.utils.Box;
import com.p6majo.math.utils.Utils;
import com.princeton.Draw;

import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;

public class OutputChannelFile implements OutputChannel {

    private final File filename;
    private Box range;
    private int width=300;
    private int height = 300;
    private double zmax = 1;

    private final PlotOptions options ;
    private final Plot plot;

    public OutputChannelFile(Plot plot, PlotOptions plotOptions){
        this.options = plotOptions;
        this.plot = plot;
        this.width = plot.plotRange.getRange(0).getSamples();
        this.height = plot.plotRange.getRange(1).getSamples();
        this.zmax = plot.plotRange.getRange(2).getEnd().doubleValue();

        this.filename = new File(plotOptions.getOutputFilename());

    }


    /**
     * finish the creation, no further changes are performed to the output channel
     */
    @Override
    public void finished() {
        StringBuilder out = new StringBuilder();
        //construct file header
        out.append("P3\n");
        out.append(this.width+" "+this.height+"\n");
        out.append("256\n");

        Iterator<PrimitivePoint> it = primitivePoints.iterator();
        int x =0;
        int y =0;
        while (it.hasNext()){
           PrimitivePoint p  = it.next();
           //line feed
           if (p.point.y!=y) {
               out.append("\n");
               y=p.point.y;
           }

           Color c = p.color;
           out.append(c.getRed()+" "+c.getGreen()+" "+c.getBlue()+" ");
        }

        PrintWriter writer = null;
        try{
            writer = new PrintWriter(this.filename,"UTF-8");
            writer.write(out.toString());
            writer.close();
        }
        catch (Exception ex){
            Utils.errorMsg(ex.getMessage());
        }
    }

    /**
     * close output
     */
    @Override
    public void close() {

    }
}
