package com.jgnuplot;

import com.jgnuplot.Graph;
import com.jgnuplot.Plot;
import com.jgnuplot.Terminal;

/**
 * This class provides the samefunctionality as gnuplot polar.dem
 * 
 * Show some of the new polar capabilities.
 * 
 * @author Pander
 */
public final class PolarDem {
   private PolarDem() {
   }

   public static void gnuplot() {
      Plot.setGnuplotExecutable("gnuplot");
      Plot.setPlotDirectory("/tmp");

      Plot aPlot = new Plot();
      // TODOaPlot.unsetBorder();
      aPlot.setClip();
      aPlot.setPolar();
      aPlot.setXTics("axis nomirror");
      aPlot.setYTics("axis nomirror");
      aPlot.setSamples("160");
      aPlot.addExtra("set zeroaxis");
      aPlot.setTRange("0", "2*pi");
      aPlot.setTitle("Three circles (with aspect ratio distortion)");
      aPlot.pushGraph(new Graph(".5"));
      aPlot.pushGraph(new Graph("1"));
      aPlot.pushGraph(new Graph("1.5"));
      aPlot.setOutput(Terminal.PNG, "test/out/polar-01.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.setKey("box");
      aPlot.pushGraph(new Graph("cos(2*t)"));
      aPlot.setOutput(Terminal.PNG, "test/out/polar-02.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.pushGraph(new Graph("2*sqrt(cos(t))"));
      aPlot.pushGraph(new Graph("-2*sqrt(cos(t))"));
      aPlot.setOutput(Terminal.PNG, "test/out/polar-03.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.addExtra("set offsets 0,0,0,0");
      aPlot.pushGraph(new Graph("sin(4*t)"));
      aPlot.pushGraph(new Graph("cos(4*t)"));
      aPlot.setOutput(Terminal.PNG, "test/out/polar-04.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.setXRange("-5", "5");
      aPlot.setYRange("-5", "5");
      aPlot.pushGraph(new Graph("t/cos(3*t)"));
      aPlot.setOutput(Terminal.PNG, "test/out/polar-05.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.setAutoscale();
      aPlot.setAutoscaleAfterRanges();
      aPlot.pushGraph(new Graph("1-sin(t)"));
      aPlot.setOutput(Terminal.PNG, "test/out/polar-06.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.setTRange("0", "12*pi");
      aPlot.pushGraph(new Graph("2*t"));
      aPlot.setOutput(Terminal.PNG, "test/out/polar-07.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.addExtra("butterfly(x)=exp(cos(x))-2*cos(4*x)+sin(x/12)**5");
      aPlot.setSamples("800");
      // This is a big one (many samples), be patient...
      aPlot.setTitle("Butterfly");
      aPlot.unsetKey();
      aPlot.pushGraph(new Graph("butterfly(t)"));
      aPlot.setOutput(Terminal.PNG, "test/out/polar-08.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }
   }
}
