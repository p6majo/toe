#include <jni.h>
#include "NswcMath.h"
//
//###################################################################
//                   TempCalcJava.cpp
//###################################################################
//
//
//  Creator : Chris Anderson
//  (C) UCLA 1997  7/16/97
//
//
// To have this code call the FORTRAN versions; define __FORTRAN_BUILD__
// when compiling.
// Typically this is specified by -D __FORTRAN_BUILD__ as part of the C++
// compilation command (Unix) or as a project setting when working within
// an integrated development environment.
//
//###################################################################
//
extern "C" void mycgamma_(double* zPtr, double* wPtr);
extern "C" void mycloggamma_(double* zPtr, double* wPtr);
extern "C" void mycbesselj_(double* nuPtr, double* zPtr, double* wPtr);
extern "C" void mybesselj_(int*n, double* zPtr, double* wPtr);
extern "C" int myellipticcomplex_(double* uPtr,float*k,float*l,double* sPtr, double* cPtr, double* dPtr,int*err);

//Attention: integer variables have to be passed as pointers, follow the example in interlanguage

JNIEXPORT void JNICALL Java_com_p6majo_math_nswc_NswcMath_mycgamma(JNIEnv *env, jobject, jdoubleArray Zarray, jdoubleArray Warray )
{
     jdouble* zPtr   = env->GetDoubleArrayElements(Zarray,0);
     jdouble* wPtr   = env->GetDoubleArrayElements(Warray,0);


     mycgamma_(zPtr,wPtr);

     env->ReleaseDoubleArrayElements(Warray, wPtr,0);
};

JNIEXPORT void JNICALL Java_com_p6majo_math_nswc_NswcMath_mycloggamma(JNIEnv *env, jobject, jdoubleArray Zarray, jdoubleArray Warray )
{
     jdouble* zPtr   = env->GetDoubleArrayElements(Zarray,0);
     jdouble* wPtr   = env->GetDoubleArrayElements(Warray,0);


     mycloggamma_(zPtr,wPtr);

     env->ReleaseDoubleArrayElements(Warray, wPtr,0);
};

JNIEXPORT void JNICALL Java_com_p6majo_math_nswc_NswcMath_mycbesselj(JNIEnv *env, jobject,jdoubleArray Nuarray, jdoubleArray Zarray, jdoubleArray Warray )
{
     jdouble* nuPtr  = env->GetDoubleArrayElements(Nuarray,0);
     jdouble* zPtr   = env->GetDoubleArrayElements(Zarray,0);
     jdouble* wPtr   = env->GetDoubleArrayElements(Warray,0);


     mycbesselj_(nuPtr,zPtr,wPtr);

     env->ReleaseDoubleArrayElements(Warray, wPtr,0);
};

JNIEXPORT void JNICALL Java_com_p6majo_math_nswc_NswcMath_mybesselj(JNIEnv *env, jobject,jint n, jdoubleArray Zarray, jdoubleArray Warray )
{
     jdouble* zPtr   = env->GetDoubleArrayElements(Zarray,0);
     jdouble* wPtr   = env->GetDoubleArrayElements(Warray,0);


     mybesselj_(&n,zPtr,wPtr);

     env->ReleaseDoubleArrayElements(Warray, wPtr,0);
};

JNIEXPORT jint JNICALL Java_com_p6majo_math_nswc_NswcMath_myellipticcomplex(JNIEnv *env, jobject, jdoubleArray Uarray, jfloat k,jfloat l,jdoubleArray  Sarray,jdoubleArray Carray,jdoubleArray Darray,jint err)
{
     jdouble* uPtr   = env->GetDoubleArrayElements(Uarray,0);
    // jdouble* pPtr   = env->GetDoubleArrayElements(Parray,0);
     jdouble* sPtr   = env->GetDoubleArrayElements(Sarray,0);
     jdouble* cPtr   = env->GetDoubleArrayElements(Carray,0);
     jdouble* dPtr   = env->GetDoubleArrayElements(Darray,0);


     myellipticcomplex_(uPtr,&k,&l,sPtr,cPtr,dPtr,&err);

     env->ReleaseDoubleArrayElements(Sarray, sPtr,0);
     env->ReleaseDoubleArrayElements(Carray, cPtr,0);
     env->ReleaseDoubleArrayElements(Darray, dPtr,0);


     return err;
};
