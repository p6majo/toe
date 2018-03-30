package com.jgnuplot;

/**
 * This class provides the samefunctionality as gnuplot fillstyle.dem.
 * 
 * Demo for revised fillstyle code selected by ./configure --enable-filledboxes
 * --enable-relative-boxwidth
 * 
 * @author Pander
 */
public final class FillStyleDem {
   private FillStyleDem() {
   }

   public static void gnuplot() {
      Plot.setGnuplotExecutable("gnuplot");
      Plot.setPlotDirectory("/tmp");

      Plot aPlot = new Plot();
      aPlot.setSamples("25");
      aPlot.unsetXTics();
      aPlot.unsetYTics();
      aPlot.setYRange("0", "120");
      aPlot.setTitle("A demonstration of boxes with default properties");
      aPlot.setRanges("[-10:10]");
      Graph aGraph = new Graph("100/(1.0+x*x)", Axes.NOT_SPECIFIED,
            "distribution", Style.BOXES);
      aPlot.pushGraph(aGraph);
      aPlot.setOutput(Terminal.PNG, "test/out/fillstyle-01.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      // Now draw the boxes with a black border
      aPlot.clear();
      aPlot.setTitle("A demonstration of boxes with style fill solid 1.0");
      aPlot.addExtra("set style fill solid 1.0");
      aPlot.pushGraph(aGraph);
      aPlot.setOutput(Terminal.PNG, "test/out/fillstyle-02.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      // Now draw the boxes with a black border
      aPlot.clear();
      aPlot.setTitle("A demonstration of boxes with style fill solid border -1");
      aPlot.addExtra("set style fill solid border -1");
      aPlot.pushGraph(aGraph);
      aPlot.setOutput(Terminal.PNG, "test/out/fillstyle-03.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      // Now make the boxes a little less wide
      aPlot.clear();
      aPlot.setTitle("Filled boxes of reduced width");
      aPlot.addExtra("set boxwidth 0.5");
      aPlot.pushGraph(aGraph);
      aPlot.setOutput(Terminal.PNG, "test/out/fillstyle-04.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      // And now let's try a different fill density
      aPlot.clear();
      aPlot.setTitle("Filled boxes at 50% fill density");
      aPlot.addExtra("set style fill solid 0.25 border");
      aPlot.pushGraph(aGraph);
      aPlot.setOutput(Terminal.PNG, "test/out/fillstyle-05.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      // Now draw the boxes with no border
      aPlot.clear();
      aPlot.setTitle("A demonstration of boxes with style fill solid 0.25 noborder");
      aPlot.addExtra("set style fill solid 0.25 noborder");
      aPlot.pushGraph(aGraph);
      aPlot.setOutput(Terminal.PNG, "test/out/fillstyle-06.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      // Or maybe a pattern fill, instead?
      aPlot.clear();
      aPlot.setTitle("A demonstration of boxes in mono with style fill pattern");
      aPlot.setSamples("11");
      aPlot.addExtra("set boxwidth 0.5");
      aPlot.addExtra("set style fill pattern border");
      aPlot.setRanges("[-2.5:4.5]");
      aPlot.pushGraph(new Graph("100/(1.0+x*x)", Axes.NOT_SPECIFIED,
            "pattern 0", Style.BOXES, LineType.SCREEN_BLACK_SOLID_BOLD,
            PointType.NOT_SPECIFIED));
      aPlot.pushGraph(new Graph("80/(1.0+x*x)", Axes.NOT_SPECIFIED,
            "pattern 0", Style.BOXES, LineType.SCREEN_BLACK_SOLID_BOLD,
            PointType.NOT_SPECIFIED));
      aPlot.pushGraph(new Graph("40/(1.0+x*x)", Axes.NOT_SPECIFIED,
            "pattern 0", Style.BOXES, LineType.SCREEN_BLACK_SOLID_BOLD,
            PointType.NOT_SPECIFIED));
      aPlot.pushGraph(new Graph("20/(1.0+x*x)", Axes.NOT_SPECIFIED,
            "pattern 0", Style.BOXES, LineType.SCREEN_BLACK_SOLID_BOLD,
            PointType.NOT_SPECIFIED));
      aPlot.setOutput(Terminal.PNG, "test/out/fillstyle-07.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }
   }
}
