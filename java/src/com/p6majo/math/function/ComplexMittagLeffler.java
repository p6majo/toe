package com.p6majo.math.function;


import com.nr.sf.Gamma;
import com.p6majo.math.complex.Complex;
import com.p6majo.math.fraction.Fraction;
import com.p6majo.math.nswc.NswcMath;
import com.p6majo.math.utils.Utils;

/**
 * special type of complex function
 * The solution to the differential equation D^\alpha f = f
 * with the fractional derivative
 * @author jmartin
 *
 */
public class ComplexMittagLeffler extends ComplexFunction{

	    private Complex alpha;
	    private Complex[] posCoeff;
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
		public ComplexMittagLeffler(Complex alpha, double precision, int maxOrder){

			if (alpha.re()<0) Utils.errorMsg("Real part of alpha has to be larger than zero, but received "+alpha.toString()+" instead.");
		    this.alpha=alpha;
            this.precision = precision;
            this.maxOrder = maxOrder;
			NswcMath math = new NswcMath();

            Gamma gamma = new Gamma();

            //initialize an array of coefficients to reduce the amount of Gamma function evaluations
            this.posCoeff = new Complex[maxOrder+1];

            for (int i=0;i<maxOrder+1;i++) {
                posCoeff[i] = math.Gamma(Complex.ONE.plus(alpha.scale(i)));
               System.out.println(posCoeff[i].toString());
            }
            System.out.println("Coefficients calculated up to order "+maxOrder);

        }

		@Override
		public Complex eval(Complex z) {

		    int order = 1;
		    Complex result = Complex.ONE;
		    Complex old = Complex.ONE;
		    Complex diff = Complex.ONE;
		    Complex factor = z;
		    Complex numerator = (Complex) factor.clone();

		    while (order<maxOrder && diff.abs()>this.precision){
		        old = (Complex) result.clone();
		        result=result.plus(numerator.divides(posCoeff[order]));

		        diff = old.minus(result);
		        numerator = numerator.times(factor);
		        order++;
            }
            //System.out.println(order);
		    if (order==maxOrder) Utils.errorMsg("Result did not converge for z="+z.toString()+". Try to increase the order.");

			return result;
		}

		 @Override
		 public ComplexFunction derivative() {
			return null;
		 }

		@Override
		public Complex evalDerivative(Complex z) {
			return null;
		}

	}

