package com.jgnuplot;

/**
 * This class provides the samefunctionality as gnuplot simple.dem
 * 
 * Requires data files "[123].dat" from this directory, so change current
 * working directory to this directory before running.
 * 
 * @author Pander
 */
public final class SimpleDem {
   private SimpleDem() {
   }

   public static void gnuplot() {
      Plot.setGnuplotExecutable("gnuplot");
      Plot.setPlotDirectory("/tmp");

      Plot aPlot = new Plot();
      aPlot.setKey("left box");
      aPlot.setSamples("50");
      aPlot.setRanges("[-10:10]");
      aPlot.pushGraph(new Graph("sin(x)"));
      aPlot.pushGraph(new Graph("atan(x)"));
      aPlot.pushGraph(new Graph("cos(atan(x))"));
      aPlot.setOutput(Terminal.PNG, "test/out/simple-01.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.setKey("right nobox");
      aPlot.setSamples("100");
      aPlot.setRanges("[-pi/2:pi]");
      aPlot.pushGraph(new Graph("cos(x)"));
      aPlot.pushGraph(new Graph("-(sin(x) > sin(x+1) ? sin(x) : sin(x+1))"));
      aPlot.setOutput(Terminal.PNG, "test/out/simple-02.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.setKey("left box");
      aPlot.setSamples("200");
      aPlot.setRanges("[-3:5]");
      aPlot.pushGraph(new Graph("asin(x)"));
      aPlot.pushGraph(new Graph("acos(x)"));
      aPlot.setOutput(Terminal.PNG, "test/out/simple-03.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.setRanges("[-30:20]");
      aPlot.pushGraph(new Graph("besj0(x)*0.12e1", Axes.NOT_SPECIFIED, null,
            Style.IMPULSES));
      aPlot.pushGraph(new Graph("(x**besj0(x))-2.5", Axes.NOT_SPECIFIED, null,
            Style.POINTS));
      aPlot.setOutput(Terminal.PNG, "test/out/simple-04.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.setSamples("400");
      aPlot.setRanges("[-10:10]");
      aPlot.pushGraph(new Graph("real(sin(x)**besj0(x))"));
      aPlot.setOutput(Terminal.PNG, "test/out/simple-05.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.setKey("outside below");
      aPlot.setRanges("[-5*pi:5*pi] [-5:5]");
      aPlot.pushGraph(new Graph("real(tan(x)/atan(x))"));
      aPlot.pushGraph(new Graph("1/x"));
      aPlot.setOutput(Terminal.PNG, "test/out/simple-06.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.setKey("left box");
      aPlot.setAutoscale();
      aPlot.setSamples("800");
      aPlot.setRanges("[-30:20]");
      aPlot.pushGraph(new Graph("sin(x*20)*atan(x)"));
      aPlot.setOutput(Terminal.PNG, "test/out/simple-07.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.setRanges("[-19:19]");
      aPlot.pushGraph(new Graph("1.dat", null, Axes.NOT_SPECIFIED, null,
            Style.IMPULSES, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
      aPlot.pushGraph(new Graph("2.dat", null, Axes.NOT_SPECIFIED, null,
            Style.NOT_SPECIFIED, LineType.NOT_SPECIFIED,
            PointType.NOT_SPECIFIED));
      aPlot.pushGraph(new Graph("3.dat", null, Axes.NOT_SPECIFIED, null,
            Style.LINES, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
      aPlot.setOutput(Terminal.PNG, "test/out/simple-08.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.setRanges("[-19:19]");
      aPlot.addExtra("f(x) = x/100");
      aPlot.pushGraph(new Graph("1.dat", null, Axes.NOT_SPECIFIED, null,
            Style.IMPULSES, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
      aPlot.pushGraph(new Graph("2.dat", null, "thru f(x)", Axes.NOT_SPECIFIED,
            null, Style.NOT_SPECIFIED, LineType.NOT_SPECIFIED,
            PointType.NOT_SPECIFIED));
      aPlot.pushGraph(new Graph("3.dat", null, Axes.NOT_SPECIFIED, null,
            Style.LINES, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
      aPlot.setOutput(Terminal.PNG, "test/out/simple-09.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }
   }
}
