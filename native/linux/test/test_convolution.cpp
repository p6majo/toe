#include<fftw3.h>
#include"convolution_implementation.h"
#include<time.h>


/*
for testing purposes
*/

int main(void){
    int rowsImg = 4092;
    int colsImg = 4092;
    int posImg;
    int posKer;
    int posConv;
    int rowsKer = 5;
    int colsKer = 5;

    float *image;
    float *kernel;
    float *result;

    int rowsResult=rowsImg-rowsKer+1;
    int colsResult=colsImg-colsKer+1;


    clock_t start,end;
    double cpu_time_used;


    image = (float*) fftwf_malloc(sizeof(float)*rowsImg*colsImg);
    kernel = (float*) fftwf_malloc(sizeof(float)*rowsKer*colsKer);
    result=(float*) fftwf_malloc(sizeof(float*)*rowsResult*colsResult);


   printf("Image:\n");
    for (int r=0;r<rowsImg;r++){
        for (int c = 0;c<colsImg;c++){
            posImg = c+r*colsImg;
            image[posImg]=posImg;
           // printf("%f ",image[posImg]);
        }
      //  printf("\n");
    }


    printf("\nKernel:\n");
    for (int r=0;r<rowsKer;r++){
        for (int c = 0;c<colsKer;c++){
            posKer = c+r*colsKer;
            kernel[posKer]=posKer;
            printf("%f ",kernel[posKer]);
        }
        printf("\n");
    }




    start= clock();
    nativeconvolution(image,rowsImg,colsImg,kernel,rowsKer,colsKer,result);
    end = clock();
    cpu_time_used=((double) (end-start))/CLOCKS_PER_SEC;

    printf("\nConvolution in %f secs.:\n",cpu_time_used);

    for (int r=0;r<rowsResult;r++){
        for (int c = 0;c<colsResult;c++){
            posImg = c+r*colsResult;
            //printf("%f ",result[posImg]);
        }
    //printf("\n");
    }


   printf("%f\n",result[0]);
    return 0;
}