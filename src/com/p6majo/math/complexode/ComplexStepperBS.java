package com.p6majo.math.complexode;

import com.nr.ode.DerivativeInf;
import com.nr.ode.StepperBase;
import com.p6majo.math.complex.Complex;
import org.netlib.util.doubleW;
import org.netlib.util.intW;

import static com.nr.NRUtil.DBL_EPSILON;
import static com.nr.NRUtil.SQR;
import static java.lang.Math.*;


/**
 * Bulirsch-Stoer stepper base class
 * 
 * Copyright (C) Numerical Recipes Software 1986-2007
 * Java translation Copyright (C) Huang Wen Hui 2012
 *
 * @author hwh
 *
 */
public class ComplexStepperBS extends ComplexStepperBase{
  static final int KMAXX=8,IMAXX=KMAXX+1;
  int k_targ;
  int[] nseq;
  int[] cost;
  Complex[][] table;
  Complex[] dydxnew;
  int mu;
  double[][] coeff;
  double[] errfac;
  Complex[][] ysave;
  Complex[][] fsave;
  int[] ipoint;
  Complex[] dens;
  public ComplexStepperBS(){

  }

  public ComplexStepperBS(final Complex[] yy, final Complex[] dydxx, final double xx,
                          final double atoll, final double rtoll, final boolean dense){
    setParam(yy, dydxx, xx, atoll, rtoll, dense);
  }
  
  public void setParam(final Complex[] yy,final Complex[] dydxx,final double xx,
    final double atoll,final double rtoll, final boolean dense) {
    super.setParam(yy,dydxx,xx,atoll,rtoll,dense);
    nseq = new int[IMAXX];
    cost = new int[IMAXX];
    table = new Complex[KMAXX][n];
    dydxnew = new Complex[n];
    coeff = new double[IMAXX][IMAXX];
    errfac = new double[2*IMAXX+2];
    ysave = new Complex[IMAXX][n];
    fsave = new Complex[IMAXX*(2*IMAXX+1)][n];
    ipoint = new int[IMAXX+1];
    dens = new Complex[(2*IMAXX+5)*n];
    
    EPS=DBL_EPSILON;
    if (dense)
      for (int i=0;i<IMAXX;i++)
        nseq[i]=4*i+2;
    else
      for (int i=0;i<IMAXX;i++)
        nseq[i]=2*(i+1);
    cost[0]=nseq[0]+1;
    for (int k=0;k<KMAXX;k++) cost[k+1]=cost[k]+nseq[k+1];
    hnext=-1.0e99;
    double logfact=-log10(max(1.0e-12,rtol))*0.6+0.5;
    k_targ=max(1,min(KMAXX-1,(int)(logfact)));
    for (int k = 0; k<IMAXX; k++) {
      for (int l=0; l<k; l++) {
        double ratio=((double)nseq[k])/nseq[l];
        coeff[k][l]=1.0/(ratio*ratio-1.0);
      }
    }
    for (int i=0; i<2*IMAXX+1; i++) {
      int ip5=i+5;
      errfac[i]=1.0/(ip5*ip5);
      double e = 0.5*sqrt((i+1)/ip5);
      for (int j=0; j<=i; j++) {
        errfac[i] *= e/(j+1);
      }
    }
    ipoint[0]=0;
    for (int i=1; i<=IMAXX; i++) {
      int njadd=4*i-2;
      if (nseq[i-1] > njadd) njadd++;
      ipoint[i]=ipoint[i-1]+njadd;
    }
  }
  
  public Complex dense_out(final int i,final double x,final double h) {
    double theta=(x-xold)/h;
    double theta1=1.0-theta;
    //double yinterp=dens[i]+theta*(dens[n+i]+theta1*(dens[2*n+i]*theta+dens[3*n+i]*theta1));
    Complex yinterp=dens[i].plus((dens[n+i].plus((dens[2*n+i].scale(theta).plus(dens[3*n+i].scale(theta1))).scale(theta1))).scale(theta));
    if (mu<0)
      return yinterp;
    double theta05=theta-0.5;
    double t4=SQR(theta*theta1);
    Complex c=dens[n*(mu+4)+i];
    for (int j=mu;j>0; j--)
      c=dens[n*(j+3)+i].plus(c.scale(theta05/j));
    yinterp = yinterp.plus(c.scale(t4));
    return yinterp;
  }
  
  public void dense_interp(final int n, final Complex[] y, final int imit) {
    double fac1,fac2;
    Complex y0,y1,yp0,yp1,ydiff,aspl,bspl,ph0,ph1,ph2,ph3;
    Complex[] a = new Complex[31];
    for (int i=0; i<n; i++) {
      y0=y[i];
      y1=y[2*n+i];
      yp0=y[n+i];
      yp1=y[3*n+i];
      ydiff=y1.minus(y0);
      aspl=yp1.neg().plus(ydiff);
      bspl=yp0.minus(ydiff);
      y[n+i]=ydiff;
      y[2*n+i]=aspl;
      y[3*n+i]=bspl;
      if (imit < 0) continue;
      ph0=y0.plus(y1).scale(0.5).plus(aspl.plus(bspl).scale(0.125));
      ph1=ydiff.plus(aspl.minus(bspl).scale(0.25));
      ph2=yp0.minus(yp1).neg();
      ph3=bspl.minus(aspl).scale(6.0);
      if (imit >= 1) {
        a[1]=y[5*n+i].minus(ph1).scale(16.0);
        if (imit >= 3) {
          a[3]=y[7*n+i].minus(ph3).plus(a[1].scale(3.)).scale(16.0);
          for (int im=5; im <=imit; im+=2) {
            fac1=im*(im-1)/2.0;
            fac2=fac1*(im-2)*(im-3)*2.0;
            a[im]=y[(im+4)*n+i].plus(a[im-2].scale(fac1)).minus(a[im-4].scale(fac2)).scale(16.0);
          }
        }
      }
      a[0]=y[4*n+i].minus(ph0).scale(16.0);
      if (imit >= 2) {
        a[2]=y[n*6+i].minus(ph2).plus(a[0]).scale(16.0);
        for (int im=4; im <=imit; im+=2) {
          fac1=im*(im-1)/2.0;
          fac2=im*(im-1)*(im-2)*(im-3);
          a[im]=y[n*(im+4)+i].plus(a[im-2].scale(fac1)).minus(a[im-4].scale(fac2)).scale(16.0);
        }
      }
      for (int im=0; im<=imit; im++)
        y[n*(im+4)+i]=a[im];
    }
  }
  
  public void dy(final Complex[] y,final double htot,final int k,final Complex[] yend, final intW ipt,final ComplexDerivativeInf derivs) {
    Complex[] ym = new Complex[n],yn = new Complex[n];
    int nstep=nseq[k];
    double h=htot/nstep;
    for (int i=0;i<n;i++) {
      ym[i]=y[i];
      yn[i]=y[i].plus(dydx[i].scale(h));
    }
    double xnew=x+h;
    derivs.derivs(xnew,yn,yend);
    double h2=2.0*h;
    for (int nn=1;nn<nstep;nn++) {
      if (dense && nn == nstep/2) {
          for (int i=0;i<n;i++)
            ysave[k][i]=yn[i];
      }
      if (dense && abs(nn-nstep/2) <= 2*k+1) {
        ipt.val++;
        for (int i=0;i<n;i++)
          fsave[ipt.val][i]=yend[i];
      }
      for (int i=0;i<n;i++) {
        Complex swap=ym[i].plus(yend[i].scale(h2));
        ym[i]=yn[i];
        yn[i]=swap;
      }
      xnew += h;
      derivs.derivs(xnew,yn,yend);
    }
    if (dense && nstep/2 <= 2*k+1) {
      ipt.val++;
      for (int i=0;i<n;i++)
        fsave[ipt.val][i]=yend[i];
    }
    for (int i=0;i<n;i++)
      yend[i]=ym[i].plus(yn[i]).plus(yend[i].scale(h)).scale(0.5);
  }
  
  public void polyextr(final int k, final Complex[][] table, final Complex[] last) {
    int l=last.length;
    for (int j=k-1; j>0; j--)
      for (int i=0; i<l; i++)
        table[j-1][i]=table[j][i].plus(table[j][i].minus(table[j-1][i]).scale(coeff[k][j]));
    for (int i=0; i<l; i++)
      last[i]=table[0][i].plus(table[0][i].minus(last[i]).scale(coeff[k][0]));
  }
  
  public void prepare_dense(final double h,final Complex[] dydxnew,
    final Complex[] ysav,final double[] scale,final int k,final doubleW error) {
    mu=2*k-1;
    for (int i=0; i<n; i++) {
      dens[i]=ysav[i];
      dens[n+i]=dydx[i].scale(h);
      dens[2*n+i]=y[i];
      dens[3*n+i]=dydxnew[i].scale(h);
    }
    for (int j=1; j<=k; j++) {
      double dblenj=nseq[j];
      for (int l=j; l>=1; l--) {
        double factor=SQR(dblenj/nseq[l-1])-1.0;
        for (int i=0; i<n; i++)
          ysave[l-1][i]=ysave[l][i].plus(ysave[l][i].minus(ysave[l-1][i]).scale(factor));
      }
    }
    for (int i=0; i<n; i++)
      dens[4*n+i]=ysave[0][i];
    for (int kmi=1; kmi<=mu; kmi++) {
      int kbeg=(kmi-1)/2;
      for (int kk=kbeg; kk<=k; kk++) {
        double facnj=pow(nseq[kk]/2.0,kmi-1);
        int ipt=ipoint[kk+1]-2*kk+kmi-3;
        for (int i=0; i<n; i++)
          ysave[kk][i]=fsave[ipt][i].scale(facnj);
      }
      for (int j=kbeg+1; j<=k; j++) {
        double dblenj=nseq[j];
        for (int l=j; l>=kbeg+1; l--) {
          double factor=SQR(dblenj/nseq[l-1])-1.0;
          for (int i=0; i<n; i++)
            ysave[l-1][i]=ysave[l][i].plus(ysave[l][i].minus(ysave[l-1][i]).scale(factor));
        }
      }
      for (int i=0; i<n; i++)
        dens[(kmi+4)*n+i]=ysave[kbeg][i].scale(h);
      if (kmi == mu) continue;
      for (int kk=kmi/2; kk<=k; kk++) {
        int lbeg=ipoint[kk+1]-1;
        int lend=ipoint[kk]+kmi;
        if (kmi == 1) lend += 2;
        for (int l=lbeg; l>=lend; l-=2)
          for (int i=0; i<n; i++)
            fsave[l][i]=fsave[l][i].minus(fsave[l-2][i]);
        if (kmi == 1) {
          int l=lend-2;
          for (int i=0; i<n; i++)
            fsave[l][i]=fsave[l][i].minus(dydx[i]);
        }
      }
      for (int kk=kmi/2; kk<=k; kk++) {
        int lbeg=ipoint[kk+1]-2;
        int lend=ipoint[kk]+kmi+1;
        for (int l=lbeg; l>=lend; l-=2)
          for (int i=0; i<n; i++)
            fsave[l][i]=fsave[l][i].minus(fsave[l-2][i]);
      }
    }
    dense_interp(n,dens,mu);
    error.val=0.0;
    if (mu >= 1) {
      for (int i=0; i<n; i++)
        error.val +=sqrt( dens[(mu+4)*n+i].scale(scale[i]).abs());
      error.val=sqrt(error.val/n)*errfac[mu-1];
    }
  }
  
  static boolean first_step=true,last_step=false;
  static boolean forward,reject=false,prev_reject=false;
  public void step(final double htry,final ComplexDerivativeInf derivs) {
    final double STEPFAC1=0.65,STEPFAC2=0.94,STEPFAC3=0.02,STEPFAC4=4.0,
      KFAC1=0.8,KFAC2=0.9;
    int i,k=0;
    double fac,h,hnew,hopt_int=0;
    doubleW err = new doubleW(0);
    boolean firstk;
    double[] hopt =new double[IMAXX],work=new double[IMAXX];
    Complex[] ysav=new Complex[n],yseq=new Complex[n];
    double[] scale=new double[n]; // ymid =new double[n]
    work[0]=0;
    h=htry;
    forward = h>0 ? true : false;
    for (i=0;i<n;i++) ysav[i]=y[i];
    if (h != hnext && !first_step) {
      last_step=true;
    }
    if (reject) {
      prev_reject=true;
      last_step=false;
    }
    reject=false;
    firstk=true;
    hnew=abs(h);
    
    while(true) {
    //interp_error:
    while (firstk || reject) {
      h = forward ? hnew : -hnew;
      firstk=false;
      reject=false;
      if (abs(h) <= abs(x)*EPS)
        throw new IllegalArgumentException("step size underflow in StepperBS");
      intW ipt = new intW(-1);
      for (k=0; k<=k_targ+1;k++) {
        dy(ysav,h,k,yseq,ipt,derivs);
        if (k == 0)
           // y=yseq; XXX
          System.arraycopy(yseq, 0, y, 0, y.length);
        else
          for (i=0;i<n;i++)
            table[k-1][i]=yseq[i];
        if (k != 0) {
          polyextr(k,table,y);
          err.val=0.0;
          for (i=0;i<n;i++) {
            scale[i]=atol+rtol*max(ysav[i].abs(),y[i].abs());
            err.val+=SQR(y[i].minus(table[0][i]).abs()/scale[i]);
          }
          err.val=sqrt(err.val/n);
          double expo=1.0/(2*k+1);
          double facmin=pow(STEPFAC3,expo);
          if (err.val == 0.0)
            fac=1.0/facmin;
          else {
            fac=STEPFAC2/pow(err.val/STEPFAC1,expo);
            fac=max(facmin/STEPFAC4,min(1.0/facmin,fac));
          }
          hopt[k]=abs(h*fac);
          work[k]=cost[k]/hopt[k];
          if ((first_step || last_step) && err.val <= 1.0)
            break;
          if (k == k_targ-1 && !prev_reject && !first_step && !last_step) {
            if (err.val <= 1.0)
              break;
            else if (err.val>SQR(nseq[k_targ]*nseq[k_targ+1]/(nseq[0]*nseq[0]))) {
              reject=true;
              k_targ=k;
              if (k_targ>1 && work[k-1]<KFAC1*work[k])
                k_targ--;
              hnew=hopt[k_targ];
              break;
            }
          }
          if (k == k_targ) {
            if (err.val <= 1.0)
              break;
            else if (err.val>SQR(nseq[k+1]/nseq[0])) {
              reject=true;
              if (k_targ>1 && work[k-1]<KFAC1*work[k])
                k_targ--;
              hnew=hopt[k_targ];
              break;
            }
          }
          if (k == k_targ+1) {
            if (err.val > 1.0) {
              reject=true;
              if (k_targ>1 && work[k_targ-1]<KFAC1*work[k_targ])
                k_targ--;
              hnew=hopt[k_targ];
            }
            break;
          }
        }
      }
      if (reject)
        prev_reject=true;
    }
    derivs.derivs(x+h,y,dydxnew);
    if (dense) {
      prepare_dense(h,dydxnew,ysav,scale,k,err);
      hopt_int=h/max(pow(err.val,1.0/(2*k+3)),0.01);
      if (err.val > 10.0) {
        hnew=abs(hopt_int);
        reject=true;
        prev_reject=true;
        //goto interp_error;
        continue;
      }
    }
    break;
    } // while(true)
    
    System.arraycopy(dydxnew, 0, dydx, 0, dydxnew.length);
    //dydx=dydxnew; XXX
    xold=x;
    x+=h;
    hdid=h;
    first_step=false;
    int kopt;
    if (k == 1)
      kopt=2;
    else if (k <= k_targ) {
      kopt=k;
      if (work[k-1] < KFAC1*work[k])
        kopt=k-1;
      else if (work[k] < KFAC2*work[k-1])
        kopt=min(k+1,KMAXX-1);
    } else {
      kopt=k-1;
      if (k > 2 && work[k-2] < KFAC1*work[k-1])
        kopt=k-2;
      if (work[k] < KFAC2*work[kopt])
        kopt=min(k,KMAXX-1);
    }
    if (prev_reject) {
      k_targ=min(kopt,k);
      hnew=min(abs(h),hopt[k_targ]);
      prev_reject=false;
    }
    else {
      if (kopt <= k)
        hnew=hopt[kopt];
      else {
        if (k<k_targ && work[k]<KFAC2*work[k-1])
          hnew=hopt[k]*cost[kopt+1]/cost[k];
        else
          hnew=hopt[k]*cost[kopt]/cost[k];
      }
      k_targ=kopt;
    }
    if (dense)
      hnew=min(hnew,abs(hopt_int));
    if (forward)
      hnext=hnew;
    else
      hnext=-hnew;  
  }
}
