package com.jgnuplot;

/**
 * This class provides the samefunctionality as gnuplot using.dem
 * 
 * Requires data file "using.dat" from this directory, so change current working
 * directory to this directory before running.
 * 
 * @author Pander
 */
public final class UsingDem {
   private UsingDem() {
   }

   public static void gnuplot() {
      Plot.setGnuplotExecutable("gnuplot");
      Plot.setPlotDirectory("/tmp");

      Plot aPlot = new Plot();
      aPlot.setTitle("Convex     November 1-7 1989    Circadian");
      aPlot.setKey("left box");
      aPlot.setXRange("-1", "24");
      aPlot.pushGraph(new Graph("trc/com/jgnuplot/in/using.dat", "2:4", Axes.NOT_SPECIFIED,
            "Logged in", Style.IMPULSES));
      aPlot.pushGraph(new Graph("trc/com/jgnuplot/in/using.dat", "2:4", Axes.NOT_SPECIFIED,
            "Logged in", Style.POINTS));
      aPlot.setOutput(Terminal.PNG, "test/out/using-01.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.setXRange("1", "8");
      aPlot.setTitle("Convex     November 1-7 1989");
      aPlot.setKey("below");
      aPlot.addExtra("set label \"(Weekend)\" at 5,25 center");
      aPlot.pushGraph(new Graph("trc/com/jgnuplot/in/using.dat", "3:4", Axes.NOT_SPECIFIED,
            "Logged in", Style.IMPULSES));
      aPlot.pushGraph(new Graph("trc/com/jgnuplot/in/using.dat", "3:5", Axes.NOT_SPECIFIED,
            "Load average", Style.POINTS));
      aPlot.pushGraph(new Graph("trc/com/jgnuplot/in/using.dat", "3:6", Axes.NOT_SPECIFIED,
            "%CPU used", Style.LINES));
      aPlot.setOutput(Terminal.PNG, "test/out/using-02.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }
   }
}
