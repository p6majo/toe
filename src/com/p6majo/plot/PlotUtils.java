package com.p6majo.plot;

import java.util.ArrayList;

public class PlotUtils {

    /**
     * bin data to a given number of data points
     * @param data
     * @param bins
     * @return
     */
    public static float[] binData(ArrayList<Float> data, int bins){
        int binSize = data.size()/bins+1;
        float[] binnedData = new float[data.size()/binSize];
        int binCounter=  0;
        if (data.size()>bins){
            for (int i=0;(i+binSize-1)<data.size();i+=binSize){
                float bin = 0f;
                for (int b=0;b<binSize;b++)
                    bin+=data.get(i+b);
                bin/=binSize;
                binnedData[binCounter]=bin;
            }
        }
        else{
            for (int i=0;i<binnedData.length;i++)
                binnedData[i]=data.get(i);
        }
        return binnedData;
    }

    /**
     * bin data to a given number of data points
     * @param data
     * @param bins
     * @return
     */
    public static float[] binData(float[] data, int bins){

        int binSize = data.length % bins == 0? data.length/bins: data.length/bins+1;
        float[] binnedData = new float[data.length/binSize];
        int binCounter=  0;
        if (data.length>bins){
            for (int i=0;(i+binSize-1)<data.length;i+=binSize){
                float bin = 0f;
                for (int b=0;b<binSize;b++)
                    bin+=data[i+b];
                bin/=binSize;
                binnedData[binCounter]=bin;
                binCounter++;
            }
        }
        else{
            for (int i=0;i<binnedData.length;i++)
                binnedData[i]=data[i];
        }
        return binnedData;
    }

    /**
     * bin data to a given number of data points
     * @param data
     * @param bins
     * @return
     */
    public static float[] binData(double[] data, int bins){
        int binSize = data.length/bins+1;
        float[] binnedData = new float[data.length/binSize];
        int binCounter=  0;
        if (data.length>bins){
            for (int i=0;i<data.length;i+=binSize){
                float bin = 0f;
                for (int b=0;b<binSize;b++)
                    bin+=data[i+b];
                bin/=binSize;
                binnedData[binCounter]=bin;
            }
        }
        else{
            for (int i=0;i<binnedData.length;i++)
                binnedData[i]=(float) data[i];
        }
        return binnedData;
    }

    public static float findMax(float[] data){
        float max = data[0];
        for (int i = 1; i < data.length; i++)
            if (max<data[i]) max = data[i];

        return max;
    }

    public static float findMin(float[] data){
        float min = data[0];
        for (int i = 1; i < data.length; i++)
            if (min>data[i]) min = data[i];

        return min;
    }

}
