package com.p6majo.math.network2;

import org.junit.Test;

import static org.junit.Assert.*;

public class GradientCheckerTest {
    @Test
    public void GCT(){
        GradientChecker gradChecker = new GradientChecker();
        System.out.println(gradChecker.gradientCheck(2, 1));
    }

}