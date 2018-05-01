#include <jni.h>
#include "convolution.h"



JNIEXPORT void JNICALL Java_com_p6majo_math_convolution_Convolution_convolute(JNIEnv *env, jobject, jfloatArray image, jint rowsImg, jint colsImg, jfloatArray kernel, jint rowsKer, jint colsKer, jfloatArray convolution )
{
     jfloat* imgPtr   = (*env)-> GetFloatArrayElements(image,0);
     jfloat* kerPtr   = (*env)-> GetFloatArrayElements(kernel,0);
     jfloat* convPtr   = (*env)-> GetFloatArrayElements(convolution,0);

     convolute(imgPtr,&rowsImg, &colsIm,kerPtr,&rowsKer,&colsKer,convPtr);

     env->ReleaseFloatArrayElements(convolution, convPtr,0);
};

