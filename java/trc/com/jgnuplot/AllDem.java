package com.jgnuplot;

/**
 * This class executes ALL demos in this directory, in functionality order.
 * 
 * @author Pander
 */
public final class AllDem {
   private AllDem() {
   }

   public static void main(String[] args) {
      SimpleDem.gnuplot();
      ControlsDem.gnuplot();
      ElectronDem.gnuplot();
      UsingDem.gnuplot();
      FillStyleDem.gnuplot();
      ParamDem.gnuplot();
      PolarDem.gnuplot();
   }
}
