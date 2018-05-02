package com.princeton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExtendedDrawTest {


    public static void main(String[] args) {
            ExtendedDraw draw = new ExtendedDraw("Test Drawing");

            draw.square(.2, .8, .1);
            draw.filledSquare(.8, .8, .2);
            draw.circle(.8, .2, .2);
            draw.setPenColor(Draw.MAGENTA);
            draw.setPenRadius(.02);
            draw.arc(.8, .2, .1, 200, 45);





        draw.setButton1Label("Circles");
        draw.setActionListenerForButton1(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                draw.setPenColor(Draw.MAGENTA);
                draw.circle(Math.random(),Math.random(),Math.random());
            }
        });
        draw.setButton2Label("Lines");
        draw.setActionListenerForButton2(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                draw.setPenColor(Draw.PRINCETON_ORANGE);
                draw.line(Math.random(),Math.random(),Math.random(),Math.random());
            }
        });

        draw.setButton3Label("Dots");
        draw.setActionListenerForButton3(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                draw.setPenColor(Draw.BLUE);
                draw.point(Math.random(),Math.random());
            }
        });

    }

}
