package com.p6majo.math.newtoniteration;

public class RootsDisplayer {
    public static void main(String[] args) {
        RootsOnDisplay rootsDisplay = new RootsOnDisplay(6,5);
        long start = System.currentTimeMillis();
        rootsDisplay.generateRoots();
        rootsDisplay.displayRoots();
        rootsDisplay.showGrid();
        System.out.println((System.currentTimeMillis()-start)+" ms.");
    }
}
