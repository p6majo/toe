package com.jgnuplot;

/**
 * This class provides the samefunctionality as gnuplot param.dem
 * 
 * Show some of the new parametric capabilities.
 * 
 * @author Pander
 */
public final class ParamDem {
   private ParamDem() {
   }

   public static void gnuplot() {
      Plot.setGnuplotExecutable("gnuplot");
      Plot.setPlotDirectory("/tmp");

      Plot aPlot = new Plot();
      aPlot.setParametric();
      aPlot.setDummy("t");
      aPlot.setAutoscale();
      aPlot.setSamples("160");
      aPlot.setTitle("");
      aPlot.setKey("below box");
      aPlot.pushGraph(new Graph("t,sin(t)/t", Axes.NOT_SPECIFIED,
            "t, sin(t)/t or sin(x)/x"));
      aPlot.setOutput(Terminal.PNG, "test/out/param-01.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.pushGraph(new Graph("sin(t),t"));
      aPlot.setOutput(Terminal.PNG, "test/out/param-02.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.pushGraph(new Graph("sin(t),cos(t)"));
      aPlot.setOutput(Terminal.PNG, "test/out/param-03.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.setXRange("-3", "3");
      aPlot.setYRange("-3", "3");
      aPlot.setTitle("Parametric Conic Sections");
      aPlot.pushGraph(new Graph("-t,t"));
      aPlot.pushGraph(new Graph("cos(t),cos(2*t)"));
      aPlot.pushGraph(new Graph("2*cos(t),sin(t)"));
      aPlot.pushGraph(new Graph("-cosh(t),sinh(t)"));
      aPlot.setOutput(Terminal.PNG, "test/out/param-04.png");
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
      aPlot.pushGraph(new Graph("tan(t),t"));
      aPlot.pushGraph(new Graph("t,tan(t)"));
      aPlot.setOutput(Terminal.PNG, "test/out/param-05.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }
   }
}
