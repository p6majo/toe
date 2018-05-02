package com.p6majo.math.function;


import com.nr.sf.Gamma;
import com.p6majo.math.complex.Complex;
import com.p6majo.math.nswc.NswcMath;

import java.util.function.Function;

/**
 * special type of complex function
 * The solution to the differential equation D^\alpha f = f
 * with the fractional derivative
 * @author jmartin
 *
 */
public class DoubleFractionalExp implements Function<Double,Double> {

	    private double alpha;
	    private double[] posCoeff;
	    private double[] negCoeff;
	    private double precision = 1e-4;
	    private int maxOrder;

    /**
     * The fractional exponential function is set up
     * a value for the required precision can be given. The calculation stops, when the increase is
     * smaller than the precision
     * and a maximal value for the order up to which the laurent series is calculated at most
     * @param alpha
     * @param precision
     * @param maxOrder
     */
		public DoubleFractionalExp(double alpha, double precision, int maxOrder){

		    this.alpha=alpha;
            this.precision = precision;
            this.maxOrder = maxOrder;

           	Gamma gamma = new Gamma();

            //initialize an array of coefficients to reduce the amount of Gamma function evaluations
            this.posCoeff = new double[maxOrder+1];
            this.negCoeff = new double[maxOrder+1];

             for (int i=0;i<maxOrder+1;i++) {
                posCoeff[i] = Math.exp(gamma.gammln(alpha*i+1));
                if (alpha==1) negCoeff[i] = 1./0;
                else negCoeff[i] = org.apache.commons.math3.special.Gamma.gamma(alpha * (-i) + 1);
                System.out.println(i+" "+posCoeff[i]+" "+negCoeff[i]);
            }
            System.out.println("Coefficients calculated up to order "+maxOrder);

        }


    /**
     * Applies this function to the given argument.
     *
     * @param x the function argument
     * @return the function result
     */
    @Override
    public Double apply(Double x) {
        int order = 1;
        Double result = 1.; //zero order
        double old =1.;
        double diff = 1.;
        double factor = Math.exp(Math.log(x)*alpha); //x^alpha
        double numerator = factor;

        while (order<maxOrder && Math.abs(diff)>this.precision){
            old = result;
            result=result+numerator/posCoeff[order];
            if (!Double.isInfinite(negCoeff[order]))
                result=result+1./numerator/negCoeff[order];

            diff = old-result;
            numerator = numerator*factor;
            order++;
        }
        if (order==maxOrder) System.out.println("Result did not converge for z="+x+". Try to increase the order.");

        return result;
    }




}

