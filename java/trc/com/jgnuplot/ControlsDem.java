package com.jgnuplot;

/**
 * This class provides the samefunctionality as gnuplot simple.dem
 * 
 * warning: this demo is SLOW on PCs without math coprocessors!
 * 
 * From _Automatic_Control_Systems_, fourth ed., figure 6-14 transient response
 * of a second-order system to a unit step input function
 * 
 * @author Pander
 */
public final class ControlsDem {
   private ControlsDem() {
   }

   public static void gnuplot() {
      Plot.setGnuplotExecutable("gnuplot");
      Plot.setPlotDirectory("/tmp");

      Plot aPlot = new Plot();
      aPlot.setXRange("0", "13");
      aPlot.setKey("box");
      aPlot.setSamples("50");
      aPlot.setDummy("t");
      aPlot.addExtra("damp(t) = exp(-s*wn*t)/sqrt(1.0-s*s)");
      aPlot.addExtra("per(t) = sin(wn*sqrt(1.0-s**2)*t - atan(-sqrt(1.0-s**2)/s))");
      aPlot.addExtra("c(t) = 1-damp(t)*per(t)");
      aPlot.addExtra("# wn is undamped natural frequency");
      aPlot.addExtra("# s is damping factor");
      aPlot.addExtra("wn = 1.0");
      aPlot.pushGraph(new Graph("s=.1,c(t)"));
      aPlot.pushGraph(new Graph("s=.3,c(t)"));
      aPlot.pushGraph(new Graph("s=.5,c(t)"));
      aPlot.pushGraph(new Graph("s=.7,c(t)"));
      aPlot.pushGraph(new Graph("s=.9,c(t)"));
      aPlot.pushGraph(new Graph("s=1.0,c(t)"));
      aPlot.pushGraph(new Graph("s=1.5,c(t)"));
      aPlot.pushGraph(new Graph("s=2.0,c(t)"));
      aPlot.addExtra("# plot c(t) for several different damping factors s");
      aPlot.setOutput(Terminal.PNG, "test/out/controls-01.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }
   }
}
