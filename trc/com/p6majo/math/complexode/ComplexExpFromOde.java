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
import com.p6majo.math.function.ComplexElliptSn;
import com.p6majo.math.function.ComplexExp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.nr.test.NRTestUtil.maxel;
import static com.nr.test.NRTestUtil.vecsub;
import static org.junit.Assert.fail;

public class ComplexExpFromOde {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    int i,nvar=1,nouts=200;
    final double atol=1.0e-8,rtol=atol,h1=0.01,hmin=0.0,x1=0,x2=20.0;
    double sbeps;
    // dydx= new double[nvar], not used
    boolean localflag, globalflag=false;
    Complex[] yc = new Complex[nvar], youtc=new Complex[nvar], yexpc = new Complex[nvar];

    // Test StepperBS
    System.out.println("Testing complex integration with ComplexStepperBS of the ode for exponential in imaginary direction");

    ComplexExp exp  = new ComplexExp();
    for (i=0;i<nvar;i++) {
      yc[i]=exp.eval(new Complex(x1,0));
      yexpc[i]=exp.eval(new Complex(0,x2));
    }

    ComplexOutput outc = new ComplexOutput(nouts);
    rhs_ComplexStepperBS dc = new rhs_ComplexStepperBS();
    ComplexStepperBS sc = new ComplexStepperBS();

    ComplexOdeint odec = new ComplexOdeint(yc,x1,x2,atol,rtol,h1,hmin,outc,dc,sc);
    long startComplex = System.currentTimeMillis();
    odec.integrate();
    System.out.println("Time for complex integration: "+(System.currentTimeMillis()-startComplex)+" ms.");

    for (i=0;i<nvar;i++) {
      youtc[i]=outc.ysave[i][outc.count-1];
      System.out.printf("%s %s %f %f %d\n", youtc[i].toString(),yexpc[i].toString(),Math.cos(x2),Math.sin(x2),outc.count-1);
    }

    sbeps = 1.e-5;
    double devMax = 0.;
    for (i=0;i<nvar;i++)
        devMax = Math.max(devMax,youtc[i].minus(yexpc[i]).abs())/Math.max(youtc[i].abs(),yexpc[i].abs());

    System.out.println("maximal deviation in magnitude "+devMax);
    localflag = devMax>sbeps;
    globalflag = globalflag||localflag;
    if (localflag) {
      fail("*** StepperBS: Inaccurate integration");
      
    }

    if (globalflag) System.out.println("Failed\n");
    else System.out.println("Passed\n");

    double arg = 0.;
    for (i=0;i<nouts;i++){
        arg = i*x2/nouts;
        System.out.printf("%s %f %f\n",outc.ysave[0][i].toString(),Math.cos(arg),Math.sin(arg));
    }


  }


  //differential equation for the exponential function along the imaginary direction
  class rhs_ComplexStepperBS implements ComplexDerivativeInf {
    public void derivs(final double x,Complex[] y,Complex[] dydx) {
      dydx[0]= y[0].times(Complex.I);
    }
    public void jacobian(final double x, Complex[] y, Complex[] dfdx, Complex[][] dfdy){}
  }
}
