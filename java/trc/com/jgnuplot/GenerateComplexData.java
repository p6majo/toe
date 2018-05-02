package com.jgnuplot;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.utils.FileWriter;

public class GenerateComplexData {
    private static int width = 100;
    private static int height = 100;
    private static double dx ;
    private static double dy;
    private static double xmin = -1;
    private static double xmax = 1;
    private static double ymin = -1;
    private static double ymax = 1;

    public static void main(String[] args) {
        dx = (xmax-xmin)/(width-1);
        dy = (ymax-ymin)/(height-1);

        StringBuilder out = new StringBuilder();

        long start = System.currentTimeMillis();
        for (int i=0;i<width;i++){
            for (int j=0;j<height;j++){
                Complex z = new Complex(xmin+i*dx,ymin+j*dy);
                Complex w = z.times(z);
                out.append(z.re()+" "+z.im()+" "+w.re()+" "+w.im()+"\n");

            }

        }

        FileWriter.writeStringToFile("test.dat",out.toString());

        System.out.println("Data generated in "+(System.currentTimeMillis()-start)+" ms.");
    }
}
