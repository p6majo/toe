#include <jni.h>
#include <functional>
#include <iostream>
#include <memory>
#include "convolution.h"
#include "convolution_implementation.h"

using std::string;
using std::function;
using std::unique_ptr;
using std::shared_ptr;
using std::cout;
using std::endl;

extern "C" void nativeconvolution_(float* img, int rowsImg,int colsImg, float *kernel, int rowsKer, int colsKer,float* result);

JNIEXPORT void JNICALL Java_com_p6majo_math_convolution_ConvolutionWrapper_convolutionwrap
(JNIEnv *env, jfloatArray image, jint rowsImg, jint colsImg, jfloatArray kernel, jint rowsKer, jint colsKer, jfloatArray conv )
{
     jfloat* imgPtr   = env-> GetFloatArrayElements(image,0);
     jfloat* kerPtr   = env-> GetFloatArrayElements(kernel,0);
     jfloat* convPtr   =  env-> GetFloatArrayElements(conv,0);

     nativeconvolution_(imgPtr,rowsImg, colsImg,kerPtr,rowsKer,colsKer,convPtr);

     env->ReleaseFloatArrayElements(conv, convPtr,0);
};

