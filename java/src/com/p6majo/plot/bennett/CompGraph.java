package com.p6majo.plot.bennett;



import java.awt.*;
import java.awt.event.*;

    /**
     CompGraph implements a canvas which graphs a complex function
     where the modulus is plotted on the vertical axis and the
     argument is denoted by color. It defines the function with a
     Functionz object and also uses Complex objects for the complex
     numbers.
     Last updated April 23, 2001
     (copyright 1997-2001)
     @version 1.0
     @author Andrew G. Bennett
     @see Functionz
     @see Complex
     */
    public class CompGraph extends Canvas {
        private int[][] x,y;    // x and y coordinate matrices
        private double[][] z;   // z coordinate matrix
        private double[][] h;   // color coordinate matrix
        private Color[] hue;    // palette
        private double xmin,xmax,ymin,ymax,zmin,zmax;   // plot min and max
        private boolean zauto;  // flag for automatic calculuation of zmax, zmin
        private String xminstr,xmaxstr,yminstr,ymaxstr,zminstr,zmaxstr;
        // plot label strings
        private String view="side"; // view from side or top
        private Functionz fct;  // Function to graph
        private Complex[][] zval;   // Complex coords of domain
        private Complex fzval;      // Complex coords of range
        private double[][] fzmod;   // modulus of f(z)
        private double[][] fzarg;   // argument of f(z) (normalized to (0,1])
        private Font f;             // Font for labels
        private FontMetrics fm;     // FontMetric for labels
        private boolean fontSet=false;  // flag for initial font setup
        private int ascent;             // ascent of Font f

        /**
         Default Constructor
         */
        public CompGraph() {
            f=new Font("Helvetica",Font.PLAIN,12);
            x=new int[101][101];
            y=new int[101][101];
            z=new double[101][101];
            h=new double[101][101];
            hue=new Color[256];
            xmin=-2;
            xmax=2;
            xminstr="-2.0";
            xmaxstr="2.0";
            ymin=-2;
            ymax=2;
            yminstr="-2.0";
            ymaxstr="2.0";
            zmin=0;
            zmax=3;
            zminstr="0.0";
            zmaxstr="3.0";
            zauto=true;
            fct=new Functionz("z");
            fzmod=new double[101][101];
            fzarg=new double[101][101];
            zval=new Complex[101][101];

            // Initialize coordinate matrices
            for (int i=0;i<101;i++) {
                for (int j=0;j<101;j++) {
                    x[i][j]=i;
                    y[i][j]=j;
                    zval[i][j]=new Complex(0.04*i-2,0.04*j-2);
                }
            }
            //Initialize surface f(z)=z
            for (int i=0;i<101;i++) {
                for (int j=0;j<101;j++) {
                    fzval=fct.eval(zval[i][j]);
                    z[i][j]=fzval.mod();
                    h[i][j]=(fzval.arg()+Math.PI)/(2*Math.PI);
                }
            }

            // Define palette (saves execution time defining colors later)
            for (int i=0;i<256;i++) {
                hue[i]=Color.getHSBColor(i/256f,1f,1f);
            }
        }

        // Find exponent in zmin,zmax in scientific notation
        private double exponent(double zm) {
            if (zm==0) {
                return 0d;
            } else {
                zm=Math.abs(zm);
                return Math.floor(Math.log(zm)/Math.log(10d));
            }
        }

        // Update zval for new domain
        private void setzval() {
            double deltax=(xmax-xmin)/100;
            double deltay=(ymax-ymin)/100;
            for (int i=0;i<101;i++) {
                for (int j=0;j<101;j++) {
                    zval[i][j]=new Complex(deltax*i+xmin,deltay*j+ymin);
                }
            }
        }

        /**
         Set new domain.
         <br>This will change the base rectangle for the display box.
         The changes will not be visible until this component is
         repainted.
         @param rmin minimum Re(z).
         @param rmax maximum Re(z).
         @param imin minimum Im(z).
         @param imax maximum Im(z).
         */
        public void setLimits(double rmin,double rmax, double imin, double imax) {
            if ((rmin<rmax) && (imin<imax)) {
                xmin=rmin;
                xmax=rmax;
                xminstr=String.valueOf(xmin);
                xmaxstr=String.valueOf(xmax);
                ymin=imin;
                ymax=imax;
                yminstr=String.valueOf(ymin);
                ymaxstr=String.valueOf(ymax);
                setzval();
            } else {
                throw new IllegalArgumentException("min must be less than max");
            }
        }

        /**
         Get minimum Re(z) for domain.
         @return min Re(z).
         */
        public double getxmin() {
            return xmin;
        }

        /**
         Get maximum Re(z) for domain.
         @return max Re(z).
         */
        public double getxmax() {
            return xmax;
        }

        /**
         Get minimum Im(z) for domain.
         @return min Im(z).
         */
        public double getymin() {
            return ymin;
        }

        /**
         Get maximum Im(z) for domain.
         @return max Im(z).
         */
        public double getymax() {
            return ymax;
        }

        /**
         Get minimum |f(z)| for the display box.
         <br>Note the minimum |f(z)| for the display box won't necessarily
         be the minimum |f(z)| over the domain, even if zauto is true so
         automatic scaling is in effect, since the minimum probably
         will not be attained at a grid point.
         @return min |f(z)| of display box .
         */
        public double getzmin() {
            return zmin;
        }

        /**
         Get maximum |f(z) for the display box.
         <br>Note the maximum |f(z)| for the display box won't necessarily
         be the maximum |f(z)| over the domain, even if zauto is true so
         automatic scaling is in effect, since the maximum probably will
         not be attained at a grid point.
         @return max |f(z)| of display box.
         */
        public double getzmax() {
            return zmax;
        }

        /**
         Set new limits for |f(z)| for the display box.
         <br>Sets new minimum and maximum values for |f(z)|, the vertical
         axis of the display box. Note that no changes will be visible
         until this component is repainted.
         @param min minimum |f(z)| of display box.
         @param max maximum |f(z)| of display box.
         */
        public void setzLimits(double min,double max) {
            if (min<max) {
                zmin=min;
                zmax=max;
                zminstr=String.valueOf(zmin);
                zmaxstr=String.valueOf(zmax);
            } else {
                throw new IllegalArgumentException("min must be less than max");
            }
        }

        /**
         Set flag for automatic vertical scaling.
         @param flag true for automatic scaling, false to set limits manually
         */
        public void setzauto(boolean flag) {
            zauto=flag;
        }

        /**
         Get flag for automatic vertical scaling.
         @return true if automatical vertical scaling in effect.
         */
        public boolean getzauto() {
            return zauto;
        }

        /**
         Set a new function for graph
         <br>Creates a new Functionz object to be graphed.
         Note that this method doesn't change the display, so the
         changes won't be visible until this component is repainted.
         @param s formula for the Functionz to be graphed.
         */
        public void setfct(String s) {
            fct=new Functionz(s);
            setz();
        }

        //  Updates z values for fct
        private void setz() {
            fzval=fct.eval(zval[0][0]);
            if (zauto) {
                zmin=0;
                zmax=0;
            }
            for (int i=0;i<101;i++) {
                for (int j=0;j<101;j++) {
                    fzval=fct.eval(zval[i][j]);
                    z[i][j]=fzval.mod();
                    h[i][j]=(fzval.arg()+Math.PI)/(2*Math.PI);
                    if (!Double.isInfinite(z[i][j]) && !Double.isNaN(z[i][j])) {
                        if (zauto && (z[i][j]<zmin)) {
                            zmin=z[i][j];
                        }
                        if (zauto && (z[i][j]>zmax)) {
                            zmax=z[i][j];
                        }
                    }
                }
            }
            if (zauto) {
                // Round zmin and zmax
                if (zmax==0) zmax=1;
                double k=exponent(zmin);    // exponent of zmin (in scientific notation)
                if (k<4) {  // zmin<10,000, truncate to 3 places
                    zmin=Math.floor(zmin);
                } else {    // zmin>=10,000, truncate to 1 place
                    zmin=Math.floor(zmin/Math.pow(10,k))*Math.pow(10,k);
                }
                k=exponent(zmax);    // repeat for zmax
                if (k<4) {  // zmax<10,000, round up to 3 places
                    zmax=Math.ceil(zmax);
                } else {    // zmax>10,000 round up to 1 place
                    zmax=Math.ceil(zmax/Math.pow(10,k))*Math.pow(10,k);
                }
                zminstr=String.valueOf(zmin);
                zmaxstr=String.valueOf(zmax);
            }
        }

        /**
         Set the view for the graph.
         <br>Chooses whether graph is viewed from the side, showing both
         |f(z)| on vertical axis and arg(f(z)) with color, or viewed from
         the top so only the color is visible. Changes won't be visible
         until this component is repainted.
         @param s  "top" for top view (otherwise defaults to side view).
         */
        public void setView(String s) {
            if (s.equalsIgnoreCase("top")) {
                view="top";
            } else {
                view="side";
            }
        }

        /**
         Get current view.
         @return "side" or "top".
         */
        public String getView() {
            return view;
        }

        // Sets the FontMetrics (duh)
        private void setFontMetrics(Graphics g) {
            if (fontSet) return;
            fm=g.getFontMetrics(f);
            ascent=fm.getAscent();
            fontSet=true;
        }

        // Compute horizontal screen coordinate
        private int xCoord(int x,int y, double z) {
            return (int) Math.round(50+2.6f*x+0.65f*y);
        }

        // Compute vertical screen coordinate
        private int yCoord(int x,int y, double z) {
            return (int) Math.round(300+.2*x-1.2*y-(150*(z-zmin)/(zmax-zmin)));
        }

        // Compute Color of polygon
        private int hueindex(double a, double b, double c, double d) {
            int index;      // index to palette entry
            double avg,dev; // avg and dev of vertex colors

            avg=(a+b+c+d)/4;
            dev=(Math.abs(a-avg)+Math.abs(b-avg)+Math.abs(c-avg)+Math.abs(d-avg))/4;
            if (dev<.1) {   // similar colors, use average
                index=(int) Math.round(256*avg);
                if (index==256) {   // wrap 256 to 0 (branch cut in color wheel)
                    index=0;
                }
                return index;
            } else {        // disimilar colors at vertices
                // check for cut in color wheel
                // (color 0 and 1 are the same but dev will be large)
                if (a<.5) {a++;}
                if (b<.5) {b++;}
                if (c<.5) {c++;}
                if (d<.5) {d++;}
                avg=(a+b+c+d)/4;
                if (avg>1) {
                    avg--;
                    a--;
                    b--;
                    c--;
                    d--;
                }
                index=(int) Math.round(256*avg);
                if (index==256) {   // wrap 256 to 0 (branch cut in color wheel)
                    index=0;
                }
                return index;
            }
        }

        /**
         Paints the CompGraph on the screen.
         <br>This method must be called to update the screen after any
         changes are made to the graph. <b>Usually invoked indirectly
         through a call to repaint().</b>
         */
        public void paint(Graphics g) {
            int[] px=new int[4];    // x coords of polygon to paint
            int[] py=new int[4];    // y coords of polygon to paint
            setFontMetrics(g);
            if (view.equalsIgnoreCase("side")) {
                //
                // SIDE VIEW
                //
                // Draw back lines of box
                g.setColor(Color.white);
                g.drawLine(xCoord(0,0,zmin),yCoord(0,0,zmin),xCoord(0,100,zmin),yCoord(0,100,zmin));
                g.drawLine(xCoord(0,100,zmin),yCoord(0,100,zmin),xCoord(0,100,zmax),yCoord(0,100,zmax));
                g.drawLine(xCoord(100,100,zmin),yCoord(100,100,zmin),xCoord(0,100,zmin),yCoord(0,100,zmin));
                g.drawLine(xCoord(100,100,zmax),yCoord(100,100,zmax),xCoord(0,100,zmax),yCoord(0,100,zmax));
                g.drawLine(xCoord(0,100,zmax),yCoord(0,100,zmax),xCoord(0,0,zmax),yCoord(0,0,zmax));
                // Main Loop
                for (int j=99;j>-1;j--) {
                    for (int i=0;i<100;i++) {
                        // Draw patch
                        px[0]=xCoord(x[i][j],y[i][j],z[i][j]);
                        px[1]=xCoord(x[i][j+1],y[i][j+1],z[i][j+1]);
                        px[2]=xCoord(x[i+1][j+1],y[i+1][j+1],z[i+1][j+1]);
                        px[3]=xCoord(x[i+1][j],y[i+1][j],z[i+1][j]);
                        py[0]=yCoord(x[i][j],y[i][j],z[i][j]);
                        py[1]=yCoord(x[i][j+1],y[i][j+1],z[i][j+1]);
                        py[2]=yCoord(x[i+1][j+1],y[i+1][j+1],z[i+1][j+1]);
                        py[3]=yCoord(x[i+1][j],y[i+1][j],z[i+1][j]);
                        g.setColor(hue[hueindex(h[i][j],h[i][j+1],h[i+1][j+1],h[i+1][j])]);
                        g.fillPolygon(px,py,4);
                    }
                    // Draw front of box
                    g.setColor(Color.white);
                    g.drawLine(xCoord(0,0,zmax),yCoord(0,0,zmax),xCoord(0,0,zmin),yCoord(0,0,zmin));
                    g.drawLine(xCoord(100,0,zmax),yCoord(100,0,zmax),xCoord(100,0,zmin),yCoord(100,0,zmin));
                    g.drawLine(xCoord(100,100,zmax),yCoord(100,100,zmax),xCoord(100,100,zmin),yCoord(100,100,zmin));
                    g.drawLine(xCoord(0,0,zmin),yCoord(0,0,zmin),xCoord(100,0,zmin),yCoord(100,0,zmin));
                    g.drawLine(xCoord(100,0,zmin),yCoord(100,0,zmin),xCoord(100,100,zmin),yCoord(100,100,zmin));
                    g.drawLine(xCoord(0,0,zmax),yCoord(0,0,zmax),xCoord(100,0,zmax),yCoord(100,0,zmax));
                    g.drawLine(xCoord(100,0,zmax),yCoord(100,0,zmax),xCoord(100,100,zmax),yCoord(100,100,zmax));
                    // Add labels
                    g.drawString(xminstr,50,300+ascent+5);
                    g.drawString(xmaxstr,310-fm.stringWidth(xmaxstr),320+ascent+5);
                    g.drawString(yminstr,318,322);
                    g.drawString(ymaxstr,375,200+ascent);
                    g.drawString("|f(z)|",50-fm.stringWidth("|f(z)|")-15,225+ascent/2);
                    g.drawString("Re(z)",180-fm.stringWidth("Re(z)")/2,310+ascent+5);
                    g.drawString("Im(z)",346,261+ascent/2);
                    g.drawString(zminstr,50-fm.stringWidth(zminstr)-5,300);
                    g.drawString(zmaxstr,50-fm.stringWidth(zmaxstr)-5,150+ascent);
                }
            } else {
                //
                // TOP VIEW
                //
                // Main Loop
                for (int j=99;j>-1;j--) {
                    for (int i=0;i<100;i++) {
                        // Draw patch
                        px[0]=50+3*i;
                        px[1]=50+3*i;
                        px[2]=53+3*i;
                        px[3]=53+3*i;
                        py[0]=310-3*j;
                        py[1]=307-3*j;
                        py[2]=307-3*j;
                        py[3]=310-3*j;
                        g.setColor(hue[hueindex(h[i][j],h[i][j+1],h[i+1][j+1],h[i+1][j])]);
                        g.fillPolygon(px,py,4);
                    }
                }
                // Draw edging and labels
                g.setColor(Color.white);
                g.drawLine(50,10,50,310);
                g.drawLine(50,310,350,310);
                g.drawLine(350,310,350,10);
                g.drawLine(350,10,50,10);
                g.drawString(yminstr,45-fm.stringWidth(yminstr),310);
                g.drawString(ymaxstr,45-fm.stringWidth(ymaxstr),10+ascent);
                g.drawString(xminstr,50,315+ascent);
                g.drawString(xmaxstr,350-fm.stringWidth(xmaxstr),315+ascent);
                g.drawString("Im(z)",45-fm.stringWidth("Im(z)"),160+ascent/2);
                g.drawString("Re(z)",200-fm.stringWidth("Re(z)")/2,315+ascent);
            }
        }
    }

