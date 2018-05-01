package com.jfftw.java.real;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlanTest {

    @Test
    public void transform() {
        double[] in = new double[5];
        for (int i = 0; i < 5; i++) {
            in[i]=i+1.;
        }

        Plan fftPlan = new Plan(5);

        double[] out = fftPlan.transform(in);

        for (int i = 0; i < 5; i++) {
            System.out.println(i + ": " + in[i] + " " + out[i]);
        }
    }

    @Test
    public void transform1() {
    }
}