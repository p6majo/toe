package com.p6majo.plot;

import com.p6majo.math.utils.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlotUtilsTest {

    @Test
    public void binData() {
        int size = 19;
        float[] data = new float[size];
        for (int i = 0; i < size; i++)
            data[i]=(float) Math.random()*10;

        System.out.println(Utils.floatArray2String(data, ";", "[]"));
        float[] binnedData = PlotUtils.binData(data,10);
        System.out.println(binnedData.length+" bins of data: " + Utils.floatArray2String(binnedData, ";", "[]"));
    }


}