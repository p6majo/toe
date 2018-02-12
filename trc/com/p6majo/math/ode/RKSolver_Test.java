package com.p6majo.math.ode;
import com.nr.ode.DerivativeInf;
import com.nr.ode.Odeint;
import com.nr.ode.Output;
import com.nr.sf.Bessjy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.nr.test.NRTestUtil.maxel;
import static com.nr.test.NRTestUtil.vecsub;
import static org.junit.Assert.fail;

public class RKSolver_Test {

  private int maxOrder = 4;
  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    int i;
    double x,h,sbeps;
    double[] y= new double[maxOrder+1];
    double[] dydx= new double[maxOrder+1];
    double[] yout= new double[maxOrder+1];
    double[] yexp= new double[maxOrder+1];
    boolean localflag, globalflag=false;


    // Test rk4
    System.out.println("Testing rk4");

    x=1;
    h=10;//integration distance

    long start = System.currentTimeMillis();
    Bessjy bess=new Bessjy();
    for (i=0;i<maxOrder+1;i++) {//loop through various orders
      y[i]=bess.jn(i,x);//initial values
      yexp[i]=bess.jn(i,x+h);//final values
    }
    long timeForBesselCalc = System.currentTimeMillis()-start;

    start = System.currentTimeMillis();
    rk4_derivs(x,y,dydx);
    RK4_derivs rk4_derivs = new RK4_derivs();


    RKSolver solver = new RKSolver(y,yout,x,x+h,1e-5,1e-5,h,0.,rk4_derivs);
    solver.integrate();


    long timeForOdeInt = System.currentTimeMillis()-start;


    for (i=0;i<maxOrder+1;i++)
      System.out.printf("%f  %f\n", yout[i], yexp[i]);

    System.out.printf("time for bessel function calculation: %d ms.\n",timeForBesselCalc);
    System.out.printf("time for ode integration: %d ms.\n",timeForOdeInt);

    sbeps = 1.e-6;//required accuracy
    System.out.println(maxel(vecsub(yout,yexp)));
    localflag = maxel(vecsub(yout,yexp)) > sbeps;
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
  void rk4_derivs(final double x,final double[] y,final double[] dydx) {
    dydx[0]= -y[1];
    for (int i=1;i<maxOrder+1;i++)
      dydx[i]=y[i-1]-(double) i/x*y[i];
  }
  
  class RK4_derivs implements DerivativeInf {
    public void derivs(final double x, double[] y, double[] dydx){
      rk4_derivs(x,y,dydx);
    }
    public void jacobian(final double x, double[] y, double[] dfdx, double[][] dfdy){}
  }
  

}
