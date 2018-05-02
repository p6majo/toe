package com.jgnuplot;

/**
 * This class provides the samefunctionality as gnuplot electron.dem.
 * 
 * Electronics demo
 * 
 * @author Pander
 */
public final class ElectronDem {
   private ElectronDem() {
   }

   public static void gnuplot() {
      Plot.setGnuplotExecutable("gnuplot");
      Plot.setPlotDirectory("/tmp");

      Plot aPlot = new Plot();
      aPlot.addExtra("# Bipolar Transistor (NPN) Mutual Characteristic");
      aPlot.addExtra("Ie(Vbe)=Ies*exp(Vbe/kT_q)");
      aPlot.addExtra("Ic(Vbe)=alpha*Ie(Vbe)+Ico");
      aPlot.addExtra("alpha = 0.99");
      aPlot.addExtra("Ies = 4e-14");
      aPlot.addExtra("Ico = 1e-09");
      aPlot.addExtra("kT_q = 0.025");
      aPlot.setDummy("Vbe");
      aPlot.setGrid();
      aPlot.addExtra("set offsets");
      aPlot.addExtra("unset log");
      aPlot.addExtra("unset polar");
      aPlot.setSamples("160");
      aPlot.setTitle("Mutual Characteristic of a Transistor");
      aPlot.setXLabel("Vbe (base emmitter voltage)");
      aPlot.setXRange("0", "0.75");
      aPlot.setYLabel("Ic (collector current)");
      aPlot.setYRange("0", "0.005");
      aPlot.setKey("box");
      aPlot.setKey(".2,.0045");
      aPlot.addExtra("set format y \"%.4f\"");
      aPlot.pushGraph(new Graph("Ic(Vbe)"));
      aPlot.setOutput(Terminal.PNG, "test/out/electron-01.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.addExtra("set format \"%g\"");
      aPlot.addExtra("# Junction Field Effect Transistor (JFET) Mutual Characteristic");
      aPlot.addExtra("# drain current above pinch off");
      aPlot.addExtra("Ida(Vd)=Ido*(1-Vg/Vp)**2");
      aPlot.addExtra("# drain current below pinch off");
      aPlot.addExtra("Idb(Vd)=Ido*(2*Vd*(Vg-Vp)-Vd*Vd)/(Vp*Vp)");
      aPlot.addExtra("# drain current");
      aPlot.addExtra("Id(Vd)= (Vd>Vg-Vp) ? Ida(Vd) : Idb(Vd)");
      aPlot.addExtra("# drain current at zero gate voltage");
      aPlot.addExtra("Ido = 2.5");
      aPlot.addExtra("# pinch off voltage");
      aPlot.addExtra("Vp = -1.25");
      aPlot.addExtra("# gate voltage");
      aPlot.addExtra("Vg = 0");
      aPlot.setDummy("Vd");
      aPlot.unsetGrid();
      aPlot.unsetKey();
      aPlot.addExtra("set offsets 0, 1, 0, 0");
      aPlot.setTitle("JFET Mutual Characteristic");
      aPlot.setXLabel("Drain voltage Vd (V)");
      aPlot.setXRange("0", "4");
      aPlot.setYLabel("Drain current Id (mA)");
      aPlot.setYRange("0", "5");
      aPlot.addExtra("set label \"-0.5 Vp\" at 4.1,0.625");
      aPlot.addExtra("set label \"-0.25 Vp\" at 4.1,1.4");
      aPlot.addExtra("set label \"0\" at 4.1,2.5");
      aPlot.addExtra("set label \"Vg = 0.5 Vp\" at 4.1,3.9");
      aPlot.pushGraph(new Graph("Vg=0.5*Vp,Id(Vd)"));
      aPlot.pushGraph(new Graph("Vg=0.25*Vp,Id(Vd)"));
      aPlot.pushGraph(new Graph("Vg=0,Id(Vd)"));
      aPlot.pushGraph(new Graph("Vg=-0.25*Vp,Id(Vd)"));
      aPlot.setOutput(Terminal.PNG, "test/out/electron-02.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      aPlot.clear();
      aPlot.addExtra("unset label");
      aPlot.addExtra("# show off double axes");
      aPlot.addExtra("# amplitude frequency response");
      aPlot.addExtra("A(jw) = ({0,1}*jw/({0,1}*jw+p1)) * (1/(1+{0,1}*jw/p2))");
      aPlot.addExtra("p1 = 10");
      aPlot.addExtra("p2 = 10000");
      aPlot.setDummy("jw");
      aPlot.setGrid("x y2");
      aPlot.setKey("default");
      aPlot.setLogscale("xy");
      aPlot.addExtra("set log x2");
      aPlot.addExtra("unset log y2");
      aPlot.setTitle("Amplitude and Phase Frequency Response");
      aPlot.setXLabel("jw (radians)");
      aPlot.setXRange("1.1", "90000.0");
      aPlot.setX2Range("1.1", "90000.0");
      aPlot.setYLabel("Magnitude of A(jw)");
      aPlot.setY2Label("Phase of A(jw) (degrees)");
      aPlot.setYTics("nomirror");
      aPlot.setY2Tics();
      aPlot.addExtra("set tics out");
      aPlot.setAutoscale("y,y2");
      aPlot.setAutoscaleAfterRanges();
      aPlot.pushGraph(new Graph("abs(A(jw))"));
      aPlot.pushGraph(new Graph("180/pi*arg(A(jw))", Axes.X2Y2));
      aPlot.setOutput(Terminal.PNG, "test/out/electron-03.png");
      try {
         aPlot.plot();
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }
   }
}
