package com.princeton;

public class DrawApplication {
    private static final Draw draw=new Draw();
    private static int cx=0;
    private static int cy=0;
    private static final DrawListener drawListener = new DrawListener(){

        /**
         * Invoked when the mouse has been pressed.
         *
         * @param x the x-coordinate of the mouse
         * @param y the y-coordinate of the mouse
         */
        @Override
        public void mousePressed(double x, double y) {
            System.out.println(x+" "+y);
        }

        /**
         * Invoked when the mouse has been dragged.
         *
         * @param x the x-coordinate of the mouse
         * @param y the y-coordinate of the mouse
         */
        @Override
        public void mouseDragged(double x, double y) {

        }

        /**
         * Invoked when the mouse has been released.
         *
         * @param x the x-coordinate of the mouse
         * @param y the y-coordinate of the mouse
         */
        @Override
        public void mouseReleased(double x, double y) {

        }

        /**
         * Invoked when a key has been typed.
         *
         * @param c the character typed
         */
        @Override
        public void keyTyped(char c) {

        }

        /**
         * Invoked when a key has been pressed.
         *
         * @param keycode the key combination pressed
         */
        @Override
        public void keyPressed(int keycode) {
            if (keycode==39){
                //move right
                cx=cx+1;
            }
            draw.clear();
            draw.circle(cx,cy,50);

        }

        /**
         * Invoked when a key has been released.
         *
         * @param keycode the key combination released
         */
        @Override
        public void keyReleased(int keycode) {

        }
    };


    public static void main(String[] args) {
        draw.setCanvasSize(500,500);
        draw.setXscale(-250,250);
        draw.setYscale(-250,250);
        draw.addListener(drawListener);






        draw.setPenColor(Draw.RED);
        int max = 10000;
        double deltaphi = 2.*Math.PI/max;
        double x0=250.;

        draw.text(-100,-100,"Hallo jetzt geht es los!");

       draw.enableDoubleBuffering();
        double phi=0.;
        for (int i=0;i<max;i++) {
            phi=0+deltaphi*i;
            draw.line(0,0,x0*Math.cos(phi),x0*Math.sin(phi));
        }
        draw.show();

        draw.disableDoubleBuffering();
        draw.setPenColor(Draw.BLACK);
        draw.circle(0,0,50);



    }


}
