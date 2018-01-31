package com.p6majo.math.nswc;


import com.p6majo.math.complex.Complex;
import com.p6majo.math.utils.Utils;

//
//###################################################################
//                   Class TempCalcJava
//###################################################################
//
//   
//  Creator               : Chris Anderson
//  (C) UCLA 1997
//
//###################################################################
//
public class NswcMath
{

public Complex Gamma(Complex arg){
    double[] comps = new double[]{arg.re(),arg.im()};
    mycgamma(comps,comps);

    return new Complex(comps[0],comps[1]);
}

public  Complex LogGamma(Complex arg){
    double[] comps = new double[]{arg.re(),arg.im()};
    mycloggamma(comps,comps);

    return new Complex(comps[0],comps[1]);
}

public  Complex BesselJ(Complex nu,Complex arg) {
    double[] nucomps = new double[]{nu.re(),nu.im()};
    double[] comps = new double[]{arg.re(), arg.im()};
    double n =  Math.round(nu.re());
    if (nu.im()==0 && nu.re()==n) mybesselj((int) n, comps, comps);
    else mycbesselj(nucomps,comps, comps);

    return new Complex(comps[0], comps[1]);
}

public Complex Sn(double k,double l,Complex arg){
    double[] ucomps = new double[]{arg.re(),arg.im()};
    int err =0;
    double[] result=new double[2];
    //double[] params = new double[]{k,l};
    err = myellipticcomplex(ucomps,(float) k,(float) l,result,new double[2],new double[2],err);
    if (err==0) return new Complex(result[0],result[1]);
    else if (err==1) Utils.errorMsg("moduli don't satisfy circular condition: k^2+l^2=1"+k+" "+l);
    else Utils.errorMsg("elliptic function calculation returned error code: "+err);
    return null;
}

    public Complex Cn(double k,double l,Complex arg){
        double[] ucomps = new double[]{arg.re(),arg.im()};
        int err =0;
        double[] result=new double[2];
        //double[] params = new double[]{k,l};
        err = myellipticcomplex(ucomps,(float) k,(float) l,new double[2],result,new double[2],err);
        if (err==0) return new Complex(result[0],result[1]);
        else if (err==1) Utils.errorMsg("moduli don't satisfy circular condition: k^2+l^2=1"+k+" "+l);
        else Utils.errorMsg("elliptic function calculation returned error code: "+err);
        return null;
    }

    public Complex Dn(double k,double l,Complex arg){
        double[] ucomps = new double[]{arg.re(),arg.im()};
        int err =0;
        double[] result=new double[2];
        //double[] params = new double[]{k,l};
        err = myellipticcomplex(ucomps,(float) k,(float) l,new double[2],new double[2],result,err);
        if (err==0) return new Complex(result[0],result[1]);
        else if (err==1) Utils.errorMsg("moduli don't satisfy circular condition: k^2+l^2=1"+k+" "+l);
        else Utils.errorMsg("elliptic function calculation returned error code: "+err);
        return null;
    }

    //
//  native method declarations
//
// public native void cgamma(double[] Zarray,double[] Warray);
//
//
public native void mycgamma(double[] Zarray,double[] Warray);
public native void mycloggamma(double[] Zarray,double[] Warray);
public native void mycbesselj(double[] Nuarray,double[] Zarray,double[] Warray);
public native void mybesselj(int n, double[] Zarray, double[] Warray);
public native int myellipticcomplex(double[] ucomps, float k,float l,double[] scomps,double[] ccomps, double[]dcomps,int err);
//
// Load DLL (or shared library) which contains implementation of native methods
//
static
{
    System.loadLibrary("NswcMath");
}

}