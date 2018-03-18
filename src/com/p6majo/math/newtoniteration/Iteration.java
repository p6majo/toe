package com.p6majo.math.newtoniteration;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexFunction;

public class Iteration {

        int steps = -1;
        Complex result = null;
        double MAXVAL = 1.E99;
        double EPS = 1.e-3;

        public Iteration(ComplexFunction function, Complex z, int iterationDepth) {
            //System.out.println("Iteration with start: "+z.toString());
            Complex z1 = null;
            double distance = 1;

            //we try to be more precise than float precision to identify closeby numbers
            //in the comparator of complex numbers on the level of float precision
            while (distance > EPS/10 && steps<iterationDepth) {
                z1 = z.minus(function.eval(z).divides(function.evalDerivative(z)));
                Complex diff = z1.minus(z);
                distance = diff.abs(); //absolute distance is sufficient, since we approach zero
                steps++;
                //System.out.println(function.evalDerivative(z)+" "+function.eval(z)+" "+z.toString());
                z = z1;
                if (z.abs()>MAXVAL) {
                    z=null;
                    break;
                }
            }
            //System.out.println();
            //in case of successful iteration set result
            if (steps<iterationDepth) this.result = z;
        }

        public int getSteps() {
            return this.steps;
        }

        public Complex getResult() {
            return this.result;
        }
    }
