package com.p6majo.math.newtoniteration;

import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.ComplexPolynomial;
import com.p6majo.math.utils.Box;
import com.p6majo.math.utils.Utils;

import java.util.TreeSet;

public class RootFinder {
    private ComplexPolynomial pol;
    private int deg = 0;

    public TreeSet<Complex> findRoots(ComplexPolynomial pol) {
        this.pol = pol;
        this.deg = pol.getDegree();

        TreeSet<Complex> roots = new TreeSet<Complex>();
        Box box = new Box();
        box.rescale(10);

        int steps =0;
        while (roots.size() < this.deg) {
            Complex rnd = Complex.random(box);
            Iteration iteration = new Iteration(pol, rnd, 50);
            Complex root = iteration.getResult();
            if (root!=null){
                if (!roots.contains(root)) {
                    checkMultiplicity(root,pol);
                    roots.add(root);
                }
            }
            steps++;
            if (steps>1000) Utils.errorMsg("Not all roots have been found for "+pol.toString()+".\nOnly the following roots: "+roots.toString());
        }

        return roots;
    }

    /**
     * The multiplicity is checked basing on the idea that for root with higher multiplicity, the root is also a zero
     * for the derivative of the polynomial. However, the derivative usually has larger coefficients, therefore the
     * root will not have the required accuracy. Therefore the additional scaling is imposed.
     * @param root
     * @param pol
     */
    private void checkMultiplicity(Complex root,ComplexPolynomial pol){
        ComplexPolynomial derivative = pol.derivative();
        double scaling = 0.001/deg/deg;

        if (derivative.eval(root).times(Complex.ONE.scale(0.001/deg/deg)).compareTo(Complex.NULL)==0) {
            this.deg --;
            checkMultiplicity(root,derivative);
        }

    }
}
