#include<fftw3.h>
#include<time.h>

#define REAL 0
#define IMAG 1

//the c program that performs the convolution by utilizing the fftw3 library
void nativeconvolution(float* img, int rowsImg,int colsImg, float *kernel, int rowsKer, int colsKer,float* result){
   
    int posImg;
    int posKer;
    int posConv;

    int rowsConv = rowsImg+rowsKer-1;
    int colsConv = colsImg+colsKer-1;

    int rowsResult = rowsImg -rowsKer+1;
    int colsResult = colsImg -colsKer+1;

    int rowsPadding=rowsConv-rowsImg;
    int colsPadding=colsConv-colsImg;

    float *paddedImage;
    float *paddedKernel;

    fftwf_complex *fftImg;
    fftwf_complex *fftKernel;
   
    clock_t start,end;
    double cpu_time_used;


    paddedImage = (float*) fftwf_malloc(sizeof(float)*rowsConv*colsConv);
    paddedKernel = (float*) fftwf_malloc(sizeof(float)*rowsConv*colsConv);
   
    fftImg = (fftwf_complex*) fftwf_malloc(sizeof(fftwf_complex)*rowsConv*(colsConv/2+1));
    fftKernel = (fftwf_complex*) fftwf_malloc(sizeof(fftwf_complex)*rowsConv*(colsConv/2+1));
   

    //Perform padding
    for (int r=0;r<rowsImg;r++){
        for (int c = 0;c<colsImg;c++){
            posImg = c+r*colsImg;
            posConv = c+r*colsConv;
            paddedImage[posConv]=img[posImg];
        }
    }

    /*
    printf("Image with padding:\n");
    for (int r=0;r<rowsConv;r++){
        for (int c = 0;c<colsConv;c++){
            posImg = c+r*colsConv;
            printf("%f ",paddedImage[posImg]);
        }
        printf("\n");
    }
    */

     //Perform padding and rotation
    for (int r=0;r<rowsKer;r++){
        for (int c = 0;c<colsKer;c++){
            posImg = c+r*colsKer;
            posConv = c+r*colsConv;
            paddedKernel[posConv]=kernel[colsKer*rowsKer-1-posImg];
        }
    }

    /*
    printf("Rotated kernel with padding:\n");
    for (int r=0;r<rowsConv;r++){
        for (int c = 0;c<colsConv;c++){
            posImg = c+r*colsConv;
            printf("%f ",paddedKernel[posImg]);
        }
        printf("\n");
    }
    */

    start=clock();
    fftwf_plan plan = fftwf_plan_dft_r2c_2d(rowsConv,colsConv,paddedImage,fftImg,FFTW_ESTIMATE);
    fftwf_execute(plan);
    end = clock();
    cpu_time_used=((double) (end-start))/CLOCKS_PER_SEC;
    printf("time for first fourier transform: %f\n",cpu_time_used);

    start = clock();
    plan =fftwf_plan_dft_r2c_2d(rowsConv,colsConv,paddedKernel,fftKernel,FFTW_ESTIMATE);
    fftwf_execute(plan);
    end = clock();
    cpu_time_used=((double) (end-start))/CLOCKS_PER_SEC;
    printf("time for second fourier transform: %f\n",cpu_time_used);
  
    /*
    printf("Image in Fourier space:\n");
    for (int r=0;r<rowsConv;r++){
        for (int c = 0;c<colsConv/2+1;c++){
            posImg = c+r*(colsConv/2+1);
            printf("(%f,%f) ",fftImg[posImg][REAL],fftImg[posImg][IMAG]);
        }
        printf("\n");
    }
    */

   /*
    printf("\nKernel in Fourier space:\n");
    for (int r=0;r<rowsConv;r++){
        for (int c = 0;c<colsConv/2+1;c++){
            posKer = c+r*(colsConv/2+1);
            printf("(%f,%f) ",fftKernel[posKer][REAL],fftKernel[posKer][IMAG]);
        }
        printf("\n");
    }
    */

      //the convolution corresponds to multiplication in Fourier space
      start = clock();
    
    /*
    for (int r=0;r<rowsConv;r++){
        for (int c = 0;c<colsConv/2+1;c++){
            posConv = c+r*(colsConv/2+1);
            float real = fftImg[posConv][REAL]*fftKernel[posConv][REAL]-fftImg[posConv][IMAG]*fftKernel[posConv][IMAG];
            float imag = fftImg[posConv][REAL]*fftKernel[posConv][IMAG]+fftImg[posConv][IMAG]*fftKernel[posConv][REAL];
            fftImg[posConv][REAL] = real;
            fftImg[posConv][IMAG] = imag;
        }
    }
    */

    //store pointer positions for reset to the beginning of the arrays

    fftwf_complex *fftImgStart;
    fftwf_complex *fftKernelStart;
    fftImgStart = fftImg;
    fftKernelStart =fftKernel;
   
    for (int i=0;i<rowsConv*(colsConv/2+1);i++){
        float r1 = (*fftImg)[REAL];
        float i1 = (*fftImg)[IMAG];
        float r2 = (*fftKernel)[REAL];
        float i2 = (*fftKernel)[IMAG];
        float real = r1*r2-i1*i2;
        float imag = r1*i2+i1*r2;
        (*fftImg)[REAL]=real;
        (*fftImg)[IMAG]=imag;
        fftImg++;
        fftKernel++;
    }
    
    //reset to the beginning of the array;
    fftImg = fftImgStart;
    fftKernelStart =fftKernel;
    end = clock();
    cpu_time_used=((double) (end-start))/CLOCKS_PER_SEC;
    printf("time for multiplication: %f\n",cpu_time_used);
  

    /*
    printf("Image in Fourier space:\n");
    for (int r=0;r<rowsConv;r++){
        for (int c = 0;c<colsConv/2+1;c++){
            posImg = c+r*(colsConv/2+1);
            printf("(%f,%f) ",fftImg[posImg][REAL],fftImg[posImg][IMAG]);
        }
        printf("\n");
    }
    */

   
    printf("Convoluted image in Fourier space:\n");
    for (int r=0;r<rowsConv;r++){
        for (int c = 0;c<colsConv/2+1;c++){
            posConv = c+r*(colsConv/2+1);
           // printf("(%f,%f) ",fftImg[posConv][REAL],fftImg[posConv][IMAG]);
        }
       // printf("\n");
    }
    

    //back transformation in position space
    start = clock();
    plan = fftwf_plan_dft_c2r_2d(rowsConv,colsConv,fftImg,paddedImage,FFTW_ESTIMATE);
    fftwf_execute(plan);
     end = clock();
    cpu_time_used=((double) (end-start))/CLOCKS_PER_SEC;
    printf("time for back transform: %f\n",cpu_time_used);
  
    
    /*
    printf("Convolution in position space:\n");
    for (int r=0;r<rowsConv;r++){
        for (int c = 0;c<colsConv;c++){
            posImg = c+r*rowsConv;
            printf("%f ",paddedImage[posImg]/(rowsConv*colsConv));
        }
       printf("\n");
    }
    */

    //cut out the relevant part
    // printf("Convoluted image in position space:\n");
    for (int r=0;r<rowsResult;r++){
        for (int c = 0;c<colsResult;c++){
            posImg = c+r*colsResult;
            posConv = c+colsPadding+(r+rowsPadding)*colsConv;
            result[posImg]=paddedImage[posConv]/(rowsConv*colsConv);
            //printf("%f ",result[posImg]);
        }
    //   printf("\n");
    }



    fftwf_destroy_plan(plan);
    fftwf_cleanup();
    
}

int main(void){
    int rowsImg = 8;
    int colsImg = 8;
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