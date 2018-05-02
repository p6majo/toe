package com.p6majo.math.complexode;
import com.nr.ode.DerivativeInf;
import com.nr.sf.Bessjy;
import com.p6majo.math.complex.Complex;
import com.p6majo.math.ode.RKSolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.nr.test.NRTestUtil.maxel;
import static com.nr.test.NRTestUtil.vecsub;
import static com.p6majo.math.complexode.ComplexRKSolver.cmaxel;
import static com.p6majo.math.complexode.ComplexRKSolver.cvecsub;
import static org.junit.Assert.fail;

public class ComplexRKSolver_Test {

  private int maxOrder = 4;
  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    int i,nouts=1001;
    double x,h,sbeps;
    Complex[] y= new Complex[maxOrder+1];
    Complex[][] result = new Complex[maxOrder+1][nouts];
    Complex[] dydx= new Complex[maxOrder+1];
    Complex[] yexp= new Complex[maxOrder+1];
    boolean localflag, globalflag=false;


    // Test rk4
    System.out.println("Testing rk4");

    x=1;
    h=1;//integration distance

    long start = System.currentTimeMillis();
    Bessjy bess=new Bessjy();
    for (i=0;i<maxOrder+1;i++) {//loop through various orders
      y[i]=Complex.ONE.scale(bess.jn(i,x));//initial values
      yexp[i]=Complex.ONE.scale(bess.jn(i,x+h));//final values
    }
    long timeForBesselCalc = System.currentTimeMillis()-start;

    start = System.currentTimeMillis();
    rk4_derivs(x,y,dydx);
    RK4_derivs rk4_derivs = new RK4_derivs();


    ComplexRKSolver solver = new ComplexRKSolver(y,result,nouts,x,x+h,1e-5,1e-5,0.,rk4_derivs);
    solver.integrate();


    long timeForOdeInt = System.currentTimeMillis()-start;


    Complex yout[] = new Complex[maxOrder+1];
    for (i=0;i<maxOrder+1;i++){
        yout[i]=result[i][nouts-1];
        System.out.printf("%s  %s\n", result[i][nouts-1].toString(), yexp[i].toString());
    }

    System.out.printf("time for bessel function calculation: %d ms.\n",timeForBesselCalc);
    System.out.printf("time for ode integration: %d ms.\n",timeForOdeInt);

    sbeps = 1.e-6;//required accuracy
    System.out.println(cmaxel(cvecsub(yout,yexp)));
    localflag = cmaxel(cvecsub(yout,yexp)) > sbeps;
    globalflag = globalflag || localflag;
    if (localflag) {
      fail("*** RKSolver: Accuracy hasn't been reached.");
      
    }

    if (globalflag) System.out.println("Failed\n");
    else System.out.println("Passed\n");
  }

  /**
   * calculate the derivatives from the set of differnetial equations
   * @param x
   * @param y
   * @param dydx
   */
  void rk4_derivs(final double x,final Complex[] y,final Complex[] dydx) {
    dydx[0]= y[1].neg();
    for (int i=1;i<maxOrder+1;i++)
      dydx[i]=y[i-1].minus(y[i].scale((double) i/x));
  }
  
  class RK4_derivs implements ComplexDerivativeInf {
    public void derivs(final double x, Complex[] y, Complex[] dydx){
      rk4_derivs(x,y,dydx);
    }
    public void jacobian(final double x, Complex[] y, Complex[] dfdx, Complex[][] dfdy){}
  }
  

}
