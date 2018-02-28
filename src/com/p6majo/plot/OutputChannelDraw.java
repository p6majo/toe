package com.p6majo.plot;

import com.p6majo.math.utils.Box;
import com.princeton.Draw;

public class OutputChannelDraw implements OutputChannel {

    private final Draw frame;
    private Box range;
    private int width=300;
    private int height = 300;

    private final PlotOptions options ;
    private final Plot plot;

    public OutputChannelDraw(Plot plot,PlotOptions plotOptions){
        this.options = plotOptions;
        this.plot = plot;
        this.width = plot.plotRange.getRange(0).getSamples();
        this.height = plot.plotRange.getRange(1).getSamples();

        frame = new Draw();
        frame.setCanvasSize(width+1,height+1);
        frame.setXscale(0,width);
        frame.setYscale(0,height);
        frame.enableDoubleBuffering();
    }

    /**
     * finish the creation, no further changes are performed to the output channel
     */
    @Override
    public void finished() {
        primitivePoints.stream()
                .forEach(p->{frame.setPenColor(p.color);
                             frame.point(p.point.x,height-p.point.y);
                            });
        primitiveLines.stream()
                .forEach(l->{frame.setPenColor(l.color);
                             frame.line(l.start.x,height-l.start.y,l.end.x,height-l.end.y);});
        frame.show();
    }

    /**
     * close output
     */
    @Override
    public void close() {
        frame.clear();
    }
}
