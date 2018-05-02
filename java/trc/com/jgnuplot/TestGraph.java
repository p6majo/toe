package com.jgnuplot;

import java.io.File;

import com.jgnuplot.*;
import junit.framework.Assert;
import junit.framework.TestCase;

public class TestGraph extends TestCase {
   public final void test() {
      // preparation
      // TODO Plot.setGnuplotExecutable("c:\\program
      // files\\gnuplot\\wgnupl32.exe");
      Plot.setGnuplotExecutable("gnuplot");
      // TODO Plot.setPlotDirectory("c:\\temp");
      Plot.setPlotDirectory("/tmp");
      Graph aGraph = null;
      Plot aPlot = new Plot();

      // most simple graph
      // deze simpele moet een static method worden
      aPlot.setDataFileName("test" + File.separator + "in" + File.separator
            + "test.dat");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      // more tuned graph
      aPlot.setTitle("Example");
      aPlot.setXLabel("xxx");
      aPlot.setYLabel("yyy");
      aPlot.setXRange(".5", "4");
      aPlot.setYRange("-.5", "4.5");
      aGraph = new Graph("test" + File.separator + "in" + File.separator
            + "test.dat", "1:2", Axes.NOT_SPECIFIED, "qwer", Style.LINES,
            LineType.SCREEN_BLUE, PointType.NOT_SPECIFIED);
      aPlot.pushGraph(aGraph);
      aGraph = new Graph("test" + File.separator + "in" + File.separator
            + "testtest.dat", "1:3", Axes.NOT_SPECIFIED, "asdf",
            Style.LINESPOINTS, LineType.SCREEN_GREEN,
            PointType.SCREEN_SQUARE_DOT);
      aPlot.pushGraph(aGraph);
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      // even more tuned graph
      aPlot.setY2Label("y2y2y2");
      aPlot.setY2Range("2", "3.4");
      aPlot.popGraph();
      aGraph = aPlot.popGraph();
      aGraph.setName("dataY");
      aPlot.pushGraph(aGraph);
      aGraph = new Graph("test" + File.separator + "in" + File.separator
            + "testtest.dat", "1:3", Axes.X1Y2, "dataY2", Style.LINESPOINTS,
            LineType.SCREEN_GREEN, PointType.SCREEN_SQUARE_DOT);
      aPlot.pushGraph(aGraph);
      aPlot.addExtra("set label \"label\" at 1,1 center");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      // plot to PNG file
      aPlot.setOutput(Terminal.PNG, "test/out/example.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      // plot to GIF file
      aPlot.setOutput(Terminal.GIF, "test/out/example.gif");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      // plot to PostScript file
      aPlot.setOutput(Terminal.POSTSCRIPT, "test/out/example.ps");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      // plot to SVG file
      aPlot.setOutput(Terminal.SVG, "test/out/example.svg");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      Assert.assertTrue(true);
   }
}
