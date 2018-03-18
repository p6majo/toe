package com.p6majo.math.newtoniteration;

import com.p6majo.info.aachen.Kon;
import org.junit.Test;

public class RootsOnDisplayTest {
    @Test
    public void generateRoots() throws Exception {
        RootsOnDisplay rootsDisplay = new RootsOnDisplay(2,30);
        rootsDisplay.generateRoots();
        rootsDisplay.displayRoots();

        Kon.readInt();
    }

}