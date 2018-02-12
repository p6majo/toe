package com.p6majo.math.complexode;

import com.nr.ode.DerivativeInf;
import com.nr.ode.Odeint;
import com.nr.ode.Output;
import com.nr.ode.StepperBS;
import com.nr.sf.Bessjy;
import com.p6majo.math.complex.Complex;
import com.p6majo.math.complexode.ComplexDerivativeInf;
import com.p6majo.math.complexode.ComplexOdeint;
import com.p6majo.math.complexode.ComplexOutput;
import com.p6majo.math.complexode.ComplexStepperBS;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.nr.test.NRTestUtil.maxel;
import static com.nr.test.NRTestUtil.vecsub;
import static org.junit.Assert.fail;

public class BenmarkTestComplexifiedVersion {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    int i,nvar=4;
    final double atol=1.0e-8,rtol=atol,h1=0.01,hmin=0.0,x1=1.0,x2=40.0;
    double sbeps;
    // dydx= new double[nvar], not used
    double[] y= new double[nvar],yout= new double[nvar],yexp= new double[nvar]; 
    boolean detailflag=false, localflag, globalflag=false;

    Complex[] yc = new Complex[nvar], youtc=new Complex[nvar], yexpc = new Complex[nvar];

    // Test StepperBS
    System.out.println("Testing StepperBS and ComplexStepperBS in parallel");
    System.out.println("A system of odes for bessel functions is solved once with double variables and once with complex variables (along the real number line).");
    System.out.println("The agreement is required for successful integration of complex systems of odes. The numerical cost of the transition from double to complex can be estimated.");

    Bessjy bess = new Bessjy();
    for (i=0;i<nvar;i++) {
      y[i]=bess.jn(i,x1);
      yexp[i]=bess.jn(i,x2);
      yc[i]=Complex.ONE.scale(bess.jn(i,x1));
      yexpc[i]=Complex.ONE.scale(bess.jn(i,x2));
    }


    Output out=new Output(400001);
    ComplexOutput outc = new ComplexOutput(400001);
    rhs_StepperBS d = new rhs_StepperBS();
    rhs_ComplexStepperBS dc = new rhs_ComplexStepperBS();

    StepperBS s = new StepperBS();
    ComplexStepperBS sc = new ComplexStepperBS();

    Odeint ode = new Odeint(y,x1,x2,atol,rtol,h1,hmin,out,d,s);
    ComplexOdeint odec = new ComplexOdeint(yc,x1,x2,atol,rtol,h1,hmin,outc,dc,sc);
    long startDouble = System.currentTimeMillis();
    ode.integrate();
    long doubleDuration = (System.currentTimeMillis()-startDouble);
    System.out.println("Time for double integration: "+doubleDuration+" ms.");
    long startComplex = System.currentTimeMillis();
    odec.integrate();
    long complexDuration = System.currentTimeMillis()-startComplex;
    System.out.println("Time for complex integration: "+complexDuration+" ms.");
    System.out.println("Increase in expenses by: "+(100.*(complexDuration-doubleDuration)/doubleDuration)+"%.");

    for (i=0;i<nvar;i++) {
      yout[i]=out.ysave[i][out.count-1];
      youtc[i]=outc.ysave[i][outc.count-1];
      System.out.printf("%f  %f %s %s\n", yout[i],yexp[i],youtc[i].toString(),yexpc[i].toString());
    }

    sbeps = 1.e-7;
    System.out.println(maxel(vecsub(yout,yexp)));
    localflag = maxel(vecsub(yout,yexp)) > sbeps;

    //check each value
    for (i=0;i<nvar;i++)
      for (int j=0;j<out.count;j++)
        if (outc.ysave[i][j].minus(Complex.ONE.scale(out.ysave[i][j])).abs()>sbeps) detailflag = true;


    globalflag = globalflag || localflag||detailflag;

    if (localflag) {
      fail("*** StepperBS: Inaccurate integration");
    }

    if (globalflag) System.out.println("Failed\n");
    else System.out.println("Passed\n");
  }
  
  class rhs_StepperBS implements DerivativeInf {
    public void derivs(final double x,double[] y,double[] dydx) {
      dydx[0]= -y[1];
      dydx[1]=y[0]-(1.0/x)*y[1];
      dydx[2]=y[1]-(2.0/x)*y[2];
      dydx[3]=y[2]-(3.0/x)*y[3];
    }
    public void jacobian(final double x, double[] y, double[] dfdx, double[][] dfdy){}
  }

  class rhs_ComplexStepperBS implements ComplexDerivativeInf {
    public void derivs(final double x,Complex[] y,Complex[] dydx) {
      dydx[0]= y[1].neg();
      dydx[1]=y[0].minus(y[1].scale(1.0/x));
      dydx[2]=y[1].minus(y[2].scale(2.0/x));
      dydx[3]=y[2].minus(y[3].scale(3.0/x));
    }
    public void jacobian(final double x, Complex[] y, Complex[] dfdx, Complex[][] dfdy){}
  }
}
