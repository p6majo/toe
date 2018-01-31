package com.p6majo.math.newtoniteration;

import org.junit.Test;

public class RootsOnDisplayTest {
    @Test
    public void generateRoots() throws Exception {
        RootsOnDisplay rootsDisplay = new RootsOnDisplay(2,10);
        rootsDisplay.generateRoots();
        //rootsDisplay.displayRoots();
    }

}