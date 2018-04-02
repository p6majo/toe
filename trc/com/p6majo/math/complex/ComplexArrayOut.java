package com.p6majo.math.complex;

import com.p6majo.math.utils.Utils;

import java.io.File;
import java.io.PrintWriter;

public class ComplexArrayOut {
    public static void main(String[] args) {

        int height = 100;
        int width = 100;
        double xmin =-1;
        double ymin =-1;
        double xmax = 1;
        double ymax = 1;

        double dx = (xmax-xmin)/(width-1);
        double dy = (ymax-ymin)/(height-1);

        StringBuilder out = new StringBuilder();
        long start = System.currentTimeMillis();
        for (int i=0;i<width;i++){
            for (int j=0;j<height;j++){
                Complex z  = new Complex (xmin+i*dx,ymin+j*dy);
                Complex w = z.times(z);
                out.append(z.re()+" "+z.im()+" " +w.re() +" "+w.im()+"\n");
            }
        }

        try {
            PrintWriter writer = new PrintWriter(new File("test.dat"));
            writer.write(out.toString());
            writer.close();
            System.out.println("File generated in "+(System.currentTimeMillis()-start)+" ms.");
        }
        catch(Exception ex){
            Utils.errorMsg(ex.getMessage());
        }
        }
}
