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
public class ComplexFractionalExp extends ComplexFunction{

	    private Fraction alpha;
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
		public ComplexFractionalExp(Fraction alpha, double precision, int maxOrder){

		    if (!alpha.isProper()) Utils.errorMsg("The fractional part of the derivative has to be smaller than one but it was received: "+alpha.toString());
		    this.alpha=alpha;
            this.precision = precision;
            this.maxOrder = maxOrder;

            Gamma gamma = new Gamma();

            //initialize an array of coefficients to reduce the amount of Gamma function evaluations
            this.posCoeff = new double[maxOrder+1];
            this.negCoeff = new double[maxOrder+1];

            //for alpha = p/q real the laurent series terminates
            int q = this.alpha.getDenominatorIntValue();
            NswcMath math = new NswcMath();
            for (int i=0;i<maxOrder+1;i++) {
                posCoeff[i] = Math.exp(-gamma.gammln(alpha.getDoubleValue()*i+1));
                if (i< q) negCoeff[i] = Math.exp(-gamma.gammln(-alpha.getDoubleValue()*i+1));
                else negCoeff[i]=0.;
            }
            System.out.println("Coefficients calculated up to order "+maxOrder);

        }

        public ComplexFractionalExp(Fraction alpha,double precision){
            this(alpha,precision,100);
        }

        public ComplexFractionalExp(Fraction alpha){
		    this(alpha,1.e-4,100);
        }

		@Override
		public Complex eval(Complex z) {

		    int order = 1;
		    Complex result = Complex.ONE;
		    Complex old = Complex.ONE;
		    Complex diff = Complex.ONE;
		    Complex factor = (z.log().scale(alpha.getDoubleValue())).exp();
		    Complex numerator = factor.clone();

		    while (order<maxOrder && diff.abs()>this.precision){
		        old = result.clone();
		        result=result.plus(numerator.scale(posCoeff[order]));

		        diff = old.minus(result);
		        numerator = numerator.times(factor);
		        order++;
            }
		    if (order==maxOrder) Utils.errorMsg("Result did not converge for z="+z.toString()+". Try to increase the order.");

			return z.exp().times(super.coefficient);
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

